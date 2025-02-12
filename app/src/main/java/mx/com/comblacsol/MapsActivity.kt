package mx.com.comblacsol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import mx.com.comblacsol.composables.StatusCard
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val latitud = intent.getStringExtra("latitud")
        val longitud = intent.getStringExtra("longitud")
        setContent {
            MxcomblacsolTheme {
                MapScreen(latitud, longitud)
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun MapScreen(latitud: String? = "0.0", longitud: String? = "0.0") {
        // Validar latitud y longitud
        if (latitud.isNullOrEmpty() || longitud.isNullOrEmpty()) {
            Log.e("MapScreen", "Latitud o longitud no válidas")
            return
        }

        // Convertir a LatLng
        val localizacion = LatLng(latitud.toDouble(), longitud.toDouble())

        // Configurar posición inicial del mapa
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(localizacion, 12f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 72.dp, start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                Text(
                    text = "Mi unidad",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = gilroy,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .padding(4.dp)

                )

                val code = intent.getStringExtra("code")
                val dateTime = intent.getStringExtra("dateTime")
                val status = intent.getStringExtra("status")
                val location = intent.getStringExtra("location")

                StatusCard(
                    code = code!!,
                    dateTime = dateTime!!,
                    status = status!!,
                    location = location!!
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {

                var mapType by remember { mutableStateOf(MapType.NORMAL) }


                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true, // Muestra los controles de zoom
                        compassEnabled = true,      // Muestra la brújula
                        mapToolbarEnabled = false  // Opcional, desactiva la barra de herramientas
                    ),
                    properties = MapProperties(
                        mapType = mapType // Configura el tipo de mapa
                    )
                ) {
                    // Agregar marcador
                    Marker(
                        state = MarkerState(position = localizacion),
                        title = intent.getStringExtra("code"),
                        snippet = intent.getStringExtra("location")
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                horizontalArrangement = Arrangement.Center, // Centra los elementos horizontalmente
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.Center // Distribuye los botones uniformemente
                ) {
                    FloatingActionButton(
                        onClick = {
                            Intent(this@MapsActivity, PrincipalActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 16.dp) // Espaciado entre los botones
                            .size(48.dp),
                        shape = CircleShape,
                        containerColor = Color.Black
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Home",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            Intent(this@MapsActivity, HistorialMovimientoActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp),
                        shape = CircleShape,
                        containerColor = Color.Black
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hourglass),
                            contentDescription = "Historial",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }



                }

            }
        }

    }


