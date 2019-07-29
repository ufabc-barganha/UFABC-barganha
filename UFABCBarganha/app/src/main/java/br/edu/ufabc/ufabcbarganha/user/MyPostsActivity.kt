package br.edu.ufabc.ufabcbarganha.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.ProductDAO
import br.edu.ufabc.ufabcbarganha.model.Post

class MyPostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        populatePosts()
    }

    private fun populatePosts() {
        val posts = ArrayList<Post>()
        val daoInst = ProductDAO.instance

        for (i in 0 until daoInst.size())
            posts.add(daoInst.getItemAt(i))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView?.layoutManager = LinearLayoutManager(App.context)
        recyclerView?.adapter = MyPostsAdapter(posts)
    }

}