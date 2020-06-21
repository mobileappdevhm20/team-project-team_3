package team3.recipefinder.service

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import team3.recipefinder.logic.crawl.CrawlRecipe.getRecipe

class CrawlRecipeService : IntentService(TAG) {

    companion object {
        val TAG = CrawlRecipeService::class.simpleName
        const val RESULT_CODE = 0
        const val ERROR_CODE = 2
        const val RECIPE_ID_EXTRA = "recipeId"
        const val RECIPE_RESULT_EXTRA = "recipe"
        const val PENDING_RESULT_EXTRA = "pending_result"
    }

    override fun onHandleIntent(intent: Intent?) {
        val reply: PendingIntent = intent?.getParcelableExtra(PENDING_RESULT_EXTRA)!!
        val recipeId = intent.getStringExtra(RECIPE_ID_EXTRA)
        try {
            try {
                // Crawl recipe
                val recipe = getRecipe(recipeId!!)
                Log.i(TAG, recipe)

                // Send result
                val result = Intent()
                result.putExtra(RECIPE_RESULT_EXTRA, recipe)

                reply.send(this, RESULT_CODE, result)
            } catch (ex: Exception) {
                Log.e(TAG, "Error while trying to get recipe", ex)
                reply.send(ERROR_CODE)
            }
        } catch (ex: PendingIntent.CanceledException) {
            Log.e(TAG, "reply cancelled", ex)
        }
    }
}
