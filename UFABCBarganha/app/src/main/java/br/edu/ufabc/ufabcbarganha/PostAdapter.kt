package br.edu.ufabc.ufabcbarganha

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.model.Post
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class PostAdapter(val posts: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun getItemCount(): Int = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = posts[position].username
        holder.productName.text = posts[position].productName
        Picasso.get().load(posts[position].photo).into(holder.photo)
        holder.price.text = String.format("%.2f", posts[position].price)
        holder.description.text = posts[position].description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val price: TextView = itemView.findViewById(R.id.price)
        val description: TextView = itemView.findViewById(R.id.description)
    }
}