package br.edu.ufabc.ufabcbarganha.user

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.data.firestore.UserDataDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import kotlinx.android.synthetic.main.activity_create_post.*

class MyInterestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        populatePosts()
    }

    private fun populatePosts() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(App.appContext)

        PostDAO.getFavoritedPosts( object : FirestoreDatabaseOperationListener<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                recyclerView?.adapter = MyInterestsAdapter(result, this@MyInterestsActivity)
            }
            override fun onFailure(e: Exception) {
                //Failed to retrieve post data
            }
        })
    }

    fun unfavoritePost(post: Post) {
        UserDataDAO.unfavoritePost(post, object : FirestoreDatabaseOperationListener<Void?> {
            override fun onSuccess(result: Void?) {
                Toast.makeText(this@MyInterestsActivity, R.string.unfavorite_post_success, Toast.LENGTH_LONG).show()
                populatePosts() //refresh
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@MyInterestsActivity, R.string.unfavorite_post_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun bargainPost() {
        val contact = "+55 1752"
        val url = "https://api.whatsapp.com/send?phone=" + contact

        try {
            val pm = App.appContext.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)

            Intent(Intent.ACTION_VIEW).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.parse(url)
                App.appContext.startActivity(this)
            }

        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(App.appContext, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show()
        }
    }

}

