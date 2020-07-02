package team3.recipefinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import team3.recipefinder.R
import team3.recipefinder.model.Recipe

class RecipeListAdapter(context: Context, val onClick: (Recipe) -> Unit) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private val inflater by lazy { LayoutInflater.from(context) }

    var recipes = mutableListOf<Recipe>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view= view
        val recipeName = view.findViewById<TextView>(R.id.recipeName)
        val description = view.findViewById<TextView>(R.id.recipeDescription)
        val image= view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recipe, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        recipes[position].let {
            holder.recipeName.text = it.name
            holder.description.text = it.description
            Glide.with(holder.view).load(
                it.imageUrl
            ).apply(
                RequestOptions()
                    // .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.food)
            ).into(holder.image)
            holder.itemView.setOnClickListener { _ -> onClick(it) }
        }
    }
}
