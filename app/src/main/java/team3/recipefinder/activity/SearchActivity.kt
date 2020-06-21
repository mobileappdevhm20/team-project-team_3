package team3.recipefinder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team3.recipefinder.R
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.dialog.SearchAddIngredientDialogFragment
import team3.recipefinder.logic.search.IngredientSearch
import team3.recipefinder.model.Ingredient
import team3.recipefinder.ui.ManagedChipsListView

class SearchActivity : AppCompatActivity() {

    private val ingredients = mutableListOf<Ingredient>()
    private val adapter by lazy { RecipeListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Search"

        val tagView = findViewById<ManagedChipsListView<Ingredient>>(R.id.search_tags)

        tagView.addButton(getString(R.string.search_add_ingredient)) {
            val dialog = SearchAddIngredientDialogFragment {
                ingredients.add(0, it)
                tagView.addChip(it)

                performSearchInBackground()
            }

            dialog.showNow(supportFragmentManager, "Add Ingredient")
        }

        tagView.deleteChipObserver = {
            ingredients.remove(it)
            performSearchInBackground()
        }

        search_result_recipes.adapter = adapter
        search_result_recipes.layoutManager = LinearLayoutManager(this)
    }

    private fun performSearchInBackground() = GlobalScope.launch { performSearch() }

    private fun performSearch() {
        val recipes = IngredientSearch.search(this, ingredients)
        adapter.recipes = recipes.map { it.recipe }.toMutableList()

        runOnUiThread { adapter.notifyDataSetChanged() }
    }
}