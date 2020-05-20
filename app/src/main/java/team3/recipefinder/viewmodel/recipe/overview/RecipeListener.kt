package team3.recipefinder.viewmodel.recipe.overview


import team3.recipefinder.model.Recipe


class RecipeListener(private val viewModel: RecipeViewModel) {
    fun onEdit(t: Recipe) = viewModel.editPropertyDetails(t)
}