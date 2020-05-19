package team3.recipefinder.viewmodel.recipe.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep

class EditViewModel(
    private val recipeKey: Long = 0,
    dataSource: RecipeDao,
    application: Application
) :
    AndroidViewModel(application) {


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val database = dataSource


    val recipe = database.get(recipeKey)
    val ingrediants = database.getAllIngredients()
    var steps = database.getAllStepsByRecipe(recipeKey)
//var steps= database.getAllSteps()

    fun addIngredient(name: String) {
        Log.i("adddStip", "step CALLED ${name}")

    }

    private suspend fun addS(s: RecipeStep) {
        Log.i("jfdkslf", "dsjklf $s")

        withContext(Dispatchers.IO) {

         var a =  database.insertStep(s)
            Log.i("jfdkslf", " id recipe ${recipe.value!!.id}")
            Log.i("jfdkslf", " id step ${s.id}")

            database.assignStepToRecipe(a, recipe.value!!.id)
var aaa = database.getAllSteps()
            Log.i("jfdkslf", "dsjklf ${aaa}")

         //   steps = database.getAllStepsByRecipe(recipe.value!!.id)
            Log.i("jfdkslf", "steps recipe ${steps}")

        }
    }

    fun addStep(name: String) {
        uiScope.launch {
            val step = RecipeStep(0, name)
            addS(step)
        }
    }
}

