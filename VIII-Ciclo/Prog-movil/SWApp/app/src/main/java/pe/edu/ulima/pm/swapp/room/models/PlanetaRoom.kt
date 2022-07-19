package pe.edu.ulima.pm.swapp.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Primero, hay que ir al build.gradle Module y agregar Room (ORM) y Kapt

// Room
//implementation 'androidx.room:room-runtime:2.4.2'
//annotationProcessor 'androidx.room:room-compiler:2.4.2'
//kapt 'androidx.room:room-compiler:2.4.2'

// Kapt
//id 'kotlin-kapt'

@Entity
data class PlanetaRoom(
    @PrimaryKey(autoGenerate = true)
    val id : Int,

    @ColumnInfo(name = "nombre")
    val nombre : String,

    @ColumnInfo(name = "terreno")
    val terreno : String,

    @ColumnInfo(name = "poblacion")
    val poblacion : String
)

// Por lo tanto, en nuestra base de datos SQLite se va a crear una tabla PlanetaRoom con las columnas de id, nombre, terreno y población, donde la columna id es el PK