package com.yazmin.especialmas3.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yazmin.especialmas3.model.Usuario

/*Interfaz de Acceso a Datos,de los usuarios o docentes, utilizando Room para
* interactuar con tabla de docentes: Inserta, verifica credenciales de acceso, login*/
@Dao
interface UsuarioDao {
    @Insert suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM Docentes WHERE correo = :correo AND contrasena = :contrasena ")
    suspend fun  login(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM Docentes WHERE correo = :correo")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

}
