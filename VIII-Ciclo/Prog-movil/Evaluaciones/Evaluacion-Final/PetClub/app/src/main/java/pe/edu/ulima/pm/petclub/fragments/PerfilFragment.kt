package pe.edu.ulima.pm.petclub.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.edu.ulima.pm.petclub.*
import pe.edu.ulima.pm.petclub.adapters.ListadoPublicacionesAdapter
import pe.edu.ulima.pm.petclub.models.GestorPublicaciones
import pe.edu.ulima.pm.petclub.models.GestorUsuarios
import pe.edu.ulima.pm.petclub.models.beans.Publicacion

class PerfilFragment : Fragment() {
    private lateinit var tviUsername: TextView
    private lateinit var tviEmail: TextView
    private lateinit var tviNombreCompleto: TextView
    private lateinit var tviCelular: TextView
    private lateinit var tviDescripcion: TextView
    private lateinit var rviPublicaciones: RecyclerView
    private lateinit var tviSinPublicaciones: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bNviBottomMenu)
            .selectedItemId = R.id.menPerfilUsuario

        // Redireccionar a pantalla de editar perfil al pulsar botón de editar
        view.findViewById<ImageButton>(R.id.ibutEditarPerfil).setOnClickListener {
            requireActivity().goToActivity(EditarPerfilActivity())
        }

        tviUsername = view.findViewById(R.id.tviUsername)
        tviEmail = view.findViewById(R.id.tviEmail)
        tviNombreCompleto = view.findViewById(R.id.tviNombreCompleto)
        tviCelular = view.findViewById(R.id.tviCelular)
        tviDescripcion = view.findViewById(R.id.tviDescripcion)
        rviPublicaciones = view.findViewById(R.id.rviPublicaciones)
        tviSinPublicaciones = view.findViewById(R.id.tviSinPublicaciones)

        val sp = requireActivity().getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Obtener data de los shared preferences
        val username = sp.getString(Constantes.SP_USERNAME, "").toString()

        mostrarDatos(username)

        // Obtener publicaciones del usuario desde Firebase
        GestorPublicaciones.getInstance()
            .obtenerPublicacionesParaUsuario(username) { listaPublicaciones ->
                cargarPublicaciones(listaPublicaciones)
            }
    }

    // Mostrar datos del usuario
    private fun mostrarDatos(username: String) {
        val sp = requireActivity().getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Obtener data de los shared preferences
        val nombre = sp.getString(Constantes.SP_NOMBRE, "").toString()
        val apellido = sp.getString(Constantes.SP_APELLIDO, "").toString()
        val descripcion = sp.getString(Constantes.SP_DESCRIPCION, "").toString()

        // Mostrar data del usuario
        tviUsername.text = username
        tviEmail.text = sp.getString(Constantes.SP_EMAIL, "").toString()
        tviNombreCompleto.text = "$nombre $apellido"
        tviCelular.text = sp.getString(Constantes.SP_CELULAR, "").toString()

        // Verificar si el usuario tiene registrado una descripción
        if (descripcion != "") tviDescripcion.text = descripcion
        else tviDescripcion.visibility = View.GONE
    }

    // Cargar publicaciones que le pertenecen un determinado usuario
    private fun cargarPublicaciones(listaPublicaciones: List<Publicacion>) {
        if (listaPublicaciones.isEmpty()) {
            // Si no hay publicaciones, indicarlo por pantalla
            tviSinPublicaciones.visibility = View.VISIBLE
        } else {
            // Si hay publicaciones, ocultar mensaje de sin publicaciones y cargar recycler view
            tviSinPublicaciones.visibility = View.GONE

            val adapter = ListadoPublicacionesAdapter(true, listaPublicaciones,
                { nombreFoto ->
                    // Click en eliminar publicación
                    eliminarPublicacion(nombreFoto)
                }, { username ->
                    // Click en ver detalle
                    // Obtener detalle de la publicación desde Firebase
                    GestorUsuarios.getInstance()
                        .obtenerDatosUsuario(username) { nombre:
                                                         String,
                                                         apellido: String,
                                                         celular: String,
                                                         descripcion: String ->
                            mostrarDatosContacto(nombre, apellido, celular)
                        }
                }, {
                    // Click en ver mapa
                    // Redireccionar al activity de mapa
                    requireActivity().goToActivity(MapActivity())
                }) { publicacion ->
                // Click en compartir publicacion
                compartirPublicacion(publicacion)
            }

            rviPublicaciones.adapter = adapter
        }
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

    // Eliminar una publicación
    private fun eliminarPublicacion(nombreFoto: String) {
        // Eliminar publicación desde Firebase
        GestorPublicaciones.getInstance().eliminarPublicacion(nombreFoto)

        // Recargar las vistas
        requireActivity().goToActivity(MainActivity(), true)
        requireActivity().toast("Publicacion eliminada")
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