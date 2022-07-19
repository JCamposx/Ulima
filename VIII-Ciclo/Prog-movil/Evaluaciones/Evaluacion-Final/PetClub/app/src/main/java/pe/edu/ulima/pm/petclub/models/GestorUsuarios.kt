package pe.edu.ulima.pm.petclub.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GestorUsuarios {
    private val dbFirestore = Firebase.firestore
    private val usuariosCol = dbFirestore.collection("usuarios")

    companion object {
        private var instance: GestorUsuarios? = null

        fun getInstance(): GestorUsuarios {
            if (instance == null) instance = GestorUsuarios()

            return instance!!
        }
    }

    // Login con Firebase Authetication
    fun login(
        username: String,
        password: String,
        success: (email: String) -> Unit,
        error: (cod: Int) -> Unit
    ) {
        usuariosCol.whereEqualTo("username", username).get()
            .addOnSuccessListener {
                if (it!!.documents.size > 0) {
                    // Username se encuentra registrado
                    val email = it.documents[0]["email"].toString()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { auth ->
                            if (auth.isSuccessful) {
                                success(email)
                            } else {
                                // Contraseña incorrecta
                                error(1)
                            }
                        }
                } else {
                    // Username no registrado
                    error(2)
                }
            }
    }

    // Registro con Firebase Authetication
    fun registro(
        email: String,
        username: String,
        password: String,
        success: () -> Unit,
        error: (cod: Int) -> Unit
    ) {
        usuariosCol.whereEqualTo("email", email).get()
            .addOnSuccessListener { emailField ->
                if (emailField.documents.size > 0) {
                    // Email ya registrado
                    error(1)
                } else {
                    usuariosCol.whereEqualTo("username", username).get()
                        .addOnSuccessListener { usernameField ->
                            if (usernameField.documents.size > 0) {
                                // Username ya registrado
                                error(2)
                            } else {
                                // Email y username válidos
                                // Registro con Firebase Authetication
                                FirebaseAuth.getInstance()
                                    .createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { auth ->
                                        if (auth.isSuccessful) {
                                            val usuario = hashMapOf(
                                                "email" to email,
                                                "username" to username,
                                                "registroCompleto" to false
                                            )

                                            usuariosCol.add(usuario)
                                                .addOnSuccessListener {
                                                    // Usuario registrado
                                                    success()
                                                }
                                        } else {
                                            // Contraseña muy corta
                                            error(3)
                                        }
                                    }
                            }
                        }
                }
            }
    }

    // Logout con Firebase Authetication
    fun logout(callback: () -> Unit) {
        FirebaseAuth.getInstance().signOut()

        callback()
    }

    // Verificar si el usuario ha completado registro previamente
    fun verificarRegistroCompleto(username: String, success: () -> Unit, error: () -> Unit) {
        usuariosCol.whereEqualTo("username", username).get()
            .addOnSuccessListener {
                val registroCompleto = it.documents[0]["registroCompleto"]

                if (registroCompleto == true) success()
                else error()
            }
    }

    // Obtener datos de un determinado usuario
    fun obtenerDatosUsuario(
        username: String,
        callback: (nombre: String, apellido: String, celular: String, descripcion: String) -> Unit
    ) {
        usuariosCol.whereEqualTo("username", username).get()
            .addOnSuccessListener {
                callback(
                    it.documents[0]["nombre"].toString(),
                    it.documents[0]["apellido"].toString(),
                    it.documents[0]["celular"].toString(),
                    it.documents[0]["descripcion"].toString(),
                )
            }
    }

    // Guardar datos de un determinado perfil
    fun guardarCambiosPerfil(
        username: String,
        nombre: String,
        apellido: String,
        celular: String,
        descripcion: String,
        callback: () -> Unit
    ) {
        usuariosCol.whereEqualTo("username", username).get()
            .addOnSuccessListener {
                val datos = mapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "celular" to celular,
                    "descripcion" to descripcion,
                    "registroCompleto" to true
                )

                usuariosCol.document(it.documents[0].id).update(datos)
                    .addOnSuccessListener {
                        // Cambios guardados
                        callback()
                    }
            }
    }
}