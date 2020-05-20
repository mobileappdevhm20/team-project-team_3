package team3.recipefinder.viewmodel.recipe.edit

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.ActivityRecipeDetailBinding
import team3.recipefinder.viewmodel.recipe.overview.AddRecipeFragment

class EditRecipeActivity : AppCompatActivity(), AddRecipeFragment.EditRecipeListener {
    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        var binding: ActivityRecipeDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val recipeKey = message.toLong()

        val application = requireNotNull(this).application

        // Get DAO instance
        val dataSource = getAppDatabase(application).recipeDao()

        // Create ViewModel
        val viewModelFactory =
            EditViewModelFactory(
                recipeKey,
                dataSource,
                application
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)

        binding.model = viewModel

        viewModel.recipe.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED RR ${it.name}")
            val textview = findViewById<TextView>(R.id.recipe_name)
            textview.text = it.name
        })


        viewModel.steps.observe(this, Observer { it ->
            val listView = findViewById<ListView>(R.id.stepList)
            var pairedDevices1: MutableList<String> = ArrayList()
            it?.forEach { a ->
                pairedDevices1.add(a.description)

            }
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevices1)

            listView.adapter = adapter
        })

        viewModel.ingredients.observe(this, Observer { it ->

            val listView = findViewById<ListView>(R.id.ingredientList)
            var pairedDevices1: MutableList<String> = ArrayList()
            it?.forEach { a ->
                pairedDevices1.add(a.name)

            }
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevices1)

            listView.adapter = adapter
        })

        viewModel.editMode.observe(this, Observer {

            if (it) {
                editButton.setVisibility(View.GONE);
                addStepButton.setVisibility(View.VISIBLE);
                doneEditButton.setVisibility(View.VISIBLE);
                addIngredientButton.setVisibility(View.VISIBLE);
            } else {
                editButton.setVisibility(View.VISIBLE);
                addStepButton.setVisibility(View.GONE);
                doneEditButton.setVisibility(View.GONE);
                addIngredientButton.setVisibility(View.GONE);
            }
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
        Log.i("edi", "jkdfslafj $id")
        if (value != null) {
            viewModel.addStep(value)
        }
    }

    override fun onDialogNegativeClick() {

    }
}