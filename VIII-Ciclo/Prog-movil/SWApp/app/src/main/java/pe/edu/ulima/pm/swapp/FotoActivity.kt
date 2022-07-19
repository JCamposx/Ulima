package pe.edu.ulima.pm.swapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class FotoActivity : AppCompatActivity() {
    private lateinit var butTomarFoto : Button
    private lateinit var iviFoto : ImageView

    private var mFotoPath : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        iviFoto = findViewById(R.id.iviFoto)

        butTomarFoto = findViewById(R.id.butTomarFoto)
        butTomarFoto.setOnClickListener {
            tomarFoto()
        }
    }

    private fun tomarFoto() {
        // Intent para llamar a una camara
        // val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Llamamos a la camara
        // startActivityForResult(intent,100)

        val imagenFile = crearArchivoImagen()
        if (imagenFile != null) {
            val fotoURI = FileProvider.getUriForFile(
                    this,
                    "pe.edu.ulima.pm.swapp.fileprovider",
                    imagenFile
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI)
            startActivityForResult(intent, 100)
        }
    }

    private fun crearArchivoImagen() : File? {
        // El nombre del archivo de imagen a crear debe de ser único
        // Por esto, una opción sería usar un timestamp para el nombre de esta foto
        val timestamp = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val nombreArchivo = "SWAPP_${timestamp}"

        // Directorio donde se va a almacenar la foto
        val folderAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // La foto
        val imageFile = File.createTempFile(nombreArchivo, ".jpg", folderAlmacenamiento)

        // Guardamos en esta variable la ruta donde se va a guardar la foto
        mFotoPath = imageFile.absolutePath

        return imageFile
    }

    // Cuando se termine de tomar la foto, se vendrá a esta función
    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si el resultCode es el que pusimos para la cámara y bota como resultado un OK
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // PARA TOMAR LA FOTO EN MINIATURA
            // Recibimos la foto y la pintamos en el ImageView
            // val bitmap : Bitmap = data!!.extras!!.get("data") as Bitmap
            // iviFoto.setImageBitmap(bitmap)

            mostrarFoto()
        }
    }

    private fun mostrarFoto() {
        val rotationMatrix = Matrix()
        val angulo = obtenerAnguloRotacion()
        rotationMatrix.postRotate(angulo)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(mFotoPath!!, options)

        // Cálculo de espacio disponible
        val iviHeight = iviFoto.height
        val iviWidth = iviFoto.width

        // Cálculo del factor de escalamiento
        var scaleFactor = 1
        if (angulo == 90f || angulo == 270f) {
            scaleFactor = min(
                    iviWidth / options.outHeight,
                    iviHeight / options.outWidth
            )
        } else {
            scaleFactor = min(
                    iviWidth / options.outWidth,
                    iviHeight / options.outHeight
            )
        }

        options.inJustDecodeBounds = false
        options.inSampleSize = scaleFactor

        val bitmap : Bitmap = BitmapFactory.decodeFile(mFotoPath!!, options)
        val bitmapRotated = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                rotationMatrix,
                true
        )
        iviFoto.setImageBitmap(bitmapRotated)
    }

    private fun obtenerAnguloRotacion() : Float {
        val exifInterface = ExifInterface(mFotoPath!!)

        val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
        )

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90f
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180f
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270f
        } else {
            return 0f
        }
    }
}