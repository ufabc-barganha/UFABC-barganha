package br.edu.ufabc.ufabcbarganha.feed.general

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.data.firestore.UserDataDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.cardview_item_product.*

class PostDetailActivity : AppCompatActivity() {

    companion object{
        val POST_EXTRA = "extra_post"
    }

    lateinit var username: TextView
    lateinit var postTime: TextView
    lateinit var productName: TextView
    lateinit var productPrice: TextView
    lateinit var productDescription: TextView
    lateinit var userPhoto: CircleImageView
    lateinit var productPhoto: ImageView

    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        post = intent.getSerializableExtra(POST_EXTRA) as Post

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setViews()
        populate(post)

        bargain.setOnClickListener { onBargainClicked() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setViews() {
        username = findViewById(R.id.username)
        postTime = findViewById(R.id.post_time)
        productName = findViewById(R.id.product_name)
        productPrice = findViewById(R.id.product_price)
        productDescription = findViewById(R.id.product_description)
        userPhoto = findViewById(R.id.user_photo)
        productPhoto = findViewById(R.id.product_photo)
        bargain
    }

    private fun populate(post: Post) {
        if(FirebaseUserHelper.getUserId()!! == post.userId){
            bargain.visibility = View.GONE
            interest.setText(R.string.remove_post)
            interest.setOnClickListener { removePost() }
        }
        else{
            interest.setOnClickListener { onInterestClicked() }
        }

        username.text = post.username
        postTime.text = post.postTime.toString()
        productName.text = post.productName
        productPrice.text = String.format("R$ %.2f", post.price)
        productDescription.text = post.description

        userPhoto.setImageResource(R.drawable.ic_person)
        Picasso.get().load(post.photo).into(productPhoto)
    }

    private fun removePost(){
        PostDAO.remove(post, object : FirestoreDatabaseOperationListener<Void?>{
            override fun onSuccess(result: Void?) {
                Toast.makeText(this@PostDetailActivity, R.string.removed_with_success, Toast.LENGTH_LONG).show()
                val intent = Intent(this@PostDetailActivity, FeedActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@PostDetailActivity, R.string.remove_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onInterestClicked(){
        UserDataDAO.favoritePost(post, object : FirestoreDatabaseOperationListener<Void?>{
            override fun onSuccess(result: Void?) {
                Toast.makeText(this@PostDetailActivity, R.string.favorite_post_success, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@PostDetailActivity, R.string.favorite_post_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onBargainClicked() {
        val contact = post.phone
        val url = "https://api.whatsapp.com/send?phone=" + contact

        try {
            val pm = packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)

            Intent(Intent.ACTION_VIEW).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.parse(url)
                startActivity(this)
            }

        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show()
        }

    }
}
