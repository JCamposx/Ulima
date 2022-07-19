package pe.edu.ulima.pm.covid19tool.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.covid19tool.room.models.CasosCovid

@Dao
interface CasosCovidDAO {
    @Query("SELECT * FROM CasosCovid WHERE fechaResultado=:fecha")
    fun findByFecha(fecha : String) : List<CasosCovid>

    @Insert
    fun insert(covidData : CasosCovid)

    @Query("DELETE FROM CasosCovid")
    fun deleteAll()
}