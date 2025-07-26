package com.yazmin.especialmas3.evaluacionesApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.adapter.AdaptadorEstudianteHA
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.InicioApp.Menu
import com.yazmin.especialmas3.database.EspecialMasDataBase
import kotlinx.coroutines.launch


class EvaluarListAlumnos : AppCompatActivity() {
    private lateinit var recyclerEstudiantesEvaluar: RecyclerView
    private lateinit var adaptador: AdaptadorEstudianteHA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_evaluar_listalumnos)

        //obtener docenteId
        val docenteId = intent.getIntExtra("docenteId", 0)
        if (docenteId == 0) {
            startActivity(Intent(this, Menu::class.java))
            finish()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerEstudiantesEvaluar = findViewById(R.id.recyclerEstudiantesEvaluar)
        recyclerEstudiantesEvaluar.layoutManager = LinearLayoutManager(this)

        /*Adaptador para que al seleccionar un estudiante de la lista de alumnos
        muestre su pantalla de evaluacion historial: inicial, seg, final- evaluar*/

        adaptador = AdaptadorEstudianteHA(emptyList()) {estudiante ->
            val intent = Intent (this, EvaluarAlumnoHistorial::class.java).apply {
                putExtra("idEstudiante", estudiante.id)
                putExtra("nombre", estudiante.nombre)
                putExtra("apellidos", estudiante.apellidos)
                putExtra("grado", estudiante.grado)
                putExtra("grupo",estudiante.grupo)
                putExtra("diagnóstico",estudiante.diagnostico)
                putExtra("fecha de nacimiento",estudiante.fechaNacimiento)
                putExtra("docenteId", docenteId)
                }
            startActivity(intent)
        }
        recyclerEstudiantesEvaluar.adapter = adaptador

        //Dao y lista de estudiantes
        val estudianteDao = EspecialMasDataBase.getDatabase(this).estudianteDao()
        lifecycleScope.launch {
            val estudiantes = estudianteDao.mostrarEstudiantesPorDocente(docenteId)
            Log.d("PruebaDB", "Cantidad de estudiantes : ${estudiantes.size}")
            adaptador.actualizarLista(estudiantes)
        }
    }
    }