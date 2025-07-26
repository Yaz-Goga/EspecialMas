package com.yazmin.especialmas3.ResultadosInformes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.model.Estudiante
import kotlinx.coroutines.launch

class ResultadosGeneral : AppCompatActivity() {

    private lateinit var spinnerTipoEvaluacion: Spinner
    private lateinit var spinnerEstudiante: Spinner
    private lateinit var btnVerResultados: Button

    private var tipoSeleccionado = "Inicial"
    private var listAlumnosFiltrados = listOf<Estudiante>()
    private var mapNomAlumId = mutableMapOf<String, Int>()

    private var docenteId = -1 //Para filtrar datos con mismo docente id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados_general)

        docenteId = intent.getIntExtra("docenteId", -1)
        if (docenteId == -1) {
            Toast.makeText(this, "Error: Docente no identificado", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        //Iniciar spinners para el tipo de evaluacion y los estudiantes con esa evaluacion
        spinnerTipoEvaluacion = findViewById(R.id.spinnerTipoEvaluacion)
        spinnerEstudiante = findViewById(R.id.spinnerEstudiante)
        btnVerResultados = findViewById(R.id.btnVerResultados)

        configurarSpinner()
        configurarBoton()
    }
    //Spinner de Tipo de Evaluacion
    private fun configurarSpinner() {
        val tiposEvaluacion = listOf("Inicial", "Seguimiento", "Final")
        val adaptadorTipos =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposEvaluacion)
        adaptadorTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoEvaluacion.adapter = adaptadorTipos

        spinnerTipoEvaluacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tipoSeleccionado = tiposEvaluacion[position]
                cargarEstudiantesFiltrados()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    //Obtiene a los estudiantes con ese tipo de evalacion y que son de ese docente
    private fun cargarEstudiantesFiltrados() {
        lifecycleScope.launch {
            try {
                val db = EspecialMasDataBase.getDatabase(this@ResultadosGeneral)
                val evaluacionDao = db.evaluacionDao()
                val estudianteDao = db.estudianteDao()

                val idsEstudiantes = evaluacionDao.obtenerIdsEstudiantesConEvaluacionesPorDocente(
                    tipoEvaluacion = tipoSeleccionado,
                    docenteId = docenteId
                )

                listAlumnosFiltrados = estudianteDao.obtenerEstudiantesPorIdsYDocente(
                    ids = idsEstudiantes,
                    docenteId = docenteId
                )
                //Crea el nombre completo de los alumnos filtrados para ese spinner
                val nombresEstudiantes = listAlumnosFiltrados.map {
                    "${it.nombre} ${it.apellidos}"
                }

                mapNomAlumId.clear()
                listAlumnosFiltrados.forEach { estudiante ->
                    mapNomAlumId["${estudiante.nombre} ${estudiante.apellidos}"] = estudiante.id
                }

                val adaptadorSpinner = ArrayAdapter(
                    this@ResultadosGeneral,
                    android.R.layout.simple_spinner_item,
                    nombresEstudiantes
                )
                adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstudiante.adapter = adaptadorSpinner

            } catch (e: Exception) {
                Toast.makeText(
                    this@ResultadosGeneral,
                    "Error al cargar estudiantes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Configuracion del boton para ir a graficos
    private fun configurarBoton(){
        btnVerResultados.setOnClickListener {
            val nombreSeleccionado = spinnerEstudiante.selectedItem?.toString()
            val estudianteId = mapNomAlumId[nombreSeleccionado]

            if (estudianteId == null) {
                Toast.makeText(this, "Seleccione un estudiante válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, GraficosResultados::class.java)
            intent.putExtra("ESTUDIANTE_ID", estudianteId)
            intent.putExtra("TIPO_EVALUACION", tipoSeleccionado)
            intent.putExtra("DOCENTE_ID", docenteId)
            startActivity(intent)

        }
    }
}