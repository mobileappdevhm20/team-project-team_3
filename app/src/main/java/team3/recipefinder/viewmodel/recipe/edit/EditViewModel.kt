package team3.recipefinder.viewmodel.recipe.edit

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val ingredients = database.getAllIngredients()
    val stepsRecipe = database.getAllStepsByRecipe(recipeKey)
    val ingredientRecipe = database.getAllIngredientsByRecipe(recipeKey)

    private val _editMode = MutableLiveData<Boolean>()

    // The external immutable LiveData for the navigation property
    val editMode: LiveData<Boolean>
        get() = _editMode


    init {
        disableEdit()
    }


    fun enableEdit() {
        _editMode.value = true
    }

    fun disableEdit() {
        _editMode.value = false
    }

    fun addIngredient(id: Long) {
        uiScope.launch {
            addI(id)
        }
    }

    private suspend fun addI(i: Long) {
        withContext(Dispatchers.IO) {
            database.assignIngredientToRecipe(i, recipe.value!!.id)
        }
    }

    private suspend fun addS(s: RecipeStep) {
        withContext(Dispatchers.IO) {
            var k = database.insertStep(s)
            database.assignStepToRecipe(k, recipe.value!!.id)
        }
    }

    fun addStep(name: String) {
        uiScope.launch {
            val step = RecipeStep(0, name)
            addS(step)
        }
    }

}

