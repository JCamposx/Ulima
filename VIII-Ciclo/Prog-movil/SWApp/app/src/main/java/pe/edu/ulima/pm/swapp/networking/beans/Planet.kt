package pe.edu.ulima.pm.swapp.networking.beans

data class Planet (
    // El nombre de las variables debe de ser el mismo que sale en la lista que contiene results en swapi.dev al consultar por planets
    val name : String,
    val terrain : String,
    val population : String
)