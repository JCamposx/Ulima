package pe.edu.ulima.pm.cinelunaapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.cinelunaapp.MainActivity
import pe.edu.ulima.pm.cinelunaapp.R

class NosotrosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View =  inflater.inflate(
            R.layout.fragment_nosotros,
            container,
            false
        )

        // Colocar título del toolbar
        (activity as MainActivity).supportActionBar!!.title = "¿Quiénes somos?"

        // Seleccionar el item (que corresponde al fragment) en el menú como seleccionado
        (activity as MainActivity)
            .findViewById<NavigationView>(R.id.nviMain)
            .setCheckedItem(R.id.menNosotros)

        // Colocar funcionalidad al botón regresar
        view.findViewById<Button>(R.id.butNosotrosRegresar).setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return view
    }
}