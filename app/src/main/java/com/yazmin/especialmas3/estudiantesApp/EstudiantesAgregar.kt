package com.yazmin.especialmas3.estudiantesApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.model.Estudiante
import kotlinx.coroutines.launch


class EstudiantesAgregar : AppCompatActivity() {
    private var idEstudiante: Int? = null //Identificar si es edición o creacion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estudiantes_agregar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Referencias para capturar datos, asignacion de los EditText
        val escuela = findViewById<EditText>(R.id.nombreEscuela)
        val nombre = findViewById<EditText>(R.id.nombreEstudiante)
        val apellidos = findViewById<EditText>(R.id.ApellidosEstudiante)
        val grado = findViewById<EditText>(R.id.gradoEstudiante)
        val grupo = findViewById<EditText>(R.id.grupoEstudiante)
        val fechaNacimiento = findViewById<EditText>(R.id.fechaNacEstudiante)
        val diagnostico = findViewById<EditText>(R.id.dxEstudiante)
        val btnBorrarEstudiante: Button = findViewById(R.id.btnBorrarEstudiante)
        val btnGuardarEstudiante: Button = findViewById(R.id.btnGuardarEstudiante)

        val docenteId =intent.getIntExtra("docenteId",0) //Importante para la asociacion con docente
        //BBDD y Dao
        val db = EspecialMasDataBase.getDatabase(this)
        val dao = db.estudianteDao()

        idEstudiante = intent.getIntExtra("idEstudiante", -1).takeIf { it != -1 }
        //Si es edicion, carga datos del estudiante
        if (idEstudiante != null) {
            lifecycleScope.launch {
                val estudiante = dao.obtenerEstudiantePorId(idEstudiante!!)
                estudiante?.let {
                    escuela.setText(it.escuela)
                    nombre.setText(it.nombre)
                    apellidos.setText(it.apellidos)
                    grado.setText(it.grado.toString())
                    grupo.setText(it.grupo)
                    fechaNacimiento.setText(it.fechaNacimiento)
                    diagnostico.setText(it.diagnostico)
                    btnBorrarEstudiante.isEnabled = true //Se habilita el boton para eliminar datos cargados
                }
            }
        } else {
            btnBorrarEstudiante.isEnabled = false //Para no poder borrar estudiante si estas creandolo
        }


        //Listener de Guardar - modificar estudiante
        btnGuardarEstudiante.setOnClickListener {
            val escuelaCampo = escuela.text.toString().trim()
            val nombreCampo = nombre.text.toString().trim()
            val apellidosCampo = apellidos.text.toString().trim()
            val gradoCampo = grado.text.toString().trim()
            val grupoCampo = grupo.text.toString().trim()
            val fechNacCampo = fechaNacimiento.text.toString().trim()
            val dxCampo = diagnostico.text.toString().trim()

            //Verifica que no haya campos vacios
            if (escuelaCampo.isEmpty() || nombreCampo.isEmpty() || apellidosCampo.isEmpty() || gradoCampo.isEmpty()
                || grupoCampo.isEmpty() || fechNacCampo.isEmpty() || dxCampo.isEmpty()
            ) {
                Toast.makeText(this, "Es necesario registrar todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                lifecycleScope.launch {
                    val estudiante = Estudiante(
                        id = idEstudiante ?: 0,
                        escuela = escuelaCampo,
                        nombre = nombreCampo,
                        apellidos = apellidosCampo,
                        grado = gradoCampo.toInt(),
                        grupo = grupoCampo,
                        fechaNacimiento = fechNacCampo,
                        diagnostico = dxCampo,
                        docenteId = docenteId
                    )
                    //Actualizar o insertar
                    if (idEstudiante != null) {
                        dao.actualizarEstudiante(estudiante)
                        Toast.makeText(
                            this@EstudiantesAgregar,
                            "Datos actualizados",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        dao.insertarEstudiante(estudiante)
                        Toast.makeText(
                            this@EstudiantesAgregar,
                            "Estudiante guardado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    startActivity(Intent(this@EstudiantesAgregar, Estudiantes::class.java))
                    finish()
                }

            }
        }

    //Eliminar datos estudiante, con confirmacion para prevenir accidentes de borrado
    btnBorrarEstudiante.setOnClickListener {
        if (idEstudiante != null) {
            AlertDialog.Builder(this).apply {
                setTitle("Confirmar borrar datos")
                setMessage("¿Desea borrar los datos de este estudiante de todos los registros?")
                setPositiveButton("Si, eliminar") { _, _ ->
                    lifecycleScope.launch {
                        val estudiante = dao.obtenerEstudiantePorId(idEstudiante!!)
                        estudiante?.let {
                            dao.eliminarEstudiante(it)
                            Toast.makeText(
                                this@EstudiantesAgregar,
                                "Estudiante eliminado",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@EstudiantesAgregar,
                                    Estudiantes::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
                setNegativeButton("Cancelar", null)
                show()
            }
        }
    }

    //Comportamiento boton atras en esta parte
    onBackPressedDispatcher.addCallback(this,
    object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            AlertDialog.Builder(this@EstudiantesAgregar).apply {
                setTitle("¿Salir sin guardar?")
                setMessage("¿Desea salir sin guardar?")
                setPositiveButton("Si, salir") { _, _ ->
                    val intent = Intent(this@EstudiantesAgregar, Estudiantes::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                    finish()
                }
                setNegativeButton("Cancelar", null)
                show()
            }
        }
    }
)
    }
}

