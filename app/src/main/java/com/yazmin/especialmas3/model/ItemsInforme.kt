package com.yazmin.especialmas3.model

data class ItemsInforme( //Entidad de tabla en Room de Items en informes
    val tipoEvaluacion: String,
    val categoria: String,
    val item: String,
    val estado: String?,
    val observaciones: String?
)