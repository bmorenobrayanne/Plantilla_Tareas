package com.example.aplicaciontareas.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

// Indicamos que esta clase representa una tabla llamada "tareas"
@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                // Identificador único
    val titulo: String,             // Título de la tarea
    val descripcion: String,        // Detalle o descripción
    val fecha: String               // Fecha de creación o vencimiento (en formato texto)
)