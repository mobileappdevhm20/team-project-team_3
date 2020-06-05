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
import team3.recipefinder.model.Cookbook
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    lateinit var db: AppDatabase

    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Start with a completely fresh database for every test
        context.deleteDatabase("app_database")
        db = getAppDatabase(context)
    }

    @Test
    fun insertShouldIncrementId() {
        Assert.assertEquals(1,
            db.recipeDao().insertStep(RecipeStep(0, "test")))
        Assert.assertEquals(2,
            db.recipeDao().insertStep(RecipeStep(0, "test")))

        Assert.assertEquals(1,
            db.recipeDao().insertRecipe(Recipe(0, "test","description","imageUrl")))
        Assert.assertEquals(2,
            db.recipeDao().insertRecipe(Recipe(0, "test","description","imageUrl")))

        Assert.assertEquals(1,
            db.recipeDao().insertIngredient(Ingredient(0, "test")))
        Assert.assertEquals(2,
            db.recipeDao().insertIngredient(Ingredient(0, "test")))
    }

    @Test
    fun testInsertRecipe() {
        // Insert recipe
        db.recipeDao().apply {
            insertRecipe(Recipe(0, "testRecipe","description","imageUrl")) // ID 1
            insertRecipe(Recipe(0, "testRecipe2","description","imageUrl")) // ID 2
        }

        // Test getAll
        val expected = listOf("testRecipe", "testRecipe2")
        Assert.assertEquals(expected, db.recipeDao().getAll().value?.map { it.name })

        // Test get
        Assert.assertEquals("testRecipe2", db.recipeDao().get(2).value?.name)
    }

    @Test
    fun testIngredients() {
        // Insert recipes and ingredients
        db.recipeDao().apply {
            // Recipes
            insertRecipe(Recipe(0, "testRecipe","description","imageUrl")) // ID 1
            insertRecipe(Recipe(0, "testRecipe2","description","imageUrl")) // ID 2

            // Ingredients
            insertIngredient(Ingredient(0, "Tomato")) // ID 1
            insertIngredient(Ingredient(0, "Milk")) // ID 2
            insertIngredient(Ingredient(0, "Butter")) // ID 3

            // Relations
            assignIngredientToRecipe(1, 1,"X L")
            assignIngredientToRecipe(2, 1,"X L")
            assignIngredientToRecipe(1, 2,"X L")
            assignIngredientToRecipe(3, 2,"X L")

            // Remove Tomato from recipe 2
            removeIngredientFromRecipe(1, 2)
        }

        // Test ingredients of recipe 1
        val expected = listOf("Tomato", "Milk")
        Assert.assertEquals(expected,
            db.recipeDao().getAllIngredientsByRecipe(1).value?.map { it.name })

        // Test ingredients of recipe 2
        val expected2 = listOf("Butter")
        Assert.assertEquals(expected2,
            db.recipeDao().getAllIngredientsByRecipe(2).value?.map { it.name })
    }

    @Test
    fun testSteps() {
        // Insert recipes and ingredients
        db.recipeDao().apply {
            // Recipes
            insertRecipe(Recipe(0, "testRecipe","description","imageUrl")) // ID 1

            // Steps
            insertStep(RecipeStep(0, "Peal the banana")) // ID 1
            insertStep(RecipeStep(0, "Peal the avocado")) // ID 2
            insertStep(RecipeStep(0, "Pop the corn")) // ID 3

            // Relations
            assignStepToRecipe(1, 1)
            assignStepToRecipe(2, 1)
            assignStepToRecipe(3, 1)

            removeStepFromRecipe(2, 1)
        }

        // Test ingredients of recipe 1
        val expected = listOf("Peal the banana", "Pop the corn")
        Assert.assertEquals(expected,
            db.recipeDao().getAllStepsByRecipe(1).value?.map { it.description })
    }

    @Test
    fun testDeleteRecipe() {
        // Insert recipe
        db.recipeDao().apply {
            insertRecipe(Recipe(0, "testRecipe","description","imageUrl")) // ID 1
            insertRecipe(Recipe(0, "testRecipe2","description","imageUrl")) // ID 2

            deleteRecipe(Recipe(1, "testRecipe","description","imageUrl"))
        }

        // Test getAll
        val expected = listOf("testRecipe2")
        Assert.assertEquals(expected, db.recipeDao().getAll().value?.map { it.name })
    }

    @Test
    fun testCookbook() {
        // Insert cookbooks
        db.cookbookDao().apply {
            insertCookbook(Cookbook(0, "testCookbook")) // ID 1
            insertCookbook(Cookbook(0, "testCookbook2")) // ID 2
        }

        // Test getAll
        val expected = listOf("testCookbook", "testCookbook2")
        Assert.assertEquals(expected, db.cookbookDao().getAll().map { it.name })

        // Test get
        Assert.assertEquals("testCookbook2", db.cookbookDao().get(2).name)
    }

    @Test
    fun testCookbookManagement() {
        // Insert recipes
        db.recipeDao().apply {
            insertRecipe(Recipe(0, "testRecipe","description","imageUrl")) // ID 1
            insertRecipe(Recipe(0, "testRecipe2","description","imageUrl")) // ID 2
        }
        // Insert cookbooks
        db.cookbookDao().apply {
            insertCookbook(Cookbook(0, "testCookbook")) // ID 1
            insertCookbook(Cookbook(0, "testCookbook2")) // ID 2

            // Relations
            addRecipeToCookbook(1, 1)
            addRecipeToCookbook(1, 2)
            addRecipeToCookbook(2, 1)
            addRecipeToCookbook(2, 2)

            removeRecipeFromCookbook(2,2)
        }

        // Test ingredients of recipe 1
        val expected = listOf("testRecipe", "testRecipe2")
        Assert.assertEquals(expected,
            db.cookbookDao().getAllRecipesByCookbook(1).map { it.name })

        // Test ingredients of recipe 2
        val expected2 = listOf("testRecipe")
        Assert.assertEquals(expected2,
            db.cookbookDao().getAllRecipesByCookbook(2).map { it.name })
    }

    @Test
    fun deleteCookbookTest() {
        // Insert cookbooks
        db.cookbookDao().apply {
            insertCookbook(Cookbook(0, "testCookbook")) // ID 1
            insertCookbook(Cookbook(0, "testCookbook2")) // ID 2

            deleteCookbook(Cookbook(1, "testCookbook"))
        }

        // Test getAll
        val expected = listOf("testCookbook2")
        Assert.assertEquals(expected, db.cookbookDao().getAll().map { it.name })
    }
}