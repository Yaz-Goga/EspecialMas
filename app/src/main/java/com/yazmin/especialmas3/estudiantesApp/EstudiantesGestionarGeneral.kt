package com.yazmin.especialmas3.estudiantesApp

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.InicioApp.Menu
import com.yazmin.especialmas3.adapter.AdaptadorEstudianteHA
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.databinding.ActivityEstudiantesGestionarGeneralBinding
import kotlinx.coroutines.launch

class EstudiantesGestionarGeneral : AppCompatActivity() {

    private lateinit var binding: ActivityEstudiantesGestionarGeneralBinding
    private lateinit var adapter: AdaptadorEstudianteHA
    private var docenteId : Int = 0 //importante para filtrar estudiantes por docente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
         binding = ActivityEstudiantesGestionarGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //validacion de que hay un docente asociado o te regresa al menu
        docenteId = intent.getIntExtra("docenteId", 0)
        if (docenteId == 0){
            startActivity(Intent(this, Menu::class.java))
            finish()
            return
        }

        binding.recViewEstudiantes.layoutManager = LinearLayoutManager(this)
        adapter = AdaptadorEstudianteHA(emptyList()){ estudiante ->
            val intent = Intent(this, EstudiantesAgregar::class.java)
            intent.putExtra("idEstudiante", estudiante.id)
            intent.putExtra("docenteId", docenteId)
            startActivity(intent)
        }
        binding.recViewEstudiantes.adapter = adapter

        //Cargar estudiantes desde el dao

        val dao = EspecialMasDataBase.getDatabase(this).estudianteDao()
        lifecycleScope.launch {
            val listaEstudiantes = dao.mostrarEstudiantesPorDocente(docenteId)
            adapter.actualizarLista(listaEstudiantes)
        }

    }

}