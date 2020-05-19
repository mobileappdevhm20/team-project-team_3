package team3.recipefinder.viewmodel.recipe.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.viewmodel.recipe.overview.RecipeViewModel
import java.lang.IllegalArgumentException

class EditViewModelFactory(
    private val recipeKey: Long,
    private val dataSource: RecipeDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(
                recipeKey,
                dataSource,
                application

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}