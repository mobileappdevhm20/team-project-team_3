package team3.recipefinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import team3.recipefinder.dao.CookbookDao
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.Cookbook
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep
import team3.recipefinder.model.RelCookbookRecipes
import team3.recipefinder.model.RelRecipeIngredient
import team3.recipefinder.model.RelRecipeStep

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        RecipeStep::class,
        RelRecipeIngredient::class,
        RelRecipeStep::class,
        Cookbook::class,
        RelCookbookRecipes::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun cookbookDao(): CookbookDao
}

fun getAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
        .fallbackToDestructiveMigration().build()
}
