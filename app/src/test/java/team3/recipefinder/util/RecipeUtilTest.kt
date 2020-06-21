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
import java.util.*

class RecipeUtilTest {

    private lateinit var recipeJson: String
    private lateinit var recipeJsonSmall: String

    @Before
    fun setup() {
        recipeJson = readTestData("recipe.json")
        recipeJsonSmall = readTestData("recipeSmall.json")
    }

    @Test
    fun convertJson_Positive() {
        val input = recipeJsonSmall
        val ingredients: List<CrawlIngredient> = listOf(
            CrawlIngredient("Kabeljaufilet(s)", "g", 600F),
            CrawlIngredient("Salz und Pfeffer", "", 0F)
        )
        val ingredientGroups: List<CrawlIngredientGroup> = listOf(CrawlIngredientGroup(ingredients))
        val expected = CrawlRecipe(
            "Schlemmerfisch Bordelaise",
            "Low Fat",
            4,
            "Den Fisch waschen und trockentupfen, mit Zitronensaft, " +
                    "Salz und Pfeffer würzen und in die mit Margarine leicht gefettete " +
                    "Auflaufform geben.\r\n\r\n" +
                    "Den Backofen auf 200°C vorheizen!\r\n\r\n" +
                    "Die Paprika waschen, entkernen und in wirklich kleine Würfel schneiden.",
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
    fun extractInstructions_Positive() {
        val input = recipeJson
        val recipe = convert(input)
        val expected = LinkedList<String>()
        expected.add("Den Fisch waschen und trockentupfen, mit Zitronensaft, Salz und Pfeffer würzen und in die mit Margarine leicht gefettete Auflaufform geben.")
        expected.add("Den Backofen auf 200°C vorheizen!")
        expected.add("Die Paprika waschen, entkernen und in wirklich kleine Würfel schneiden.")
        expected.add("Den Zwieback in einen Beutel geben, verschließen und mit dem Nudelholz ganz fein zerkrümeln.")
        expected.add("Alle trockenen Zutaten, die Kräuter, die Zitronenschale und die Paprikawürfel vermischen.")
        expected.add("Das Öl und den Senf unterrühren und nach und nach so viel Wasser dazu geben, bis die Masse streichfähig ist.")
        expected.add("Die Panade auf dem Fisch verteilen und im Backofen ca. 25 Minuten backen, bis es goldgelb ist.")
        expected.add("Pro Portion: 241,5 kcal 3,8 g Fett 14,2% Fettkalorien")

        val result = extractInstructions(recipe)

        assertEquals(expected.size, result.size)
        assertEquals(listToString(expected), listToString(result))
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

    private fun listToString(list: List<String>): String {
        var listString = ""
        for (item in list) {
            listString += item
        }
        return listString
    }
}