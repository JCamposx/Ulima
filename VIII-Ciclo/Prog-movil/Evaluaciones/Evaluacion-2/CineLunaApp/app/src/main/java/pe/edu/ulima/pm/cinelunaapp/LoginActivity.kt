package pe.edu.ulima.pm.cinelunaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var eteNombreLogin : EditText
    private lateinit var butLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        eteNombreLogin = findViewById(R.id.eteNombreLogin)
        butLogin = findViewById(R.id.butLogin)

        // Ir al MainActivity cuando se coloque nombre y se dé click a login
        butLogin.setOnClickListener {
            val nombreLogin = eteNombreLogin.text.toString()

            if (nombreLogin == "") {
                Toast.makeText(
                    this,
                    "Ingrese un nombre",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent()
                intent.setClass(this, MainActivity::class.java)

                val data = Bundle()
                data.putString("NOMBRE_USUARIO", nombreLogin)
                intent.putExtras(data)

                startActivity(intent)
            }
        }
    }
}