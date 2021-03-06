package br.edu.ufabc.ufabcbarganha.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import kotlinx.android.synthetic.main.activity_create_post.*

class MyPostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        populatePosts()
    }

    private fun populatePosts() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView?.layoutManager = LinearLayoutManager(App.appContext)

        PostDAO.getAllByUserId(FirebaseUserHelper.getUserId(), object : FirestoreDatabaseOperationListener<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                recyclerView?.adapter = MyPostsAdapter(result)
            }
            override fun onFailure(e: Exception) {
                //Failed to retrieve post data
            }
        })
    }

}