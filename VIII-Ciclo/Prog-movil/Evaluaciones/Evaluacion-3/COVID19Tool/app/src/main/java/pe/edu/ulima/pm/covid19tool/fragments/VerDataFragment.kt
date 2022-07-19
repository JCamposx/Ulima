package pe.edu.ulima.pm.covid19tool.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.covid19tool.Constantes
import pe.edu.ulima.pm.covid19tool.MainActivity
import pe.edu.ulima.pm.covid19tool.R
import pe.edu.ulima.pm.covid19tool.adapters.ListadoCasosCovidAdapter
import pe.edu.ulima.pm.covid19tool.models.GestorCovidData

class VerDataFragment : Fragment() {
    private lateinit var eteFecha : EditText
    private lateinit var rviCasosCovid : RecyclerView
    private lateinit var tviNoData : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar!!.title = "Ver data"

        return inflater.inflate(
            R.layout.fragment_ver_data,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eteFecha = view.findViewById(R.id.eteFecha)
        rviCasosCovid = view.findViewById(R.id.rviCasosCovid)
        tviNoData = view.findViewById(R.id.tviNoData)

        val gestor = GestorCovidData()

        val sp = requireActivity().getSharedPreferences(
            Constantes.SP,
            Context.MODE_PRIVATE
        )

        val primVez = sp.getBoolean(Constantes.SP_1ST_TIME, true)

        if (primVez) {
            val alerta = AlertDialog.Builder(requireActivity())
                .setTitle("COVID 19 Tool")
                .setMessage("Seleccione una fecha")
                .setPositiveButton("Entendido") {_,_->}
                .create()
            alerta.setCanceledOnTouchOutside(false)
            alerta.show()

            sp.edit().putBoolean(Constantes.SP_1ST_TIME, false).apply()
        }

        eteFecha.setOnClickListener {
            mostrarDatePicker()
        }

        eteFecha.addTextChangedListener {
            val fecha : String = eteFecha.text.toString()
                .split("/")
                .reversed()
                .joinToString("")

            val listaCasosCovidxFecha = gestor.obtenerCovidDataxFechaRoom(
                requireActivity().applicationContext,
                fecha
            )

            val casos : MutableMap<String, Int> = mutableMapOf()
            listaCasosCovidxFecha.forEach {
                casos[it[0]] = (casos[it[0]] ?: 0) + 1
            }

            println("CASOS")
            println(casos)

            if (casos.isEmpty()) {
                tviNoData.visibility = View.VISIBLE
            } else {
                tviNoData.visibility = View.GONE

                val lista = casos.toList().map {
                    listOf(
                        it.first,            // Departamento
                        it.second.toString() // Cantidad
                    )
                }

                println("LISTA CASOS")
                println(lista)

                rviCasosCovid.adapter = ListadoCasosCovidAdapter(lista)
            }
        }
    }

    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment {
                dia : Int, mes : Int, ano : Int -> onFechaSeleccionada(dia, mes+1, ano)
        }

        datePicker.show(
            requireActivity().supportFragmentManager,
            "datePicker"
        )
    }

    private fun onFechaSeleccionada(dia : Int, mes : Int, ano : Int) {
        var eteFechaDia = dia.toString()
        if (eteFechaDia.length == 1) eteFechaDia = "0$eteFechaDia"

        var eteFechaMes = mes.toString()
        if (eteFechaMes.length == 1) eteFechaMes = "0$eteFechaMes"

        eteFecha.setText("$eteFechaDia/$eteFechaMes/$ano")
    }
}