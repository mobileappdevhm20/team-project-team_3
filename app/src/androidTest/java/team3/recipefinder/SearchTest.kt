package team3.recipefinder

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.logic.search.IngredientSearch
import team3.recipefinder.logic.search.IngredientSearch.sortByScore
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe

@RunWith(AndroidJUnit4::class)
class SearchTest {

    lateinit var db: AppDatabase
    lateinit var context: Context

    @Before
    fun initialize() {
        context = ApplicationProvider.getApplicationContext<Context>()
        // Start with a completely fresh database for every test
        context.deleteDatabase("app_database")
        db = getAppDatabase(context)
    }

    @Test
    fun testSimpleSearch() {
        db.recipeDao().apply {
            // Recipes
            insertRecipe(Recipe(0, "testRecipe", "description", "imageUrl")) // ID 1

            // Ingredients
            insertIngredient(Ingredient(0, "Tomato")) // ID 1
            insertIngredient(Ingredient(0, "Milk")) // ID 2
            insertIngredient(Ingredient(0, "Butter")) // ID 3

            // Relations
            assignIngredientToRecipe(1, 1, "X L")
            assignIngredientToRecipe(2, 1, "X L")
        }

        val recipes = IngredientSearch.search(context, listOf(
            Ingredient(1, "Tomato"),
            Ingredient(2, "Milk")
        ))

        Assert.assertArrayEquals(arrayOf(1L), recipes.map { it.recipe.id }.toTypedArray())
        Assert.assertEquals(1.0f, recipes[0].score)
    }

    @Test
    fun testScore() {
        db.recipeDao().apply {
            // Recipes
            insertRecipe(Recipe(0, "testRecipe", "description", "imageUrl")) // ID 1

            // Ingredients
            insertIngredient(Ingredient(0, "Tomato")) // ID 1
            insertIngredient(Ingredient(0, "Milk")) // ID 2
            insertIngredient(Ingredient(0, "Butter")) // ID 3

            // Relations
            assignIngredientToRecipe(1, 1, "X L")
            assignIngredientToRecipe(2, 1, "X L")
            assignIngredientToRecipe(3, 1, "X L")
        }

        val recipes = IngredientSearch.search(context, listOf(
            Ingredient(1, "Tomato"),
            Ingredient(2, "Milk")
        ))

        Assert.assertArrayEquals(arrayOf(1L), recipes.map { it.recipe.id }.toTypedArray())
        Assert.assertTrue(Math.abs(recipes[0].score - 0.66f) < 0.01f)
    }

    @Test
    fun testFail() {
        db.recipeDao().apply {
            // Recipes
            insertRecipe(Recipe(0, "testRecipe", "description", "imageUrl")) // ID 1

            // Ingredients
            insertIngredient(Ingredient(0, "Tomato")) // ID 1
            insertIngredient(Ingredient(0, "Milk")) // ID 2
            insertIngredient(Ingredient(0, "Butter")) // ID 3

            // Relations
            assignIngredientToRecipe(1, 1, "X L")
            assignIngredientToRecipe(2, 1, "X L")
            assignIngredientToRecipe(3, 1, "X L")
        }

        val recipes = IngredientSearch.search(context, listOf(
            Ingredient(1, "Tomato")
        ))

        Assert.assertEquals(0, recipes.size)
    }

    @Test
    fun testSort() {
        val searchResult = listOf<IngredientSearch.SearchResult>(
            IngredientSearch.SearchResult(Recipe(1, "", "", ""), 0.7f),
            IngredientSearch.SearchResult(Recipe(2, "", "", ""), 0.9f),
            IngredientSearch.SearchResult(Recipe(3, "", "", ""), 0.65f)
        )

        Assert.assertArrayEquals(arrayOf(2L, 1L, 3L),
            searchResult.sortByScore().map { it.recipe.id }.toTypedArray())
    }

}