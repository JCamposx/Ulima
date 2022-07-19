package pe.edu.ulima.pm.petclub

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.petclub.fragments.PerfilFragment
import pe.edu.ulima.pm.petclub.fragments.PublicacionesFragment
import pe.edu.ulima.pm.petclub.fragments.NuevaPublicacionFragment
import pe.edu.ulima.pm.petclub.models.GestorUsuarios

class MainActivity : AppCompatActivity() {
    private lateinit var dlaUserData: DrawerLayout
    private lateinit var bNviBottomMenu: BottomNavigationView
    private lateinit var nviUserData: NavigationView
    private lateinit var nviMainHeader: View

    private var bottomItemId: Int = 0

    // Fragments
    private val fragmentPublicaciones = PublicacionesFragment()
    private val fragmentNuevaPublicacion = NuevaPublicacionFragment()
    private val fragmentMAdopcion = PerfilFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dlaUserData = findViewById(R.id.dlaMain)
        bNviBottomMenu = findViewById(R.id.bNviBottomMenu)
        nviUserData = findViewById(R.id.nviUserData)
        nviMainHeader = nviUserData.getHeaderView(0)

        val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        // Escribir título en el header del navigation view
        nviMainHeader.findViewById<TextView>(R.id.nombreUsuarioHeader).text =
            "Hola, ${sp.getString(Constantes.SP_USERNAME, "").toString()}"

        // Desabilitar abrir navigation view con swipe
        dlaUserData.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Abrir navigation view con botón
        findViewById<ImageButton>(R.id.ibutList).setOnClickListener {
            dlaUserData.openDrawer(nviUserData)
        }

        // Cargar fragment por defecto
        supportFragmentManager.beginTransaction()
            .add(R.id.fcvSecciones, fragmentPublicaciones)
            .commit()

        // Acción al seleccionar ítems del navigation view
        nviUserData.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menCerrarSesion -> cerrarSesion()
                else -> this.goToActivity(EditarPerfilActivity())
            }

            dlaUserData.closeDrawers()
            true
        }

        // Cambio de fragments al seleccionat ítems del bottom navigation view
        bNviBottomMenu.setOnItemSelectedListener {
            val ft = supportFragmentManager.beginTransaction()

            when (it.itemId) {
                R.id.menPublicaciones -> ft.replace(R.id.fcvSecciones, fragmentPublicaciones)
                R.id.menNPublicacion -> ft.replace(R.id.fcvSecciones, fragmentNuevaPublicacion)
                R.id.menPerfilUsuario -> ft.replace(R.id.fcvSecciones, fragmentMAdopcion)
            }

            // Si no se ha seleccionado el mismo ítem varias veces
            if (it.itemId != bottomItemId) {
                // Añadir el fragment a la pila de fragments
                ft.addToBackStack(null)
                bottomItemId = it.itemId
            }

            ft.commit()
            true
        }
    }

    // Cerrar sesión
    private fun cerrarSesion() {
        // Cerrar sesión con Firebase
        GestorUsuarios.getInstance().logout {
            // Borramos los shared preferences
            getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE).edit().clear().apply()

            goToActivity(SplashActivity(), true)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        super.onBackPressed()
    }
}