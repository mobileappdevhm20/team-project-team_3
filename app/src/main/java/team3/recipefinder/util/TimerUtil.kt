package team3.recipefinder.util

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock

fun Int.startTimer(context: Context, name: String) {
    val intent = Intent(AlarmClock.ACTION_SET_TIMER).let {
        it.putExtra(AlarmClock.EXTRA_MESSAGE, name)
        it.putExtra(AlarmClock.EXTRA_LENGTH, this)
        it.putExtra(AlarmClock.EXTRA_SKIP_UI, false)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
