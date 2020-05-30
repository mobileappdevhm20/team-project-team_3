package ßteam3.recipefinder.adapter

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import team3.recipefinder.R
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.IngredientListItem

class IngredientListAdapter(context: Context, private val ingredientNames: List<String>, private val editMode: Boolean) : ArrayAdapter<String>(context, R.layout.ingredient_list_item,  ingredientNames){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.ingredient_list_item, null, true)

        val amount = rowView.findViewById(R.id.ingredient_item__amount) as TextView
        val name = rowView.findViewById(R.id.ingredient_item__name) as TextView
        val editImageView = rowView.findViewById(R.id.edit_ingredient) as ImageView

        amount.text = "5 tbs"
        name.text = ingredientNames[position]

        if(editMode) {
            editImageView.visibility = View.VISIBLE
        } else {
            editImageView.visibility = View.GONE
        }

        return rowView
    }
}