package team3.recipefinder.model

import java.math.BigInteger

data class CrawlRecipe(
    val id: BigInteger,
    val title: String,
    val subtitle: String,
    val servings: Int,
    val hasImage: Boolean,
    val previewImageId: Int,
    val instructions: String,
    val ingredientGroups: List<CrawlIngredientGroup>
)

data class CrawlIngredientGroup(
    val ingredients: List<CrawlIngredient>
)

data class CrawlIngredient(
    val name: String,
    val unit: String,
    val amount: Float
)
