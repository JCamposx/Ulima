package pe.edu.ulima.pm.swapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pe.edu.ulima.pm.swapp.fragments.Registro1Fragment
import pe.edu.ulima.pm.swapp.fragments.Registro2Fragment

class RegistroSlidePagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) { // Al colocar esto, nos saldrá error, hacemos alt+<CR> - implement members - seleccionamos los 2 métodos y aceptar

    private val fragmentsRegistro: List<Fragment> = listOf(
        Registro1Fragment(),
        Registro2Fragment()
    )

    // Retornamos la cantidad de elementos que se van a mostrar en el ViewPager
    override fun getItemCount(): Int {
        return 2 // Porque solamente tenemos 2 fragments
    }

    // Retornamos el fragment que queremos que se muestre en el view pager dependiendo de la posición de este último
    override fun createFragment(position: Int): Fragment {
        return fragmentsRegistro[position]
    }

}