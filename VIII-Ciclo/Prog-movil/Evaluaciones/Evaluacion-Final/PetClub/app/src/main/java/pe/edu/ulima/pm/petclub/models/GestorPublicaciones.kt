package pe.edu.ulima.pm.petclub.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import pe.edu.ulima.pm.petclub.models.beans.Publicacion
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GestorPublicaciones {
    private val dbFirestore = Firebase.firestore
    private val postsCol = dbFirestore.collection("posts")

    companion object {
        private var instance: GestorPublicaciones? = null

        fun getInstance(): GestorPublicaciones {
            if (instance == null) instance = GestorPublicaciones()

            return instance!!
        }
    }

    // Subir publicación a Firebase
    fun subirPublicacion(
        username: String,
        tipoPublicacionId: Int,
        nombreMascota: String,
        edad: Int,
        tipoEdad: String,
        distrito: String,
        fechaPerdida: String,
        descripcionPublicacion: String,
        fotoUri: Uri,
        callback: () -> Unit
    ) {
        // Definir nombre de la foto a subir como el timestamp de publicación
        val formato = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS", Locale.getDefault())
        val nombreFoto = formato.format(Date())

        // Publicación a subir
        val post = hashMapOf(
            "username" to username,
            "tipoPublicacionId" to tipoPublicacionId,
            "nombreMascota" to nombreMascota,
            "edad" to edad,
            "tipoEdad" to tipoEdad,
            "distrito" to distrito,
            "fechaPerdida" to fechaPerdida,
            "descripcionPublicacion" to descripcionPublicacion,
            "nombreFoto" to nombreFoto
        )

        subirFoto(fotoUri, nombreFoto)

        postsCol.add(post)
            .addOnSuccessListener {
                // Publicación registrada
                callback()
            }
    }

    // Subir foto de la publicación a Firebase Storage
    private fun subirFoto(fotoUri: Uri, nombreFoto: String) {
        val storageReference = FirebaseStorage.getInstance()
            .getReference("posts/$nombreFoto")

        storageReference.putFile(fotoUri)
    }

    // Obteneter publicaciones
    fun obtenerPublicaciones(callback: (List<Publicacion>) -> Unit) {
        postsCol.get()
            .addOnSuccessListener {
                val listaPublicaciones = it.documents.map { documentSnapshot ->
                    Publicacion(
                        documentSnapshot["username"].toString(),
                        documentSnapshot["tipoPublicacionId"].toString().toInt(),
                        documentSnapshot["nombreMascota"].toString(),
                        documentSnapshot["edad"].toString().toInt(),
                        documentSnapshot["tipoEdad"].toString(),
                        documentSnapshot["distrito"].toString(),
                        documentSnapshot["fechaPerdida"].toString(),
                        documentSnapshot["descripcionPublicacion"].toString(),
                        documentSnapshot["nombreFoto"].toString()
                    )
                }

                callback(listaPublicaciones)
            }
    }

    // Obteneter publicaciones de un determinado usuario
    fun obtenerPublicacionesParaUsuario(username: String, callback: (List<Publicacion>) -> Unit) {
        postsCol.whereEqualTo("username", username).get()
            .addOnSuccessListener {
                val listaPublicaciones = it.documents.map { documentSnapshot ->
                    Publicacion(
                        documentSnapshot["username"].toString(),
                        documentSnapshot["tipoPublicacionId"].toString().toInt(),
                        documentSnapshot["nombreMascota"].toString(),
                        documentSnapshot["edad"].toString().toInt(),
                        documentSnapshot["tipoEdad"].toString(),
                        documentSnapshot["distrito"].toString(),
                        documentSnapshot["fechaPerdida"].toString(),
                        documentSnapshot["descripcionPublicacion"].toString(),
                        documentSnapshot["nombreFoto"].toString()
                    )
                }

                callback(listaPublicaciones)
            }
    }

    // Obtener foto de una publicación de Firebase Storage
    fun obtenerFotoPublicacion(nombreFoto: String, callback: (bitmap: Bitmap) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
            .child("posts/$nombreFoto")

        val localFile = File.createTempFile("tempImage", "jpg")

        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            callback(bitmap)
        }
    }

    // Obtener Uri de una determinada foto de Firebase Storage
    fun obtenerFotoUri(nombreFoto: String, success: (fotoUri: Uri) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
            .child("posts/$nombreFoto")

        storageReference.downloadUrl.addOnSuccessListener { fotoUri ->
            success(fotoUri)
        }
    }

    // Eliminar una publicación de Firebase
    fun eliminarPublicacion(nombreFoto: String) {
        postsCol.whereEqualTo("nombreFoto", nombreFoto).get()
            .addOnSuccessListener {
                postsCol.document(it.documents[0].id).delete()
            }

        eliminarFoto(nombreFoto)
    }

    // Eliminar una foto de Firebase Storage
    private fun eliminarFoto(nombreFoto: String) {
        val storageReference = FirebaseStorage.getInstance().reference
            .child("posts/$nombreFoto")

        storageReference.delete()
    }
}