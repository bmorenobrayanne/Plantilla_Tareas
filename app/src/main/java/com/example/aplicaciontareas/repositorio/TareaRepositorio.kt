package com.example.aplicaciontareas.repositorio

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.aplicaciontareas.basedatos.AppBaseDatos
import com.example.aplicaciontareas.modelo.Tarea
import com.example.aplicaciontareas.red.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class TareaRepositorio(
    private val context: Context,
    private val baseDatos: AppBaseDatos
) {
    private val tareaDao = baseDatos.tareaDao()
    private val apiService = RetrofitClient.apiService

    fun obtenerTareasLocales(): Flow<List<Tarea>> = tareaDao.obtenerTareas()

    /**
     * 🔹 Obtiene tareas desde la API o desde Room según la conexión.
     */
    suspend fun obtenerTareas(): List<Tarea> = withContext(Dispatchers.IO) {
        try {
            if (hayConexionInternet(context)) {
                println("🌐 Conexión detectada: intentando descargar tareas...")
                val tareasRemotas = descargarTareasDesdeAPI()

                // 🔹 Insertamos solo si no existen (ya se hace en descargarTareasDesdeAPI)
                return@withContext tareasRemotas
            } else {
                println("📴 Sin conexión: cargando tareas locales...")
                return@withContext emptyList()
            }
        } catch (e: IOException) {
            println("⚠️ Error de red (sin conexión o tiempo de espera): ${e.message}")
            return@withContext emptyList()
        } catch (e: HttpException) {
            println("❌ Error HTTP ${e.code()}: ${e.message}")
            return@withContext emptyList()
        } catch (e: Exception) {
            println("⚠️ Error inesperado: ${e.localizedMessage}")
            return@withContext emptyList()
        }
    }


    /**
     * 🔹 Descarga y convierte las tareas desde Google Sheets
     */
    suspend fun descargarTareasDesdeAPI(): List<Tarea> {
        val listaConvertida = mutableListOf<Tarea>()
        try {
            val response = apiService.obtenerTareasDesdeGoogleSheet(
                "https://docs.google.com/spreadsheets/d/1L_m63Bq86r1OKYyowuTcTW_vda3Lvgv4A45ja8cDjaU/gviz/tq?tqx=out:json"
            )
            if (!response.isSuccessful) return emptyList()

            val rawJson = response.body() ?: return emptyList()
            val jsonLimpio = rawJson.substringAfter("setResponse(").substringBeforeLast(");")
            val jsonObject = org.json.JSONObject(jsonLimpio)
            val rows = jsonObject.getJSONObject("table").getJSONArray("rows")

            for (i in 0 until rows.length()) {
                val c = rows.getJSONObject(i).getJSONArray("c")
                val titulo = c.optJSONObject(0)?.optString("v") ?: "Sin título"
                val descripcion = c.optJSONObject(1)?.optString("v") ?: "Sin descripción"
                val fecha = c.optJSONObject(2)?.optString("v") ?: "Sin fecha"

                val tarea = Tarea(
                    apiId = "sheet_$i",
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = fecha
                )

                // 🔹 Solo insertamos si no existe
                val existe = tareaDao.obtenerTareaPorApiId(tarea.apiId!!)
                if (existe == null) {
                    tareaDao.insertarTarea(tarea)
                }

                listaConvertida.add(tarea)
            }

        } catch (e: Exception) {
            println("⚠️ Error al procesar JSON: ${e.localizedMessage}")
        }

        return listaConvertida
    }






    /**
     * 🔹 Verifica si hay conexión a Internet disponible
     */
    private fun hayConexionInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
