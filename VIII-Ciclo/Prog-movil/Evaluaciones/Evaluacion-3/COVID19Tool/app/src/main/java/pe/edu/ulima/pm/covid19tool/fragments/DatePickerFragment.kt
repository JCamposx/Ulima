package pe.edu.ulima.pm.covid19tool.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import pe.edu.ulima.pm.covid19tool.R
import java.util.*

class DatePickerFragment(
    val listener : (dia : Int, mes : Int, ano : Int) -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var eteFecha : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eteFecha = requireActivity().findViewById(R.id.eteFecha)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val locale = Locale("es", "ES")
        Locale.setDefault(locale)

        val calendar = Calendar.getInstance()

        val fecha = eteFecha.text.toString().split("/")

        val picker : DatePickerDialog

        if (fecha[0] == "") {
            picker = DatePickerDialog(
                requireContext(),
                R.style.datePickerTheme,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            picker = DatePickerDialog(
                requireContext(),
                R.style.datePickerTheme,
                this,
                Integer.parseInt(fecha[2]),
                Integer.parseInt(fecha[1]) - 1,
                Integer.parseInt(fecha[0])
            )
        }

        picker.datePicker.maxDate = calendar.timeInMillis

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 2)
        calendar.set(Calendar.YEAR, 2020)

        picker.datePicker.minDate = calendar.timeInMillis

        return picker
    }
}