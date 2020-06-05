package team3.recipefinder

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import team3.recipefinder.activity.RecipeDetailActivity
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep

@RunWith(AndroidJUnit4::class)
class RecipeActivityTest {

    lateinit var db: AppDatabase

    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Start with a completely fresh database for every test
        context.deleteDatabase("app_database")
        db = getAppDatabase(context)
    }

    @Test
    fun startRecipeActivity() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val recipeId = db.recipeDao().insertRecipe(Recipe(0, "Test-Recipe","description","imageUrl"))

        val addStepToRecipe: (Long) -> Unit = { db.recipeDao().assignStepToRecipe(it, recipeId) }

        db.recipeDao().insertStep(RecipeStep(0, "Cut the onions")).let(addStepToRecipe)
        db.recipeDao().insertStep(RecipeStep(0, "Cook the onions for 10 minutes")).let(addStepToRecipe)
        db.recipeDao().insertStep(RecipeStep(0, "Add garlic")).let(addStepToRecipe)

        val recipeAcitivityIntent = Intent(appContext, RecipeDetailActivity::class.java)
        recipeAcitivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        recipeAcitivityIntent.putExtra("recipe_id", recipeId)

        appContext.startActivity(recipeAcitivityIntent)

        Thread.sleep(30_000)
    }
}