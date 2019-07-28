package br.edu.ufabc.ufabcbarganha.feed.general

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.DAO
import br.edu.ufabc.ufabcbarganha.data.FoodDAO
import br.edu.ufabc.ufabcbarganha.data.HousingDAO
import br.edu.ufabc.ufabcbarganha.data.ProductDAO
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostDetailActivity : AppCompatActivity() {

    lateinit var username: TextView
    lateinit var postTime: TextView
    lateinit var productName: TextView
    lateinit var productPrice: TextView
    lateinit var productDescription: TextView
    lateinit var userPhoto: CircleImageView
    lateinit var productPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val productPosition = intent.getIntExtra(App.PRODUCT_POSITION, -1)
        val foodPosition = intent.getIntExtra(App.FOOD_POSITION, -1)
        val housingPosition = intent.getIntExtra(App.HOUSING_POSITION, -1)

        setViews()
        populateCard(productPosition, foodPosition, housingPosition)
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

    private fun populateCard(productPosition: Int, foodPosition: Int, housingPosition: Int) {
        when {
            productPosition != -1 -> populate(productPosition, ProductDAO.instance)
            foodPosition != -1 -> populate(foodPosition, FoodDAO.instance)
            housingPosition != -1 -> populate(housingPosition, HousingDAO.instance)
        }
    }

    private fun populate(position: Int, daoInst: DAO) {
        username.text = daoInst.getItemAt(position).username
        postTime.text = daoInst.getItemAt(position).postTime.toString()
        productName.text = daoInst.getItemAt(position).productName
        productPrice.text = String.format("R$ %.2f", daoInst.getItemAt(position).price)
        productDescription.text = daoInst.getItemAt(position).description

        userPhoto.setImageResource(R.drawable.ic_person)

        Picasso.get().load(daoInst.getItemAt(position).photo).into(productPhoto)
    }
}
