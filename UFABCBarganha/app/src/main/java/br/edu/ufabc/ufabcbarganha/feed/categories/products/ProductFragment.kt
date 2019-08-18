package br.edu.ufabc.ufabcbarganha.feed.categories.products


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.data.firestore.UserDataDAO
import br.edu.ufabc.ufabcbarganha.model.Post


class ProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populatePosts()
    }

    private fun populatePosts() {
        val recyclerView = getView()?.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(App.appContext)
        PostDAO.getAllByType(Post.PostType.PRODUCT, object : FirestoreDatabaseOperationListener<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                recyclerView?.adapter = ProductAdapter(result, this@ProductFragment)
            }
            override fun onFailure(e: Exception) {
                //Failed to retrieve post data
            }
        })
    }

    fun favoritePost(post: Post){
        UserDataDAO.favoritePost(post, object : FirestoreDatabaseOperationListener<Void?>{
            override fun onSuccess(result: Void?) {
                Toast.makeText(this@ProductFragment.context, R.string.favorite_post_success, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@ProductFragment.context, R.string.favorite_post_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

}
