package team3.recipefinder.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.IngredientAmount
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe")
    fun getAllSync(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun get(id: Long): LiveData<Recipe>

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Query("UPDATE recipe SET name = :name WHERE id = :recipeId")
    fun updateRecipeName(recipeId: Long, name: String)

    @Query("UPDATE recipe SET servings = :servings WHERE id = :recipeId")
    fun updateRecipeServings(recipeId: Long, servings: Int)

    @Query("SELECT id FROM recipe WHERE name = :name AND description = :description")
    fun getRecipeId(name: String, description: String): Long

    @Query(
        """SELECT i.*,r.amount, r.id as relId FROM ingredient i 
            INNER JOIN rel_recipe_ingredient r 
            ON i.id = r.ingredientId 
            WHERE r.recipeId = :recipeId"""
    )
    fun getAllIngredientsByRecipe(recipeId: Long): LiveData<List<IngredientAmount>>

    @Query(
        """SELECT i.*,r.amount, r.id as relId FROM ingredient i 
            INNER JOIN rel_recipe_ingredient r 
            ON i.id = r.ingredientId 
            WHERE r.recipeId = :recipeId"""
    )
    fun getAllIngredientsByRecipeSync(recipeId: Long): List<IngredientAmount>

    @Query("SELECT * FROM ingredient")
    fun getAllIngredients(): LiveData<List<Ingredient>>

    @Query("SELECT id FROM ingredient WHERE name = :name")
    fun getIngredientId(name: String): Long

    @Query("UPDATE rel_recipe_ingredient SET amount = :amount WHERE id = :id")
    fun updateAmountForRecipe(id: Long, amount: String)

    @Query("DELETE FROM rel_recipe_ingredient WHERE id = :id")
    fun deleteIngredientFromRelationById(id: Long)

    @Query("SELECT * FROM ingredient WHERE id = :id")
    fun getIngredientById(id: Long): Ingredient

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Query(
        """INSERT INTO rel_recipe_ingredient (recipeId, ingredientId,amount)
        VALUES (:recipeId, :ingredientId, :amount)"""
    )
    fun assignIngredientToRecipe(ingredientId: Long, recipeId: Long, amount: String)

    @Query(
        """DELETE FROM rel_recipe_ingredient
        WHERE recipeId = :recipeId AND ingredientId = :ingredientId"""
    )
    fun removeIngredientFromRecipe(ingredientId: Long, recipeId: Long)

    @Query(
        """DELETE FROM rel_recipe_ingredient
            WHERE recipeId = :recipeId"""
    )
    fun deleteRecipeIngredientRelations(recipeId: Long)

    @Insert
    fun insertStep(step: RecipeStep): Long

    @Query("UPDATE step SET description = :description WHERE id = :stepId")
    fun updateStep(stepId: Long, description: String)

    @Delete
    fun deleteStep(step: RecipeStep)

    @Query(
        """SELECT s.* FROM step s
            INNER JOIN rel_recipe_step r 
            ON s.id = r.stepId 
            WHERE r.recipeId = :recipeId"""
    )
    fun getAllStepsByRecipe(recipeId: Long): LiveData<List<RecipeStep>>

    @Query(
        """INSERT INTO rel_recipe_step (recipeId, stepId)
        VALUES (:recipeId, :stepId)"""
    )
    fun assignStepToRecipe(stepId: Long, recipeId: Long)

    @Query(
        """DELETE FROM rel_recipe_step
        WHERE recipeId = :recipeId AND stepId = :stepId"""
    )
    fun removeStepFromRecipe(stepId: Long, recipeId: Long)

    @Query(
        """DELETE FROM rel_recipe_step
            WHERE recipeId = :recipeId"""
    )
    fun deleteRecipeStepRelations(recipeId: Long)

    @Query(
        """DELETE FROM recipe
            WHERE id = :recipeId"""
    )
    fun deleteRecipeById(recipeId: Long)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}
