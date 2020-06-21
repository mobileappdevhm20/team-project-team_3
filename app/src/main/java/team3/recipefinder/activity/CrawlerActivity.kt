package team3.recipefinder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.crawler_activity.*
import team3.recipefinder.MainActivity
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.CrawlerActivityBinding
import team3.recipefinder.model.CrawlRecipe
import team3.recipefinder.service.CrawlRecipeService
import team3.recipefinder.util.checkUrl
import team3.recipefinder.util.convert
import team3.recipefinder.util.extractRecipeId
import team3.recipefinder.viewModelFactory.CrawlRecipeViewModelFactory
import team3.recipefinder.viewmodel.CrawlRecipeViewModel

class CrawlerActivity : AppCompatActivity() {

    private lateinit var viewModel: CrawlRecipeViewModel

    private lateinit var thread: Thread

    companion object {
        const val REQUEST_RECIPE_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup DataBinding
        var binding: CrawlerActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.crawler_activity)

        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            CrawlRecipeViewModelFactory(
                dataSource,
                application
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(CrawlRecipeViewModel::class.java)

        // Bind model to layout
        binding.lifecycleOwner = this
        binding.crawlRecipeViewModel = viewModel

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        toolBar.title = "Import Recipe"

        thread = Thread { }

        importRecipeButton.setOnClickListener {
            val url = urlInputText.text.toString()
            // Check if url is valid
            if (observeUrl(url)) {
                Log.i("CrawlerActivity", url)

                // Crawl recipe and add it to database
                val pendingResult = createPendingResult(
                    REQUEST_RECIPE_CODE, Intent(), 0
                )
                val intent =
                    Intent(applicationContext, CrawlRecipeService::class.java)
                intent.putExtra(CrawlRecipeService.RECIPE_ID_EXTRA, extractRecipeId(url))
                intent.putExtra(CrawlRecipeService.PENDING_RESULT_EXTRA, pendingResult)
                startService(intent)
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_RECIPE_CODE) {
            when (resultCode) {
                CrawlRecipeService.ERROR_CODE -> handleError()
                CrawlRecipeService.RESULT_CODE -> handleSuccess(data!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSuccess(data: Intent) {
        val recipeJson: String = data.getStringExtra(CrawlRecipeService.RECIPE_RESULT_EXTRA)!!
        // Convert to recipe
        val recipe = convert(recipeJson)
        Log.i("CrawlerActivity", recipeJson)
        Log.i("CrawlerActivity", recipe.title)

        saveRecipe(recipe)

        Log.i("CrawlerActivity", "Recipe was saved successfully")

        // Switch to MainActivity
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun handleError() {
        Log.e("CrawlerActivity", "Could not retrieve recipe from chefkoch.")
    }

    private fun saveRecipe(recipe: CrawlRecipe) {
        // Add Recipe
        viewModel.importRecipe(recipe)
        Log.i("CrawlerActivity", "Recipe was added successfully")
    }

    private fun observeUrl(url: String): Boolean {
        if (!checkUrl(url)) {
            urlOutline.isErrorEnabled = true
            urlOutline.error = "Not a valid chefkoch url"
            return false
        }
        urlOutline.isErrorEnabled = false
        urlOutline.error = ""
        return true
    }
}
