package pe.edu.ulima.pm.petclub

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.petclub.models.GestorUsuarios

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verificarLogin()
    }

    // Verificar si el usuario está logeado en la app
    private fun verificarLogin() {
        val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Obtener data de los shared preferences
        val usernameSP = sp.getString(Constantes.SP_USERNAME, "")

        // Usuario ya está logeado en la app
        if (usernameSP != "") verificarRegistroCompletado(usernameSP.toString())
        else this.goToActivity(LoginActivity(), true)
    }

    // Verificar que el usuario haya completado sus datos en el registro
    private fun verificarRegistroCompletado(username: String) {
        val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Verificar registro completo con Firebase
        GestorUsuarios.getInstance().verificarRegistroCompleto(username, {
            // Usuario completó datos de registro
            sp.edit().putBoolean(Constantes.SP_REGISTRO_COMPLETO, true).apply()

            GestorUsuarios.getInstance().obtenerDatosUsuario(username)
            { nombre: String, apellido: String, celular: String, descripcion: String ->
                sp.edit().putString(Constantes.SP_NOMBRE, nombre).apply()
                sp.edit().putString(Constantes.SP_APELLIDO, apellido).apply()
                sp.edit().putString(Constantes.SP_CELULAR, celular).apply()
                sp.edit().putString(Constantes.SP_DESCRIPCION, descripcion).apply()
            }

            // Redireccionar a la pantalla principal
            this.goToActivity(MainActivity(), true)
        }) {
            // Usuario no completó datos de registro
            sp.edit().putBoolean(Constantes.SP_REGISTRO_COMPLETO, false).apply()

            // Redireccionar a la pantalla de editar perfil
            this.goToActivity(EditarPerfilActivity(), true)
        }
    }
}