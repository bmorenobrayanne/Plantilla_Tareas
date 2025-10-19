package com.example.aplicaciontareas.dao

import com.google.gson.annotations.SerializedName

/**
 * 📦 Data Transfer Object (DTO)
 * Representa el formato de la tarea cuando se comunica con una API o servicio web.
 * Se utiliza para convertir entre JSON <-> Objeto Kotlin.
 */
data class TareaDTO(

    @SerializedName("id")
    val id: String?, // ID remoto (Firebase o API)

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("fecha")
    val fecha: String,

)