package pe.edu.ulima.pm.covid19tool.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CasosCovid (
    @PrimaryKey(autoGenerate = true)
    val id : Int,

    @ColumnInfo(name = "departamento")
    val departamento : String,

    @ColumnInfo(name = "fechaResultado")
    val fechaResultado : String,
)