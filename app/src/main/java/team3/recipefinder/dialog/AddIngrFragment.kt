package team3.recipefinder.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R
import team3.recipefinder.model.Ingredient


class AddIngrFragment() : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditListListener


    interface EditListListener {
        fun onDialogPositiveClick1(amount: String?, name: String?)
    }


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_set_list, null)

            val a = requireArguments().getParcelableArrayList<Ingredient>("name")
            var ab = "dslf"
            val mRgAllButtons = view.findViewById<RadioGroup>(R.id.radiogroup);

            if (a != null) {


                for (item: Ingredient in a) {
                    val rdbtn = RadioButton(context)
                    rdbtn.id = item.id.toInt()
                    rdbtn.text = "Item: ${item.name}"
                    rdbtn.isChecked = true
                    mRgAllButtons.addView(rdbtn)
                }
            }
            var amountTextedit = view.findViewById<EditText>(R.id.amount)


            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    ab = mRgAllButtons.checkedRadioButtonId.toString()

                    listener.onDialogPositiveClick1(amountTextedit.text.toString(), ab)
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
            listener = context as EditListListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }


}