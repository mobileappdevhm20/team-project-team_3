package team3.recipefinder.dao

import androidx.room.Dao
import androidx.room.Query
import team3.recipefinder.model.Ingredient

@Dao
interface RecipeSearchDao {

    @Query(
        """
        SELECT i.* FROM ingredient i
        WHERE EXISTS (
            SELECT * FROM rel_recipe_ingredient WHERE ingredientId = i.id
        )"""
    )
    fun getAllUsedIngredients(): List<Ingredient>
}
