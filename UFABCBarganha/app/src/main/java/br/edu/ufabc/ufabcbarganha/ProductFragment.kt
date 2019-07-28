package br.edu.ufabc.ufabcbarganha


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.model.Post


class ProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populatePosts(view)
    }

    private fun populatePosts(view: View) {
        val posts: ArrayList<Post> = ArrayList()

        for (i in 0..5) {
            posts.add(Post("igor",
               "caneta",
               "https://img.kalunga.com.br/FotosdeProdutos/176470z.jpg",
               5.00,
               "Caneta muito boa"))
        }

        val recyclerView = getView()?.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView?.layoutManager = LinearLayoutManager(App.context)
        recyclerView?.adapter = PostAdapter(posts)
    }

}
