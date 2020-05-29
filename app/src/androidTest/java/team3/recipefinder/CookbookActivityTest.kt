package team3.recipefinder

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import team3.recipefinder.activity.CookbookActivity

@RunWith(AndroidJUnit4::class)
class CookbookActivityTest {

   @Test
   fun launchCookbookActivity() {
       val context = InstrumentationRegistry.getInstrumentation().targetContext
       context.startActivity(Intent(context, CookbookActivity::class.java))

       Thread.sleep(20_000)
   }
}