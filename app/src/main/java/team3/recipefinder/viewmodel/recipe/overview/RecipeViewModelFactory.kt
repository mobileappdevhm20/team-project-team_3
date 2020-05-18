package team3.recipefinder.viewmodel.recipe.overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.dao.RecipeDao
import java.lang.IllegalArgumentException
import javax.sql.CommonDataSource

class RecipeViewModelFactory(
    private val dataSource: RecipeDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(
                dataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}