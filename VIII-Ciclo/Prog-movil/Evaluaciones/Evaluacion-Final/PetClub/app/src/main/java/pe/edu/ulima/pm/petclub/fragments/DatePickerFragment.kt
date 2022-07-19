package pe.edu.ulima.pm.petclub.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import pe.edu.ulima.pm.petclub.R
import java.util.*

class DatePickerFragment(val listener: (dia: Int, mes: Int, ano: Int) -> Unit) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private lateinit var eteFecha: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eteFecha = requireActivity().findViewById(R.id.aTviFecha)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val locale = Locale("es", "ES")
        Locale.setDefault(locale)

        val calendar = Calendar.getInstance()

        val fecha = eteFecha.text.toString().split("/")

        val picker: DatePickerDialog

        if (fecha[0] == "") {
            // Si no se ha colocado una fecha, colocar el día actual al abrir el datepicker
            picker = DatePickerDialog(
                requireContext(),
                R.style.datePickerTheme,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            // Si hay una fecha, colocar esta al abrir el datepicker
            picker = DatePickerDialog(
                requireContext(),
                R.style.datePickerTheme,
                this,
                Integer.parseInt(fecha[2]),
                Integer.parseInt(fecha[1]) - 1,
                Integer.parseInt(fecha[0])
            )
        }

        // Definir el día actual como fecha máxima para el datepicker
        picker.datePicker.maxDate = calendar.timeInMillis

        return picker
    }
}