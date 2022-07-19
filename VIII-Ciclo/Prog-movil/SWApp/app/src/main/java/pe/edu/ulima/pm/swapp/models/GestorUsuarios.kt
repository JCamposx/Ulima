package pe.edu.ulima.pm.swapp.models

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pe.edu.ulima.pm.swapp.models.beans.Usuario

class GestorUsuarios {
    private val dbFirebase = Firebase.firestore

    companion object {
        private var instance: GestorUsuarios? = null

        fun getInstance(): GestorUsuarios {
            if (instance == null) {
                instance = GestorUsuarios()
            }
            return instance!!
        }
    }

    fun login(username: String, password: String, callback: (Usuario?) -> Unit) {
        val usuariosCol = dbFirebase.collection("usuarios")

        usuariosCol
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener {
                if (it!!.documents.size > 0) {
                    val usuario = Usuario(
                        it.documents[0].id,
                        it.documents[0]["username"].toString(),
                        it.documents[0]["nombre"].toString()
                    )
                    callback(usuario)
                } else {
                    callback(null)
                }
            }
    }
}