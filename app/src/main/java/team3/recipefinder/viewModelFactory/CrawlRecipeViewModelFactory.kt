package team3.recipefinder.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.viewmodel.CrawlRecipeViewModel
import team3.recipefinder.viewmodel.RecipeViewModel
import java.lang.IllegalArgumentException

class CrawlRecipeViewModelFactory(
    private val dataSource: RecipeDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrawlRecipeViewModel::class.java)) {
            return CrawlRecipeViewModel(
                dataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}