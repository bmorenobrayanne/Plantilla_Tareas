package com.example.aplicaciontareas.interfaz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicaciontareas.R
import com.example.aplicaciontareas.databinding.ActivityMainBinding
import com.example.aplicaciontareas.basedatos.AppBaseDatos
import com.example.aplicaciontareas.modelo.Tarea
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ---- 🔹 LÓGICA AGREGADA SIN MODIFICAR LA PLANTILLA ----

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = AppBaseDatos.obtenerBaseDatos(this).tareaDao()
        val adaptador = AdaptadorTareas(emptyList<Tarea>())

        binding.rvTareas.layoutManager = LinearLayoutManager(this)
        binding.rvTareas.adapter = adaptador

        // Insertar nueva tarea
        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                lifecycleScope.launch {
                    dao.insertarTarea(Tarea(titulo = titulo, descripcion = descripcion, fecha = fecha))
                    binding.etTitulo.text?.clear()
                    binding.etDescripcion.text?.clear()
                }
            }
        }

        // Observar y mostrar tareas en el RecyclerView
        lifecycleScope.launch {
            dao.obtenerTareas().collect { lista ->
                adaptador.actualizarDatos(lista)
            }
        }

        // ---- 🔹 FIN DE LA LÓGICA AGREGADA ----
    }
}
