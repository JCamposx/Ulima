package pe.edu.ulima.pm.swapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.swapp.R
import pe.edu.ulima.pm.swapp.models.beans.Planeta

class ListadoPlanetasAdapter(
        private val listaPlanetas : List<Planeta>,
        private val mOnItemClickListener: (planeta : Planeta) -> Unit
) : RecyclerView.Adapter<ListadoPlanetasAdapter.ViewHolder>() {

    // Clase que va a contener referencias a los elementos que vamos a definir en cada uno de los items del recycler viewer
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tviPlanetaNombre : TextView
        val tviPlanetaTerreno : TextView
        val tviPlanetaPoblacion : TextView

        init {
            tviPlanetaNombre = view.findViewById(R.id.tviPlanetaNombre)
            tviPlanetaTerreno = view.findViewById(R.id.tviPlanetaTerreno)
            tviPlanetaPoblacion = view.findViewById(R.id.tviPlanetaPoblacion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        R.layout.item_planeta,
                        parent,
                        false
                    )

        return ViewHolder(view)
    }

    // Definimos cómo enlazar data con un viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val planeta = listaPlanetas[position]

        holder.tviPlanetaNombre.text = planeta.nombre
        holder.tviPlanetaTerreno.text = planeta.terreno
        holder.tviPlanetaPoblacion.text = planeta.poblacion

        holder.itemView.setOnClickListener {
            mOnItemClickListener(planeta)
        }
    }

    override fun getItemCount(): Int {
        // Devolver el número de items a mostrar
        return listaPlanetas.size
    }
}