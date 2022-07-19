package pe.edu.ulima.pm.petclub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.edu.ulima.pm.petclub.MapActivity
import pe.edu.ulima.pm.petclub.R
import pe.edu.ulima.pm.petclub.adapters.ListadoPublicacionesAdapter
import pe.edu.ulima.pm.petclub.goToActivity
import pe.edu.ulima.pm.petclub.models.GestorPublicaciones
import pe.edu.ulima.pm.petclub.models.GestorUsuarios
import pe.edu.ulima.pm.petclub.models.beans.Publicacion

class PublicacionesFragment : Fragment() {
    private lateinit var rviPublicaciones: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publicaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bNviBottomMenu)
            .selectedItemId = R.id.menPublicaciones

        rviPublicaciones = view.findViewById(R.id.rviPublicaciones)

        // Obtener publicaciones desde Firebase
        GestorPublicaciones.getInstance().obtenerPublicaciones { listaPublicaciones ->
            cargarPublicaciones(listaPublicaciones)
        }
    }

    // Cargar las publicaciones generales
    private fun cargarPublicaciones(listaPublicaciones: List<Publicacion>) {
        val adapter = ListadoPublicacionesAdapter(
            false,
            listaPublicaciones.sortedBy { it.nombreFoto }.reversed(),
            {/* Eliminar publicación desactivado en posts generales */ },
            { username ->
                // Click en ver detalle
                // Obtener detalle de la publicación desde Firebase
                GestorUsuarios.getInstance()
                    .obtenerDatosUsuario(username) { nombre: String,
                                                     apellido: String,
                                                     celular: String,
                                                     descripcion: String ->
                        mostrarDatosContacto(nombre, apellido, celular)
                    }
            }, {
                // Click en ver mapa
                requireActivity().goToActivity(MapActivity())
            }) { publicacion ->
            // Click en compartir publicacion
            compartirPublicacion(publicacion)
        }
        rviPublicaciones.adapter = adapter
    }

    // Motrar pop-up de detalles de contacto de una publicación
    private fun mostrarDatosContacto(
        nombre: String,
        apellido: String,
        celular: String
    ) {
        // Definir data a pasar al fragment del pop-up
        val data = Bundle()
        data.putString("NOMBRE_COMPLETO", "$nombre $apellido")
        data.putString("CELULAR", celular)

        // Pasar data al fragment del pop-up
        val datosContactoFragment = DatosContactoFragment()
        datosContactoFragment.arguments = data

        // Mostrar pop-up
        datosContactoFragment.show(requireActivity().supportFragmentManager, "customDialog")
    }

    // Compartir una publicación
    private fun compartirPublicacion(publicacion: Publicacion) {
        val msg: String
        val title: String

        // Definir mensaje y título de acuerdo al tipo de publicación
        if (publicacion.tipoPublicacionId == 0) {
            title = "Mascota perdida"
            msg = "Ayúdanos a encontrar a ${publicacion.nombreMascota}." +
                "\nTiene ${publicacion.edad} ${publicacion.tipoEdad.toLowerCase()}." +
                "\nSe perdió el ${publicacion.fechaPerdida} en el distrito de ${publicacion.distrito}."
        } else {
            title = "Mascota en adopción"
            msg = "Ayúdanos a encontrar a una familida para ${publicacion.nombreMascota}." +
                "\nTiene ${publicacion.edad} ${publicacion.tipoEdad.toLowerCase()}." +
                "\nActualmente vive en el distrito de ${publicacion.distrito}."
        }

        // Obtener Uri de la foto de la publicación desde Firebase
        GestorPublicaciones.getInstance().obtenerFotoUri(publicacion.nombreFoto) { fotoUri ->
            // Definir intent a compartir
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, msg)
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, fotoUri)
                type = "image/jpeg"
                putExtra(Intent.EXTRA_TITLE, title)
            }

            // Compartir publicación
            startActivity(Intent.createChooser(intent, null))
        }
    }
}