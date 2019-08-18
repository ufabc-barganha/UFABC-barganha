package br.edu.ufabc.ufabcbarganha.data.firestore

import br.edu.ufabc.ufabcbarganha.model.Post
import com.google.firebase.firestore.QuerySnapshot

object PostDAO {

    const val POSTS_COLLECTION = "posts"
    const val POST_TYPE_FIELD = "postType"

    fun add(post: Post, callback: FirestoreDatabaseOperationListener<Void?>) {
        val doc = BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION).document()
        post.id = doc.id
        BarganhaFirebaseDatabase.getInstance().collection(POSTS_COLLECTION).add(post)
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { result -> callback.onFailure(result) }

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

    fun documentsToPosts(qSnapshot: QuerySnapshot): List<Post> {
        val posts = mutableListOf<Post>()
        for (document in qSnapshot.documents) {
            val p = document.toObject(Post::class.java)
            if (p != null) {
                posts.add(p)
            }
        }
        return posts
    }
}