package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class EditRecipePictureFragment : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditRecipePictureListener

    private lateinit var inputField: EditText

    interface EditRecipePictureListener {
        fun onDialogPositiveEditRecipePicture(name: String?)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_edit_recipe_picture, null)

            inputField = view.findViewById(R.id.recipe_value_url)
         /*   var textValue = ""
            if (arguments != null) {
                textValue = requireArguments().getString("oldName").toString()

                var textView = view.findViewById<TextView>(R.id.text_recipe_name)
                textView.hint = textValue
            }
*/
            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    if (inputField.text.toString() != "") {
                        listener.onDialogPositiveEditRecipePicture(
                            inputField.text.toString()
                        )
                    }
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
            listener = context as EditRecipePictureListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement EditRecipeListener")
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("name", inputField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
