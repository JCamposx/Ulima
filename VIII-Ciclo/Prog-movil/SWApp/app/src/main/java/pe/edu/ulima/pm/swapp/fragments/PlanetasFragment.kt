package pe.edu.ulima.pm.swapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import pe.edu.ulima.pm.swapp.Constantes
import pe.edu.ulima.pm.swapp.FotoActivity
import pe.edu.ulima.pm.swapp.R
import pe.edu.ulima.pm.swapp.RegistroActivity
import pe.edu.ulima.pm.swapp.adapters.ListadoPlanetasAdapter
import pe.edu.ulima.pm.swapp.models.GestorPlanetas
import pe.edu.ulima.pm.swapp.models.beans.Planeta

class PlanetasFragment : Fragment() {
    private lateinit var rviPlanetas : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title="Planetas"
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_planetas,
            container,
            false
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_planetas, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menIrRegistro) {
            // Ir al nuevo foto activity
            val intent = Intent(activity, FotoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //obtenemos una referencia al recycler view
        rviPlanetas = view.findViewById(R.id.rviPlanetas)

        //val listaPlanetas : List<Planeta> = GestorPlanetas().obtenerListaPlanetas()
        //GestorPlanetas().obtenerListaPlanetas {
        //    val adapter = ListadoPlanetasAdapter(it) {
        //        Log.i("PlanetasFragment", "Se hizo click en el planeta ${it.nombre}")
        //    }
        //    rviPlanetas.adapter = adapter

        // Obtenemos la lista de planetas de manera asíncrona con corutinas
        val gestor = GestorPlanetas()

        // Contexto de lanzamiento de la corrutina
        // Por defecto: Default -> Para tareas de alto costo computacional
        // UI: -> Para tareas que no tienen un costo alto pero tiene que esperar cierto tiempo, tienen paradas, no son continuas

        // Contexto por default
        //GlobalScope.launch {
        //    val lista = gestor.obtenerListaPlanetasCorrutinas()
        //
        //    withContext(Dispatchers.Main) {
        //        val adapter = ListadoPlanetasAdapter(lista) {
        //            Log.i("PlanetasFragment", "Se hizo click en el planeta ${it.nombre}")
        //        }
        //        rviPlanetas.adapter = adapter
        //    }
        //}

        // Contexto UI
        //GlobalScope.launch(Dispatchers.IO) {
        //    val lista = gestor.obtenerListaPlanetasCorrutinas()
        //
        //    // Como es Main, se ejecuta en el hilo principal
        //    withContext(Dispatchers.Main) {
        //        val adapter = ListadoPlanetasAdapter(lista) {
        //            Log.i("PlanetasFragment", "Se hizo click en el planeta ${it.nombre}")
        //        }
        //        rviPlanetas.adapter = adapter
        //    }
        //}

        // Obtenemos referencia al SP
        val sp = requireActivity().getSharedPreferences(
            Constantes.SP,
            Context.MODE_PRIVATE
        )

        // Contexto UI v2
        // Como es Main, lo de dentro se ejecuta en el hilo principal
        GlobalScope.launch(Dispatchers.Main) {
            var lista : List<Planeta> = mutableListOf()

            val estaSincronizado = sp.getBoolean(Constantes.SP_IS_SYNC, false)

            if (!estaSincronizado) {
                // Lo siguiente, como es IO, lo ejecutamos en un hilo secundario

                // Obtenemos la data de la API mediante corrutinas
                lista = withContext(Dispatchers.IO) {
                    gestor.obtenerListaPlanetasCorrutinas()
                }

                // Guardamos en Room los planetas obtenidos por Room
                gestor.guardarListaPlanetasRoom(
                    requireActivity().applicationContext,
                    lista
                )

                gestor.guardarListaPlanetasFirebase (
                    lista,
                    { // Caso éxito guardado Firebase
                        sp.edit().putBoolean(Constantes.SP_IS_SYNC, true).commit()

                        // Como salimos de IO, esto vuelve a ser ejecutado en el hilo principal
                        cargarListaPlanetas(lista)
                    }
                ) { // Casos error guardado Firebase
                    Toast.makeText(
                        requireActivity(),
                        "Error: ${it}",
                        Toast.LENGTH_SHORT
                    )
                }
            } else {
                // Obtenemos la lista de planetas desde Room
                //lista = gestor.obtenerListaPlanetasPlanetasRoom(
                //    requireContext().applicationContext
                //)

                // Obtenemos la lista de planetas desde Firebase
                gestor.obtenerListaPlanetasFirebase({
                    cargarListaPlanetas(it)
                }) {
                    Toast.makeText(
                        requireActivity(),
                        "Error: ${it}",
                        Toast.LENGTH_SHORT
                    )
                }

                // Como salimos de IO, esto vuelve a ser ejecutado en el hilo principal
                cargarListaPlanetas(lista)
            }
        }
    }

    private fun cargarListaPlanetas(lista : List<Planeta>) {
        val adapter = ListadoPlanetasAdapter(lista) {
            Log.i("PlanetasFragment", "Se hizo click en el planeta ${it.nombre}")
        }
        rviPlanetas.adapter = adapter
    }
}