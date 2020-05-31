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
import com.google.firebase.auth.FirebaseAuth
import team3.recipefinder.activity.LoginActivity
import team3.recipefinder.adapter.RecipeAdapter
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.MainActivityBinding
import team3.recipefinder.dialog.CreateRecipeFragment
import team3.recipefinder.listener.RecipeListener
import team3.recipefinder.viewModelFactory.RecipeViewModelFactory
import team3.recipefinder.viewmodel.RecipeViewModel

class MainActivity : AppCompatActivity(), CreateRecipeFragment.CreateRecipeListener {
    private lateinit var viewModel: RecipeViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
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
        val adapter =
            RecipeAdapter(RecipeListener(viewModel))
        binding.recipeView.adapter = adapter

        // Observe LiveData
        viewModel.recipes.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED")
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    fun showAddRecipeDialog() {
        val args = Bundle()
        args.putString("name", resources.getString(R.string.text_recipeFragName))

        val createRecipeFragment = CreateRecipeFragment()
        createRecipeFragment.arguments = args
        createRecipeFragment.show(supportFragmentManager, "Create Recipe")
    }

    fun showAddIngredientDialog() {
        val args = Bundle()
        args.putString("name", resources.getString(R.string.text_ingredientFragName))

        val createRecipeFragment = CreateRecipeFragment()
        createRecipeFragment.arguments = args
        createRecipeFragment.show(supportFragmentManager, "Create Ingredient")
    }

    override fun onDialogPositiveClick(id: String?, name: String?) {
        when (id) {
            getString(R.string.text_recipeFragName) -> viewModel.addRecipe(name!!)
            getString(R.string.text_ingredientFragName) -> viewModel.addIngredient(name!!)
        }
    }

    override fun onDialogNegativeClick() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val mainToolbar: Toolbar = findViewById(R.id.toolbar)
        val mainMenu: Menu = mainToolbar.menu
        val userNameItem: MenuItem = mainMenu.findItem(R.id.user_name_settings)

        userNameItem.title = FirebaseAuth.getInstance().currentUser?.email
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.user_logout_settings -> {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_LONG).show()
            true
        }
        R.id.user_setting_create_recipe -> {
            showAddRecipeDialog()
            true
        }
        R.id.user_setting_create_ingredient -> {
            showAddIngredientDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}
