package com.example.aplicaciontareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.aplicaciontareas.modelo.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    // 🔹 Insertar una nueva tarea
    @Insert
    suspend fun insertarTarea(tarea: Tarea)

    // 🔹 Eliminar una tarea específica
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)

    // 🔹 Consultar todas las tareas (ordenadas por fecha o id)
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTareas(): Flow<List<Tarea>>
}