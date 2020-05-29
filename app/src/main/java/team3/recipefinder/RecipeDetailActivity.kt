package team3.recipefinder

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.recipe_detail_activity.*
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.RecipeDetailActivityBinding
import team3.recipefinder.dialog.AddIngrFragment
import team3.recipefinder.dialog.AddRecipeFragment
import team3.recipefinder.viewModelFactory.EditViewModelFactory
import team3.recipefinder.viewmodel.RecipeDetailViewModel


class RecipeDetailActivity : AppCompatActivity(), AddRecipeFragment.EditRecipeListener,
    AddIngrFragment.EditListListener {
    private lateinit var viewModel: RecipeDetailViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_detail_activity)

        var binding: RecipeDetailActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.recipe_detail_activity)

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

        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailViewModel::class.java)

        binding.model = viewModel

        viewModel.recipe.observe(this, Observer {
            Log.i("MainActivity", "OBSERVER CALLED RR ${it.name}")
            val toolBar = findViewById<Toolbar>(R.id.toolbar)
            toolBar.title = it.name
        })


        viewModel.stepsRecipe.observe(this, Observer { it ->
            val listView = findViewById<ListView>(R.id.stepList)

            val adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                it.map { s -> s.description }.toList()
            )
            listView.adapter = adapter

            justifyListViewHeightBasedOnChildren(listView)
        })

        viewModel.ingredients.observe(this, Observer { })
        viewModel.ingredientRecipe.observe(this, Observer { it ->
            val listView = findViewById<ListView>(R.id.ingredientList)

            val adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                it.map { i -> i.name }.toList()
            )
            listView.adapter = adapter

            justifyListViewHeightBasedOnChildren(listView)
        })

        viewModel.editMode.observe(this, Observer {

            if (it) {
                editButton.setVisibility(View.GONE);
                shareButton.setVisibility(View.GONE);
                addStepButton.setVisibility(View.VISIBLE);
                doneEditButton.setVisibility(View.VISIBLE);
                addIngredientButton.setVisibility(View.VISIBLE);
            } else {
                editButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                addStepButton.setVisibility(View.GONE);
                doneEditButton.setVisibility(View.GONE);
                addIngredientButton.setVisibility(View.GONE);
            }
        })


    }


    fun showAddRecipeDialog(view: View) {
        val id: String = view.getTag().toString()
        val args = Bundle()
        args?.putString("name", id)

        val editTimerFragment = AddRecipeFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Edit Timer")
    }

    fun showAddRecipeDialog1(view: View) {
        val args = Bundle()


        args?.putParcelableArrayList("name", viewModel.ingredients.value?.let { ArrayList(it) })
        val editTimerFragment = AddIngrFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Edit Timer")
    }


    override fun onDialogPositiveClick(id: String?, value: String?) {
        when (id) {
            getString(R.string.text_stepName) -> viewModel.addStep(value!!)
        }
    }

    override fun onDialogPositiveClick1(id: String?, value: String?) {
        Toast.makeText(this, "bframgment ${value?.toLong()}", Toast.LENGTH_SHORT).show()
        if (value != null && value != "-1") {

            viewModel.addIngredient(value.toLong())
        }
    }

    override fun onDialogNegativeClick() {

    }

    fun justifyListViewHeightBasedOnChildren(listView: ListView) {
        val adapter = listView.adapter ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val par = listView.layoutParams
        par.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = par
        listView.requestLayout()
    }
}