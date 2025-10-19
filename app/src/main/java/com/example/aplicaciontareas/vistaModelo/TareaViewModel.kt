package com.example.aplicaciontareas.vistaModelo

import android.app.Application
import androidx.lifecycle.*
import com.example.aplicaciontareas.basedatos.AppBaseDatos
import com.example.aplicaciontareas.modelo.Tarea
import com.example.aplicaciontareas.repositorio.TareaRepositorio
import kotlinx.coroutines.launch

class TareaViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: TareaRepositorio
    val listaTareas: LiveData<List<Tarea>>

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    init {
        val baseDatos = AppBaseDatos.obtenerBaseDatos(application)
        repositorio = TareaRepositorio(application, baseDatos)
        listaTareas = repositorio.obtenerTareasLocales().asLiveData()
    }

    /**
     * 🔹 Inserta una tarea nueva en la base local
     */
    fun insertarTarea(tarea: Tarea) = viewModelScope.launch {
        try {
            val dao = AppBaseDatos.obtenerBaseDatos(getApplication()).tareaDao()
            dao.insertarTarea(tarea)
            _mensaje.postValue("✅ Tarea guardada correctamente")
        } catch (e: Exception) {
            _mensaje.postValue("⚠️ Error al guardar tarea: ${e.message}")
        }
    }

    /**
     * 🔹 Sincroniza tareas con la API y las agrega a Room sin borrar las locales
     */
    fun sincronizarTareas() = viewModelScope.launch {
        try {
            val tareasDescargadas = repositorio.obtenerTareas()
            if (tareasDescargadas.isNotEmpty()) {
                _mensaje.postValue("✅ ${tareasDescargadas.size} tareas nuevas agregadas")
            } else {
                _mensaje.postValue("ℹ️ No hay nuevas tareas para sincronizar")
            }
        } catch (e: Exception) {
            _mensaje.postValue("⚠️ Error al sincronizar: ${e.message}")
        }
    }

}
