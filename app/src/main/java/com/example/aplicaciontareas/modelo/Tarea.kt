package com.example.aplicaciontareas.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 📘 Entidad que representa una tarea dentro de la base de datos local (Room).
 * Cada objeto de esta clase corresponde a una fila en la tabla "tareas".
 */
@Entity(tableName = "tareas")
data class Tarea(

    // Identificador único local autogenerado (clave primaria)
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    // ID remoto opcional (por si se sincroniza con una API o Firebase)
    val apiId: String? = null,

    // Campos principales
    val titulo: String,             // Título de la tarea
    val descripcion: String,        // Descripción o detalle
    val fecha: String,              // Fecha de creación o vencimiento (en formato texto)
    val completada: Boolean = false // Estado de la tarea
)
