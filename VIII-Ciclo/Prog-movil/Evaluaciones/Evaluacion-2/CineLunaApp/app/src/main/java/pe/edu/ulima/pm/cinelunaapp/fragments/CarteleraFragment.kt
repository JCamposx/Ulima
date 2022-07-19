package pe.edu.ulima.pm.cinelunaapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.cinelunaapp.MainActivity
import pe.edu.ulima.pm.cinelunaapp.R
import pe.edu.ulima.pm.cinelunaapp.adapters.ListadoPeliculasAdapter
import pe.edu.ulima.pm.cinelunaapp.models.GestorPeliculas
import pe.edu.ulima.pm.cinelunaapp.models.Pelicula

class CarteleraFragment : Fragment() {
    private lateinit var rviPeliculas : RecyclerView

    // Capturamos los fragments
    private val fragmentDetallePelicula = DetallePeliculaFragment()

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Obtener argumentos del fragment
        val nombre = requireArguments().getString("NOMBRE_USUARIO")

        // Colocar título del toolbar
        (activity as MainActivity).supportActionBar!!.title = "Hola $nombre"

        // Seleccionar el item (que corresponde al fragment) en el menú como seleccionado
        (activity as MainActivity)
            .findViewById<NavigationView>(R.id.nviMain)
            .setCheckedItem(R.id.menCartelera)

        return inflater.inflate(
            R.layout.fragment_peliculas,
            container,
            false
        )
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rviPeliculas = view.findViewById(R.id.rviPeliculas)

        // Obtener lista de películas
        val listaPeliculas : List<Pelicula> = GestorPeliculas().obtenerPeliculas()

        // Crear adapter con la lista de películas
        val adapter = ListadoPeliculasAdapter(listaPeliculas) {
            val data = Bundle()
            data.putString("NOMBRE_PELICULA", it.nombre)
            data.putString("RESENA", it.resena)

            val ft = (activity as MainActivity).supportFragmentManager.beginTransaction()

            // Agregar argumentos de entrada al fragment
            fragmentDetallePelicula.arguments = data

            ft.replace(R.id.fcvSecciones, fragmentDetallePelicula)
                .addToBackStack(null)
                .commit()
        }

        rviPeliculas.adapter = adapter
    }
}