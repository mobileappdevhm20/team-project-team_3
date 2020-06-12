package team3.recipefinder.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.CrawlRecipe
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe

class CrawlRecipeViewModel (val database: RecipeDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun importRecipe(recipe: CrawlRecipe) {
        uiScope.launch {
            importR(recipe)
        }
    }

    private suspend fun importR(recipe: CrawlRecipe) {
        withContext(Dispatchers.IO) {
            Log.i("CrawlRecipeViewModel", "Creating recipe")
            val recipeId = database.insertRecipe(Recipe(0, recipe.title, recipe.subtitle, "Sampleurl"))
            // Add Ingredients
            Log.i("CrawlRecipeViewModel", "Iterating throught ingredientGroups")
            for (ingredientGroup in recipe.ingredientGroups) {
                Log.i("CrawlRecipeViewModel", "Ingredientgroup: " + ingredientGroup)
                Log.i("CrawlRecipeViewModel", "Iterating throught ingredients")
                for (ingredient in ingredientGroup.ingredients) {
                    Log.i("CrawlRecipeViewModel", "Ingredient: " + ingredient.name + " " + ingredient.unit + " " + ingredient.amount)
                    var ingredientId = database.getIngredientId(ingredient.name)
                    if ( ingredientId == 0L) {
                        ingredientId = database.insertIngredient(Ingredient(0, ingredient.name))
                    }
                    println("-----------------------")
                    println(ingredientId)
                    println("-----------------------")
                    // Add relation
                    val amount = ingredient.amount
                    var amountString = amount.toString()
                    if (amount.rem(1).compareTo(0.0) == 0) {
                        amountString = amount.toInt().toString()
                    }
                    amountString = amountString + " " + ingredient.unit
                    Log.i("CrawlRecipeViewModel", "Relation: " + recipeId + " " + ingredientId + " " + amountString)
                    database.assignIngredientToRecipe(recipeId, ingredientId, amountString)
                }
            }
            // Add Instructions
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}