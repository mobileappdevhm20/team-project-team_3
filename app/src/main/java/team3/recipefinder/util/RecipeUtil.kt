package team3.recipefinder.util

import com.google.gson.Gson
import team3.recipefinder.model.CrawlRecipe
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.util.regex.Pattern

fun convert(recipeJson: String): CrawlRecipe {
    val gson = Gson()
    return gson.fromJson(recipeJson, CrawlRecipe::class.java)
}

fun checkUrl(url: String): Boolean {
    val regex = "https:\\/\\/www\\.chefkoch\\.de\\/rezepte\\/\\d+(\\/(\\w|\\W)+\\.html)?"

    return Pattern.matches(regex, url)
}

fun extractRecipeId(url: String): String {
    val regex = "https:\\/\\/www\\.chefkoch\\.de\\/rezepte\\/(\\d+)(\\/(\\w|\\W)+\\.html)?"

    val match = Regex(regex).find(url)
        ?: throw IllegalArgumentException("Could not extract recipeId from given url.")
    return match.groups[1]!!.value
}