package com.example.aplicaciontareas.red

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * 🌐 Cliente Retrofit configurado para manejar tanto JSON como texto plano.
 * Esto permite leer correctamente el formato devuelto por Google Sheets (setResponse(...)).
 */
object RetrofitClient {

    // URL base requerida por Retrofit (aunque usemos la URL completa en el @GET)
    private const val BASE_URL = "https://docs.google.com/"

    private val client = OkHttpClient.Builder().build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        // 👇 Primero Scalars para manejar texto plano (Sheets devuelve "setResponse(...)")
        .addConverterFactory(ScalarsConverterFactory.create())
        // 👇 Luego Gson para casos en que sí haya JSON limpio
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
