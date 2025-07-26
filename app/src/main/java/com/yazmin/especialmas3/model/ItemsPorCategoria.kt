package com.yazmin.especialmas3.model

data class ItemsPorCategoria( //Entidad de tabla en Room de los items por categoria
    val categoria : String,
    val item : String,
    val estado : String,
    val cantidad: Int
)
