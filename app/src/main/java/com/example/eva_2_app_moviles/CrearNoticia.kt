package com.example.eva_2_app_moviles

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//////
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue


class CrearNoticia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.crear_noticia)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.crearNoticia)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Boton Regresar
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Noticias::class.java)
            startActivity(intent)
        }

        //Formulario Noticia
        val tituloInput = findViewById<EditText>(R.id.newTextitulo)
        val bajadaInput = findViewById<EditText>(R.id.newTexbajada)
        val imagenInput = findViewById<EditText>(R.id.newImgUrl)
        val cuerpoInput = findViewById<EditText>(R.id.newCuerpo)

        fun crearNoticia(titulo: String, bajada: String, imagen: String, cuerpo: String) {

            if (titulo.isBlank() || bajada.isBlank() || imagen.isBlank() || cuerpo.isBlank()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            val db = FirebaseFirestore.getInstance()
            val usuarioActual = FirebaseAuth.getInstance().currentUser

            if (usuarioActual != null) {
                val autorRef = db.collection("Usuarios").document(usuarioActual.uid)

                // datos de la noticia
                val noticia = hashMapOf(
                    "titulo" to titulo,
                    "bajada" to bajada,
                    "imagen" to imagen,
                    "cuerpo" to cuerpo,
                    "fecha_creacion" to FieldValue.serverTimestamp(),
                    "autor" to autorRef
                )

                // guarda en la colección Noticias
                db.collection("Noticias")
                    .add(noticia)
                    .addOnSuccessListener { docRef ->
                        Toast.makeText(this, "Noticia creada con ID: ${docRef.id}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Noticias::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al crear noticia: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_LONG).show()
            }
        }

        //Boton Send llama a funcion crearNoticia
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        btnSend.setOnClickListener {
            val titulo = tituloInput.text.toString()
            val bajada = bajadaInput.text.toString()
            val imagen = imagenInput.text.toString()
            val cuerpo = cuerpoInput.text.toString()

            crearNoticia(titulo, bajada, imagen, cuerpo)
        }
    }
}