package team3.recipefinder

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.recipe_detail_activity.*
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.RecipeDetailActivityBinding
import team3.recipefinder.dialog.AddIngredientFragment
import team3.recipefinder.dialog.AddItemFragment
import team3.recipefinder.viewModelFactory.EditViewModelFactory
import team3.recipefinder.viewmodel.RecipeDetailViewModel

class RecipeDetailActivity : AppCompatActivity(), AddItemFragment.EditRecipeListener,
    AddIngredientFragment.EditListListener {
    private lateinit var viewModel: RecipeDetailViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_detail_activity)

        val binding: RecipeDetailActivityBinding =
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
            val toolBar = findViewById<Toolbar>(R.id.toolbar)
            toolBar.title = it.name
        })

        viewModel.stepsRecipe.observe(this, Observer {
            val listView = findViewById<ListView>(R.id.stepList)

            val adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                it.map { s -> s.description }.toList()
            )

            listView.adapter = adapter
        })

        viewModel.ingredients.observe(this, Observer {
           if(viewModel.editMode.value!!){
            showAddIngredientDialog()
        }})
        viewModel.ingredientRecipe.observe(this, Observer {
            val listView = findViewById<ListView>(R.id.ingredientList)

            val adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                it.map { i -> i.amount + " " + i.name }.toList()
            )
            listView.adapter = adapter
        })

        viewModel.editMode.observe(this, Observer {

            if (it) {
                editButton.visibility = View.GONE
                addStepButton.visibility = View.VISIBLE
                doneEditButton.visibility = View.VISIBLE
                addIngredientButton.visibility = View.VISIBLE
            } else {
                editButton.visibility = View.VISIBLE
                addStepButton.visibility = View.GONE
                doneEditButton.visibility = View.GONE
                addIngredientButton.visibility = View.GONE
            }
        })


    }


    fun showAddItemDialogListener(view: View) {
        showAddItemDialog(view.tag.toString())

    }

    private fun showAddItemDialog(id: String) {
        val args = Bundle()
        args.putString("name", id)

        val editTimerFragment = AddItemFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Edit_Timer")

    }

    fun showAddIngredientDialogListener(view: View) {
        showAddIngredientDialog()
    }

    private fun showAddIngredientDialog() {
        val args = Bundle()

        args.putParcelableArrayList("name", viewModel.ingredients.value?.let { ArrayList(it) })
        val editTimerFragment = AddIngredientFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Edit_Timer1")
    }

    override fun saveItem(id: String?, name: String?) {
        when (id) {
            getString(R.string.text_stepName) -> {
                if (name != null) {
                    viewModel.addStep(name)
                }
            }
            getString(R.string.text_ingredientName) -> {

                if (name != null) {
                    viewModel.addIngredient(name)
                } else {
                    showAddIngredientDialog()
                }
            }
        }
    }

    override fun onDialogPositiveClick1(amount: String?, name: String?) {
        val prev: Fragment? = supportFragmentManager.findFragmentByTag("Edit_Timer")
        if (prev != null) {
            val df: DialogFragment = prev as DialogFragment
            df.dismiss()
        }
        if (amount != null && name != null && name != "-1") {

            viewModel.addIngredient(name.toLong(), amount)
        }

    }

    override fun onDialogNegativeClick2() {
        showAddItemDialog(getString(R.string.text_ingredientName))}


}