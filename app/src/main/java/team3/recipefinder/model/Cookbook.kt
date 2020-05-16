package team3.recipefinder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cookbook")
data class Cookbook (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "rel_cookbook_recipes")
data class RelCookbookRecipes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "cookbookId") val cookbookId: Int,
    @ColumnInfo(name = "recipeId") val recipeId: Int
)