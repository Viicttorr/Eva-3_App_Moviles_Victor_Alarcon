package com.example.eva_2_app_moviles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import android.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
///////////////////////////////
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declara variable auth con Funciones Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Dirige a pantalla Recuperar cuenta
        findViewById<ImageButton>(R.id.btnForgot).setOnClickListener {
            val intent = Intent(this, RecuperaCuenta::class.java)
            startActivity(intent)
        }
        //Dirige a pantalla de Registro
        findViewById<ImageButton>(R.id.btnSingup).setOnClickListener {
            val intent = Intent(this, RegistraCuenta::class.java)
            startActivity(intent)
        }
        //Funciones de sesion Firebase
        auth = FirebaseAuth.getInstance()

        //Datos de la vistas
        val emailField = findViewById<EditText>(R.id.txtCorreo)
        val passField = findViewById<EditText>(R.id.txtPass)
        val loginButton = findViewById<ImageButton>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Intento de login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Bienvenido ${user?.email}", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, Noticias::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        //Boton Bluetooth
        var btoothActivo = false
        val toggleBtn = findViewById<ImageButton>(R.id.btn_Bth)

        toggleBtn.setOnClickListener {
            btoothActivo = !btoothActivo

            if (btoothActivo) {
                toggleBtn.setImageResource(R.drawable.btoo_on)
                Toast.makeText(this, "Bluetooth activado", Toast.LENGTH_SHORT).show()
            } else {
                toggleBtn.setImageResource(R.drawable.btoo_off)
                Toast.makeText(this, "Bluetooth desactivado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}