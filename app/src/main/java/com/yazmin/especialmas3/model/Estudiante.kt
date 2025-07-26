package com.yazmin.especialmas3.model

import androidx.room.PrimaryKey
import androidx.room.Entity


@Entity( tableName = "Estudiantes") //Entidad de tabla en Room Estudiante
data class Estudiante(
    @PrimaryKey(autoGenerate = true) val id : Int =0, //Clave primaria autogenerada (id)
    val docenteId : Int,
    val escuela: String,
    val nombre: String,
    val apellidos: String,
    val grado: Int,
    val grupo: String,
    val fechaNacimiento: String,
    val diagnostico: String
)
