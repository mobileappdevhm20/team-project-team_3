package team3.recipefinder.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import team3.recipefinder.R
import team3.recipefinder.activity.RecipeDetailActivity
import team3.recipefinder.dialog.EditIngredientFragment

/**
 * Custom adapter to display the ingredients in a custom listview.
 */
class IngredientListAdapter(private val inputContext: Context, private val ingredientNames: List<String>, private val ingredientAmounts: List<String>, private val ingredientIds: List<Long>, private val editMode: Boolean) : ArrayAdapter<String>(inputContext, R.layout.ingredient_list_item,  ingredientNames){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.ingredient_list_item, null, true)

        val amount = rowView.findViewById(R.id.ingredient_item__amount) as TextView
        val name = rowView.findViewById(R.id.ingredient_item__name) as TextView
        val editImageView = rowView.findViewById(R.id.edit_ingredient) as ImageView

        val currentId = ingredientIds[position]
        val currentIngredient = ingredientNames[position]
        val currentAmount = ingredientAmounts[position]

        amount.text = currentAmount
        name.text = currentIngredient

        if(editMode) {
            editImageView.visibility = View.VISIBLE
            rowView.setOnClickListener(View.OnClickListener {
                Log.i("IngredientListAdapter", "Editing with id = $currentId and ingredient = $currentIngredient")
                (inputContext as RecipeDetailActivity).showEditIngredients(currentId, currentIngredient, currentAmount)
            })
        } else {
            editImageView.visibility = View.GONE
        }

        return rowView
    }

    override fun getItem(position: Int): String? {
        return ingredientNames[position]
    }
}
