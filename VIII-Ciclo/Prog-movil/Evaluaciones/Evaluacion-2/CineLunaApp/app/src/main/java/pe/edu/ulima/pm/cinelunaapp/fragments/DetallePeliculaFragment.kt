package pe.edu.ulima.pm.cinelunaapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.cinelunaapp.MainActivity
import pe.edu.ulima.pm.cinelunaapp.R

class DetallePeliculaFragment : Fragment() {
    private var nombre: String? = null
    private var resena: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_detalle_pelicula,
            container,
            false
        )

        // Seleccionar el item (que corresponde al fragment) en el menú como seleccionado
        (activity as MainActivity)
            .findViewById<NavigationView>(R.id.nviMain)
            .setCheckedItem(R.id.menCartelera)

        // Obtener argumentos del fragment
        nombre = requireArguments().getString("NOMBRE_PELICULA")
        resena = requireArguments().getString("RESENA")

        // Colocar información en el layout
        view.findViewById<TextView>(R.id.tviDetalleNombrePelicula).text = nombre
        view.findViewById<TextView>(R.id.tviDetalleResenaPelicula).text = resena

        // Colocar funcionalidad al botón regresar
        view.findViewById<Button>(R.id.butDetalleRegresar).setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return view
    }

    // Cuando se muestre el fragment, esconder el toolbar
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.hide()
    }

    // Cuando se deje de mostrar el fragment, mostrar el toolbar
    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar!!.show()
    }
}