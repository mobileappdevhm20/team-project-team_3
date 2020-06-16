package team3.recipefinder.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.recipe_detail_activity.*
import team3.recipefinder.MainActivity
import team3.recipefinder.R
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.RecipeDetailActivityBinding
import team3.recipefinder.viewModelFactory.EditViewModelFactory
import team3.recipefinder.viewmodel.RecipeDetailViewModel
import team3.recipefinder.adapter.IngredientListAdapter
import team3.recipefinder.dialog.*
import team3.recipefinder.util.extractTime
import team3.recipefinder.util.startTimer


class RecipeDetailActivity : AppCompatActivity(),
    AddIngredientFragment.CreateIngredientListener,
    CreateInstructionFragment.CreateInstructionListener,
    EditIngredientFragment.EditIngredientListener, CreateIngredientFragment.EditRecipeListener,
    EditInstructionFragment.EditInstructionListener, EditRecipeFragment.EditRecipeListener {
    private lateinit var viewModel: RecipeDetailViewModel
    private var editModeActive = false
    private var ingredientListNameHolder: List<String> = emptyList()
    private var ingredientListAmountHolder: List<String> = emptyList()
    private var ingredientListIdHolder: List<Long> = emptyList()
    private val checkedSteps = hashSetOf<Long>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_detail_activity)

        val binding: RecipeDetailActivityBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.recipe_detail_activity
            )

        val recipeKey = intent.getLongExtra("recipe_id", 0L)

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
            Log.i("RecipeDetailActivity", "OBSERVER CALLED FOR ${it.name}")
            val imageView = findViewById<ImageView>(R.id.imageView);
            Glide.with(this).load(
                it.imageUrl
            ).apply(
                RequestOptions()
                    // .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.tomatensuppe115_v_zweispaltig)
            ).into(imageView);
            val toolBar = findViewById<Toolbar>(R.id.toolbar)
            toolBar.title = it.name
            toolBar.setOnClickListener {
                if (editModeActive) {
                    val args = Bundle()
                    args.putString("oldName", toolBar.title.toString())

                    val editRecipeFragment = EditRecipeFragment()
                    editRecipeFragment.arguments = args
                    editRecipeFragment.show(supportFragmentManager, "Edit Recipe")
                }
            }
        })

        viewModel.stepsRecipe.observe(this, Observer { instructions ->
            stepList.removeAllViews()
            instructions.forEach { instruction ->
                val view = layoutInflater.inflate(R.layout.recipe_instruction_card, null)
                view.findViewById<TextView>(R.id.instructionText).text = instruction.description

                val timerValue = instruction.description.extractTime()

                val timerButton = view.findViewById<Button>(R.id.timerButton)
                val layout = view.findViewById<ConstraintLayout>(R.id.instructionCardLayout)
                val checkMark = view.findViewById<ImageView>(R.id.checkMark)

                if (timerValue == 0) {
                    layout.removeView(timerButton)
                } else {
                    timerButton.setOnClickListener {
                        timerValue.startTimer(
                            this,
                            instruction.description
                        )
                    }
                }

                if (checkedSteps.contains(instruction.id)) {
                    checkMark.setImageResource(R.drawable.green_check)
                } else {
                    checkMark.setImageResource(R.drawable.gray_check)
                }

                checkMark.setOnClickListener {
                    checkedSteps.plusElement(instruction.id)
                    checkMark.setImageResource(R.drawable.green_check)
                }

                view.setOnLongClickListener {
                    if (editModeActive) {
                        showEditInstructions(instruction.id, instruction.description)
                    }
                    true
                }
                stepList.addView(view)
            }
        })

        viewModel.ingredients.observe(this, Observer {
            if (viewModel.editMode.value!!) {
                showAddIngrediantDialog()
            }
        })

        viewModel.ingredientRecipe.observe(this, Observer { it ->
            // Save ingredientNameList and ingredientIdList to update the list view inside the editMode observer
            ingredientListNameHolder = it.map { i -> i.name }.toList()
            ingredientListIdHolder = it.map { i -> i.relId }.toList()
            ingredientListAmountHolder = it.map { i -> i.amount }.toList()
            createAndSetListViewAdapter(
                ingredientListNameHolder,
                ingredientListAmountHolder,
                ingredientListIdHolder,
                editModeActive
            )
        })

        viewModel.editMode.observe(this, Observer {
            // Save editmode fot ingredient observer
            editModeActive = it
            // Update ingredient list view adapter
            createAndSetListViewAdapter(
                ingredientListNameHolder,
                ingredientListAmountHolder,
                ingredientListIdHolder,
                editModeActive
            )
            changeListItemBehaviour(it)

            if (it) {
                editButton.visibility = View.GONE
                shareButton.visibility = View.GONE
                addStepButton.visibility = View.VISIBLE
                doneEditButton.visibility = View.VISIBLE
                deleteRecipeButton.visibility = View.VISIBLE
                addIngredientButton.visibility = View.VISIBLE
            } else {
                editButton.visibility = View.VISIBLE
                shareButton.visibility = View.VISIBLE
                addStepButton.visibility = View.GONE
                doneEditButton.visibility = View.GONE
                deleteRecipeButton.visibility = View.GONE
                addIngredientButton.visibility = View.GONE
            }
        })

        toolbar.setOnClickListener {

        }
    }

    /**
     * OnClick method to show create instruction dialog.
     */
    fun showCreateInstructionDialog(@Suppress("UNUSED_PARAMETER") view: View) {
        val args = Bundle()
        args.putString("name", resources.getString(R.string.text_stepName))

        val createInstructionFragment = CreateInstructionFragment()
        createInstructionFragment.arguments = args
        createInstructionFragment.show(supportFragmentManager, "Create Instruction")
    }

    /**
     * OnClick method to show add ingredient to recipe dialog.
     */
    fun showAddIngredientDialog(@Suppress("UNUSED_PARAMETER") view: View) {
        showAddIngrediantDialog()
    }

    fun showAddIngrediantDialog() {
        val args = Bundle()
        args.putParcelableArrayList("name", viewModel.ingredients.value?.let { ArrayList(it) })

        val createIngredientFragment = AddIngredientFragment()
        createIngredientFragment.arguments = args
        createIngredientFragment.show(supportFragmentManager, "Create Ingredient")

    }


    /**
     * OnClick method to show add edit ingredient dialog.
     *
     * @property currentId the database id of the current ingredient
     * @property name the name of the current ingredient
     * @property oldAmount the old amount of the current ingredient
     */
    fun showEditIngredients(currentId: Long, name: String, oldAmount: String) {
        val args = Bundle()
        args.putLong("ingredientId", currentId)
        args.putString("ingredientName", name)
        args.putString("oldAmount", oldAmount)

        val editIngredientFragment =
            EditIngredientFragment()
        editIngredientFragment.arguments = args
        editIngredientFragment.show(supportFragmentManager, "Edit Ingredient")
    }

    /**
     * OnClick method to show add edit ingredient dialog.
     *
     * @property currentId the database id of the current instruction
     * @property instruction the instrcution description of the current instruction
     */
    private fun showEditInstructions(currentId: Long, instruction: String) {
        val args = Bundle()
        args.putLong("relInstructionId", currentId)
        args.putString("oldInstruction", instruction)

        val editInstructionFragment =
            EditInstructionFragment()
        editInstructionFragment.arguments = args
        editInstructionFragment.show(supportFragmentManager, "Edit Instruction")
    }

    /**
     * OnClick method to show create ingredient dialog.
     *
     * @property id the database id of the current ingredient
     */
    private fun showCreateIngredientDialog(id: String) {
        val args = Bundle()
        args.putString("name", id)

        val editTimerFragment = CreateIngredientFragment()
        editTimerFragment.arguments = args
        editTimerFragment.show(supportFragmentManager, "Create Ingredient")

    }

    /**
     * OnClick method to handle a recipe delete request.
     */
    fun deleteRecipe(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.deleteRecipeStepRelation()
        viewModel.deleteRecipeIngredientRelation()
        viewModel.deleteRecipe()

        removeObservers()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    /**
     * Method that handles the positiveClick for the different dialogs.
     */
    override fun onDialogPositiveClick(id: String?, name: String?) {
        when (id) {
            getString(R.string.text_stepName) -> viewModel.addStep(name!!)
        }
    }

    /**
     * Method that handles the positiveClick specificly for the AddIngriedient dialog.
     */
    override fun onDialogPositiveEditIngredient(id: Long?, name: String?, amount: String?) {
        viewModel.updateIngredientAmount(id!!, amount!!)
    }

    /**
     * Method that handles the positiveClick specificly for the AddIngriedient dialog.
     */
    override fun onDialogPositiveClickIngredient(amount: String?, name: String?) {
        val prev: Fragment? = supportFragmentManager.findFragmentByTag("Add Ingredient")
        if (prev != null) {
            val df: DialogFragment = prev as DialogFragment
            df.dismiss()
        }
        if (amount != null && name != null && name != "-1") {

            viewModel.addIngredient(name.toLong(), amount)
        }
    }

    override fun onDialogPositiveEditInstruction(id: Long?, instruction: String?) {
        Toast.makeText(this, "Updating....", Toast.LENGTH_LONG).show()
        viewModel.updateStep(id!!, instruction!!)
    }

    override fun onDialogPositiveEditRecipe(name: String?) {
        viewModel.updateRecipeName(name!!)
    }

    override fun onDialogNeutralEditInstruction(id: Long?) {
        Toast.makeText(this, "Deleting....", Toast.LENGTH_LONG).show()
        viewModel.removeStepFromRecipe(id!!)
    }

    override fun openCreateIngredientDialog() {
        showCreateIngredientDialog(getString(R.string.text_ingredientName))
    }


    /**
     * Method that handles the neutralClick specificly for the EditIngredient dialog.
     *
     * It deletes the current ingredient from the database
     *
     * @property id the database id of the current ingredient
     * @property name the name of the current ingredient
     */
    override fun onDialogNeutralClick(id: Long?, name: String?) {
        Log.i("RecipeDetailActivity", "Deleterequest for id = $id and name = $name")
        viewModel.deleteIngredientRelation(id!!)
    }

    /**
     * Helper method to remove the click animation for the listview items if editmode is off.
     *
     * It deletes the current ingredient from the database
     *
     * @property editMode boolean if the editmode is currently on
     */
    private fun changeListItemBehaviour(editMode: Boolean) {
        val color: Drawable
        val transparent: Drawable = ColorDrawable(Color.TRANSPARENT)
        val clickable: Boolean
        if (editMode) {
            color = ColorDrawable(Color.LTGRAY)
            clickable = true
        } else {
            color = ColorDrawable(Color.TRANSPARENT)
            clickable = false
        }

        val selector =
            StateListDrawable().apply {
                addState(
                    intArrayOf(android.R.attr.state_pressed),
                    color
                )
                addState(
                    intArrayOf(-android.R.attr.state_pressed),
                    transparent
                )
                setEnterFadeDuration(500)
                setExitFadeDuration(700)
            }
        ingredientList.selector = selector
        ingredientList.isClickable = clickable
    }

    /**
     * Helper method to set the custom listview adpater.
     *
     * @property ingredientNames a list of all ingredient names to display them in the custom listview
     * @property ingredientIds a list of all ingredient ids to later delete the ingredient if needed
     * @property editMode the editmode to decide rather the edit icon should be shown
     */
    private fun createAndSetListViewAdapter(
        ingredientNames: List<String>,
        ingredientAmounts: List<String>,
        ingredientIds: List<Long>,
        editMode: Boolean
    ) {
        val listView = findViewById<ListView>(R.id.ingredientList)
        val ingredientListAdapter =
            IngredientListAdapter(this, ingredientNames, ingredientAmounts, ingredientIds, editMode)

        listView.adapter = ingredientListAdapter

        justifyListViewHeightBasedOnChildren(listView)
    }

    /**
     * Helper method to display all listview items without having to scoll.
     *
     * @property listView the listview that should be edited
     */
    private fun justifyListViewHeightBasedOnChildren(listView: ListView) {
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
                    showAddIngredientDialog(View(this))
                }
            }
        }
    }

    /**
     * Helper method to destroy all observers on deletion
     */
    private fun removeObservers() {
        viewModel.recipe.removeObservers(this)
        viewModel.ingredients.removeObservers(this)
        viewModel.ingredientRecipe.removeObservers(this)
        viewModel.editMode.removeObservers(this)
        viewModel.stepsRecipe.removeObservers(this)
    }
}
