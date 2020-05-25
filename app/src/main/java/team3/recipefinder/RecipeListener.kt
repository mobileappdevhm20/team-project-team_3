package team3.recipefinder


import team3.recipefinder.model.Recipe
import team3.recipefinder.viewmodel.RecipeViewModel


class RecipeListener(private val viewModel: RecipeViewModel) {
    fun onEdit(t: Recipe) = viewModel.editPropertyDetails(t)
}