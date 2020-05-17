package team3.recipefinder.viewmodel.recipe.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import team3.recipefinder.dao.RecipeDao
import team3.recipefinder.database.getAppDatabase

class DetailViewModel( application: Application) :
    AndroidViewModel(application) {


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var database: RecipeDao =  getAppDatabase(application).recipeDao()

  //  lateinit var recipe


    init {

    }



}
