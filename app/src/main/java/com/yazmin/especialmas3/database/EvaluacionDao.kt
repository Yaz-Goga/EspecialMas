package com.yazmin.especialmas3.database

import androidx.room.*
import com.yazmin.especialmas3.model.Evaluacion
import com.yazmin.especialmas3.model.ItemsInforme

//Dao para Evaluacion, con CRUD por estudiante, momento y docente.

@Dao
interface EvaluacionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvaluacion(evaluacion: Evaluacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvaluaciones(evaluaciones: List<Evaluacion>)


    @Query("SELECT * FROM Evaluacion WHERE idEstudiante = :idEstudiante AND tipoEvaluacion = :tipoEvaluacion")
    suspend fun obtenerEvaluacionesPorMomento(idEstudiante: Int, tipoEvaluacion: String): List<Evaluacion>

    @Query("DELETE FROM Evaluacion WHERE idEstudiante = :idEstudiante AND tipoEvaluacion = :tipoEvaluacion")
    suspend fun eliminarEvaluacionesPorMomento(idEstudiante: Int, tipoEvaluacion: String): Int

    @Query("SELECT COUNT(*) FROM Evaluacion WHERE idEstudiante = :idEstudiante AND tipoEvaluacion = :tipoEvaluacion")
    suspend fun existeEvaluacion(idEstudiante: Int, tipoEvaluacion: String): Int

    @Query("SELECT DISTINCT idEstudiante FROM Evaluacion WHERE tipoEvaluacion = :tipo")
    suspend fun obtenerIdsEstudiantesConEvaluaciones(tipo: String): List<Int>

    @Query("SELECT DISTINCT idEstudiante FROM evaluacion WHERE tipoEvaluacion = :tipoEvaluacion AND docenteId = :docenteId")
    suspend fun obtenerIdsEstudiantesConEvaluacionesPorDocente(tipoEvaluacion: String, docenteId: Int): List<Int>

    @Delete
    suspend fun eliminarEvaluacion(evaluacion: Evaluacion)

    @Query("""
    SELECT tipoEvaluacion, categoria, item,estado, observaciones 
    FROM Evaluacion 
    WHERE idEstudiante = :idEstudiante AND tipoEvaluacion = :tipoEvaluacion
    ORDER BY categoria, item """)
    suspend fun obtenerInformePorEstudianteyTipo(
        idEstudiante: Int,
        tipoEvaluacion: String
    ): List<ItemsInforme>


}
