package team3.recipefinder.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R

class EditInstructionFragment : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditInstructionListener

    private lateinit var instructionTextField: EditText

    interface EditInstructionListener {
        fun onDialogPositiveEditInstruction(id: Long?, instruction: String?)
        fun onDialogNeutralEditInstruction(id: Long?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_edit_instruction, null)

            instructionTextField = view.findViewById(R.id.text_instruction_value)

            val relInstructionId = requireArguments().getLong("relInstructionId")

            if (arguments != null) {
                val oldInstruction = requireArguments().getString("oldInstruction").toString()

                instructionTextField.hint = oldInstruction
            }

            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    if (instructionTextField.text.toString() != "") {
                        listener.onDialogPositiveEditInstruction(
                            relInstructionId,
                            instructionTextField.text.toString()
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
                    listener.onDialogNeutralEditInstruction(relInstructionId)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as EditInstructionListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (
                    context.toString() +
                        " must implement EditInstructionListener"
                    )
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("name", instructionTextField.text.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
