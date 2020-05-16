package team3.recipefinder.viewmodel.recipe.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import team3.recipefinder.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import team3.recipefinder.database.getAppDatabase
import team3.recipefinder.databinding.ActivityRecipeDetailBinding

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var viewModel : RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setup DataBinding
        var binding: ActivityRecipeDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail)
        val application = requireNotNull(this).application
        // Get DAO instance
        val dataSource = getAppDatabase(application)


        // Create ViewModel
        val viewModelFactory =
            RecipeViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)
        // Bind model to layout
        binding.lifecycleOwner = this
        binding.recipeViewModel = viewModel



        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val textview = findViewById<TextView>(R.id.textView2)
        textview.text = message
        // Capture the layout's TextView and set the string as its text
        /*val textView = findViewById<TextView>(R.id.textView2).apply {
            text = message
        }*/
        val nameObserver = Observer<String> { newName ->
            // Update the UI, in this case, a TextView.
            textView2.text = newName
        }

        /*
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val viewModelFactory = DetailRecipeViewModelFactory(message, application)
        binding.viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailRecipeViewModel::class.java)
        return binding.root
    */
    }
}
