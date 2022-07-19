package pe.edu.ulima.pm.petclub

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.petclub.models.GestorUsuarios

class EditarPerfilActivity : AppCompatActivity() {
    private lateinit var tviUsername: TextView
    private lateinit var tviEmail: TextView
    private lateinit var eteNombre: EditText
    private lateinit var eteApellido: EditText
    private lateinit var eteCelular: EditText
    private lateinit var eteDescripcion: EditText
    private lateinit var butGuardarCambios: Button
    private lateinit var butCancelarCambios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        tviUsername = findViewById(R.id.tviUsername)
        tviEmail = findViewById(R.id.tviEmail)
        eteNombre = findViewById(R.id.eteNombre)
        eteApellido = findViewById(R.id.eteApellido)
        eteCelular = findViewById(R.id.eteCelular)
        eteDescripcion = findViewById(R.id.eteDescripcion)
        butGuardarCambios = findViewById(R.id.butGuardarCambios)
        butCancelarCambios = findViewById(R.id.butCancelarCambios)

        val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Obtener data de los shared preferences
        val usernameSP = sp.getString(Constantes.SP_USERNAME, "")

        // Mostrar data del usuario
        tviUsername.text = usernameSP!!
        tviEmail.text = sp.getString(Constantes.SP_EMAIL, "")
        eteNombre.setText(sp.getString(Constantes.SP_NOMBRE, ""))
        eteApellido.setText(sp.getString(Constantes.SP_APELLIDO, ""))
        eteCelular.setText(sp.getString(Constantes.SP_CELULAR, ""))
        eteDescripcion.setText(sp.getString(Constantes.SP_DESCRIPCION, ""))

        verificarRegistroCompletado(sp, usernameSP)

        // Guardar cambios del perfil
        butGuardarCambios.setOnClickListener {
            guardarCambios(sp, usernameSP)
        }

        // Cancelar cambios del perfil
        butCancelarCambios.setOnClickListener {
            cancelarCambios()
        }
    }

    // Verificar que el usuario haya completado sus datos cuando se registró
    private fun verificarRegistroCompletado(sp: SharedPreferences, username: String) {
        // Obtener data de los shared preferences
        val registroCompletoSP = sp.getBoolean(Constantes.SP_REGISTRO_COMPLETO, false)

        if (!registroCompletoSP) {
            // Usuario no ha completado registro
            butCancelarCambios.visibility = View.GONE
        } else {
            // Usuario ya ha completado el registro
            butCancelarCambios.visibility = View.VISIBLE

            // Obtener datos del usuario desde Firebase
            GestorUsuarios.getInstance().obtenerDatosUsuario(username)
            { nombre: String, apellido: String, celular: String, descripcion: String ->
                sp.edit().putString(Constantes.SP_NOMBRE, nombre).apply()
                sp.edit().putString(Constantes.SP_APELLIDO, apellido).apply()
                sp.edit().putString(Constantes.SP_CELULAR, celular).apply()
                sp.edit().putString(Constantes.SP_DESCRIPCION, descripcion).apply()
            }
        }
    }

    // Guardar cambios del perfil
    private fun guardarCambios(sp: SharedPreferences, username: String) {
        val nombre = eteNombre.text.toString()
        val apellido = eteApellido.text.toString()
        val celular = eteCelular.text.toString()
        val descripcion = eteDescripcion.text.toString()

        if (verificarCampos(nombre, apellido, celular)) {
            // Se han completado todos los campos del perfil
            GestorUsuarios.getInstance()
                .guardarCambiosPerfil(username, nombre, apellido, celular, descripcion) {
                    sp.edit().putBoolean(Constantes.SP_REGISTRO_COMPLETO, true).apply()
                    sp.edit().putString(Constantes.SP_NOMBRE, nombre).apply()
                    sp.edit().putString(Constantes.SP_APELLIDO, apellido).apply()
                    sp.edit().putString(Constantes.SP_CELULAR, celular).apply()
                    sp.edit().putString(Constantes.SP_DESCRIPCION, descripcion).apply()
                    this.toast("Datos guardados")
                    this.goToActivity(MainActivity())
                }
        } else {
            // Faltan completar campos del perfil
            this.setHintEditTextColor(eteNombre)
            this.setHintEditTextColor(eteApellido)
            this.setHintEditTextColor(eteCelular)

            this.toast("Complete todos los campos")
        }
    }

    // Verificar que se hayan completado los campos del perfil
    private fun verificarCampos(nombre: String, apellido: String, celular: String): Boolean {
        if (nombre == "" || apellido == "" || celular == "") return false
        return true
    }

    // Cancelar hechos al perfil
    private fun cancelarCambios() {
        this.onBackPressed()
    }
}