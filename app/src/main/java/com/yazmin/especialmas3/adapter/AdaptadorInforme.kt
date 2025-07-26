package com.yazmin.especialmas3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.yazmin.especialmas3.model.ItemsInforme
import com.yazmin.especialmas3.R

class AdaptadorInforme(private val lista: List<ItemsInforme>) :
    RecyclerView.Adapter<AdaptadorInforme.ViewHolder>() {
    /*Adaptador: para mostrar los datos de las evaluaciones, con los datos de
    * tipo de Evaluacion, categoria, items, estado y observaciones si las hay.
    *Hace uso del siguiente ViewHolder */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoria: TextView = view.findViewById(R.id.tvCategoria)
        val item: TextView = view.findViewById(R.id.tvItem)
        val estado: TextView = view.findViewById(R.id.tvEstado)
        val observaciones: TextView = view.findViewById(R.id.tvObservaciones)
    }

    //Infla la vista para los items de cada categoria
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.informes_item, parent, false)
        return ViewHolder(view)
    }

    //Aqui se relacionan con los elementos del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.categoria.text = item.categoria
        holder.item.text = item.item
        holder.estado.text = item.estado ?: "Sin estado"
        holder.observaciones.text = item.observaciones ?: "-"

        when (item.estado?.lowercase()) {
            "independiente" -> holder.estado.setTextColor(Color.parseColor("#2E7D32")) // verde
            "en proceso" -> holder.estado.setTextColor(Color.parseColor("#F9A825")) // amarillo
            "dependiente" -> holder.estado.setTextColor(Color.parseColor("#C62828")) // rojo
            else -> holder.estado.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = lista.size
}