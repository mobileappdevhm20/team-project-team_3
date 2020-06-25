package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class EditIngredientFragment : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditIngredientListener

    private lateinit var amountTextField: EditText
    private lateinit var ingredientTextView: TextView

    interface EditIngredientListener {
        fun onDialogPositiveEditIngredient(id: Long?, name: String?, amount: String?)
        fun onDialogNeutralClick(id: Long?, name: String?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_edit_ingredient, null)

            amountTextField = view.findViewById(R.id.text_ingredient_amount_value)
            ingredientTextView = view.findViewById(R.id.text_ingredient_name)

            val ingredientId = requireArguments().getLong("ingredientId")
            var ingredientName = ""

            if (arguments != null) {
                ingredientName = requireArguments().getString("ingredientName").toString()
                val oldAmount = requireArguments().getString("oldAmount").toString()

                ingredientTextView.text = ingredientName
                amountTextField.hint = oldAmount
            }

            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    if (amountTextField.text.toString() == "") {
                        Toast.makeText(activity, "Please enter an amount.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        listener.onDialogPositiveEditIngredient(
                            ingredientId,
                            ingredientName,
                            amountTextField.text.toString()
                        )
                    }
                }
                .setNegativeButton(
                    R.string.text_cancel
                ) { _, _ ->
                }
                .setNeutralButton(
                    R.string.text_delete
                ) { _, _ ->
                    listener.onDialogNeutralClick(ingredientId, ingredientName)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as EditIngredientListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (
                    context.toString() +
                        " must implement EditIngredientListener"
                    )
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("name", amountTextField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
