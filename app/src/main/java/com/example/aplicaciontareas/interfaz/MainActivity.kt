package com.example.aplicaciontareas.interfaz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicaciontareas.R
import com.example.aplicaciontareas.databinding.ActivityMainBinding
import com.example.aplicaciontareas.modelo.Tarea
import com.example.aplicaciontareas.vistaModelo.TareaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TareaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ---- 🔹 (16) CONFIGURAR RECYCLER VIEW ----
        val adaptador = AdaptadorTareas(emptyList())
        binding.rvTareas.layoutManager = LinearLayoutManager(this)
        binding.rvTareas.adapter = adaptador

        // ---- 🔹 OBSERVAR CAMBIOS EN LA BASE DE DATOS ----
        viewModel.listaTareas.observe(this) { lista ->
            adaptador.actualizarDatos(lista)
        }

        // ---- 🔹 OBSERVAR MENSAJES DEL VIEWMODEL ----
        viewModel.mensaje.observe(this) { mensaje ->
            if (!mensaje.isNullOrEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        }

        // ---- 🔹 (18) GUARDAR UNA NUEVA TAREA LOCAL ----
        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                val nuevaTarea = Tarea(
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = fecha,
                    apiId = null
                )

                viewModel.insertarTarea(nuevaTarea)
                binding.etTitulo.text?.clear()
                binding.etDescripcion.text?.clear()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // ---- 🔹 (17) BOTÓN DE SINCRONIZACIÓN ----
        binding.btnSincronizar.setOnClickListener {
            lifecycleScope.launch {
                viewModel.sincronizarTareas()
                Toast.makeText(this@MainActivity, "Sincronización completada", Toast.LENGTH_SHORT).show()
            }
        }

        // ---- 🔹 SINCRONIZACIÓN AUTOMÁTICA (opcional) ----
        lifecycleScope.launch {
            viewModel.sincronizarTareas()
        }
    }
}
