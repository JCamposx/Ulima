package pe.edu.ulima.pm.petclub.models.beans

data class Publicacion(
    val username: String,
    val tipoPublicacionId: Int,
    val nombreMascota: String,
    val edad: Int,
    val tipoEdad: String,
    val distrito: String,
    val fechaPerdida: String,
    val descripcionPublicacion: String,
    val nombreFoto: String
)