package team3.recipefinder.viewmodel.recipe.overview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

import kotlinx.coroutines.*
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.viewmodel.recipe.edit.EditRecipeActivity


class RecipeViewModel(val database: RecipeDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    var recipes = database.getAll()


    fun addRecipe(name: String) {
        uiScope.launch {
            val recipe = Recipe(0, name)
            addR(recipe)
        }
    }

    fun addIngredient(name: String) {
        uiScope.launch {
            val recipe = Ingredient(0, name)
            addI(recipe)
        }
    }

    private suspend fun addI(i: Ingredient) {
        withContext(Dispatchers.IO) {
            database.insertIngredient(i)
        }
    }

    private suspend fun addR(r: Recipe) {
        withContext(Dispatchers.IO) {
            database.insertRecipe(r)
        }
    }


    fun editPropertyDetails(r: Recipe) {
        showEditActivity(getApplication(), r)
    }


    private fun showEditActivity(context: Context, r: Recipe) {
        val message = r.id.toString()

        val intent = Intent(getApplication(), EditRecipeActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)

        }
        if (intent.resolveActivity(context.packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}