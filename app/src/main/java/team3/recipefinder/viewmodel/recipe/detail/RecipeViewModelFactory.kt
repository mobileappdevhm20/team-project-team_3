package team3.recipefinder.viewmodel.recipe.detail;

import android.app.Application;
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider;
import team3.recipefinder.database.AppDatabase


class RecipeViewModelFactory(
    private val dataSource: AppDatabase,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    }
