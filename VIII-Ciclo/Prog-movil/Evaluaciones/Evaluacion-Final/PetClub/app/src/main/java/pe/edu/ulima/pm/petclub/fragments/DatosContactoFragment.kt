package pe.edu.ulima.pm.petclub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pe.edu.ulima.pm.petclub.R

class DatosContactoFragment : DialogFragment() {
    private lateinit var tviNombreContacto: TextView
    private lateinit var tviCelularContacto: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_datos_contacto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tviNombreContacto = view.findViewById(R.id.tviNombreContacto)
        tviCelularContacto = view.findViewById(R.id.tviCelularContacto)

        // Escribir en el pop-up de datos de contacto
        tviNombreContacto.text = requireArguments().getString("NOMBRE_COMPLETO")
        tviCelularContacto.text = requireArguments().getString("CELULAR")
    }
}