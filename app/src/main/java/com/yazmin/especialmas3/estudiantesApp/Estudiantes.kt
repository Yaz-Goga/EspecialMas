package com.yazmin.especialmas3.estudiantesApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import com.yazmin.especialmas3.InicioApp.Menu
import com.yazmin.especialmas3.R
import androidx.activity.OnBackPressedCallback

class Estudiantes : AppCompatActivity() {
    private var docenteId: Int = 0 //para vincularlo con docenteid a los estudiantes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estudiantes)

        //Obtiene el docenteId del intent
        docenteId = intent.getIntExtra("docenteId", 0)
        if (docenteId == 0) {
            startActivity(Intent(this, Menu::class.java))
            finish()
            return
        }
        //Botones y funciones de los mismos en la pantalla de Estudiantes (agregar-gestionar)
        val btnAgregarEstudiante: Button=findViewById(R.id.btnAgregarEstudiante)
        val btnGestionarEstudiantes: Button = findViewById(R.id.btnGestionarEstudiante)

        btnAgregarEstudiante.setOnClickListener {
            val intent = Intent(this, EstudiantesAgregar::class.java)
            intent.putExtra("docenteId", docenteId)
            startActivity(intent)
        }

        btnGestionarEstudiantes.setOnClickListener {
            val intent = Intent(this, EstudiantesGestionarGeneral::class.java)
            intent.putExtra("docenteId", docenteId)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(this@Estudiantes, Menu::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                    finish()
                }
            })
    }
}