package team3.recipefinder.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import team3.recipefinder.R
import team3.recipefinder.util.getAndRemove

class ManagedChipsListView<T>(context: Context, attributeSet: AttributeSet) : ChipGroup(context, attributeSet), View.OnClickListener {

    var deleteChipObserver: ((T) -> Unit)? = null

    private val objectMapping = hashMapOf<View, T>()

    fun addChip(chipData: T) {
        val chip = Chip(context).apply {
            text = chipData.toString()
            isCloseIconVisible = true

            setOnCloseIconClickListener(this@ManagedChipsListView)
        }

        addView(chip)
        objectMapping.put(chip, chipData)
    }

    fun addButton(chipText: String, onClick: () -> Unit) {
        val chip = Chip(context).apply {
            text = chipText
            isCloseIconVisible = false
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.search_add_button)

            setOnClickListener { onClick() }
        }

        addView(chip, 0)
    }

    override fun onClick(v: View) {
        deleteChipObserver?.invoke(objectMapping.getAndRemove(v)!!)
        removeView(v)
    }
}
