package mx.com.comblacsol

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import mx.com.comblacsol.composables.CircularGeofenceScreen
import mx.com.comblacsol.composables.GeoFenceMapScreen
import mx.com.comblacsol.composables.PolygonalGeoFenceMapScreen

import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.viewmodel.CircularGeofenceViewModel
import mx.com.comblacsol.viewmodel.PolygonalGeofenceViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GeocercaActivity : ComponentActivity() {
    val circularGeofenceViewModel: CircularGeofenceViewModel by viewModel()
    val polygonalGeofenceViewModel: PolygonalGeofenceViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    var token by mutableStateOf("")
    var usuario by mutableStateOf("")
    override fun onPause() {
        super.onPause()
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        token = sharedPreferences.getString("token", "") ?: ""
        usuario = sharedPreferences.getString("username", "") ?: ""
        val forma = intent.getIntExtra("forma", 0)
        val modo = intent.getIntExtra("modo", 0)
        val name = intent.getStringExtra("name")
        val color = intent.getStringExtra("color")
        val radio = intent.getStringExtra("radio")
        val centro = intent.getStringExtra("centro")
        val puntos = intent.getStringArrayListExtra("puntos")

        val latLngList = puntos?.mapNotNull { punto ->
            val coords = punto.split(",")
            val lat = coords[0].toDoubleOrNull()
            val lng = coords[1].toDoubleOrNull()
            if (lat != null && lng != null) {
                LatLng(lat, lng)
            } else {
                null 
            }
        }

        setContent {
            MxcomblacsolTheme {
               if(modo==0){
                   if(forma==1)
                       CircularGeofenceScreen(token,circularGeofenceViewModel,usuario)
                   else
                       PolygonalGeoFenceMapScreen(forma,token,polygonalGeofenceViewModel,usuario)
               }else{
                   if(forma==1)
                   {
                       CircularGeofenceScreen(name!!,radio!!,centro!!)
                   }else{
                       PolygonalGeoFenceMapScreen(name!!,latLngList!!)
                   }
               }
            }
        }
    }


}

