package br.edu.ufabc.ufabcbarganha

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        toolbar.title = "Tab Layout"
        setSupportActionBar(toolbar)


        viewPager.adapter = MyPagerAdapter(supportFragmentManager)

        tabLayout.setupWithViewPager(viewPager)

        /*val posts: ArrayList<Post> = ArrayList()
        for (i in 0..5) {
            posts.add(Post("igor",
                    "caneta",
                    "https://img.kalunga.com.br/FotosdeProdutos/176470z.jpg",
                    5.00,
                    "Caneta muito boa"))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostAdapter(posts)*/
    }


}
