package team3.recipefinder.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import team3.recipefinder.activity.RecipeDetailActivity
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.Recipe

class RecipeViewModel(val database: RecipeDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipes = database.getAll()

    fun addRecipe(name: String) {
        uiScope.launch {
            val recipe = Recipe(0, name, "Test Description", "BeispielUrl", 1)
            addR(recipe)
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
        val intent = Intent(getApplication(), RecipeDetailActivity::class.java).apply {
            putExtra("recipe_id", r.id)
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
