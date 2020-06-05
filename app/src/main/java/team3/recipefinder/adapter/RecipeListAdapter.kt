package team3.recipefinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recipe.view.*
import team3.recipefinder.R
import team3.recipefinder.model.Recipe

class RecipeListAdapter(context: Context, val callback: RecipeCallback): RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private val inflater by lazy { LayoutInflater.from(context) }

    interface RecipeCallback{
        fun deleteRecipe(recipe: Recipe)
    }
    var recipes = mutableListOf<Recipe>()




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val recipeName = view.findViewById<TextView>(R.id.recipeName)
        fun bind(recipe: Recipe, callback: RecipeCallback){
            itemView.delete_recipe.setOnClickListener {
                Log.i("Recipe", "recipe")
                callback.deleteRecipe(recipe)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recipe, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        recipes[position].let {
            holder.recipeName.text = it.name
        }
        holder.bind(recipes[position], callback)

    }

    public fun setListeners(){

    }

}