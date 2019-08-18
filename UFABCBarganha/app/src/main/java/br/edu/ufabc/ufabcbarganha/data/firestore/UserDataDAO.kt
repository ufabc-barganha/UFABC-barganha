package br.edu.ufabc.ufabcbarganha.data.firestore

import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.model.UserData
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import java.lang.RuntimeException

object UserDataDAO {

    const val USER_DATA_COLLECTION = "userData"
    const val FAVORITE_POSTS_FIELD = "favoritePosts"

    fun getUserData(callback: FirestoreDatabaseOperationListener<UserData>) {
        val userId = FirebaseUserHelper.getUserId()
        if (userId == null) {
            callback.onFailure(RuntimeException("Failure not logged ?"))
        }

        BarganhaFirebaseDatabase.getInstance().collection(USER_DATA_COLLECTION)
            .document(userId!!)
            .get()
            .addOnSuccessListener { result ->
                if (!result.exists()) {
                    val userData = UserData()
                    BarganhaFirebaseDatabase.getInstance().collection(USER_DATA_COLLECTION)
                        .document(userId!!)
                        .set(userData, SetOptions.merge())
                        .addOnSuccessListener { callback.onSuccess(userData) }
                        .addOnFailureListener { result -> callback.onFailure(result) }
                } else {
                    val userData = result.toObject(UserData::class.java)
                    if (userData == null) {
                        callback.onFailure(RuntimeException("Unexpect problem when retrieving user data"))
                    } else {
                        callback.onSuccess(userData)
                    }
                }
            }
            .addOnFailureListener { result -> callback.onFailure(result) }
    }

    fun favoritePost(post: Post, callback: FirestoreDatabaseOperationListener<Void?>) {
        val userId = FirebaseUserHelper.getUserId()
        if (userId == null) {
            callback.onFailure(RuntimeException("Failure not logged ?"))
        }

        getUserData(object : FirestoreDatabaseOperationListener<UserData> {
            override fun onSuccess(result: UserData) {
                if (!result.favoritePosts.contains(post.id)) {
                    BarganhaFirebaseDatabase.getInstance().collection(USER_DATA_COLLECTION)
                        .document(userId!!)
                        .update(FAVORITE_POSTS_FIELD, FieldValue.arrayUnion(post.id))
                        .addOnSuccessListener { callback.onSuccess(null) }
                        .addOnFailureListener { e -> callback.onFailure(e) }
                } else {
                    callback.onSuccess(null)
                }
            }
            override fun onFailure(e: Exception) {
                callback.onFailure(e)
            }
        })
    }

    fun unfavoritePost(post: Post, callback: FirestoreDatabaseOperationListener<Void?>){
        val userId = FirebaseUserHelper.getUserId()
        if (userId == null) {
            callback.onFailure(RuntimeException("Failure not logged ?"))
        }

        BarganhaFirebaseDatabase.getInstance().collection(USER_DATA_COLLECTION)
            .document(userId!!)
            .update(FAVORITE_POSTS_FIELD, FieldValue.arrayRemove(post.id))
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { e -> callback.onFailure(e) }
    }
}
