package br.edu.ufabc.ufabcbarganha.data.firestore

import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.firebase.firestore.QuerySnapshot
import java.lang.RuntimeException

object PostDAO {

    const val POSTS_COLLECTION = "posts"
    const val POST_TYPE_FIELD = "postType"

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

    fun getAllByUserId(userId: String?, callback: FirestoreDatabaseOperationListener<List<Post>>){
        if(userId == null){
            callback.onFailure(RuntimeException("Failure user is null"))
        }
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION)
            .get()
            .addOnSuccessListener { result -> callback.onSuccess(documentsToPostsFilterByUserId(result, userId!!)) }
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
            .whereEqualTo(POST_TYPE_FIELD, postType.toString())
            .get()
            .addOnSuccessListener { result -> callback.onSuccess(documentsToPosts(result)) }
            .addOnFailureListener { result -> callback.onFailure(result)}
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

    private fun documentsToPostsFilterByUserId(qSnapshot: QuerySnapshot, userId: String): List<Post> {
        val posts = mutableListOf<Post>()
        for (document in qSnapshot.documents) {
            val p = document.toObject(Post::class.java)
            if (p != null) {
                p.id = document.id
                if(p.userId != userId)
                    continue
                posts.add(p)
            }
        }
        return posts
    }
}