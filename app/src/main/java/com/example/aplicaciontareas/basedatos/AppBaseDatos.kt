package com.example.aplicaciontareas.basedatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aplicaciontareas.dao.TareaDao
import com.example.aplicaciontareas.modelo.Tarea

/**
 * 🔹 Clase que representa la base de datos local de la aplicación.
 * Contiene la definición de las entidades (tablas) y sus DAOs.
 */
@Database(
    entities = [Tarea::class],
    version = 2, // ⬅️ Actualizamos versión (antes 1)
    exportSchema = true // recomendable para futuras migraciones
)
abstract class AppBaseDatos : RoomDatabase() {

    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCIA: AppBaseDatos? = null

        /**
         * 🔹 Obtiene una instancia única (Singleton) de la base de datos.
         * Usa sincronización para evitar conflictos entre hilos.
         */
        fun obtenerBaseDatos(context: Context): AppBaseDatos {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppBaseDatos::class.java,
                    "tareas_db"
                )
                    // ⚠️ Permite eliminar y recrear la DB si hay cambios en el esquema (solo para desarrollo)

                    // Permite consultas en el hilo principal (no recomendado, solo para pruebas rápidas)
                    // .allowMainThreadQueries()
                    .build()

                INSTANCIA = instancia
                instancia
            }
        }
    }
}
