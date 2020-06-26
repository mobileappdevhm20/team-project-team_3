package team3.recipefinder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import team3.recipefinder.databinding.RecipeViewHolderBinding
import team3.recipefinder.listener.RecipeListener
import team3.recipefinder.model.Recipe

class RecipeAdapter(val listener: RecipeListener) :
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(
        RecipeDiffCallback()
    ) {

    class RecipeViewHolder private constructor(private val binding: RecipeViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeViewHolderBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(
                    binding
                )
            }
        }

        fun bind(recipe: Recipe, listener: RecipeListener) {
            Log.i("RecipeAdapter", "Bind called with Recipe$recipe")
            binding.recipe = recipe
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(getItem(position), listener)
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.name == newItem.name
    }
}
