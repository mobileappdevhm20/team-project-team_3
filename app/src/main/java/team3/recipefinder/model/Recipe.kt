package team3.recipefinder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "ingredient")
data class Ingredient (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "step")
data class RecipeStep (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "description") val description: String
)

@Entity(tableName = "rel_recipe_ingredient")
data class RelRecipeIngredient(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "recipeId") val recipeId: Long,
    @ColumnInfo(name = "ingredientId") val ingredientId: Long
)

@Entity(tableName = "rel_recipe_step")
data class RelRecipeStep(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "recipeId") val recipeId: Int,
    @ColumnInfo(name = "stepId") val stepId: Int
)