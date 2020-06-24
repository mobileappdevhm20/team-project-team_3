package team3.recipefinder.logic.search

import android.content.Context
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe

object IngredientSearch {

    private const val SCORE_THRESHOLD = 0.62f

    /**
     * Search recipes in the database based on ingredients.
     * @param context Context
     * @param ingredients List of ingredients
     *
     * @return List of all recipes with the search score (SCORE_THRESHOLD to 1.0)
     */
    fun search(context: Context, ingredients: List<Ingredient>): List<SearchResult> {
        val db = getAppDatabase(context)

        return db.recipeDao().getAllSync()
            .map { SearchResult(it, it.getRecipeScore(db, ingredients)) }
            .filter { it.score >= SCORE_THRESHOLD }
    }

    /**
     * Sort list of recipes and scores by score
     *
     * @return List of recipes
     */
    fun Collection<SearchResult>.sortByScore() = sortedByDescending { it.score }

    private fun Recipe.getRecipeScore(db: AppDatabase, ingredients: List<Ingredient>): Float {
        val ingredientIdsFromRecipe = db.recipeDao()
            .getAllIngredientsByRecipeSync(this.id).map { it.id }
        val ingredientIds = ingredients.map { it.id }

        val matching = ingredientIdsFromRecipe.sumBy { if (ingredientIds.contains(it)) 1 else 0 }

        return matching.toFloat() / ingredientIdsFromRecipe.count()
    }

    data class SearchResult(val recipe: Recipe, val score: Float)
}
