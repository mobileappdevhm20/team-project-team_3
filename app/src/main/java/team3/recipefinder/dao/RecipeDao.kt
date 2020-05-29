package team3.recipefinder.dao

import androidx.lifecycle.LiveData
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
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun get(id: Long): LiveData<Recipe>


    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Query("""SELECT i.* FROM ingredient i 
            INNER JOIN rel_recipe_ingredient r 
            ON i.id = r.ingredientId 
            WHERE r.recipeId = :recipeId""")
    fun getAllIngredientsByRecipe(recipeId: Long): LiveData<List<Ingredient>>


    @Query("SELECT * FROM ingredient")
    fun getAllIngredients(): LiveData<List<Ingredient>>


    @Insert

    fun insertIngredient(ingredient: Ingredient): Long

    @Query("""INSERT INTO rel_recipe_ingredient (recipeId, ingredientId,amount)
        VALUES (:recipeId, :ingredientId, :amount)""")
    fun assignIngredientToRecipe(ingredientId: Long, recipeId: Long, amount: String)

    @Query("""DELETE FROM rel_recipe_ingredient
        WHERE recipeId = :recipeId AND ingredientId = :ingredientId""")
    fun removeIngredientFromRecipe(ingredientId: Long, recipeId: Long)

    @Insert
    fun insertStep(step: RecipeStep): Long

    @Query("""SELECT s.* FROM step s
            INNER JOIN rel_recipe_step r 
            ON s.id = r.stepId 
            WHERE r.recipeId = :recipeId""")
    fun getAllStepsByRecipe(recipeId: Long): LiveData<List<RecipeStep>>

    @Query("SELECT * FROM step")
    fun getAllSteps(): LiveData<List<RecipeStep>>



    @Query("""INSERT INTO rel_recipe_step (recipeId, stepId)
        VALUES (:recipeId, :stepId)""")
    fun assignStepToRecipe(stepId: Long, recipeId: Long)

    @Query("""DELETE FROM rel_recipe_step
        WHERE recipeId = :recipeId AND stepId = :stepId""")
    fun removeStepFromRecipe(stepId: Long, recipeId: Long)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}