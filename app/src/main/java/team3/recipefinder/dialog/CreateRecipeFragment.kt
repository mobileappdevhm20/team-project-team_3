package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class CreateRecipeFragment() : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: CreateRecipeListener

    private lateinit var recipeNameField: EditText
    private lateinit var urlField: EditText

    interface CreateRecipeListener {
        fun onDialogPositiveClick(name: String?, url: String?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_recipe_input, null)

            recipeNameField = view.findViewById(R.id.recipe_value)
            urlField = view.findViewById(R.id.url_input)

            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    listener.onDialogPositiveClick(
                        recipeNameField.text.toString(),
                        urlField.text.toString()
                    )
                }
                .setNegativeButton(
                    R.string.text_cancel
                ) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as CreateRecipeListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (
                    context.toString() + " must implement CreateRecipeListener"
                    )
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("name", recipeNameField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
