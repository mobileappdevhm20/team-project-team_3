package team3.recipefinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team3.recipefinder.activity.CookbookActivity
import team3.recipefinder.activity.CrawlerActivity
import team3.recipefinder.activity.LoginActivity
import team3.recipefinder.activity.SearchActivity
import team3.recipefinder.adapter.CookbookListAdapter
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.MainActivityBinding
import team3.recipefinder.dialog.CreateIngredientFragment
import team3.recipefinder.dialog.CreateRecipeFragment
import team3.recipefinder.viewModelFactory.RecipeViewModelFactory
import team3.recipefinder.viewmodel.RecipeViewModel

class MainActivity :
    AppCompatActivity(),
    CreateRecipeFragment.CreateRecipeListener,
    CreateIngredientFragment.EditRecipeListener {
    private lateinit var viewModel: RecipeViewModel

    private lateinit var auth: FirebaseAuth

    private val database by lazy { getAppDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Setup DataBinding
        var binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)

        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            RecipeViewModelFactory(
                dataSource,
                application
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Bind model to layout
        binding.lifecycleOwner = this
        binding.recipeViewModel = viewModel

        // Register Adapter for the RecyclerView
        val adapter = RecipeListAdapter(this) {
            viewModel.editPropertyDetails(it)
        }

        adapter.recipes = mutableListOf()
        binding.recipeView.adapter = adapter
        binding.recipeView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        val cookbookAdapter = CookbookListAdapter(this) {
            val intent = Intent(this, CookbookActivity::class.java)
            intent.putExtra("cookbook", it.id)
            // TODO: Un-comment this when CookbookActivity is finished
            // startActivity(intent)
        }

        // Add all known cookbooks
        GlobalScope.launch {
            cookbookAdapter.cookbooks.addAll(database.cookbookDao().getAll())
            runOnUiThread { cookbookAdapter.notifyDataSetChanged() }
        }

        binding.cookbookView.adapter = cookbookAdapter
        binding.cookbookView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        // Observe LiveData
        viewModel.recipes.observe(
            this,
            Observer {
                Log.i("MainActivity", "OBSERVER CALLED")
                it?.let {
                    adapter.recipes.clear()
                    adapter.recipes.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        )

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    /**
     * OnClick method to show create recipe dialog.
     */
    private fun showRefactorRecipeDialog() {
        val args = Bundle()
        args.putString("name", resources.getString(R.string.text_recipeFragName))

        val createRecipeFragment = CreateRecipeFragment()
        createRecipeFragment.arguments = args
        createRecipeFragment.show(supportFragmentManager, "Create Recipe")
    }

    /**
     * OnClick method to show create ingredient dialog.
     */
    private fun showCreateIngredientDialog() {
        val args = Bundle()
        args.putString("name", resources.getString(R.string.text_ingredientFragName))

        val createIngredientFragment = CreateIngredientFragment()
        createIngredientFragment.arguments = args
        createIngredientFragment.show(supportFragmentManager, "Create Ingredient")
    }

    /**
     * Method that handles the positiveClick for the different dialogs.
     */
    override fun onDialogPositiveClick(name: String?, url: String?) {
        viewModel.addRecipe(name!!, url!!)
    }

    /**
     * Method that habdeles the negativeClick for create Ingredient
     */
    override fun saveItem(id: String?, name: String?) {
        viewModel.addIngredient(name!!)
    }

    /**
     * Method to create the options menu of the custom toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val mainToolbar: Toolbar = findViewById(R.id.toolbar)
        val mainMenu: Menu = mainToolbar.menu
        val userNameItem: MenuItem = mainMenu.findItem(R.id.user_name_settings)

        userNameItem.title = FirebaseAuth.getInstance().currentUser?.email
        return true
    }

    /**
     * Method to create onclicklisteners for all optionsmenu items
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.user_logout_settings -> {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(
                this,
                LoginActivity::class.java
            )
            startActivity(intent)
            finish()
            Toast.makeText(
                this,
                "Successfully Logged Out",
                Toast.LENGTH_LONG
            ).show()
            true
        }
        R.id.user_setting_create_recipe -> {
            showRefactorRecipeDialog()
            true
        }
        R.id.user_setting_create_ingredient -> {
            showCreateIngredientDialog()
            true
        }
        R.id.action_search -> {
            startActivity(Intent(this, SearchActivity::class.java))
            true
        }
        R.id.user_setting_import_recipe -> {
            val intent = Intent(this, CrawlerActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
