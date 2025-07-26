package com.yazmin.especialmas3.InicioApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.model.Usuario
import kotlinx.coroutines.launch



class RegistroUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Pantalla para registrarse como usuario
        val db= EspecialMasDataBase.getDatabase(this)
        val usuarioDao = db.usuarioDao()
        val txtNombre = findViewById<EditText>(R.id.txtNombreRegistro)
        val txtApellidos = findViewById<EditText>(R.id.txtApellidosRegistro)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaRegistro)
        val txtRepetirContrasena = findViewById<EditText>(R.id.txtRepetirContrasena)
        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val apellidos = txtApellidos.text.toString().trim()
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()
            val repetirContrasena = txtRepetirContrasena.text.toString()
        /*Si algun dato registrado no esta completo, las contraseñas no coinciden
            o no cumple con las caracteristicas lanza un mensaje.
            si se cumplen los campos entonces se crea un nuevo usuario*/
        when {
            nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty() -> {
                Toast.makeText(this, "Por favor, complete todos los campos marcados con *", Toast.LENGTH_SHORT).show()

            }
            contrasena != repetirContrasena -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
            !esContrasenaValida(contrasena)-> {
                Toast.makeText(
                    this, "La contraseña debe tener al menos 8 caracteres, un número y un signo", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val nuevoUsuario = Usuario (
                    nombre = nombre,
                    apellidos = apellidos,
                    correo = correo,
                    contrasena = contrasena
                )
                //Deteccion de si es un usuario registrado previamente o nuevo
                lifecycleScope.launch {
                    val usuarioExistente = usuarioDao.obtenerUsuarioPorCorreo(correo)
                    if (usuarioExistente != null) {
                        runOnUiThread {
                            Toast.makeText(this@RegistroUsuario, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        usuarioDao.insertarUsuario(nuevoUsuario)
                    runOnUiThread {
                        Toast.makeText(this@RegistroUsuario, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegistroUsuario, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    }
                }
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }
}
    /*Funcion para contraseñavalida que permita:
     al menos un numero (?=.*[0-9])
     un caracter especial (?=.*[!@#......])
     minimo 8 caracteres .{8,}
     verificacion de cumplir las reglas ^......$
    */
    private fun esContrasenaValida(contrasena: String): Boolean {
        val regex = Regex ("^(?=.*[0-9])(?=.*[!@#\$%^&*()_+{}\\[\\]:;\"'<>,.?\\\\/~`\\-]).{8,}\$")
        return regex.matches(contrasena)

    }

}