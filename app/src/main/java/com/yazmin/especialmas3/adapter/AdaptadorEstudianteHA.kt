package com.yazmin.especialmas3.adapter

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.model.Estudiante

class AdaptadorEstudianteHA (
    private var listaEstudiantes: List <Estudiante>,
    private val editarClick: (Estudiante) -> Unit
) : RecyclerView.Adapter<AdaptadorEstudianteHA.EstudianteViewHolder>(){

//RecyclerView para mostrar la lista de estudiantes con sus datos
    inner class EstudianteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val escuelaTab: TextView = itemView.findViewById(R.id.EscuelaTab)
        val nombreTab: TextView = itemView.findViewById(R.id.NombreTab)
        val apellidosTab: TextView = itemView.findViewById(R.id.ApellidosTab)
        val gradoTab: TextView = itemView.findViewById(R.id.GradoTab)
        val grupoTab : TextView = itemView.findViewById(R.id.GrupoTab)
        val fechaNacimientoTab : TextView = itemView.findViewById (R.id.FechaNacimientoTab)
        val diagnosticoTab : TextView = itemView.findViewById (R.id.DiagnosticoTab)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)

        init {
            itemView.setOnClickListener {
                val estudiante = listaEstudiantes[adapterPosition]
                editarClick(estudiante)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lista_estudiantes,parent,false)
        return EstudianteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = listaEstudiantes [position]
        holder.escuelaTab.text = estudiante.escuela
        holder.nombreTab.text = estudiante.nombre
        holder.apellidosTab.text = estudiante.apellidos
        holder.gradoTab.text = estudiante.grado.toString()
        holder.grupoTab.text = estudiante.grupo
        holder.fechaNacimientoTab.text = estudiante.fechaNacimiento
        holder.diagnosticoTab.text = estudiante.diagnostico

        holder.btnEditar.setOnClickListener {
            editarClick (estudiante) }

        }
    /*Regresa la cantidad de estudiantes a mostrar
    * La funcion actualiza la lista de estudiantes (para refrescar el Recycler)*/

    override fun getItemCount(): Int = listaEstudiantes.size
    fun actualizarLista (nuevaLista: List<Estudiante>) {
        listaEstudiantes = nuevaLista
        notifyDataSetChanged()
    }
}