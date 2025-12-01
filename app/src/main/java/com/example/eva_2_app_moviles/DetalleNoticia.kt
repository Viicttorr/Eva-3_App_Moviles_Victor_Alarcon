package com.example.eva_2_app_moviles

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
//////
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetalleNoticia : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detalle_noticia)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalleNoticia)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Boton Regresar
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Noticias::class.java)
            startActivity(intent)
        }

        // Referencia a las vistas
        val Titulo: TextView = findViewById(R.id.txtTituloDetalle)
        val Bajada: TextView = findViewById(R.id.txtBajadaDetalle)
        val FechaCreacion: TextView = findViewById(R.id.txtFechaDetalle)
        val Autor: TextView = findViewById(R.id.txtAutorDetalle)
        val imgDetalle: ImageView = findViewById(R.id.imgDetalle)
        val Cuerpo: TextView = findViewById(R.id.txtCuerpoDetalle)

        // Recibir datos desde el Intent
        val titulo = intent.getStringExtra("titulo") ?: ""
        val bajada = intent.getStringExtra("bajada") ?: ""
        val fecha_creacion = intent.getStringExtra("fecha_creacion") ?: ""
        val autor = intent.getStringExtra("autor") ?: ""
        val imagenUrl = intent.getStringExtra("imagen") ?: ""
        val cuerpo = intent.getStringExtra("cuerpo") ?: ""

        // Asigna valores a las vistas
        Titulo.text = titulo
        Bajada.text = bajada
        FechaCreacion.text = fecha_creacion
        Autor.text = autor
        Cuerpo.text = cuerpo

        // Carga imagen con Glide
        Glide.with(this)
            .load(imagenUrl)
            .into(imgDetalle)
    }
}
