package com.example.aplicaciontareas.red

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun obtenerTareasDesdeGoogleSheet(@Url url: String): Response<String>
}
