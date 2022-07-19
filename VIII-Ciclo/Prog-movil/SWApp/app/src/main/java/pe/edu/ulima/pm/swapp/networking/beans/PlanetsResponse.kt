package pe.edu.ulima.pm.swapp.networking.beans

data class PlanetsResponse (
    // Es nombre de las variables debe ser lo mismo que nos bota el api swapi.dev al consultar por planets
    val count : Int,
    val next : String,
    val previous : String?,
    val results : List<Planet>,
)