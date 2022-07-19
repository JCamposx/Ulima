package pe.edu.ulima.pm.petclub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.petclub.R
import pe.edu.ulima.pm.petclub.models.GestorPublicaciones
import pe.edu.ulima.pm.petclub.models.beans.Publicacion

class ListadoPublicacionesAdapter(
    private val esParaPerfil: Boolean,
    private val listaPeliculas: List<Publicacion>,
    private val onEliminarPublicacionClickListener: (nombreFoto: String) -> Unit,
    private val onVerDetalleClickListener: (username: String) -> Unit,
    private val onVerMapaClickListener: () -> Unit,
    private val onCompartirCLickListener: (publicacion: Publicacion) -> Unit
) : RecyclerView.Adapter<ListadoPublicacionesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tviUsername: TextView
        val iviFotoPublicacion: ImageView
        val tviTipoPublicacion: TextView
        var tviFechaPerdida: TextView
        val tviDistrito: TextView
        val tviDescripcionPublicacion: TextView
        val tviNombreMascota: TextView
        val tviEdad: TextView
        val ibutEliminarPublicacion: ImageButton
        val ibutVerDetalle: ImageButton
        val ibutVerMapa: ImageButton
        val ibutCompartir: ImageButton

        init {
            tviUsername = view.findViewById(R.id.tviUsername)
            iviFotoPublicacion = view.findViewById(R.id.iviFotoPublicacion)
            tviTipoPublicacion = view.findViewById(R.id.tviTipoPublicacion)
            tviFechaPerdida = view.findViewById(R.id.tviFechaPerdida)
            tviDistrito = view.findViewById(R.id.tviDistrito)
            tviDescripcionPublicacion = view.findViewById(R.id.tviDescripcionPublicacion)
            tviNombreMascota = view.findViewById(R.id.tviNombreMascota)
            tviEdad = view.findViewById(R.id.tviEdad)
            ibutEliminarPublicacion = view.findViewById(R.id.ibutEliminarPublicacion)
            ibutVerDetalle = view.findViewById(R.id.ibutVerDetalle)
            ibutVerMapa = view.findViewById(R.id.ibutVerMapa)
            ibutCompartir = view.findViewById(R.id.ibutCompartir)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_publicacion,
                parent,
                false
            )

        return ViewHolder(view)
    }

    // Definir cómo enlazar data con un viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publicacion = listaPeliculas[position]

        // Esconder botón de eliminar publicación en los posts generales
        if (!esParaPerfil) holder.ibutEliminarPublicacion.visibility = View.GONE

        // Escribir tipo de publicación
        if (publicacion.tipoPublicacionId == 0) {
            holder.tviTipoPublicacion.text = "Mascota perdida"
            holder.tviFechaPerdida.text = "Fecha de pérdida: ${publicacion.fechaPerdida}"
        } else {
            holder.tviTipoPublicacion.text = "Mascota en adopción"
            if (publicacion.nombreMascota == "") holder.tviNombreMascota.visibility = View.GONE
            holder.tviFechaPerdida.visibility = View.GONE
            holder.ibutVerMapa.visibility = View.GONE
        }

        // Escribir datos de la publicación
        holder.tviUsername.text = publicacion.username
        holder.tviNombreMascota.text = "Nombre: ${publicacion.nombreMascota}"
        holder.tviEdad.text = "Edad: ${publicacion.edad} ${publicacion.tipoEdad}"
        holder.tviDistrito.text = "Distrito: ${publicacion.distrito}"
        holder.tviDescripcionPublicacion.text = "Descripción: ${publicacion.descripcionPublicacion}"

        // Obtener foto de la publicación
        GestorPublicaciones.getInstance().obtenerFotoPublicacion(publicacion.nombreFoto) {
            holder.iviFotoPublicacion.setImageBitmap(it)
        }

        // Click botón de eliminar publicación
        holder.ibutEliminarPublicacion.setOnClickListener {
            onEliminarPublicacionClickListener(publicacion.nombreFoto)
        }

        // Click botón de ver detalle
        holder.ibutVerDetalle.setOnClickListener {
            onVerDetalleClickListener(publicacion.username)
        }

        // Click botón de ver mapa
        holder.ibutVerMapa.setOnClickListener {
            onVerMapaClickListener()
        }

        // Click botón de compartir publicación
        holder.ibutCompartir.setOnClickListener {
            onCompartirCLickListener(publicacion)
        }
    }

    override fun getItemCount(): Int {
        // Retornar el número de items a mostrar
        return listaPeliculas.size
    }
}