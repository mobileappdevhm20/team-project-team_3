package team3.recipefinder.viewmodel.recipe.overview

import android.app.Application
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
import team3.recipefinder.model.Recipe


class RecipeViewModel( application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var database: RecipeDao=  getAppDatabase(application).recipeDao()

    var recipes = database.getAll()

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedRecipe = MutableLiveData<Recipe>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedRecipe: LiveData<Recipe>
        get() = _navigateToSelectedRecipe



    fun addD(name :String) {
        uiScope.launch {
            val recipe = Recipe(0, name)
            add(recipe)
        }
    }

    private suspend fun add(t: Recipe) {
        withContext(Dispatchers.IO) {
            database.insertRecipe(t)
        }
    }

    fun displayPropertyDetails(r: Recipe) {
        Log.i("RecipeClick", "Bind called with Recipe$r")

        _navigateToSelectedRecipe.value = r
    }
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedRecipe.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}