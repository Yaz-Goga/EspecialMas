package com.yazmin.especialmas3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Docentes") //Entidad de tabla en Room
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //Clave primaria autogenerada
        val nombre: String,
        val apellidos: String,
        val correo: String,
        val contrasena: String) //Atributos
