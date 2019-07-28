package br.edu.ufabc.ufabcbarganha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.ufabcbarganha.model.Post
import com.squareup.picasso.Picasso

class FoodAdapter(val foodList: ArrayList<Post>): RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_item_food, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = foodList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foodName.text = foodList[position].productName
        holder.foodPrice.text = foodList[position].price.toString()

        Picasso.get().load(foodList[position].photo).into(holder.foodPhoto)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.food_name)
        val foodPhoto: ImageView = itemView.findViewById(R.id.food_photo)
        val foodPrice: TextView = itemView.findViewById(R.id.food_price)
    }
}