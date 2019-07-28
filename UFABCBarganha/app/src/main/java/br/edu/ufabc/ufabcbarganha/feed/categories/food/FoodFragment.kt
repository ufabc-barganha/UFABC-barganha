package br.edu.ufabc.ufabcbarganha.feed.categories.food


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.FoodDAO
import br.edu.ufabc.ufabcbarganha.data.ProductDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import kotlin.collections.ArrayList

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
        val daoInst = FoodDAO.instance

        for (i in 0 until daoInst.size())
            posts.add(daoInst.getItemAt(i))

        val recyclerView = getView()?.findViewById<RecyclerView>(R.id.food_recycler_view)

        recyclerView?.layoutManager = GridLayoutManager(App.context, 2)
        recyclerView?.adapter = FoodAdapter(posts)
    }
}
