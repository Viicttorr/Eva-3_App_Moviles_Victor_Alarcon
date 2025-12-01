package com.example.eva_2_app_moviles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
//////
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.util.Log


class RecuperaCuenta : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declara variable auth con Funciones Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recupera)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recupera)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Btn regresa a pantalla MainActivity
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Funciones de sesion Firebase
        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.txtCorreoRc)
        val recuperarButton = findViewById<ImageButton>(R.id.btnSend)

        recuperarButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setMessage("Correo de recuperacion enviado a tu bandeja")
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setMessage("No se pudo procesar la solicitud: ${task.exception?.message}")
                            .show()
                    }
                }
        }
    }

}
