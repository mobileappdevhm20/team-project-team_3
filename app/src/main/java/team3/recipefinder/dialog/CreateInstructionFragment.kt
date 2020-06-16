package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class CreateInstructionFragment: DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: CreateInstructionListener

    private lateinit var instructionNameField: EditText

    interface CreateInstructionListener {
        fun onDialogPositiveClick(id: String?, name: String?)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_instruction_input, null)

            instructionNameField = view.findViewById(R.id.instruction_value)
            var textValue = ""
            if (arguments != null) {
                textValue = requireArguments().getString("name").toString()

                var textView = view.findViewById<TextView>(R.id.text_instruction_name)
                textView.text = textValue
            }

            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    listener.onDialogPositiveClick(textValue, instructionNameField.text.toString())
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
            listener = context as CreateInstructionListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement CreateInstructionListener")
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("name", instructionNameField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}