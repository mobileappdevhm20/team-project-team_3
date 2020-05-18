package team3.recipefinder.viewmodel.recipe.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.viewmodel.recipe.overview.RecipeViewModel
import team3.recipefinder.viewmodel.recipe.overview.RecipeViewModelFactory

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val int = message.toInt()
        // val textview = findViewById<TextView>(R.id.recipe_name)
        //  textview.text = message

        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            DetailViewModelFactory(
                int,
                dataSource,
                application
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        viewModel.recipe.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED RR ${it.name}")
            val textview = findViewById<TextView>(R.id.recipe_name)
            textview.text = it.name

        })
        viewModel.ingrediants.observe(this, Observer { it ->
            Log.i("MainActivity", "ListOBSERVER CALLED $it")
            val listView = findViewById<ListView>(R.id.listView)
            var pairedDevices1: MutableList<String> = ArrayList()
            it?.forEach {a ->
                pairedDevices1.add(a.name)
                Log.i("MainActivity", "ListOBSERVER CALLED ${a.name}")

            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevices1)

            listView.adapter = adapter
        })
    }
}