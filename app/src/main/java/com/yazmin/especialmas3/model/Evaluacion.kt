package com.yazmin.especialmas3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluacion") //Entidad de tabla en Room de Evaluacion
data class Evaluacion(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val docenteId: Int,
    val idEstudiante: Int,
    val tipoEvaluacion: String,
    val categoria: String,
    val item: String,
    val estado: String,
    val observaciones: String? = "Sin observaciones"
)