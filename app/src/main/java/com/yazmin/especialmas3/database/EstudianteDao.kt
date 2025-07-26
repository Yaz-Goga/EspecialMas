package com.yazmin.especialmas3.database

import androidx.room.*
import com.yazmin.especialmas3.model.Estudiante
import kotlinx.coroutines.flow.Flow

//Dao para Estudiantes, con consultas CRUD para los mismos, y con filtros por docente y por ids
@Dao interface EstudianteDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEstudiante(estudiante: Estudiante)

    @Query("SELECT * FROM Estudiantes WHERE docenteId = :docenteId ORDER BY apellidos ASC")
    suspend fun mostrarEstudiantesPorDocente(docenteId: Int): List<Estudiante>

    @Update
    suspend fun actualizarEstudiante (estudiante: Estudiante)

   /*@Query ("SELECT * FROM estudiantes")
    suspend fun obtenerTodosEstudiantes() : List<Estudiante>*/

    @Query("SELECT * FROM estudiantes ORDER BY apellidos ASC")
    fun consultaEstudiantesRegistrados(): Flow<List<Estudiante>>

    @Query ("SELECT * FROM estudiantes WHERE id = :id Limit 1")
    suspend fun obtenerEstudiantePorId(id: Int): Estudiante?

    /*@Query("SELECT * FROM estudiantes WHERE id IN (:ids)")
    suspend fun obtenerEstudiantesPorIds(ids: List<Int>): List<Estudiante>*/

    @Query("SELECT * FROM estudiantes WHERE id IN (:ids) AND docenteId = :docenteId")
    suspend fun obtenerEstudiantesPorIdsYDocente(ids: List<Int>, docenteId: Int): List<Estudiante>

    @Delete
    suspend fun eliminarEstudiante(estudiante: Estudiante)
}
