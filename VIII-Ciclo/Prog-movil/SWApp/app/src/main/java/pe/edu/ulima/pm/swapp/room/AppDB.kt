package pe.edu.ulima.pm.swapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.swapp.room.dao.PlanetaRoomDAO
import pe.edu.ulima.pm.swapp.room.models.PlanetaRoom

@Database(entities = arrayOf(PlanetaRoom::class), version = 1) // Si tuviésemos más entidades, simplemente los agregamos luego de PlanetaRoom::class
abstract class AppDB : RoomDatabase() {
    abstract fun getPlanetaRoomDAO() : PlanetaRoomDAO

    companion object {
        private var mInstance : AppDB? = null

        fun getInstance(context : Context) : AppDB {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "AppDB"
                ).allowMainThreadQueries().build()
            }
            return mInstance!!
        }
    }
}