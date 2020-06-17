package team3.recipefinder

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import team3.recipefinder.activity.AndroidTestActivity

/*
This test is meant as an example how to open activities in tests.
It doesn't test anything recify-related.
 */
@Ignore
@RunWith(AndroidJUnit4::class)
class LaunchTestActivityTest {

    @Test
    fun launchActivity() {
        // Start activity
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.startActivity(
            Intent(
                context,
                AndroidTestActivity::class.java
            ).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )

        // Wait for 10 seconds
        Thread.sleep(10_000)
    }
}
