package team3.recipefinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.RecipeStep

class RecipeDetailViewModel(
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

    fun getRecipeUrl(): String? {
        return this.recipe.value?.imageUrl
    }

    fun enableEdit() {
        _editMode.value = true
    }

    fun disableEdit() {
        _editMode.value = false
    }

    fun addIngredient(id: Long, amount: String) {
        uiScope.launch {
            addI(id, amount)
        }
    }

    private suspend fun addI(i: Long, amount: String) {
        withContext(Dispatchers.IO) {
            database.assignIngredientToRecipe(i, recipe.value!!.id, amount)
        }
    }

    private suspend fun addS(s: RecipeStep) {
        withContext(Dispatchers.IO) {
            var k = database.insertStep(s)
            database.assignStepToRecipe(k, recipe.value!!.id)
        }
    }

    private suspend fun updateIngredient(id: Long, amount: String) {
        withContext(Dispatchers.IO) {
            database.updateAmountForRecipe(id, amount)
        }
    }

    private suspend fun deleteIngredientFromRecipe(id: Long) {
        withContext(Dispatchers.IO) {
            database.deleteIngredientFromRelationById(id)
        }
    }

    fun updateIngredientAmount(id: Long, amount: String) {
        uiScope.launch {
            updateIngredient(id, amount)
        }
    }

    fun deleteIngredientRelation(id: Long) {
        uiScope.launch {
            deleteIngredientFromRecipe(id)
        }
    }

    fun addStep(name: String) {
        uiScope.launch {
            val step = RecipeStep(0, name)
            addS(step)
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

    private suspend fun updateStepById(stepId: Long, description: String) {
        withContext(Dispatchers.IO) {
            database.updateStep(stepId, description)
        }
    }

    fun updateStep(stepId: Long, description: String) {
        uiScope.launch {
            updateStepById(stepId, description)
        }
    }

    private suspend fun removeStepFromRecipeById(stepId: Long, recipeId: Long) {
        withContext(Dispatchers.IO) {
            database.removeStepFromRecipe(stepId, recipeId)
        }
    }

    fun removeStepFromRecipe(stepId: Long) {
        uiScope.launch {
            removeStepFromRecipeById(stepId, recipeKey)
        }
    }

    private suspend fun deleteRecipeIngredientRelationById(recipeId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteIngredientFromRelationById(recipeId)
        }
    }

    fun deleteRecipeIngredientRelation() {
        uiScope.launch {
            deleteRecipeIngredientRelationById(recipeKey)
        }
    }

    private suspend fun deleteRecipeStepRelationById(recipeId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteRecipeStepRelations(recipeId)
        }
    }

    fun deleteRecipeStepRelation() {
        uiScope.launch {
            deleteRecipeStepRelationById(recipeKey)
        }
    }

    private suspend fun deleteRecipeById(recipeId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteRecipeById(recipeId)
        }
    }

    fun deleteRecipe() {
        uiScope.launch {
            deleteRecipeById(recipeKey)
        }
    }

    private suspend fun updateRecipeNameById(recipeId: Long, name: String) {
        withContext(Dispatchers.IO) {
            database.updateRecipeName(recipeId, name)
        }
    }

    fun updateRecipeName(name: String) {
        uiScope.launch {
            updateRecipeNameById(recipeKey, name)
        }
    }

    private suspend fun updateRecipePictureById(recipeId: Long, url: String) {
        withContext(Dispatchers.IO) {
            database.updateRecipeImageUrl(recipeId, url)
        }
    }

    fun updateRecipePicture(url: String) {
        uiScope.launch {
            updateRecipePictureById(recipeKey, url)
        }
    }

    private suspend fun updateRecipeServingsById(recipeId: Long, servings: Int) {
        withContext(Dispatchers.IO) {
            database.updateRecipeServings(recipeId, servings)
        }
    }

    fun updateRecipeServings(servings: Int) {
        uiScope.launch {
            updateRecipeServingsById(recipeKey, servings)
        }
    }
}
