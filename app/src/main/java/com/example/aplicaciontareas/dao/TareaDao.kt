package com.example.aplicaciontareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aplicaciontareas.modelo.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    // 🔹 Insertar una nueva tarea
    // onConflict = IGNORE evita errores si ya existe una con el mismo apiId
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTarea(tarea: Tarea)

    // 🔹 Actualizar una tarea existente (por ejemplo, al sincronizar con API)
    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    // 🔹 Eliminar una tarea específica
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)

    // 🔹 Consultar todas las tareas locales (ordenadas por ID local descendente)
    @Query("SELECT * FROM tareas ORDER BY localId DESC")
    fun obtenerTareas(): Flow<List<Tarea>>

    // 🔹 Buscar una tarea específica por su ID remoto (apiId)
    @Query("SELECT * FROM tareas WHERE apiId = :apiId LIMIT 1")
    suspend fun obtenerTareaPorApiId(apiId: String): Tarea?

    // 🔹 Eliminar todas las tareas locales (opcional, útil para limpieza)
    @Query("DELETE FROM tareas")
    suspend fun eliminarTodasLasTareas()
}
