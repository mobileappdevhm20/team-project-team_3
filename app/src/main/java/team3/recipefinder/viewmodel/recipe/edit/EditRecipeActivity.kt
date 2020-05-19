package team3.recipefinder.viewmodel.recipe.edit

import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.viewmodel.recipe.overview.AddRecipeFragment

class EditRecipeActivity : AppCompatActivity(), AddRecipeFragment.EditRecipeListener  {
    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_edit)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val int = message.toLong()
        val textview = findViewById<TextView>(R.id.recipe_name)
        textview.text = message
        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            EditViewModelFactory(
                int,
                dataSource,
                application
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)
        viewModel.recipe.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED RR ${it.name}")
            val textview = findViewById<TextView>(R.id.recipe_name)
            textview.text = it.name
        })

        viewModel.steps.observe(this, Observer { it ->
            Log.i("MainActivity", "ListOBSERVER CALLEDdddd $it")

                val listView = findViewById<ListView>(R.id.stepEdit)
                var pairedDevices1: MutableList<String> = ArrayList()
                it?.forEach { a ->
                    pairedDevices1.add(a.description)
                    Log.i("MainActivity", "ListOBSERVER CALLED ${a.description}")

                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevices1)

                listView.adapter = adapter
             })


    }



    fun showAddRecipeDialog(view: View) {
        val pageNumber: String = view.getTag().toString()
        val args = Bundle()
        args?.putString("name", pageNumber)

        val editTimerFragment = AddRecipeFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Edit Timer")
    }

    override fun onDialogPositiveClick(id: String?, value: String?) {
Log.i("edi","jkdfslafj $id")
        if (value != null) {
            viewModel.addStep(value)
        }
    }

    override fun onDialogNegativeClick() {

    }
}