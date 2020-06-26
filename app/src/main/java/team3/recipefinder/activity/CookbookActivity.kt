package team3.recipefinder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_cookbook.*
import kotlinx.coroutines.*
import team3.recipefinder.R
import team3.recipefinder.adapter.RecipeListAdapter
import team3.recipefinder.dao.CookbookDao
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.model.Recipe

class CookbookActivity : AppCompatActivity(), RecipeListAdapter.RecipeCallback {


    val db by lazy {
        getAppDatabase(this)
    }

    private var databaseJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + databaseJob)

    private lateinit var cookbookFromDB : CookbookDao

    private lateinit var recipeListAdapter : RecipeListAdapter

    private var cookbookId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cookbook)
        //intent.getLongExtra(cookbook)

        //edited by me
        val addButton : FloatingActionButton = findViewById(R.id.floatingActionButtonADD)
        var editEnable : Boolean = false
        addButton.setOnClickListener {
            if (!editEnable) {
                editEnable = true
                //Toast.makeText(this@CookbookActivity, "edit is", Toast.LENGTH_LONG)
                Log.i("Cookbook", "hello")

            } else if (editEnable) {
                editEnable = false
            }
        }


        recipeList.layoutManager = LinearLayoutManager(this)

         recipeListAdapter = RecipeListAdapter(this, this)
        recipeList.adapter = recipeListAdapter
        recipeListAdapter.notifyDataSetChanged()

        cookbookFromDB = getAppDatabase(application).cookbookDao()



        setSupportActionBar(findViewById(R.id.toolbar))

        // Get cookbook
         cookbookId = intent.getLongExtra("cookbook", -1)
        /*val cookbooks =cookbookFromDB.getAllRecipesByCookbook(cookbookId);

        cookbooks.observe(this, Observer {
            recipeListAdapter.recipes = it.toMutableList()
            recipeListAdapter.notifyDataSetChanged()


        })*/


        GlobalScope.launch {
            // Set toolbar title
            val cookbook = db.cookbookDao().get(cookbookId.toInt())
            title = cookbook.name
            val recipes = db.cookbookDao().getAllRecipesByCookbook(cookbookId.toInt())
            recipeListAdapter.let {
                it.recipes.clear()
                it.recipes.addAll(recipes)
                it.notifyDataSetChanged()
            }
        }

            // Recipe list




    }

    override fun deleteRecipe(recipe: Recipe) {
        Log.i("got hre","got here")
        uiScope.launch {
            withContext(Dispatchers.IO){
            cookbookFromDB.removeRecipeFromCookbook(recipe.id.toInt(), cookbookId.toInt())
                val recipes = db.cookbookDao().getAllRecipesByCookbook(cookbookId.toInt())
                recipeListAdapter.recipes.clear()
                recipeListAdapter.recipes.addAll(recipes)
                runOnUiThread { recipeListAdapter.notifyDataSetChanged() }
            }
        }
    }
}
