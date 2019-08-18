package br.edu.ufabc.ufabcbarganha.feed.general

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.UserDataDAO
import br.edu.ufabc.ufabcbarganha.model.Post
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

        interest.setOnClickListener { onInterestClicked() }
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
    }

    private fun populate(post: Post) {
        username.text = post.username
        postTime.text = post.postTime.toString()
        productName.text = post.productName
        productPrice.text = String.format("R$ %.2f", post.price)
        productDescription.text = post.description

        userPhoto.setImageResource(R.drawable.ic_person)
        Picasso.get().load(post.photo).into(productPhoto)
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
}
