package pe.edu.ulima.pm.swapp.networking

import pe.edu.ulima.pm.swapp.networking.beans.PlanetsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SWService {
    @GET("planets")
    // Como la url base de NetworkingManager es swapi.dev/api/, la consulta se hará a eso + lo que escribamos en get, que en este caso sería swapi.dev/api/planets

    fun obtenerPlanetas(@Query("page") page : String?) : Call<PlanetsResponse>
    // @Query indica que podemos hacer peticiones a la api a la dirección swapi.dev/api/planets junto a un query parameter llamado page
}