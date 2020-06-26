package team3.recipefinder.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import team3.recipefinder.database.AppDatabase
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Cookbook
import team3.recipefinder.model.Recipe

@Ignore
@RunWith(AndroidJUnit4::class)
class CookbookActivityTest {
    lateinit var db: AppDatabase

    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Start with a completely fresh database for every test
        context.deleteDatabase("app_database")
        db = getAppDatabase(context)
    }

    @Test
    fun launchCookbookActivity() {
        // Create recipes
        val recipes = listOf(
            db.recipeDao().insertRecipe(Recipe(0, "test 1", "description", "imageUrl", 1)),
            db.recipeDao().insertRecipe(Recipe(0, "test 2", "description", "imagerUrl", 1))
        )

        // Create cookbook
        val cookbook = db.cookbookDao().insertCookbook(Cookbook(0, "cookbook 1"))

        // Assign recipes to cookbook
        recipes.forEach { db.cookbookDao().addRecipeToCookbook(it.toInt(), cookbook.toInt()) }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, CookbookActivity::class.java)

        // Pass cookbook-id to activity
        intent.putExtra("cookbook", cookbook)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Start activity
        context.startActivity(intent)

        Thread.sleep(10_000)
    }
}
