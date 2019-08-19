package br.edu.ufabc.ufabcbarganha.feed.categories.food

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.feed.general.PostDetailActivity
import br.edu.ufabc.ufabcbarganha.model.Post
import com.squareup.picasso.Picasso

class FoodAdapter(val foodList: List<Post>): RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_item_food, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = foodList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = foodList[position]
        holder.foodName.text = foodList[position].productName
        holder.foodPrice.text = String.format("R$ %.2f", post.price)

        Picasso.get().load(foodList[position].photo).into(holder.foodPhoto)

        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, PostDetailActivity::class.java)
            intent.putExtra(App.POST_EXTRA, post)
            ContextCompat.startActivity(it.context, intent, null)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.food_name)
        val foodPhoto: ImageView = itemView.findViewById(R.id.food_photo)
        val foodPrice: TextView = itemView.findViewById(R.id.food_price)
    }
}