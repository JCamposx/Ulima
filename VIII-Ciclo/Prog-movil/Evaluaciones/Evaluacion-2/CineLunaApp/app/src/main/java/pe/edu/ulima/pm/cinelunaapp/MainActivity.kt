package pe.edu.ulima.pm.cinelunaapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.cinelunaapp.fragments.CarteleraFragment
import pe.edu.ulima.pm.cinelunaapp.fragments.NosotrosFragment

class MainActivity : AppCompatActivity() {
    private lateinit var dlaMain : DrawerLayout
    private lateinit var nviMain : NavigationView
    private lateinit var nviMainHeader : View
    private lateinit var toolbar :  Toolbar

    // Capturamos los fragments
    private val fragmentCartelera = CarteleraFragment()
    private val fragmentNosotros = NosotrosFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dlaMain = findViewById(R.id.dlaMain)
        nviMain = findViewById(R.id.nviMain)
        nviMainHeader = nviMain.getHeaderView(0)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Obtener el intent del activity anterior
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")

        // Colocar el texto del header del menú
        nviMainHeader.findViewById<TextView>(R.id.nombreUsuarioHeader).text = "$nombreUsuario"

        val data = Bundle()
        data.putString("NOMBRE_USUARIO", nombreUsuario)

        // Agregar argumentos de entrada al fragment
        fragmentCartelera.arguments = data

        // A ejecutar cuando se seleccione una opción del menú
        nviMain.setNavigationItemSelectedListener {
            val ft = supportFragmentManager.beginTransaction()

            // Ejecutar determinada función cuando se seleccione un item
            when(it.itemId) {
                R.id.menCartelera -> mostrarCartelera(ft)
                R.id.menNosotros -> mostrarNosotros(ft)
            }

            ft.addToBackStack(null)
                .commit()

            dlaMain.closeDrawers()
            true
        }

        // Fragment por defecto
        supportFragmentManager.beginTransaction()
            .add(R.id.fcvSecciones, fragmentCartelera)
            .commit()
    }

    private fun mostrarCartelera(ft : FragmentTransaction) {
        ft.replace(R.id.fcvSecciones, fragmentCartelera)
    }

    private fun mostrarNosotros(ft : FragmentTransaction) {
        ft.replace(R.id.fcvSecciones, fragmentNosotros)
    }
}