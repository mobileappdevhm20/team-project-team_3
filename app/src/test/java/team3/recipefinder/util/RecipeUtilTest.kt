package team3.recipefinder.util

import junit.framework.Assert.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.internal.Classes
import team3.recipefinder.logic.crawl.CrawlRecipeFromChefkochTest
import team3.recipefinder.model.CrawlIngredient
import team3.recipefinder.model.CrawlIngredientGroup
import team3.recipefinder.model.CrawlRecipe
import java.io.BufferedReader
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.math.BigDecimal

class RecipeUtilTest {

    private lateinit var recipeJson: String

    @Before
    fun setup() {
        recipeJson = readTestData("recipeSmall.json")
    }

    @Test
    fun convertJson_Positive() {
        val input = recipeJson
        val ingredients: List<CrawlIngredient> = listOf(
            CrawlIngredient("Kabeljaufilet(s)", "g", 600F),
            CrawlIngredient("Salz und Pfeffer", "", 0F)
        )
        val ingredientGroups: List<CrawlIngredientGroup> = listOf(CrawlIngredientGroup(ingredients))
        val expected = CrawlRecipe(
            "Schlemmerfisch Bordelaise",
            "Den Fisch waschen und trockentupfen, mit Zitronensaft, " +
                    "Salz und Pfeffer würzen und in die mit Margarine leicht gefettete " +
                    "Auflaufform geben.\r\n\r\n" +
                    "Den Backofen auf 200°C vorheizen!\r\n\r\n" +
                    "Die Paprika waschen, entkernen und in wirklich kleine Würfel schneiden.",
            "Low Fat",
            ingredientGroups
        )

        val result = convert(input)

        Assert.assertEquals(expected, result)
    }

    @Test
    fun checkUrl_Positive() {
        val url = "https://www.chefkoch.de/rezepte/577851156580529/Schlemmerfisch-Bordelaise.html"
        val url2 = "https://www.chefkoch.de/rezepte/577851156580529"
        assertTrue(checkUrl(url))
        assertTrue(checkUrl(url2))
    }

    @Test
    fun checkUrl_Negative() {
        val badUrl1 = "http://www.chefkoch.de/rezepte/577851156580529/Schlemmerfisch-Bordelaise.html"
        val badUrl2 = "https://www.chefkoch.de/rezepte/a577851156580529/Schlemmerfisch-Bordelaise.html"
        val badUrl3 = "https://www.chefkoch.de/rezepte/577851156580529/14342412.xml"
        assertFalse(checkUrl(badUrl1))
        assertFalse(checkUrl(badUrl2))
        assertFalse(checkUrl(badUrl3))
    }

    @Test
    fun extractRecipeId_Positive() {
        val url = "https://www.chefkoch.de/rezepte/577851156580529/Schlemmerfisch-Bordelaise.html"
        val url2 = "https://www.chefkoch.de/rezepte/577851156580529"
        val expected = "577851156580529"
        assertEquals(expected, extractRecipeId(url))
        assertEquals(expected, extractRecipeId(url2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun extractRecipeId_Negative() {
        val badUrl = "http://www.chefkoch.de/rezepte/avvv/Schlemmerfisch-Bordelaise.html"
        extractRecipeId(badUrl)
    }

    @Test
    fun idk_Positive() {
        val amount = 50.5F
        var amountParsed = amount
        var amountString = amount.toString()
        if (amount.rem(1).compareTo(0.0) == 0) {
            amountString = amount.toInt().toString()
        }
        amountString = amountString + " " + "g"
        println(amountString)
    }


    private fun readTestData(filename: String): String {
        val reader = BufferedReader(
            Classes.getClass(CrawlRecipeFromChefkochTest::class.qualifiedName).classLoader?.getResourceAsStream(
                filename
            )!!.reader()
        )
        reader.use { reader ->
            return reader.readText()
        }
    }
}