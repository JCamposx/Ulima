package pe.edu.ulima.pm.covid19tool.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.ulima.pm.covid19tool.Constantes
import pe.edu.ulima.pm.covid19tool.MainActivity
import pe.edu.ulima.pm.covid19tool.R
import pe.edu.ulima.pm.covid19tool.models.GestorCovidData

class DashboardFragment : Fragment() {
    private lateinit var butSincronizar : Button
    private lateinit var butLimpiar : Button
    private lateinit var butVerData : Button
    private lateinit var pbaCargando : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar!!.title = "Dashboard"

        return inflater.inflate(
            R.layout.fragment_dashboard,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butSincronizar = view.findViewById(R.id.butSincronizar)
        butLimpiar = view.findViewById(R.id.butLimpiar)
        butVerData = view.findViewById(R.id.butVerData)
        pbaCargando = view.findViewById(R.id.progressBar)

        val gestor = GestorCovidData()

        val sp = requireActivity().getSharedPreferences(
            Constantes.SP,
            Context.MODE_PRIVATE
        )

        val estaSync = sp.getBoolean(Constantes.SP_IS_SYNC, false)

        if (estaSync) {
            println("---- ESTA   SYNC ----")
            butSincronizar.isEnabled = false
            butLimpiar.isEnabled = true
            butVerData.isEnabled = true
        } else {
            println("---- NO ESTA SYNC ----")
        }

        butSincronizar.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                pbaCargando.visibility = View.VISIBLE
                butSincronizar.isEnabled = false
                butLimpiar.isEnabled = false
                butVerData.isEnabled = false

                withContext(Dispatchers.IO) {
                    gestor.sicronizarCovidData(
                        requireActivity().applicationContext
                    )
                }

                pbaCargando.visibility = View.GONE
                butLimpiar.isEnabled = true
                butVerData.isEnabled = true

                sp.edit().putBoolean(Constantes.SP_IS_SYNC, true).apply()

                Toast.makeText(
                    (activity as MainActivity),
                    "Data sincronizada",
                    Toast.LENGTH_SHORT
                ).show()

                println("------ DATA SINCRONIZADA ------")
            }
        }

        butLimpiar.setOnClickListener {
            gestor.limpiarCovidDataRoom(
                requireActivity().applicationContext
            )

            butSincronizar.isEnabled = true
            butLimpiar.isEnabled = false
            butVerData.isEnabled = false

            sp.edit().putBoolean(Constantes.SP_IS_SYNC, false).apply()

            Toast.makeText(
                requireActivity(),
                "Se limpió la data",
                Toast.LENGTH_SHORT
            ).show()
        }

        butVerData.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fcvSecciones, VerDataFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}