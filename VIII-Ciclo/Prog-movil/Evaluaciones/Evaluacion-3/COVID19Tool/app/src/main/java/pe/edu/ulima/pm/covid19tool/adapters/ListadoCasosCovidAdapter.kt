package pe.edu.ulima.pm.covid19tool.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.covid19tool.R

class ListadoCasosCovidAdapter(
    private val listaCasosCovid : List<List<String>>
) : RecyclerView.Adapter<ListadoCasosCovidAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tviDepartamento : TextView
        val tviCantCasos : TextView

        init {
            tviDepartamento = view.findViewById(R.id.tviDepartamento)
            tviCantCasos = view.findViewById(R.id.tviCantCasos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_caso_covid,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviDepartamento.text = listaCasosCovid[position][0]
        holder.tviCantCasos.text = listaCasosCovid[position][1]
    }

    override fun getItemCount(): Int {
        return listaCasosCovid.size
    }
}