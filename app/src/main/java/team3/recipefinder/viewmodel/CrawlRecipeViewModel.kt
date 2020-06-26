package team3.recipefinder.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.CrawlRecipe
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep
import team3.recipefinder.util.extractInstructions

class CrawlRecipeViewModel(val database: RecipeDao, application: Application) :
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
            var imageUrl = "SampleUrl"
            if (recipe.hasImage) {
                imageUrl = "https://img.chefkoch-cdn.de/rezepte/" + recipe.id + "/bilder/" + recipe.previewImageId + "/crop-600x400/"
            }
            val recipeId =
                database.insertRecipe(Recipe(0, recipe.title, recipe.subtitle, imageUrl, recipe.servings))
            // Add Ingredients
            Log.i("CrawlRecipeViewModel", "Iterating throught ingredientGroups")
            for (ingredientGroup in recipe.ingredientGroups) {
                Log.i("CrawlRecipeViewModel", "Ingredientgroup: " + ingredientGroup)
                Log.i("CrawlRecipeViewModel", "Iterating throught ingredients")
                for (ingredient in ingredientGroup.ingredients) {
                    Log.i(
                        "CrawlRecipeViewModel",
                        "Ingredient: " + ingredient.name + " " + ingredient.unit + " " + ingredient.amount
                    )
                    var ingredientId = database.getIngredientId(ingredient.name)
                    if (ingredientId == 0L) {
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
                    Log.i(
                        "CrawlRecipeViewModel",
                        "Relation: " + recipeId + " " + ingredientId + " " + amountString
                    )
                    database.assignIngredientToRecipe(ingredientId, recipeId, amountString)
                }
            }
            val instructionList = extractInstructions(recipe)
            // Add Instructions
            for (instruction in instructionList) {
                val stepId = database.insertStep(RecipeStep(0, instruction))
                database.assignStepToRecipe(stepId, recipeId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
