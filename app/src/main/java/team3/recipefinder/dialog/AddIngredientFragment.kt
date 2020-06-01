package team3.recipefinder.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import team3.recipefinder.R
import team3.recipefinder.model.Ingredient
import team3.recipefinder.viewmodel.RecipeDetailViewModel
import java.util.ArrayList


class AddIngredientFragment() : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: EditListListener


    interface EditListListener {
        fun onDialogPositiveClick1(amount: String?, name: String?)
        fun onDialogNegativeClick2()

    }
    private lateinit var viewModel: RecipeDetailViewModel


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_ingredient_input, null)

            val ingredients = requireArguments().getParcelableArrayList<Ingredient>("name")
            var checkedBox = "-1"

            val mRgAllButtons = view?.findViewById<RadioGroup>(R.id.radiogroup);

            if (ingredients != null) {


                for (item: Ingredient in ingredients) {
                    val rdbtn = RadioButton(context)
                    rdbtn.id = item.id.toInt()
                    rdbtn.text = item.name
                    rdbtn.isChecked = true
                    mRgAllButtons?.addView(rdbtn)
                }

            }
            var amountEditText = view.findViewById<EditText>(R.id.amount)



            val button: Button = view.findViewById(R.id.buttonAddIngediant)

            button.setOnClickListener {
                dialog?.dismiss()
                listener.onDialogNegativeClick2()

            }


            builder.setView(view)
                .setPositiveButton(
                    R.string.text_edit
                ) { _, _ ->
                    checkedBox = mRgAllButtons?.checkedRadioButtonId.toString()

                    listener.onDialogPositiveClick1(amountEditText.text.toString(), checkedBox)
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