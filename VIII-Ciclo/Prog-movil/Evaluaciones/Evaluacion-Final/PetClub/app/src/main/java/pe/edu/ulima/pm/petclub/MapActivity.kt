package pe.edu.ulima.pm.petclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        crearMapa()
    }

    // Crear mapa de Google Maps
    private fun crearMapa() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        crearMarcador()
    }

    // Crear marcador en el mapa
    private fun crearMarcador() {
        val coordenadas = LatLng(-12.0848, -76.9708)

        val marcador = MarkerOptions().position(coordenadas).title("Marcador")
        map.addMarker(marcador)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas, 18f),
            2_000,
            null
        )
    }
}