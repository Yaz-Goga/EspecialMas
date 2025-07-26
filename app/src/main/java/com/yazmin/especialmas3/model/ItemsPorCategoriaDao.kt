package com.yazmin.especialmas3.model

import androidx.room.Dao
import androidx.room.Query

@Dao interface ItemsPorCategoriaDao{
//DAO para items por categoria, estado y evaluacion
    @Query("""
        SELECT categoria, item, estado, COUNT(*) as cantidad
        FROM Evaluacion
        WHERE idEstudiante = :idEstudiante AND tipoEvaluacion = :tipoEvaluacion
        GROUP BY categoria, item, estado
    """)

    suspend fun obtenerItemsPorCategoria(
        idEstudiante: Int, //Para filtrar las evaluaciones
        tipoEvaluacion: String //Para filtrar si es inicial, seguimiento o final
    ): List<ItemsPorCategoria>
}
