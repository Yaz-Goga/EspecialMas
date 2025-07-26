package com.yazmin.especialmas3.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yazmin.especialmas3.model.Estudiante
import com.yazmin.especialmas3.model.Evaluacion
import com.yazmin.especialmas3.model.ItemsPorCategoriaDao
import com.yazmin.especialmas3.model.Usuario

//Base de Datos con Room, con las entidades Estudiante, Usuario y Evaluacion

@Database(
    entities = [Estudiante::class, Evaluacion::class, Usuario::class],
    version = 1,
    exportSchema = false
)
//Conexion con Daos
abstract class EspecialMasDataBase : RoomDatabase() {
    abstract fun estudianteDao(): EstudianteDao
    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun itemsPorCategoriaDao(): ItemsPorCategoriaDao

    companion object {
        @Volatile
        private var INSTANCE: EspecialMasDataBase? = null
        //Metodo para instancia singleton de la bbdd
        fun getDatabase(context: Context): EspecialMasDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EspecialMasDataBase::class.java,
                    "especialmas_db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance //Guarda la instancia creada
                instance //Retorna la instancia
            }
        }
    }
}
