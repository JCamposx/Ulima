package pe.edu.ulima.pm.petclub

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.petclub.models.GestorUsuarios

class RegistroActivity : AppCompatActivity() {
    private lateinit var eteEmail: EditText
    private lateinit var eteUsername: EditText
    private lateinit var etePassword: EditText
    private lateinit var eteConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        eteEmail = findViewById(R.id.eteEmail)
        eteUsername = findViewById(R.id.eteUsername)
        etePassword = findViewById(R.id.etePassword1)
        eteConfirmPassword = findViewById(R.id.etePassword2)

        // Registrarse con Firebase
        findViewById<Button>(R.id.butRegistro).setOnClickListener {
            val email = eteEmail.text.toString()
            val username = eteUsername.text.toString()
            val password = etePassword.text.toString()
            val confirmPassword = eteConfirmPassword.text.toString()

            if (
                verificarCampos(email, username, password, confirmPassword) &&
                validarFormatoEmail(email) &&
                verificarPasswords(password, confirmPassword)
            ) {
                // Registro con Firebase
                GestorUsuarios.getInstance().registro(email, username, password, {
                    // Registro exitoso
                    val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

                    sp.edit().putString(Constantes.SP_USERNAME, username).apply()
                    sp.edit().putString(Constantes.SP_EMAIL, email).apply()

                    this.goToActivity(SplashActivity(), true)
                }) { cod: Int ->
                    // Error en registro
                    errorRegistro(cod)
                }
            }
        }

        // Redireccionar a login
        findViewById<TextView>(R.id.butIrLogin).setOnClickListener {
            this.onBackPressed()
        }
    }

    // Verificar que se hayan completado todos los campos
    private fun verificarCampos(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email == "" || username == "" || password == "" || confirmPassword == "") {
            var msg = ""

            if (confirmPassword == "") {
                // Falta confirmar contraseña
                this.setHintEditTextColor(eteConfirmPassword, true)
                msg = "Vuelva a introducir la contraseña"
            }

            if (password == "") {
                // Falta contraseña
                this.setHintEditTextColor(etePassword, true)
                msg = "Introduzca una contraseña"
            }

            if (username == "") {
                // Falta username
                this.setHintEditTextColor(eteUsername, true)
                msg = "Introduzca un email"
            }

            if (email == "") {
                // Falta email
                this.setHintEditTextColor(eteEmail, true)
                msg = "Introduzca un email"
            }

            this.toast(msg)
            return false
        }

        return true
    }

    // Verificar que el email introducido cumpla con el patrón de un email
    private fun validarFormatoEmail(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Email no cumple patrón
            this.setHintEditTextColor(eteEmail, true)
            this.toast("Ingrese un email válido")
            return false
        }
        return true
    }

    // Verificar que las contraseñas introducidas coincidan
    private fun verificarPasswords(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            // Error en contraseñas
            this.setHintEditTextColor(etePassword)
            this.setHintEditTextColor(eteConfirmPassword, true, true)
            this.toast("Las contraseñas no coinciden")
            return false
        }
        return true
    }

    // Mostrar error en el registro
    private fun errorRegistro(cod: Int) {
        val msg: String = when (cod) {
            1 -> {
                this.setHintEditTextColor(eteEmail, true, true)
                "El email ya está registrado"
            }
            2 -> {
                this.setHintEditTextColor(eteUsername, true, true)
                "El username ya está registrado"
            }
            else -> {
                this.setHintEditTextColor(etePassword, true, true)
                this.setHintEditTextColor(eteConfirmPassword, false, true)
                "La contraseña debe tener mínimo 6 caracteres"
            }
        }

        this.toast(msg)
    }
}