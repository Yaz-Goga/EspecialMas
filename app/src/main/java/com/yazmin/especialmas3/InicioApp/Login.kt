package com.yazmin.especialmas3.InicioApp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        //Pantalla principal de la app, con los datos para entrar o registrarse
        val txtCorreo = findViewById<EditText>(R.id.correoElectronico)
        val txtContrasena = findViewById<EditText>(R.id.contrasena)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnRegistro= findViewById<Button>(R.id.btnRegistro)

        val basedatosdocentes = EspecialMasDataBase.getDatabase(this)
        val usuarioDao = basedatosdocentes.usuarioDao()

        //Comportamiento del boton entrar, si no es correcto enviara un Toast dependiendo el caso
        btnEntrar.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else lifecycleScope.launch {
                val usuario = usuarioDao.obtenerUsuarioPorCorreo(correo)
                if (usuario != null && usuario.contrasena == contrasena) {
                    runOnUiThread {
                        val sharedPref = getSharedPreferences("datos_sesion", MODE_PRIVATE)
                        sharedPref.edit().putInt("docenteId", usuario.id).apply()

                        val intent = Intent(this@Login, Menu::class.java).apply {
                            putExtra("docenteId", usuario.id)
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@Login,
                            "Los datos introducidos no coinciden",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        //Boton registro lleva a la pantalla de RegistroUsuario
        btnRegistro.setOnClickListener {
            val intent =Intent(this, RegistroUsuario::class.java)
            startActivity(intent)

        }

    }
}