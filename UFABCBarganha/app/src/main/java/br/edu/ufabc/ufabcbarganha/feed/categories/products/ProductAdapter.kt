package br.edu.ufabc.ufabcbarganha.feed.categories.products

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.feed.general.PostDetailActivity
import br.edu.ufabc.ufabcbarganha.model.Post
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProductAdapter(val posts: List<Post>, val productFragment: ProductFragment): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun getItemCount(): Int = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_item_product, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.username.text = posts[position].username
        holder.postTime.text = "6 de junho às 13:03"
        holder.productName.text = posts[position].productName
        holder.productPrice.text = String.format("R$ %.2f", posts[position].price)
        holder.productDescription.text = posts[position].description

        holder.userPhoto.setImageResource(R.drawable.ic_person)

        Picasso.get().load(posts[position].photo).into(holder.productPhoto)

        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, PostDetailActivity::class.java)
            intent.putExtra(App.POST_EXTRA, post)
            ContextCompat.startActivity(it.context, intent, null)
        }

        holder.interestButton.setOnClickListener { productFragment.favoritePost(post) }

        holder.bargainButton.setOnClickListener {
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val postTime: TextView = itemView.findViewById(R.id.post_time)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val interestButton: Button = itemView.findViewById(R.id.interest)
        val bargainButton: Button = itemView.findViewById(R.id.bargain)

        val userPhoto: CircleImageView = itemView.findViewById(R.id.user_photo)
        val productPhoto: ImageView = itemView.findViewById(R.id.product_photo)
    }
}