package team3.recipefinder

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import team3.recipefinder.model.Ingredient
import team3.recipefinder.model.Recipe
import team3.recipefinder.model.RecipeStep
import team3.recipefinder.viewmodel.recipe.detail.DetailRecipeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



    }
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
//val Recipe = new Recipe()
        val recipe= Recipe(1,"hallo")
        val ingredient = listOf(Ingredient(1,"ia"),Ingredient(2,"ib"))
        val step = listOf(RecipeStep(1,"sa"),RecipeStep(2,"sb"))

        val message = "hallo"
        val intent = Intent(this, DetailRecipeActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)


    }
}
