package com.yazmin.especialmas3.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.model.ItemsPruebaHA

class AdaptadorPruebaHA(
    private val lista: List<ItemsPruebaHA>
) : RecyclerView.Adapter<AdaptadorPruebaHA.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAspecto: TextView = itemView.findViewById(R.id.txtAspecto)
        val btnIndependiente: Button = itemView.findViewById(R.id.btnIndependiente)
        val btnEnProceso: Button = itemView.findViewById(R.id.btnEnProceso)
        val btnDependiente: Button = itemView.findViewById(R.id.btnDependiente)
        val edtObservaciones: EditText = itemView.findViewById(R.id.edtObservaciones)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_evaluacion, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.txtAspecto.text = item.aspecto
        holder.edtObservaciones.setText(item.observaciones)

        val contexto = holder.itemView.context
        val colorNormal = ContextCompat.getColor(contexto, R.color.colorBotonNormal)
        val colorSeleccionado = ContextCompat.getColor(contexto, R.color.colorBotonSeleccionado)

        fun actualizarColores() {
            holder.btnIndependiente.setBackgroundColor(
                if (item.estado == "Independiente") colorSeleccionado else colorNormal
            )
            holder.btnEnProceso.setBackgroundColor(
                if (item.estado == "En Proceso") colorSeleccionado else colorNormal
            )
            holder.btnDependiente.setBackgroundColor(
                if (item.estado == "Dependiente") colorSeleccionado else colorNormal
            )
        }

        // Lógica de selección/deselección
        holder.btnIndependiente.setOnClickListener {
            item.estado = if (item.estado == "Independiente") null else "Independiente"
            actualizarColores()
        }

        holder.btnEnProceso.setOnClickListener {
            item.estado = if (item.estado == "En Proceso") null else "En Proceso"
            actualizarColores()
        }

        holder.btnDependiente.setOnClickListener {
            item.estado = if (item.estado == "Dependiente") null else "Dependiente"
            actualizarColores()
        }

        // Aplicar colores al cargar
        actualizarColores()

        // Guardar observaciones
        holder.edtObservaciones.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.observaciones = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = lista.size
}
