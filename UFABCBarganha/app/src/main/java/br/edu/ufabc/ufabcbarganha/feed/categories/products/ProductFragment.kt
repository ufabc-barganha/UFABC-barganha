package br.edu.ufabc.ufabcbarganha.feed.categories.products


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.ProductDAO
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
        val posts = ArrayList<Post>()
        val daoInst = ProductDAO.instance

        for (i in 0 until daoInst.size())
            posts.add(daoInst.getItemAt(i))

        val recyclerView = getView()?.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView?.layoutManager = LinearLayoutManager(App.context)
        recyclerView?.adapter = ProductAdapter(posts)
    }

}
