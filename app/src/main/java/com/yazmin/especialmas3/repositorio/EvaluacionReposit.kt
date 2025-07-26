package com.yazmin.especialmas3.repositorio

import android.content.Context
import android.util.Log
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.model.Evaluacion

//Repositorio singleton para operaciones con evaluaciones en la bbdd
class EvaluacionReposit private constructor(context: Context) {
    private val db = EspecialMasDataBase.getDatabase(context) //bbdd y dao sig linea
    private val evaluacionDao = db.evaluacionDao()

    /*suspend fun insertarEvaluacion(evaluacion: Evaluacion) {
        evaluacionDao.insertarEvaluacion(evaluacion)
    }*/
    suspend fun insertarEvaluaciones(lista: List<Evaluacion>) {
        evaluacionDao.insertarEvaluaciones(lista)
    }

    suspend fun obtenerEvaluaciones(idEstudiante: Int, tipoEvaluacion: String): List<Evaluacion> {
        val lista = evaluacionDao.obtenerEvaluacionesPorMomento(idEstudiante, tipoEvaluacion)
        Log.d("EvaluacionReposit", "obtenerEvaluaciones: encontrados ${lista.size} registros para idEstudiante=$idEstudiante, tipoEvaluacion=$tipoEvaluacion")
        return lista
    }

    suspend fun eliminarEvaluacionesPorMomento(idEstudiante: Int, tipoEvaluacion: String) {
        evaluacionDao.eliminarEvaluacionesPorMomento(idEstudiante, tipoEvaluacion)
    }

    companion object { //Instancia singleton para evitar conexiones multiples a la bbdd
        @Volatile private var instance: EvaluacionReposit? = null

        //Método para obtener la instancia singleton del repositorio
        fun getInstance(context: Context) : EvaluacionReposit =
            instance?: synchronized(this){
                instance?: EvaluacionReposit(context).also { instance=it}
            }
    }
}