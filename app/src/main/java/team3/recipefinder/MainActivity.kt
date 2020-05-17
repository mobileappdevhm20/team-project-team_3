package team3.recipefinder

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.MainActivityBinding
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep
import team3.recipefinder.viewmodel.recipe.detail.DetailRecipeActivity
import team3.recipefinder.viewmodel.recipe.detail.RecipeAdapter
import team3.recipefinder.viewmodel.recipe.detail.RecipeViewModel
import team3.recipefinder.viewmodel.recipe.detail.RecipeViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.main_activity)

        // Setup DataBinding
        var binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            RecipeViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Bind model to layout
        binding.lifecycleOwner = this
        binding.recipeViewModel = viewModel

        // Register Adapter for the RecyclerView
        val adapter = RecipeAdapter()
        binding.recipeView.adapter = adapter

        // Observe LiveData
        viewModel.recipes.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED")
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })


    }
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
//val Recipe = new Recipe()
        val recipe= Recipe(1,"hallo")
        val ingredient = listOf(Ingredient(1,"ia"),Ingredient(2,"ib"))
        val step = listOf(RecipeStep(1,"sa"),RecipeStep(2,"sb"))

        val message = "hallo"
        val intent = Intent(this, DetailRecipeActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)


    }
}
