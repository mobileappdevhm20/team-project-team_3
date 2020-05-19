package team3.recipefinder.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun get(id: Int): Recipe

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Query("""SELECT i.* FROM ingredient i 
            INNER JOIN rel_recipe_ingredient r 
            ON i.id = r.ingredientId 
            WHERE r.recipeId = :recipeId""")
    fun getAllIngredientsByRecipe(recipeId: Int): List<Ingredient>

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Query("""INSERT INTO rel_recipe_ingredient (recipeId, ingredientId)
        VALUES (:recipeId, :ingredientId)""")
    fun assignIngredientToRecipe(ingredientId: Int, recipeId: Int)

    @Query("""DELETE FROM rel_recipe_ingredient
        WHERE recipeId = :recipeId AND ingredientId = :ingredientId""")
    fun removeIngredientFromRecipe(ingredientId: Int, recipeId: Int)

    @Insert
    fun insertStep(step: RecipeStep): Long

    @Query("""SELECT s.* FROM step s
            INNER JOIN rel_recipe_step r 
            ON s.id = r.stepId 
            WHERE r.recipeId = :recipeId""")
    fun getAllStepsByRecipe(recipeId: Int): List<RecipeStep>

    @Query("""INSERT INTO rel_recipe_step (recipeId, stepId)
        VALUES (:recipeId, :stepId)""")
    fun assignStepToRecipe(stepId: Int, recipeId: Int)

    @Query("""DELETE FROM rel_recipe_step
        WHERE recipeId = :recipeId AND stepId = :stepId""")
    fun removeStepFromRecipe(stepId: Int, recipeId: Int)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}