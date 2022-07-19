package pe.edu.ulima.pm.swapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import pe.edu.ulima.pm.swapp.models.GestorUsuarios
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.PrintWriter
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {
    private lateinit var eteUsername: EditText
    private lateinit var etePassword: EditText
    private lateinit var butLocalizacion: Button
    private var mObteniendoLocalizaciones: Boolean = false
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mLocationRequest: LocationRequest? = null // lazy init
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        crearCanalNotificacion()

        // if (estaLogeadoSP()) {
        if (estaLogeadoAI()) {
            // Si el usuario está logeado, nos saltamos el login y pasamos al MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_login)

            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    println("LOCALIZACION ACTUAL")
                    Log.i("LoginActivity", "Latidud: ${location?.latitude}")
                    Log.i("LoginActivity", "Latidud: ${location?.altitude}")
                }
            }

            eteUsername = findViewById(R.id.eteUsername)
            etePassword = findViewById(R.id.etePassword)

            findViewById<Button>(R.id.butLogin).setOnClickListener {
                // Guardamos el usuario en el Shared Preference
                // Los Shares Preferences son db que almacenan tuplas con llave : valor
                //guardarUsernameSP()

                // 0. Login con firebase
                GestorUsuarios.getInstance().login(
                    eteUsername.text.toString(),
                    etePassword.text.toString()
                ) {
                    if (it == null) {
                        // Error en login
                        val notif = crearNotificacion(
                            "Login SWAPP",
                            "Error en login, click para registrarse"
                        )
                        NotificationManagerCompat.from(this).notify(1, notif)

                        Log.i("GestorUsuarios", "Error en login")
                        Toast.makeText(
                            this,
                            "Error en login",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 1. Guardamos el usuario en el Almacenamiento Interno
                        // guardarUsernameSP()
                        guardarUsernameAI()

                        // 2. Luego pasamos al Main Activity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }


                // 1. Guardamos el usuario en el Almacenamiento Interno
                // guardarUsernameAI()

                // 2. Luego pasamos al Main Activity
                // startActivity(Intent(this, MainActivity::class.java))
                // finish()  // Con finish sacamos al LoginActivity de la pila de activities, por lo
                // que cuando pasemos al MainActivity y presionemos el botón back, se saldrá de la
                // aplicación y no se retornará al Login
            }
            butLocalizacion = findViewById(R.id.butLocalizacion)
            butLocalizacion.setOnClickListener {
                mObteniendoLocalizaciones = !mObteniendoLocalizaciones
                if (!mObteniendoLocalizaciones) {
                    butLocalizacion.text = "Iniciar localización"
                    pararLocalizacionActual()
                } else {
                    butLocalizacion.text = "Parar localización"
                    obtenerLocalizacionActual()
                }
            }
        }

        obtenerUltimaLocalizacion()
    }

    // Guardar el username en el SP
    private fun guardarUsernameSP() {
        val editor = getSharedPreferences(
            Constantes.SP,
            MODE_PRIVATE
        ).edit() // Nombre del Shared Preferences (db), contexto privado

        editor.putString(
            Constantes.SP_USERNAME,
            eteUsername.text.toString()
        ) // Almacenamos en el SP el username bajo la llave de USERNAME

        editor.commit()
    }

    // Leer el SP para comprobar si existe un USERNAME
    private fun estaLogeadoSP(): Boolean {
        val sp = getSharedPreferences(Constantes.SP, Context.MODE_PRIVATE)

        val username = sp.getString(Constantes.SP_USERNAME, "") // Buscamos en el SP si alguna la
        // llave USERNAME tiene algún valor, sino retornamos ""

        //if (username == "") {
        //    return false
        //} else {
        //    return true
        //}
        return username != ""
    }

    // Guardar el username en el Almacenamiento Interno
    private fun guardarUsernameAI() {
        // Escribimos en un archivo data.txt
        //val fos = openFileOutput("data.txt", Context.MODE_PRIVATE)
        //fos.write(eteUsername.text.toString().toByteArray(Charset.defaultCharset()))
        //fos.close()

        openFileOutput(Constantes.AI, Context.MODE_PRIVATE).use {
            it.write(eteUsername.text.toString().toByteArray(Charset.forName("UTF-8")))
            it.close()
        }
    }

    // Ver si en el Almacenamiento Interno existe un username
    private fun estaLogeadoAI(): Boolean {
        // Leer archivo
        try {
            openFileInput(Constantes.AI).use {
                val username = it.bufferedReader(
                    Charset.forName("UTF-8")
                ).readLine()

                return true
            }
        } catch (e: FileNotFoundException) {
            return false
        }
    }

    // Guardar el username en el Almacenamiento Externo
    private fun guardarUsernameAE() {
        // Para esto, necesitamos permisos. Por esto, en el AndroidManifest hay que agregar
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

        // Ruta donde vamos a crear el archivo
        val rutaArchivo = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ).absolutePath + Constantes.AE

        // Creamos el archivo en la ruta que definimos antes
        val archivo = File(rutaArchivo)
        PrintWriter(archivo).use {
            it.println(eteUsername.text.toString())
        }
    }

    // Ver si en el Almacenamiento Externo existe un username
    private fun estaLogeadoAE(): Boolean {
        // Ruta donde se ubica el archivo
        val rutaArchivo = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ).absolutePath + Constantes.AE

        // Leer archivo
        try {
            FileReader(rutaArchivo).use {
                val username = it.readText()
                return true
            }
        } catch (e: FileNotFoundException) {
            return false
        }
    }

    private fun crearCanalNotificacion() {
        // Vemos en qué version de Android estamos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Si el API del dispositivo donde se instala la app es mayor a 26
            // Esta verificación es necesaria porque en versiones anteriores no es necesario crear
            // el canal de notificaciones
            val name = Constantes.NotificationData.NOTIFICATION_CHANNEL_NAME
            val descriptionText = Constantes.NotificationData.NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(
                Constantes.NotificationData.NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun crearNotificacion(title: String, content: String): Notification {
        val intent = Intent(this, RegistroActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Intent pendiente que se va a ejecutar en el futuro, no en el momento, sino cuando se haga
        // click en la notificacion
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            this,
            Constantes.NotificationData.NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(com.google.android.material.R.drawable.ic_keyboard_black_24dp)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        return notification
    }

    private fun obtenerUltimaLocalizacion() {
        val permisoLoc = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permisoLoc != PackageManager.PERMISSION_GRANTED) {
            // No se tienen los permisos de localización
            // Hay que solicitar los permisos al usuario
            val puedePedirPermisoLoc = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )

            if (puedePedirPermisoLoc) {
                Toast.makeText(
                    this,
                    "Debe habilitar sus permisos por la configuración",
                    Toast.LENGTH_LONG
                ).show()
                // finish()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
                obtenerUltimaLocalizacion()
            }
        } else {
            // Ya se tienen los permisos
            // Hay que pedir la última localización
            mFusedLocationClient.lastLocation.addOnSuccessListener {
                println("ULTIMA LOCALIZACION")
                Log.i("LoginActivity", "Latitud: ${it?.latitude}")
                Log.i("LoginActivity", "Longitud: ${it?.longitude}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtenerLocalizacionActual() {
        println("LOCALIZACION ACTUAL")
        if (mLocationRequest == null) mLocationRequest = createLocationRequest() // lazy init

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback,
            null
        )
    }

    private fun pararLocalizacionActual() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 4000 // Tiempo en milisegundos
        locationRequest.fastestInterval = 2000 // Tiempo el milisegundos
        locationRequest.priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY

        return locationRequest
    }

    override fun onResume() {
        super.onResume()
        if (mObteniendoLocalizaciones) {
            println("Localizacion iniciada")
            obtenerLocalizacionActual()
        }
    }

    override fun onPause() {
        super.onPause()

        if (mObteniendoLocalizaciones) {
            println("Localizacion parada")
            pararLocalizacionActual()
        }
    }
}