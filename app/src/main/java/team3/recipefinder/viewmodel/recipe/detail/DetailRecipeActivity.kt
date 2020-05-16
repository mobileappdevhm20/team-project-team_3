package team3.recipefinder.viewmodel.recipe.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import team3.recipefinder.R

class DetailRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
val textview= findViewById<TextView>(R.id.textView2)
        textview.text=message
        // Capture the layout's TextView and set the string as its text
        /*val textView = findViewById<TextView>(R.id.textView2).apply {
            text = message
        }*/


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
