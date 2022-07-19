package pe.edu.ulima.pm.swapp.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Clase singleton
class NetworkingManager(val service : SWService) {

    // Dentro del companion object van las propiedades de clase
    companion object {
        private var instance : NetworkingManager? = null

        fun getInstance() : NetworkingManager {
            if (instance == null) {
                val retrofit : Retrofit = Retrofit.Builder()
                    .baseUrl("https://swapi.dev/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(SWService::class.java)

                instance = NetworkingManager(service)
            }
            return instance!!
        }
    }



}