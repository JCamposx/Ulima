package pe.edu.ulima.pm.petclub.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import pe.edu.ulima.pm.petclub.*
import pe.edu.ulima.pm.petclub.models.GestorPublicaciones

class NuevaPublicacionFragment : Fragment() {
    private lateinit var eteNombreMascota: EditText
    private lateinit var eteEdad: EditText
    private lateinit var aTviTipoPublicacion: AutoCompleteTextView
    private lateinit var aTviTipoEdad: AutoCompleteTextView
    private lateinit var aTviDistrito: AutoCompleteTextView
    private lateinit var tILaFecha: TextInputLayout
    private lateinit var aTviFecha: AutoCompleteTextView
    private lateinit var eteDescripcionPublicacion: EditText
    private lateinit var butSubirFoto: Button
    private lateinit var iviFotoSeleccionada: ImageView
    private lateinit var butMarcarUbicacion: Button
    private lateinit var butCancelarPublicacion: Button
    private lateinit var butPublicar: Button

    // Variables globales
    private var tipoPublicacionId: Int = 0
    private var tipoEdad: String = "Semanas"
    private var distrito: String = "Ancón"
    private lateinit var fotoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nueva_publicacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bNviBottomMenu)
            .selectedItemId = R.id.menNPublicacion

        eteNombreMascota = view.findViewById(R.id.eteNombreMascota)
        eteEdad = view.findViewById(R.id.eteEdad)
        aTviTipoPublicacion = view.findViewById(R.id.aTviTipoPublicacion)
        aTviTipoEdad = view.findViewById(R.id.aTviTipoEdad)
        aTviDistrito = view.findViewById(R.id.aTviDistrito)
        tILaFecha = view.findViewById(R.id.tILaFecha)
        aTviFecha = view.findViewById(R.id.aTviFecha)
        eteDescripcionPublicacion = view.findViewById(R.id.eteDescripcionPublicacion)
        butSubirFoto = view.findViewById(R.id.butSubirFoto)
        iviFotoSeleccionada = view.findViewById(R.id.iviFotoSeleccionada)
        butMarcarUbicacion = view.findViewById(R.id.butMarcarUbicacion)
        butCancelarPublicacion = view.findViewById(R.id.butCancelarPublicacion)
        butPublicar = view.findViewById(R.id.butPublicar)

        // Mostrar date picker
        aTviFecha.setOnClickListener {
            mostrarDatePicker()
        }

        // Elegir foto de publicación
        butSubirFoto.setOnClickListener {
            elegirFoto()
        }

        // Marcar ubicación en GoogleMaps
        butMarcarUbicacion.setOnClickListener {
            requireActivity().goToActivity(MapActivity())
        }

        // Limpiar campos al cancelar la publicación
        butCancelarPublicacion.setOnClickListener {
            eteNombreMascota.setText("")
            eteEdad.setText("")
            aTviFecha.setText("")
            eteDescripcionPublicacion.setText("")
            iviFotoSeleccionada.visibility = View.GONE
        }

        // Registrar la publicación
        butPublicar.setOnClickListener {
            val sp = requireActivity().getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

            val username = sp.getString(Constantes.SP_USERNAME, "").toString()

            // Subir la publicación a Firebase
            GestorPublicaciones.getInstance().subirPublicacion(
                username,
                tipoPublicacionId,
                eteNombreMascota.text.toString(),
                eteEdad.text.toString().toInt(),
                tipoEdad,
                distrito,
                aTviFecha.text.toString(),
                eteDescripcionPublicacion.text.toString(),
                fotoUri
            ) {
                // Recargar las vistas
                requireActivity().goToActivity(SplashActivity(), true)
                requireActivity().toast("Publicación registrada")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        elegirTipoPublicacion()
        mostrarTipoPublicacion()

        elegirTipoEdad()

        elegirDistrito()
    }

    // Cambiar id de publicación cuando se cambie el dropdown
    private fun elegirTipoPublicacion() {
        val items = resources.getStringArray(R.array.tiposPublicacion)

        val adapterItems = ArrayAdapter(requireContext(), R.layout.item_atvi, items)

        with(aTviTipoPublicacion) {
            setAdapter(adapterItems)

            aTviTipoPublicacion.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView: AdapterView<*>,
                                                  view: View,
                                                  position: Int,
                                                  id: Long ->
                    tipoPublicacionId = id.toInt()

                    mostrarTipoPublicacion()
                }
        }
    }

    // Cambiar la vista de acuerdo al tipo de publicación seleccionado
    private fun mostrarTipoPublicacion() {
        when (tipoPublicacionId) {
            0 -> tILaFecha.visibility = View.VISIBLE
            1 -> tILaFecha.visibility = View.GONE
        }
    }

    // Cambiar tipo de edad cuando se cambie el dropdown
    private fun elegirTipoEdad() {
        val items = resources.getStringArray(R.array.tiposEdad)

        val adapterItems = ArrayAdapter(requireContext(), R.layout.item_atvi, items)

        with(aTviTipoEdad) {
            setAdapter(adapterItems)

            aTviTipoEdad.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView: AdapterView<*>,
                                                  view: View,
                                                  position: Int,
                                                  id: Long ->
                    tipoEdad = when (id.toInt()) {
                        0 -> "Semanas"
                        1 -> "Meses"
                        else -> "Años"
                    }
                }
        }
    }

    // Cambiar distrito cuando se cambie el dropdown
    private fun elegirDistrito() {
        val items = resources.getStringArray(R.array.distritos)

        val adapterItems = ArrayAdapter(requireContext(), R.layout.item_atvi, items)

        with(aTviDistrito) {
            setAdapter(adapterItems)

            aTviDistrito.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView: AdapterView<*>,
                                                  view: View,
                                                  position: Int,
                                                  id: Long ->
                    distrito = items[adapterView.getItemIdAtPosition(position).toInt()]
                }
        }
    }

    // Seleccionar foto para publicación
    private fun elegirFoto() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    // Responder cuando se seleccione una foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            fotoUri = data?.data!!
            iviFotoSeleccionada.visibility = View.VISIBLE
            iviFotoSeleccionada.setImageURI(fotoUri)
        }
    }

    // Mostrar datepicker
    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { dia: Int, mes: Int, ano: Int ->
            onFechaSeleccionada(dia, mes + 1, ano)
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    // Cambiar fecha al elegirla en el datepicker
    private fun onFechaSeleccionada(dia: Int, mes: Int, ano: Int) {
        var eteFechaDia = dia.toString()
        if (eteFechaDia.length == 1) eteFechaDia = "0$eteFechaDia"

        var eteFechaMes = mes.toString()
        if (eteFechaMes.length == 1) eteFechaMes = "0$eteFechaMes"

        aTviFecha.setText("$eteFechaDia/$eteFechaMes/$ano")
    }
}
