package team3.recipefinder.model

data class CrawlRecipe(
    val title: String,
    val subtitle: String,
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