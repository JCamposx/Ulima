package pe.edu.ulima.pm.cinelunaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.cinelunaapp.R
import pe.edu.ulima.pm.cinelunaapp.models.Pelicula

class ListadoPeliculasAdapter(
    private val listaPeliculas : List<Pelicula>,
    private val mOnItemClickListener : (pelicula : Pelicula) -> Unit
) : RecyclerView.Adapter<ListadoPeliculasAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tviNombrePelicula : TextView
        val tviHoraPelicula : TextView

        init {
            tviNombrePelicula = view.findViewById(R.id.tviNombrePelicula)
            tviHoraPelicula = view.findViewById(R.id.tviHoraPelicula)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        R.layout.item_pelicula,
                        parent,
                        false
                    )

        return ViewHolder(view)
    }

    // Definir cómo enlazar data con un viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelicula = listaPeliculas[position]

        holder.tviNombrePelicula.text = pelicula.nombre
        holder.tviHoraPelicula.text = pelicula.hora

        holder.itemView.setOnClickListener {
            mOnItemClickListener(pelicula)
        }
    }

    override fun getItemCount(): Int {
        // Retornar el número de items a mostrar
        return listaPeliculas.size
    }
}