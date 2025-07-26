package com.yazmin.especialmas3.estudiantesApp
/*Este ViewModel no lo estoy utilizando actualmente
porque terminé utilizando Activities...
pero me será util en caso de una ampliación de la app, por eso no lo elimino


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yazmin.especialmas3.model.Estudiante
import com.yazmin.especialmas3.database.EstudianteDao
import kotlinx.coroutines.launch



class EstudiantesViewModel(private val estudianteDao: EstudianteDao): ViewModel(){

    val estudiantes: LiveData<List<Estudiante>> = estudianteDao.consultaEstudiantesRegistrados().asLiveData()

    fun agregarEstudiante(estudiante: Estudiante)=viewModelScope.launch {
        estudianteDao.insertarEstudiante(estudiante)
    }

    fun actualizarEstudiante(estudiante: Estudiante)= viewModelScope.launch {
        estudianteDao.actualizarEstudiante(estudiante)
    }

    fun eliminarEstudiante(estudiante: Estudiante)=viewModelScope.launch {
        estudianteDao.eliminarEstudiante(estudiante)
    }
}*/