package pe.edu.ulima.pm.swapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.swapp.fragments.EspeciesFragment
import pe.edu.ulima.pm.swapp.fragments.NavesFragment
import pe.edu.ulima.pm.swapp.fragments.PlanetasFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mDlaMain : DrawerLayout
    private lateinit var mNviMain : NavigationView
    private lateinit var toolbar : Toolbar

    // Capturamos los fragments
    private val fragmentEspecies = EspeciesFragment()
    private val fragmentNaves = NavesFragment()
    private val fragmentPlanetas = PlanetasFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDlaMain = findViewById(R.id.dlaMain)
        mNviMain = findViewById(R.id.nviMain)

        // Esta función se va a ejecutar cuando seleccionemos una opción del menú
        mNviMain.setNavigationItemSelectedListener {
            //El it representa al item del menú al que se le ha hecho click

            val ft = supportFragmentManager.beginTransaction()

            it.isChecked = true // Al seleccionar un item, se queda "marcado"

            // Cuando se haga click a determinado item, se ejecutará una función diferente para
            // mostrar un determinado fragment
            when(it.itemId) {
                R.id.menPlanetas -> mostrarFragmmenPlanetas(ft)
                R.id.menNaves -> mostrarFragmentNaves(ft)
                R.id.menVehiculos -> mostrarFragmmenVehiculos(ft)
                R.id.menPersonas -> mostrarFragmentPersonas(ft)
                R.id.menPeliculas -> mostrarFragmentPeliculas(ft)
                R.id.menEspecies -> mostrarFragmentEspecies(ft)
                R.id.menLogout -> logout()
            }

            ft.addToBackStack(null) // Añadimos el item seleccionado a una pila para regresar
                                          // a él cuando se presione back
            ft.commit()

            mDlaMain.closeDrawers() // Al seleccionar un item, se cierra el drawer layout
            true
        }

        // Cargar el fragment por defecto
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcvSecciones, fragmentPlanetas)
        ft.commit()

        // Configurando toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //ejecutarThread()
    }

    private fun logout() {
        val editor = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE).edit()
        editor.putString(Constantes.SP_USERNAME, null) // Cambiamos el valor de la llave USERNAME a
                                                       // nulo para que no se tenga almacenado a ningún usuario
        editor.commit()
        finish()
    }

    private fun mostrarFragmmenPlanetas(ft : FragmentTransaction) {
        ft.replace(R.id.fcvSecciones, fragmentPlanetas)
    }

    private fun mostrarFragmentNaves(ft : FragmentTransaction) {
        ft.replace(R.id.fcvSecciones, fragmentNaves)
    }

    private fun mostrarFragmmenVehiculos(ft : FragmentTransaction) {
        TODO("Not yet implemented")
    }

    private fun mostrarFragmentPersonas(ft : FragmentTransaction) {
        TODO("Not yet implemented")
    }

    private fun mostrarFragmentPeliculas(ft : FragmentTransaction) {
        TODO("Not yet implemented")
    }

    private fun mostrarFragmentEspecies(ft : FragmentTransaction) {
        ft.replace(R.id.fcvSecciones, fragmentEspecies)
    }

    // Definimos menú del activity (estrella)
    //override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //    menuInflater.inflate(R.menu.menu_planetas, menu)
    //    return true
    //}
    //
    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //    if (item.itemId == R.id.menIrRegistro) {
    //        val intent = Intent(this, RegistroActivity::class.java)
    //        startActivity(intent)
    //    }
    //    return true
    //}

    fun ejecutarThread() {
        val thread : Thread = Thread {
            Thread.sleep(2000L)
            toolbar.title = "Titulo 2" // Nos dará error por esto ya que todos los objetos view se
            // crean en el hilo principal, si tratamos de acceder a estos objetos view desde otros
            // hilos, android nos dará error para salvaguardar el estado visual de la aplicación
            println("Prueba de hilo")
        }
        thread.start() // Si no le decimos al hilo que empiece a ejecutarse, nunca se ejecutará la
                       // función que tiene definida el hilo
        toolbar.title = "Titulo 0"
        println(toolbar.title)
        println("Fuera del hilo")
    }
}