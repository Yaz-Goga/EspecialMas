package com.yazmin.especialmas3.model

data class CategoriaHA( //Entidad de tabla en Room de Categorias de evaluacion que tiene nombre e items
    val nombreCategoria: String,
    val items: List<ItemsPruebaHA>
)
