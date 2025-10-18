package com.example.aplicaciontareas.basedatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aplicaciontareas.dao.TareaDao
import com.example.aplicaciontareas.modelo.Tarea

// Versión inicial de la base de datos
@Database(entities = [Tarea::class], version = 1, exportSchema = false)
abstract class AppBaseDatos : RoomDatabase() {

    // Referencia al DAO
    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCIA: AppBaseDatos? = null

        // Método para obtener una instancia única de la base de datos
        fun obtenerBaseDatos(context: Context): AppBaseDatos {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppBaseDatos::class.java,
                    "tareas_db" // Nombre del archivo de base de datos local
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}
