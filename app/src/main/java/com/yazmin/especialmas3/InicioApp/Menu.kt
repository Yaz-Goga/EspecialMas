package com.yazmin.especialmas3.InicioApp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import com.yazmin.especialmas3.estudiantesApp.Estudiantes
import com.yazmin.especialmas3.evaluacionesApp.EvaluarListAlumnos
import com.yazmin.especialmas3.ResultadosInformes.Informes
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.ResultadosInformes.ResultadosGeneral

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        /*Pantalla del menu de la app, con 4 secciones definidas
        Estudiantes, Evaluaciones, Resultados e Informes
         */

        val sharedPref = getSharedPreferences("datos_sesion", MODE_PRIVATE)
        val docenteId = sharedPref.getInt("docenteId", -1)

        if (docenteId == -1) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        val btnEstudiantesMenu: ImageButton = findViewById(R.id.BtnEstudiantesMenu)
        val btnEvaluarListAlumnos: ImageButton = findViewById(R.id.btnEvaluacionMenu)
        val btnResultadosMenu: ImageButton = findViewById(R.id.btnResultadosMenu)
        val btnInformesMenu: ImageButton = findViewById(R.id.btnInformesMenu)

        //Redireccionamiento a las secciones elegidas con envio del id docente
        btnEstudiantesMenu.setOnClickListener {
            val intent = Intent(this, Estudiantes::class.java).apply {
                putExtra("docenteId", docenteId)
            }
            startActivity(intent)}

        btnEvaluarListAlumnos.setOnClickListener {
            val intent = Intent(this, EvaluarListAlumnos::class.java).apply {
                putExtra("docenteId", docenteId)
            }
            startActivity(intent)
        }

        btnResultadosMenu.setOnClickListener {
            val intent = Intent(this, ResultadosGeneral::class.java).apply {
                putExtra("docenteId", docenteId)
            }
            startActivity(intent)
        }
        btnInformesMenu.setOnClickListener {
            val intentInformes = Intent(this, Informes::class.java).apply {
                putExtra("docenteId", docenteId)
            }
            startActivity(intentInformes)
        }
        }

}