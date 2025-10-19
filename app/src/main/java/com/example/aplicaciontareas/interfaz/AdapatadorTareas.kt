package com.example.aplicaciontareas.interfaz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicaciontareas.R
import com.example.aplicaciontareas.modelo.Tarea

class AdaptadorTareas(private var listaTareas: List<Tarea>) :
    RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>() {

    // 🔹 ViewHolder: referencia a los elementos visuales de cada tarjeta/item
    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvIdInfo: TextView = itemView.findViewById(R.id.tvIdInfo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]

        // ---- 🔹 Asignamos los datos principales ----
        holder.tvTitulo.text = tarea.titulo
        holder.tvDescripcion.text = tarea.descripcion
        holder.tvFecha.text = "📅 ${tarea.fecha}"

        // ---- 🔹 Mostrar origen de la tarea (local o remota) ----
        val origen = if (tarea.apiId != null) {
            "Remota (${tarea.apiId})"
        } else {
            "Local (#${tarea.localId})"
        }
        holder.tvIdInfo.text = "🆔 $origen"

        // ---- 🔹 Mostrar estado de completado ----

    }

    override fun getItemCount(): Int = listaTareas.size

    // 🔹 Método para actualizar la lista del RecyclerView
    fun actualizarDatos(nuevaLista: List<Tarea>) {
        listaTareas = nuevaLista
        notifyDataSetChanged()
    }
}
