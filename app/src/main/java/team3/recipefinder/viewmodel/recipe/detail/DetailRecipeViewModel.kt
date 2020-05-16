package team3.recipefinder.viewmodel.recipe.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import team3.recipefinder.model.Recipe

class DetailRecipeViewModel(recipe: String, app: Application) : AndroidViewModel(app) {

    private val _recipe = MutableLiveData<String>()
    val recipe: LiveData<String>
        get() = _recipe

    init {
        _recipe.value = recipe
    }

}