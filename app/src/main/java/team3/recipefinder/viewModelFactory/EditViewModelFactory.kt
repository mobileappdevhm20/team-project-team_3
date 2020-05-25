package team3.recipefinder.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.viewmodel.RecipeDetailViewModel
import java.lang.IllegalArgumentException

class EditViewModelFactory(
    private val recipeKey: Long,
    private val dataSource: RecipeDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            return RecipeDetailViewModel(
                recipeKey,
                dataSource,
                application

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}