package team3.recipefinder.viewmodel.recipe.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.Recipe

class RecipeViewModel(val database: RecipeDao, app: Application) : AndroidViewModel(app) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipes = database.getAll()

    fun addTimer() {
        uiScope.launch {
            val recipe = Recipe(id=1,name = "hallo")
            Log.i("hallo", "add")
            add(recipe)
        }
    }
    private suspend fun add(r: Recipe) {
        withContext(Dispatchers.IO) {
            database.insertRecipe(r)
        }
    }
}