package team3.recipefinder.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import team3.recipefinder.model.Cookbook
import team3.recipefinder.model.Recipe

@Dao
interface CookbookDao {

    @Query("SELECT * FROM cookbook")
    fun getAll(): List<Cookbook>

    @Query("SELECT * FROM cookbook WHERE id = :id")
    fun get(id: Int): Cookbook

    @Insert
    fun insertCookbook(recipe: Cookbook): Long

    @Query(
        """SELECT rc.* FROM recipe rc
            INNER JOIN rel_cookbook_recipes r 
            ON rc.id = r.recipeId 
            WHERE r.cookbookId = :cookbookId"""
    )
    fun getAllRecipesByCookbook(cookbookId: Int): List<Recipe>

    @Query(
        """INSERT INTO rel_cookbook_recipes (cookbookId, recipeId)
        VALUES (:cookbookId, :recipeId)"""
    )
    fun addRecipeToCookbook(recipeId: Int, cookbookId: Int)

    @Query(
        """DELETE FROM rel_cookbook_recipes
        WHERE recipeId = :recipeId AND cookbookId = :cookbookId"""
    )
    fun removeRecipeFromCookbook(recipeId: Int, cookbookId: Int)

    @Delete
    fun deleteCookbook(cookbook: Cookbook)
}
