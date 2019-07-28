package br.edu.ufabc.ufabcbarganha.feed.food


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.model.Post

class FoodFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateFoodPosts(view)
    }

    private fun populateFoodPosts(view: View) {
        val posts: ArrayList<Post> = ArrayList()

        for (i in 0..5) {
            posts.add(
                Post("igor",
                    "caneta",
                    "https://img.kalunga.com.br/FotosdeProdutos/176470z.jpg",
                    5.00,
                    "Caneta muito boa")
            )
        }

        val recyclerView = getView()?.findViewById<RecyclerView>(R.id.food_recycler_view)

        recyclerView?.layoutManager = GridLayoutManager(App.context, 2)
        recyclerView?.adapter = FoodAdapter(posts)
    }



}
