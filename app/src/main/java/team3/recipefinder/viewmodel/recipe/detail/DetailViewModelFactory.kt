package team3.recipefinder.viewmodel.recipe.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.viewmodel.recipe.overview.RecipeViewModel
import java.lang.IllegalArgumentException

class DetailViewModelFactory(
    private val recipeKey: Int,
    private val dataSource: RecipeDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(
                recipeKey,
                dataSource,
                application

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}