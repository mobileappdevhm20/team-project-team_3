package team3.recipefinder.viewmodel.recipe.overview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class AddRecipeFragment() : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditRecipeListener

    private lateinit var recipeNameField : EditText

    interface EditRecipeListener {
        fun onDialogPositiveClick(id:String?,name: String?)
        fun onDialogNegativeClick()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_set_timer_value, null)

            recipeNameField = view.findViewById(R.id.timer_name)
            var abc=""
            if(arguments != null) {
               abc = requireArguments().getString("name").toString()

                var textView = view.findViewById<TextView>(R.id.text_timer_name)
                textView.text = abc.toString()
            }

            builder.setView(view)
                .setPositiveButton(R.string.text_edit
                ) { _, _ ->
                    listener.onDialogPositiveClick(abc,recipeNameField.text.toString())
                }
                .setNegativeButton(R.string.text_cancel
                ) { _, _ ->
                    listener.onDialogNegativeClick()
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
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("name", recipeNameField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}