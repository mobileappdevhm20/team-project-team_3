package team3.recipefinder.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team3.recipefinder.R
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.dialog.SearchAddIngredientDialogFragment
import team3.recipefinder.logic.search.IngredientSearch
import team3.recipefinder.model.Ingredient
import team3.recipefinder.ui.ManagedChipsListView

class SearchActivity : AppCompatActivity() {

    private val ingredients = mutableListOf<Ingredient>()

    private val tagView by lazy {
        findViewById<ManagedChipsListView<Ingredient>>(R.id.search_tags)
    }

    private val adapter by lazy {
        RecipeListAdapter(this) {
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipe_id", it.id)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Search a Recipe"

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLongArray("ingredients", ingredients.map { it.id }.toLongArray())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val ingredientsLong = savedInstanceState.getLongArray("ingredients")

        if (ingredientsLong != null) {
            GlobalScope.launch {
                val dao = getAppDatabase(this@SearchActivity).recipeDao()
                ingredients.addAll(ingredientsLong.map { dao.getIngredientById(it) })
                runOnUiThread { ingredients.forEach(tagView::addChip) }
                performSearch()
            }
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun performSearchInBackground() = GlobalScope.launch { performSearch() }

    private fun performSearch() {
        val recipes = IngredientSearch.search(this, ingredients)
        adapter.recipes = recipes.map { it.recipe }.toMutableList()

        runOnUiThread { adapter.notifyDataSetChanged() }
    }
}
