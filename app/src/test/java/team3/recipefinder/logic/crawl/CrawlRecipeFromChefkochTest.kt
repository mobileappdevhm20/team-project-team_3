package team3.recipefinder.logic.crawl

import com.github.paweladamski.httpclientmock.HttpClientMock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.internal.Classes.getClass
import team3.recipefinder.logic.crawl.CrawlRecipe.getRecipe
import java.io.BufferedReader
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CrawlRecipeFromChefkochTest {

    private lateinit var httpClientMock: HttpClientMock
    private var baseUrl = "https://api.chefkoch.de/v2/recipes/"
    private var recipeId = "577851156580529"
    private var badRecipeId = "1"
    private lateinit var recipeJson: String

    @Before
    fun setup() {
        recipeJson = readTestData("recipe.json")

        httpClientMock = HttpClientMock()
        httpClientMock.onGet(baseUrl + recipeId).doReturnJSON(recipeJson)
        httpClientMock.onGet(baseUrl + badRecipeId).doThrowException(IOException())
    }

    @Test
    fun getRecipe_Positive() {
        val expected = recipeJson
        val result = getRecipe(recipeId)

        assertEquals(expected, result)
    }

    @Test
    fun getRecipe_Negative() {
        var expected = "{\"notification\":{\"hasErrors\":true,\"messages\":[{\"type\":\"error\",\"identifier\":\"entity_not_found\"}]}}"
        val result = getRecipe(badRecipeId)
        assertEquals(expected, result)
    }

    private fun readTestData(filename: String): String {
        val reader = BufferedReader(getClass(CrawlRecipeFromChefkochTest::class.qualifiedName).classLoader?.getResourceAsStream(filename)!!.reader())
        reader.use { reader ->
            return reader.readText()
        }
    }
}
