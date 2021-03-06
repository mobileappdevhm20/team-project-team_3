package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class CreateIngredientFragment() : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditRecipeListener

    private lateinit var ingredientEditText: EditText

    interface EditRecipeListener {
        fun saveItem(id: String?, name: String?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_create_ingredient_input, null)

            ingredientEditText = view.findViewById(R.id.name)
            var textValue = ""
            if (arguments != null) {
                textValue = requireArguments().getString("name").toString()

                var textView = view.findViewById<TextView>(R.id.text_name)
                textView.text = textValue.toString()
            }

            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    listener.saveItem(textValue, ingredientEditText.text.toString())
                }
                .setNegativeButton(
                    R.string.text_cancel
                ) { _, _ ->
                    listener.saveItem(textValue, null)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as EditRecipeListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (
                    context.toString() +
                        " must implement EditRecipeListener"
                    )
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("name", ingredientEditText.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
