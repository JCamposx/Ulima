package pe.edu.ulima.pm.covid19tool.models

import android.content.Context
import pe.edu.ulima.pm.covid19tool.networking.NetworkingManager
import pe.edu.ulima.pm.covid19tool.room.AppDB
import pe.edu.ulima.pm.covid19tool.room.models.CasosCovid

class GestorCovidData {

    fun sicronizarCovidData(context : Context) {
        val daoCovidData = AppDB.getInstance(context).getCovidDataDAO()

        NetworkingManager.getCovidData {
            daoCovidData.insert(
                CasosCovid(0,
                    it[1],  // Departamento
                    it[7]   // Fecha
                )
            )
        }
    }

    fun obtenerCovidDataxFechaRoom(
        context : Context,
        fecha : String
    ) : List<List<String>> {
        val daoCovidData = AppDB.getInstance(context).getCovidDataDAO()

        val listaCovidDataRoom = daoCovidData.findByFecha(fecha)

        val listaCasosxFecha : MutableList<List<String>> = mutableListOf()

        listaCovidDataRoom.map {
            listaCasosxFecha.add(listOf(
                it.departamento,
                it.fechaResultado
            ))
        }

        println("------ DATA POR FECHA ------")
        println(listaCasosxFecha)

        return listaCasosxFecha
    }

    fun limpiarCovidDataRoom(context : Context) {
        AppDB.getInstance(context).getCovidDataDAO().deleteAll()
    }
}