package team3.recipefinder.util

import com.google.gson.Gson
import team3.recipefinder.model.CrawlRecipe
import java.lang.IllegalArgumentException
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

fun extractInstructions(recipe: CrawlRecipe): List<String> {
    val instructionString = recipe.instructions
    val instructionList: MutableList<String>
    instructionList = if (instructionString.contains("\r\n\r\n")) {
        instructionString.split("\r\n\r\n").toMutableList()
    } else {
        instructionString.split("\n\n").toMutableList()
    }
    return replaceNewLineInList(instructionList)
}

private fun replaceNewLineInList(list: MutableList<String>): MutableList<String> {
    for (i in 0 until list.size) {
        list[i] = list[i].replace("\r\n", " ")
    }
    return list
}
