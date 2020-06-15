package team3.recipefinder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import team3.recipefinder.R
import team3.recipefinder.dialog.SearchAddIngredientDialogFragment
import team3.recipefinder.model.Ingredient
import team3.recipefinder.ui.ManagedChipsListView

class SearchActivity : AppCompatActivity() {

    private val ingredients = mutableListOf<Ingredient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Search"

        val tagView = findViewById<ManagedChipsListView<Ingredient>>(R.id.search_tags)

        tagView.addButton("Add") {
            val dialog = SearchAddIngredientDialogFragment {
                ingredients.add(0, it)
                tagView.addChip(it)
            }

            dialog.showNow(supportFragmentManager, "Add Ingredient")
        }

        tagView.deleteChipObserver = {
            ingredients.remove(it)
        }
    }
}