package team3.recipefinder.logic.crawl

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object CrawlRecipe {

    private val baseUrl = "https://api.chefkoch.de/v2/recipes/"
    private var client: OkHttpClient = OkHttpClient()

    fun getRecipe(recipeId: String): String {
        val request: Request = Request.Builder()
            .url(baseUrl + recipeId)
            .build()
        val resp: Response = client.newCall(request).execute()
        return resp.body()!!.string()
    }

}