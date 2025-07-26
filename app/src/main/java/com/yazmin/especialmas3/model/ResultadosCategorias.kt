package com.yazmin.especialmas3.model

data class ResultadosCategorias( //Entidad de tabla en Room
    val categoria: String,
    val independientes: Int,
    val enProceso: Int,
    val dependientes: Int
)
