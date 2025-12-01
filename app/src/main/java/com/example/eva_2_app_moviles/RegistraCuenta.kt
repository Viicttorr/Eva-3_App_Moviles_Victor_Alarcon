package com.example.eva_2_app_moviles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import android.view.View
import android.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
///////////////////////////////
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FieldValue

class RegistraCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registra)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registra)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Boton Regresa a MainActivity
        findViewById<ImageButton>(R.id.btnBackRg).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        //Formulario registro
        val nombreInput = findViewById<EditText>(R.id.txtNombre)
        val emailInput = findViewById<EditText>(R.id.txtCorreo)
        val passInput = findViewById<EditText>(R.id.txtPass)
        val confirmPassInput = findViewById<EditText>(R.id.txtConfrPass)

        //Valida formulario no vacio
        fun registrarUsuario(nombre: String, email: String, pass: String, confirmPass: String) {
            if (nombre.isBlank() || email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return
            }
            if (pass != confirmPass) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }

            //Crea la cuenta
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    val userData = mapOf(
                        "nombre" to nombre,
                        "email" to email,
                        "fecha_registro" to FieldValue.serverTimestamp()
                    )
                    db.collection("Usuarios").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error en Auth: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        //Boton Singup llama a funcion registrarUsuario
        val btnSignup = findViewById<ImageButton>(R.id.btnSingup)
        btnSignup.setOnClickListener {
            val nombre = nombreInput.text.toString()
            val email = emailInput.text.toString()
            val pass = passInput.text.toString()
            val confirmPass = confirmPassInput.text.toString()

            registrarUsuario(nombre, email, pass, confirmPass)
        }
    }
}

