package mx.com.comblacsol

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import mx.com.comblacsol.model.EventosHistory
import mx.com.comblacsol.model.GetHistoVehicleRequest
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.HistorialMovimientoViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistorialMovimientoActivity : ComponentActivity() {
    private val historialMovimientoViewModel: HistorialMovimientoViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    var usuario by mutableStateOf("")
    var token by mutableStateOf("")
    var deviceId by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        deviceId = sharedPreferences.getString("deviceId", "")!!
        token = sharedPreferences.getString("token", "")!!
        usuario = sharedPreferences.getString("username", "")!!
        val fecha = setFechaActual()

        /*val getHistoVehicleRequest = GetHistoVehicleRequest(
            usuario,
            fecha + "000000",
            fecha + "235959",
            deviceId
        )*/


        val getHistoVehicleRequest = GetHistoVehicleRequest(
            usuario,
            fecha+"000000",
            fecha+"235959",
            deviceId
        )

        historialMovimientoViewModel.getHistorialDispositivo(token, getHistoVehicleRequest,
            onRenewToken = {
                Intent(this@HistorialMovimientoActivity, MainActivity::class.java).also {
                    startActivity(it)
                }
            })

        setContent {
            MxcomblacsolTheme {
                HistorialScreen(historialMovimientoViewModel)
            }
        }
    }

    @Composable
    fun HistorialScreen(historialMovimientoViewModel: HistorialMovimientoViewModel) {
        val historial by historialMovimientoViewModel.historial.collectAsState()
        var fechaSeleccionada by remember { mutableStateOf(obtenerFechaActual()) }
        val puntos = remember { mutableStateOf(listOf<LatLng>()) }
        val cameraPositionState = rememberCameraPositionState()
        // Actualiza la lista de puntos cuando cambia el historial

        LaunchedEffect(historial) {
            puntos.value = historial.map { LatLng(it.latitude.toDouble(), it.longitude.toDouble()) }
            // Mueve la cámara al primer punto si la lista no está vacía
            if (puntos.value.isNotEmpty()) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(puntos.value.first(), 7f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 48.dp, end = 12.dp, start = 12.dp)
        ) {
            // Cabecera
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.125f), // Ocupar 17.5% del espacio
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Historial",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = gilroy,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Selección de fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.125f), // Otro 17.5%
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val context = LocalContext.current
                val calendario = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val nuevaFecha = Calendar.getInstance()
                        nuevaFecha.set(year, month, dayOfMonth)
                        fechaSeleccionada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(nuevaFecha.time)
                        val f = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(nuevaFecha.time)
                        val fechaInicio = "${f}000000"
                        val fechaFin = "${f}235959"

                        historialMovimientoViewModel.getHistorialDispositivo(
                            token,
                            GetHistoVehicleRequest(usuario, fechaInicio, fechaFin, deviceId),
                            onRenewToken = {
                                Intent(this@HistorialMovimientoActivity, MainActivity::class.java).also {
                                    startActivity(it)
                                }
                            }
                        )

                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
                )

                Text(
                    text = fechaSeleccionada,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = gilroy,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .clickable { datePickerDialog.show() }
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Ajuste de padding
                )
            }

            // Mapa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f) // Ocupar el 65% restante
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    historial.forEachIndexed { index, evento ->
                        if (index < puntos.value.size) {
                            val punto = puntos.value[index]
                            val icon = when (index) {
                                0 -> BitmapDescriptorFactory.fromResource(R.drawable.start_race)
                                puntos.value.size - 1 -> BitmapDescriptorFactory.fromResource(R.drawable.finish)
                                else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                            }
                            Marker(
                                state = MarkerState(position = punto),
                                title = "Evento: ${evento.event}",
                                snippet = "Fecha: ${evento.userLocalTimeFormat} a ${evento.speed} Km/h",
                                icon = icon
                            )
                        }
                    }
                    if (puntos.value.size > 1) {
                        Polyline(
                            points = puntos.value,
                            color = Color.Black,
                            width = 7f
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f), // Otro 17.5%
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(onClick = {
                    Intent(this@HistorialMovimientoActivity, PrincipalActivity::class.java).also {
                        startActivity(it)
                    }
                }, modifier = Modifier, // Sin `size(48.dp)` para que use el tamaño por defecto (56.dp)
                    shape = CircleShape,
                    containerColor = Color.Black) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(48.dp)
                            .padding(12.dp),
                        tint = Color.White

                    )
                }
            }
        }

    }


    fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val fechaActual = Date()
        return formato.format(fechaActual)
    }

    fun setFechaActual(): String {
        val formato = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val fechaActual = Date()
        return formato.format(fechaActual)
    }



}

