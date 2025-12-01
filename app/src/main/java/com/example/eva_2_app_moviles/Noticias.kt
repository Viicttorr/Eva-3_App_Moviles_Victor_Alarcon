package com.example.eva_2_app_moviles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//////
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.util.Log
import android.widget.ImageButton
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.Timestamp


class Noticias : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoticiasAdapter
    private val listaNoticias = mutableListOf<Noticia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_noticias)

        //Para que la pantalla Funcione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.noticias)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Dirige a pantalla Crear Noticias
        findViewById<ImageButton>(R.id.btnCrear).setOnClickListener {
            val intent = Intent(this, CrearNoticia::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerNoticias)          //Toma los elemtos del layout
        recyclerView.layoutManager = LinearLayoutManager(this) //Posiciona elementos en vertical

        adapter = NoticiasAdapter(listaNoticias)                        //clase NoticiasAdapter enlaza listaNoticia con RecyclerView
        recyclerView.adapter = adapter                                  //Hace funcionar la vista utilizando el adapter

        cargarNoticias()

        // Para cerrar sesión
        auth = FirebaseAuth.getInstance()
        val logoutButton = findViewById<ImageButton>(R.id.btnLogout)
        logoutButton.setOnClickListener {
            auth.signOut()

            // Redirige al login
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
        }
    }
    private fun cargarNoticias() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Noticias")
            .get()
            .addOnSuccessListener { result ->
                listaNoticias.clear()
                for (document in result) {
                    val titulo = document.getString("titulo") ?: ""
                    val bajada = document.getString("bajada") ?: ""
                    val imagen = document.getString("imagen") ?: ""

                    val fechaTs = document.getTimestamp("fecha_creacion")
                    val fechaStr = fechaTs?.toDate()?.let {
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
                    } ?: ""

                    val cuerpo = document.getString("cuerpo") ?: ""

                    val autorRef = document.getDocumentReference("autor")
                    val autorId = autorRef?.id ?: ""

                    val noticia = Noticia(titulo, bajada, imagen, fechaStr, autorId, cuerpo)
                    listaNoticias.add(noticia)

                    //Busca el nombre del autor segun notica creada
                    autorRef?.get()?.addOnSuccessListener { autorDoc ->                  //solicita el documento del autor y si es exito
                        val autorNombre = autorDoc.getString("nombre") ?: autorId //intenta obtener nombre si no usa el id
                        val index = listaNoticias.indexOf(noticia)                       //Busca la noticia por el indice
                        if (index != -1) {                                               //Verifica si la noticia existe
                            listaNoticias[index] = noticia.copy(autor = autorNombre)     //Hace una copia de la noticia  con nombre del autor
                            adapter.notifyItemChanged(index)                   //Actualiza la noticia existente con la encontrada
                        }
                    }
                }
                adapter.notifyDataSetChanged() //Recarga si lista noticia es invalida
            }
    }
}

data class Noticia( //Almacena en string y transfiere datos
    val titulo: String,
    val bajada: String,
    val imagen: String,
    val fecha_creacion: String,
    val autor: String,
    val cuerpo: String,
)

class NoticiasAdapter(private val listaNoticias: List<Noticia>) : //Inicia listaNoticias
    RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>() {   //Trabajara con el contenedor NoticiaViewHolder

    class NoticiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Toma la estructura general (contenedor) en activity noticia
        val img: ImageView = itemView.findViewById(R.id.imgNoticia)
        val titulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val bajada: TextView = itemView.findViewById(R.id.txtBajada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder { //Toma la estructura indiviudual en itema_noticia y envia a NoticiaViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return NoticiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) { //Une los datos con la estructura de NoticiaViewHolder
        val noticia = listaNoticias[position]
        holder.titulo.text = noticia.titulo
        holder.bajada.text = noticia.bajada


        // Carga imagen desde URL con Glide
        Glide.with(holder.itemView.context)
            .load(noticia.imagen)
            .placeholder(R.drawable.cuadro_imagen)
            .error(R.drawable.cuadro_imagen)
            .into(holder.img)

        //AL tocar en la noticia muestra el detalle
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleNoticia::class.java)
            intent.putExtra("titulo", noticia.titulo)
            intent.putExtra("bajada", noticia.bajada)
            intent.putExtra("fecha_creacion", noticia.fecha_creacion)
            intent.putExtra("autor", noticia.autor)
            intent.putExtra("imagen", noticia.imagen)
            intent.putExtra("cuerpo", noticia.cuerpo)
            context.startActivity(intent)
        }
    }
    //Le dice a RecyclerView cuantos elementos hay en la lista para poder trabajar
    override fun getItemCount(): Int = listaNoticias.size
}