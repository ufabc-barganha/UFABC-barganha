package br.edu.ufabc.ufabcbarganha.data.firestore

import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.model.UserData
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.firebase.firestore.QuerySnapshot
import java.lang.RuntimeException

object PostDAO {

    const val POSTS_COLLECTION = "posts"
    const val POST_TYPE_FIELD = "postType"
    const val USER_ID_FIELD = "userId"


    fun add(post: Post, callback: FirestoreDatabaseOperationListener<Void?>) {
        val userId = FirebaseUserHelper.getUserId()
        if(userId == null){
            callback.onFailure(RuntimeException("Failure not logged ?"))
        }
        post.userId = userId!!
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION).add(post)
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { result -> callback.onFailure(result) }
    }

    fun remove(post: Post, callback: FirestoreDatabaseOperationListener<Void?>) {
        val userId = FirebaseUserHelper.getUserId()
        if(userId == null){
            callback.onFailure(RuntimeException("Failure not logged ?"))
        }
        post.userId = userId!!
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
            .document(post.id)
            .delete()
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { result -> callback.onFailure(result) }
    }

    fun getAllByUserId(userId: String?, callback: FirestoreDatabaseOperationListener<List<Post>>){
        if(userId == null){
            callback.onFailure(RuntimeException("Failure user is null"))
        }
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .addOnSuccessListener { result -> callback.onSuccess(documentsToPosts(result)) }
            .addOnFailureListener { result -> callback.onFailure(result)}
    }

    fun getAll(callback: FirestoreDatabaseOperationListener<List<Post>>) {
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
            .get()
            .addOnSuccessListener { result -> callback.onSuccess(documentsToPosts(result)) }
            .addOnFailureListener { result -> callback.onFailure(result)}
    }

    fun getAllByType(postType: Post.PostType, callback: FirestoreDatabaseOperationListener<List<Post>>){
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
            .whereEqualTo(POST_TYPE_FIELD, postType.name)
            .get()
            .addOnSuccessListener { result -> callback.onSuccess(documentsToPosts(result)) }
            .addOnFailureListener { result -> callback.onFailure(result)}
    }

    fun getFavoritedPosts(callback: FirestoreDatabaseOperationListener<List<Post>>){
        UserDataDAO.getUserData(object : FirestoreDatabaseOperationListener<UserData>{
            override fun onSuccess(userData: UserData) {
                BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
                    .get()
                    .addOnSuccessListener { result -> callback.onSuccess(documentsToPostsFilterByFavorites(result, userData)) }
                    .addOnFailureListener { result -> callback.onFailure(result)}
            }

            override fun onFailure(e: Exception) {
                callback.onFailure(e)
            }
        })
    }

    private fun documentsToPosts(qSnapshot: QuerySnapshot): List<Post> {
        val posts = mutableListOf<Post>()
        for (document in qSnapshot.documents) {
            val p = document.toObject(Post::class.java)
            if (p != null) {
                p.id = document.id
                posts.add(p)
            }
        }
        return posts
    }

    private fun documentsToPostsFilterByFavorites(qSnapshot: QuerySnapshot, userData: UserData): List<Post> {
        val posts = mutableListOf<Post>()
        for (document in qSnapshot.documents) {
            val p = document.toObject(Post::class.java)
            if (p != null && userData.favoritePosts.contains(document.id)) {
                p.id = document.id
                posts.add(p)
            }
        }
        return posts
    }
    //
}