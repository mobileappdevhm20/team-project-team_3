package team3.recipefinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import team3.recipefinder.dao.CookbookDao
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.model.*


@Database(entities = [
    Recipe::class,
    Ingredient::class,
    RecipeStep::class,
    RelRecipeIngredient::class,
    RelRecipeStep::class,
    Cookbook::class,
    RelCookbookRecipes::class
], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun cookbookDao(): CookbookDao
}

 fun getAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "app_database1").build()
}
