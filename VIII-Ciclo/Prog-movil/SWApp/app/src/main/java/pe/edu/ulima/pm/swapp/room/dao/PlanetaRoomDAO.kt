package pe.edu.ulima.pm.swapp.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.swapp.room.models.PlanetaRoom

@Dao
interface PlanetaRoomDAO {
    @Query("SELECT * FROM PlanetaRoom") // Debe ser @Query de Room, no de Retrofit
    fun getAll() : List<PlanetaRoom> // Con esta función se llamará a la query de arriba, y lo que retornará será de // tipo List<PlanetaRoom>

    @Query("SELECT * FROM PlanetaRoom WHERE id=:idBuscar")
    fun findById(idBuscar : Int) : PlanetaRoom

    // Las operaciones de insert y delete no necesitan un query

    @Insert
    fun insert(planeta : PlanetaRoom)

    @Delete
    fun delete(planeta : PlanetaRoom)
}