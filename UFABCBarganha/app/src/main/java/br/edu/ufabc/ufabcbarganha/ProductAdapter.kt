package br.edu.ufabc.ufabcbarganha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.model.Post
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProductAdapter(val posts: ArrayList<Post>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun getItemCount(): Int = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_item_product, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = posts[position].username
        holder.postTime.text = "6 de junho às 13:03"
        holder.productName.text = posts[position].productName
        holder.productPrice.text = String.format("R$ %.2f", posts[position].price)
        holder.productDescription.text = posts[position].description

        holder.userPhoto.setImageResource(R.drawable.ic_person)

        Picasso.get().load(posts[position].photo).into(holder.productPhoto)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val postTime: TextView = itemView.findViewById(R.id.post_time)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)

        val userPhoto: CircleImageView = itemView.findViewById(R.id.user_photo)
        val productPhoto: ImageView = itemView.findViewById(R.id.product_photo)
    }
}