package com.yazmin.especialmas3.evaluacionesApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EvaluarAlumnoHistorial : AppCompatActivity() {
    private var idEstudiante: Int = 0
    private var docenteId: Int=0
    private lateinit var bbdd: EspecialMasDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_evaluar_alumno_historial)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        docenteId =intent.getIntExtra("docenteId",-1)
        bbdd = EspecialMasDataBase.getDatabase(this)

        //Recibir datos del intent Estudiante...
        idEstudiante = intent.getIntExtra("idEstudiante", -1)
        Log.d("LanzarHistorial", "Enviando idEstudiante=$idEstudiante")

        val nombre = intent.getStringExtra("nombre") ?: ""
        val diagnostico = intent.getStringExtra("diagnóstico") ?: ""
        val fechaNacimiento = intent.getStringExtra("fecha de nacimiento") ?: ""

        //Asignar valores a los recursos
        findViewById<TextView>(R.id.nombreEvaluacion1).text = nombre
        findViewById<TextView>(R.id.txtDxEvalAlum).text = diagnostico
        findViewById<TextView>(R.id.fechNac1).text = fechaNacimiento

        //boton y chips referenciados
        val btnEvaluar = findViewById<Button>(R.id.btnEvaluar)
        val inicialChip = findViewById<Chip>(R.id.inicialChip)
        val seguimientoChip = findViewById<Chip>(R.id.seguimientoChip)
        val finalChip = findViewById<Chip>(R.id.finalChip)

        //comportamiento de boton. Nueva Evaluacion
        btnEvaluar.setOnClickListener {
            val tipoEvaluacion = obtenerTipoEvaluacion(inicialChip, seguimientoChip, finalChip)
            if (tipoEvaluacion != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val existe = bbdd.evaluacionDao().existeEvaluacion(idEstudiante, tipoEvaluacion)
                    runOnUiThread {
                        val modo = if (existe > 0) "continuar" else "nuevo"
                        abrirEvaluacion(tipoEvaluacion, modo)
                    }
                }
            } else {
                Toast.makeText(this, "Seleccione un tipo de evaluación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Obtiene el tipo de evaluacion seleccionado por el docente
    private fun obtenerTipoEvaluacion(
        inicialChip: Chip,
        seguimientoChip: Chip,
        finalChip: Chip
    ): String? {
        return when {
            inicialChip.isChecked -> "Inicial"
            seguimientoChip.isChecked -> "Seguimiento"
            finalChip.isChecked -> "Final"
            else -> null
        }
    }

    private fun abrirEvaluacion(tipoEvaluacion: String, modo: String) {
        val esContinuar = modo == "continuar"
        val intent = Intent(this, EvaluarPrueba::class.java).apply {
            putExtra("idEstudiante", idEstudiante)
            putExtra("tipoEvaluacion", tipoEvaluacion)
            putExtra("esContinuar", esContinuar)
            putExtra("docenteId", docenteId)
        }
        startActivity(intent)
    }
}