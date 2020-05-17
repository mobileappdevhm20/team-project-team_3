package team3.recipefinder.viewmodel.recipe.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

import kotlinx.coroutines.*
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.model.Recipe


class RecipeViewModel(val database: RecipeDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipes = database.getAll()


    fun addRecipe() {
        uiScope.launch {
            val recipe = Recipe(0, "hallo")
            add(recipe)
        }
    }

    private suspend fun add(t: Recipe) {
        withContext(Dispatchers.IO) {
            database.insertRecipe(t)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}