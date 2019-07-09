package br.edu.ufabc.ufabcbarganha

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.model.Post

import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.content_feed.*

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)

        val posts: ArrayList<Post> = ArrayList()
        for (i in 0..5) {
            posts.add(Post("igor",
                    "caneta",
                    "https://img.kalunga.com.br/FotosdeProdutos/176470z.jpg",
                    5.00,
                    "Caneta muito boa"))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostAdapter(posts)
    }


}
