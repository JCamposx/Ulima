package pe.edu.ulima.pm.covid19tool.networking

import java.net.HttpURLConnection
import java.net.URL

class NetworkingManager {
    companion object {
        private val url : URL = URL("https://files.minsa.gob.pe/s/eRqxR35ZCxrzNgr/download")

        fun getCovidData(callback : (caso : List<String>) -> Unit) {
            println("------ DESCARGANDO DATA ------")

            val connection = url.openConnection() as HttpURLConnection

            var a = 1
            try {
                connection.inputStream.bufferedReader().use { ln ->
                    ln.readLine()
                    var line: String?
                    while (ln.readLine().also { line = it } != null) {
                        val caso = line.toString().split(";")

                        callback(caso)

                        println(a)
                        if (a >= 10_000) break
                        a++
                    }
                }
            } finally {
                connection.disconnect()
                println("--------- SE TERMINO LA CONEXIÓN REMOTA ---------")
            }
        }
    }
}