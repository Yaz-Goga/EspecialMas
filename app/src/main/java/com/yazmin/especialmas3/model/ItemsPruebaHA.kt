package com.yazmin.especialmas3.model

data class ItemsPruebaHA( //Entidad de tabla en Room
    val aspecto: String,
    var estado: String? = null, //independiente, en proceso, dependiente
    var observaciones: String = ""
)
