package pe.edu.ulima.pm.swapp.models

import android.content.Context
import android.os.Handler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pe.edu.ulima.pm.swapp.models.beans.Planeta
import pe.edu.ulima.pm.swapp.networking.NetworkingManager
import pe.edu.ulima.pm.swapp.room.AppDB
import pe.edu.ulima.pm.swapp.room.dao.PlanetaRoomDAO
import pe.edu.ulima.pm.swapp.room.models.PlanetaRoom

class GestorPlanetas {
    val dbFirebase = Firebase.firestore
    val handler: Handler = Handler() // Creado en el hilo principal

    fun obtenerListaPlanetas(callback: (planetas: List<Planeta>) -> Unit): Unit {
        val hilo = Thread {
            // Simulando conexión remota

            Thread.sleep(2000L)
            val lista: List<Planeta> = listOf(
                Planeta(
                    "Tattoine",
                    "desert",
                    "200000"
                ),
                Planeta(
                    "Alderaan",
                    "drasslands",
                    "2000000000"
                ),
                Planeta(
                    "Yavin IV",
                    "jungle, rainforests",
                    "1000"
                )
            )

            // Como handler fue creado en el hilo principal, el código que pasemos en el post se ejecuta en el contexto de este hilo principal
            handler.post { // accedemos al handler desde un hilo secundario
                callback(lista)
            }
        }
        hilo.start()
    }

    //suspend fun obtenerListaPlanetasCorrutinas() : List<Planeta> {
    //    // Para usar corutinas, hay que configurar el proyecto
    //    // Vamos a build.gradle (Module), le agregamos lo siguiente
    //    // implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2'
    //    // Le damos a SyncNow
    //    // Ahora sí podemos continuar
    //
    //    delay(2000L)
    //
    //    val lista : List<Planeta> = listOf(
    //        Planeta(
    //            "Tattoine",
    //            "desert",
    //            200000
    //        ),
    //        Planeta(
    //            "Alderaan",
    //            "drasslands",
    //            2000000000
    //        ),
    //        Planeta(
    //            "Yavin IV",
    //            "jungle, rainforests",
    //            1000
    //        )
    //    )
    //
    //    return lista
    //}

    // Vamos a necesitar retrofit y converter gson
    // Agregamos la librería yendo a build.gradle (Module) y colocamos
    // implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    // implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    // Le damos a Sync Now
    // Ahora creamos un package networking, dentro una interface llamada SWService y trabajamos ahí

    // CONEXIÓN REMOTA
    // Luego de configurar la conexión remota (el package networking), ahora sí podemos llevarla a cabo
    fun obtenerListaPlanetasCorrutinas(): List<Planeta> {

        //Conexión Remota
        val networkingManager = NetworkingManager.getInstance()

        val resultado = mutableListOf<Planeta>()

        var cont: Int = 1

        while (true) {

            // cont.toString indica que vamos a hacer la petición con el query parameter de cont
            val response = networkingManager
                .service
                .obtenerPlanetas(cont.toString())
                .execute()

            val planetsResponse = response.body()!!

            // Llenar un arreglo de planetas
            planetsResponse.results.forEach {
                val planeta = Planeta(
                    it.name,
                    it.terrain,
                    it.population
                )
                resultado.add(planeta)
            }

            // Si la siguiente solicitud a la api no existe, cerramos el bucle
            if (planetsResponse.next == null) break

            cont++
        }

        //val resultado = planetsResponse.results.map {
        //    Planeta(
        //        it.name,
        //        it.terrain,
        //        it.population
        //    )
        //}

        return resultado

        // Al correr, nos va a dar error
        // Para solucionarlo, vamos a android manifest y agregamos
        // <uses-permission android:name="android.permission.INTERNET" />
    }

    fun obtenerListaPlanetasPlanetasRoom(context: Context): List<Planeta> {
        val daoPlaneta: PlanetaRoomDAO = AppDB.getInstance(context).getPlanetaRoomDAO()

        val listaPlanetasRoom =
            daoPlaneta.getAll() // Obtenemos todos los planetas de PlanetaRoom consultado a la DB mediante Room

        val listaPlanetas = listaPlanetasRoom.map {
            Planeta(it.nombre, it.terreno, it.poblacion)
        }

        return listaPlanetas
    }

    fun guardarListaPlanetasRoom(context: Context, planetas: List<Planeta>) {
        val daoPlaneta: PlanetaRoomDAO = AppDB.getInstance(context).getPlanetaRoomDAO()

        planetas.forEach {
            daoPlaneta.insert(
                PlanetaRoom(0, it.nombre, it.terreno, it.poblacion)
            )
        }
    }

    fun guardarListaPlanetasFirebase(
        planetas: List<Planeta>,
        success: () -> Unit,
        error: (String) -> Unit
    ) {
        // A pesar de que la collection planetas no existe, se va a crear automaticamente
        val planetasCol = dbFirebase.collection("planetas")

        dbFirebase.runTransaction { transaction ->
            planetas.forEach {
                val mapPlaneta = hashMapOf(
                    // Nombre de los datos a guardar en firebase
                    "nombre" to it.nombre,
                    "terreno" to it.terreno,
                    "poblacion" to it.poblacion
                )

                transaction.set(planetasCol.document(), mapPlaneta)
            }
        }
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                error(it.message.toString())
            }
    }

    fun obtenerListaPlanetasFirebase(
        success: (List<Planeta>) -> Unit,
        error: (String) -> Unit
    ) {
        val planetasCol = dbFirebase.collection("planetas")

        planetasCol.get()
            .addOnSuccessListener {
                val listaPlanetas = it.documents.map { documentSnapshot ->
                    Planeta(
                        documentSnapshot["nombre"].toString(),
                        documentSnapshot["terreno"].toString(),
                        documentSnapshot["poblacion"].toString()
                    )
                }

                success(listaPlanetas)
            }
            .addOnFailureListener {
                error(it.message.toString())
            }
    }
}