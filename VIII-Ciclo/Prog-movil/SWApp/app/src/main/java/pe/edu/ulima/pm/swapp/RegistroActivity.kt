package pe.edu.ulima.pm.swapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import pe.edu.ulima.pm.swapp.adapters.RegistroSlidePagerAdapter

class RegistroActivity : AppCompatActivity() {
    private lateinit var mViewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Obtenemos una referencia al widget ViewPager2
        mViewPager = findViewById(R.id.pager)

        // Configuramos el ViewPager con el adapter que hemos creado
        val pagerAdapter = RegistroSlidePagerAdapter(this) // Al RegistroSlidePagerAdapter hay que pasarle como argumento de entrada un activity, poniendo this le estamos pasando el activity actual, es decir RegistroActivity
        mViewPager.adapter = pagerAdapter
    }

    // El metodo se activa cada vez que hagamos click en el botón back del móvil
    override fun onBackPressed() {
        if (mViewPager.currentItem == 0) { // Si nos encontramos en el primer item del ViewPager, quiere decir que estamos en RegistroFragment1, por lo que la funcionalidad del botón back se debe mantener, ya sea para volver a un activity anterior o para salir de la app
            super.onBackPressed()
        } else { // Sino, actualizamos el item del ViewPager al anterior
            mViewPager.currentItem -= 1
        }
    }
}