package pe.edu.ulima.pm.covid19tool.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.covid19tool.room.dao.CasosCovidDAO
import pe.edu.ulima.pm.covid19tool.room.models.CasosCovid

@Database(entities = [CasosCovid::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun getCovidDataDAO() : CasosCovidDAO

    companion object {
        private var instance : AppDB? = null

        fun getInstance(context: Context) : AppDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "AppDB"
                ).allowMainThreadQueries().build()
            }

            return instance!!
        }
    }
}