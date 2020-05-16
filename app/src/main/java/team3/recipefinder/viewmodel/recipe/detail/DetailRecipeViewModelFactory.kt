package team3.recipefinder.viewmodel.recipe.detail;

import android.app.Application;
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider;

import team3.recipefinder.model.Recipe;


class DetailRecipeViewModelFactory (
    private val marsProperty: Recipe,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailRecipeViewModel::class.java)) {
            return DetailRecipeViewModel(marsProperty, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    }
