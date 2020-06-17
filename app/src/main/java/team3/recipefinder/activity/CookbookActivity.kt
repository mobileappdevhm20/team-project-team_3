package team3.recipefinder.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cookbook.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team3.recipefinder.R
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.database.getAppDatabase

class CookbookActivity : AppCompatActivity() {

    val db by lazy {
        getAppDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cookbook)

        recipeList.layoutManager = LinearLayoutManager(this)

        val recipeListAdapter = RecipeListAdapter(this)
        recipeList.adapter = recipeListAdapter
        recipeListAdapter.notifyDataSetChanged()

        setSupportActionBar(findViewById(R.id.toolbar))

        // Get cookbook
        val cookbookId = intent.getLongExtra("cookbook", -1)

        GlobalScope.launch {
            // Set toolbar title
            val cookbook = db.cookbookDao().get(cookbookId.toInt())
            title = cookbook.name

            // Recipe list
            val recipes = db.cookbookDao().getAllRecipesByCookbook(cookbookId.toInt())

            recipeListAdapter.let {
                it.recipes.clear()
                it.recipes.addAll(recipes)
                it.notifyDataSetChanged()
            }
        }
    }
}
