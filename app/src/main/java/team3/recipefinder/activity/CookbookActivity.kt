package team3.recipefinder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cookbook.*
import kotlinx.android.synthetic.main.main_activity.view.*
import team3.recipefinder.R
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.model.Recipe

class CookbookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cookbook)

        val mockedRecipeData = listOf(
            Recipe(1, "Cake"),
            Recipe(2, "Tomato Soup"),
            Recipe(3, "Beer soup")
        )

        recipeList.layoutManager = LinearLayoutManager(this)

        val recipeListAdapter = RecipeListAdapter(this)
        recipeList.adapter = recipeListAdapter
        recipeListAdapter.recipes.addAll(mockedRecipeData)
        recipeListAdapter.notifyDataSetChanged()

        setSupportActionBar(findViewById(R.id.toolbar))
        title = "Cookbook name"
    }
}
