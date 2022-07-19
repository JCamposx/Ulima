package pe.edu.ulima.pm.petclub

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.petclub.models.GestorUsuarios

class LoginActivity : AppCompatActivity() {
    private lateinit var eteUsername: EditText
    private lateinit var etePassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        eteUsername = findViewById(R.id.eteUsername)
        etePassword = findViewById(R.id.etePassword)

        findViewById<Button>(R.id.butLogin).setOnClickListener {
            val username = eteUsername.text.toString()
            val password = etePassword.text.toString()

            if (verificarCampos(username, password)) {
                // Login con Firebase
                GestorUsuarios.getInstance().login(username, password, { email ->
                    // Login correcto
                    val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

                    // Guardar datos en los shared preferences
                    sp.edit().putString(Constantes.SP_USERNAME, username).apply()
                    sp.edit().putString(Constantes.SP_EMAIL, email).apply()

                    verificarRegistroCompletado(sp)
                }) { cod: Int ->
                    // Error en el login
                    val msg: String

                    if (cod == 1) {
                        msg = "Contraseña incorrecta"
                        this.setHintEditTextColor(etePassword, setFocus = true, clearText = true)
                    } else {
                        msg = "El usuario ingresado no está registrado"
                        this.setHintEditTextColor(eteUsername, true, true)
                        this.setHintEditTextColor(etePassword, false, true)
                    }

                    this.toast(msg)
                }
            }
        }

        // Redireccionar a registro
        findViewById<Button>(R.id.butIrRegistro).setOnClickListener {
            this.goToActivity(RegistroActivity())
        }
    }

    // Verificar que se hayan completado todos los campos del login
    private fun verificarCampos(username: String, password: String): Boolean {
        var msg = ""

        if (username == "" && password == "") {
            // No se ha llenado username ni password
            this.setHintEditTextColor(eteUsername, true)
            this.setHintEditTextColor(etePassword)
            msg = "Complete todos los campos"
        } else if (username == "") {
            // No se ha llenado username
            this.setHintEditTextColor(eteUsername, true)
            msg = "Ingrese un username"
        } else if (password == "") {
            // No se ha llenado password
            this.setHintEditTextColor(etePassword, true)
            msg = "Ingrese una contraseña"
        }

        if (msg != "") {
            this.toast(msg)
            return false
        }

        return true
    }

    // Verificar que el usuario haya completado sus datos cuando se registró
    private fun verificarRegistroCompletado(sp: SharedPreferences) {
        val username = sp.getString(Constantes.SP_USERNAME, "").toString()

        GestorUsuarios.getInstance().verificarRegistroCompleto(username, {
            // Usuario completó datos de registro
            sp.edit().putBoolean(Constantes.SP_REGISTRO_COMPLETO, true).apply()

            // Obtener datos del usuario con Firebase
            GestorUsuarios.getInstance().obtenerDatosUsuario(username)
            { nombre: String, apellido: String, celular: String, descripcion: String ->
                sp.edit().putString(Constantes.SP_NOMBRE, nombre).apply()
                sp.edit().putString(Constantes.SP_APELLIDO, apellido).apply()
                sp.edit().putString(Constantes.SP_CELULAR, celular).apply()
                sp.edit().putString(Constantes.SP_DESCRIPCION, descripcion).apply()
            }

            // Redireccionar a pantalla principal
            this.goToActivity(MainActivity(), true)
        }) {
            // Usuario no completó datos de registro
            sp.edit().putBoolean(Constantes.SP_REGISTRO_COMPLETO, false).apply()

            // Redireccionar a editar perfil
            this.goToActivity(EditarPerfilActivity(), true)
        }
    }
}
