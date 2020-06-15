package team3.recipefinder.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_search.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Ingredient
import team3.recipefinder.util.replace

class SearchAddIngredientDialogFragment(val onSelect: (Ingredient)->Unit): DialogFragment(),
    AdapterView.OnItemClickListener {

    val ingredients = arrayListOf<Ingredient>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)

        val db = getAppDatabase(requireContext())

        val adapter = ArrayAdapter<Ingredient>(requireContext(), android.R.layout.simple_list_item_1, ingredients)
        view.search_ingredients_list.adapter = adapter
        view.search_ingredients_list.setOnItemClickListener(this)

        // Filter List
        view.filter_search.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {eb ->
                    GlobalScope.launch {
                        ingredients.replace(
                            db.recipeSearchDao()
                                .getAllUsedIngredients()
                                .filter { it.name.contains(eb, true) }
                        )
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })

        GlobalScope.launch {
            ingredients.replace(db.recipeSearchDao().getAllUsedIngredients())
            adapter.notifyDataSetChanged()
        }

        val builder = AlertDialog.Builder(activity).apply {
            setView(view)
            setTitle("Add Ingredient")
        }

        return builder.create()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelect(ingredients[position])
        dialog?.cancel()
    }
}