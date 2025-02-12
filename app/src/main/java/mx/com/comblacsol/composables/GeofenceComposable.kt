package mx.com.comblacsol.composables

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.comblacsol.MenuGeocercaActivity
import mx.com.comblacsol.MisGeocercasActivity
import mx.com.comblacsol.R
import mx.com.comblacsol.model.GeoCircle
import mx.com.comblacsol.model.GeocercaPoligonal
import mx.com.comblacsol.model.SetGeocercaCircleRequest
import mx.com.comblacsol.model.SetGeocercaPolyRequest
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.CircularGeofenceViewModel
import mx.com.comblacsol.viewmodel.PolygonalGeofenceViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


//Circular
@Composable
fun CircularGeofenceScreen(token:String,
                           circularGeofenceViewModel: CircularGeofenceViewModel,
                           usuario:String) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var circleRadius by remember { mutableStateOf(500.0) } // Radio inicial
    var nombreLimite by remember { mutableStateOf("") }
    var descripcionLimiteVirtual by remember { mutableStateOf("") }




    LaunchedEffect(true) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    val initialPosition = userLocation ?: LatLng(37.7749, -122.4194) // Fallback: San Francisco

    Column(modifier = Modifier.fillMaxSize()) {
        // Mapa (70% del alto)
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(initialPosition, 12f)
            ),
            onMapClick = { latLng ->
                selectedLocation = latLng
            }
        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Punto seleccionado"
                )
                Circle(
                    center = it,
                    radius = circleRadius,
                    fillColor = Color(0x220000FF),
                    strokeColor = Color(0xFF0000FF),
                    strokeWidth = 2f
                )
            }
        }

        // Contenido inferior (30% del alto)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Slider para el radio
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ajusta el tamaño del círculo: ${(circleRadius / 1000).toInt()} sq km",
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Medium,
                )
                Slider(
                    value = circleRadius.toFloat(),
                    onValueChange = { newValue -> circleRadius = newValue.toDouble() },
                    valueRange = 500f..25000f,
                    steps = 49,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto
            TextField(
                value = nombreLimite,
                onValueChange = { nombreLimite = it },
                label = { Text("Nombre de la geocerca") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcionLimiteVirtual,
                onValueChange = { descripcionLimiteVirtual = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        Intent(context, MenuGeocercaActivity::class.java).also {
                            context.startActivity(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancelar", color = Color.Black, fontFamily = gilroy, fontWeight = FontWeight.Normal)
                }
                Button(
                    onClick = {
                        val geoCircle = GeoCircle("${selectedLocation!!.latitude},${selectedLocation!!.longitude}",circleRadius.toString())
                        var grupos = mutableListOf<String>()
                        grupos.add(usuario)
                        grupos.add("Vehiculos de Blac")
                        val setGeocercaCircleRequest = SetGeocercaCircleRequest("30","#0000FF",
                            "#0000FF",descripcionLimiteVirtual,geoCircle,grupos,nombreLimite,"100","CIRCLE",usuario)
                        circularGeofenceViewModel.setCircleGeofence(token,setGeocercaCircleRequest,onSuccess = {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, "Geocerca creada exitosamente", Toast.LENGTH_SHORT).show()
                            }

                            context.startActivity(Intent(context, MisGeocercasActivity::class.java))
                        }, onError = {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    context,
                                    "Error al crear la geocerca",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Guardar", color = Color.White, fontFamily = gilroy, fontWeight = FontWeight.Normal)
                }
            }
        }
    }
}

@Composable
fun CircularGeofenceScreen(name:String, radio:String, centro:String) {
    val context = LocalContext.current
    val lat = centro.split(",")[0].toDouble()
    val lon = centro.split(",")[1].toDouble()
    val center = LatLng(lat, lon)

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(center) }
    var circleRadius by remember { mutableStateOf(radio.toDouble())} // Radio inicial
    var nombreLimite by remember { mutableStateOf(name) }
    var descripcionLimiteVirtual by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLocation = LatLng(lat, lon)
                }
            }
        }
    }

    val initialPosition = userLocation ?: LatLng(lat, lon) // Fallback: San Francisco

    Column(modifier = Modifier.fillMaxSize()) {
        // Mapa (70% del alto)
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(initialPosition, 16f)
            ),
            onMapClick = {}
        ) {
            selectedLocation?.let {
                Circle(
                    center = it,
                    radius = circleRadius,
                    fillColor = Color(0x220000FF),
                    strokeColor = Color(0xFF0000FF),
                    strokeWidth = 2f
                )
            }
        }

        // Contenido inferior (30% del alto)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Campos de texto
            Text(
                text = nombreLimite,
                fontFamily = gilroy,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        Intent(context, MisGeocercasActivity::class.java).also {
                            context.startActivity(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cerrar", color = Color.White,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(4.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun FreeGeoFenceMapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val geofencePoints = remember { mutableStateListOf<LatLng>() }

    // Gestor de clics para agregar puntos a la geocerca
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val googleMap = context as? GoogleMap
                        googleMap?.projection?.let { projection ->
                            // Convierte la ubicación en pantalla (offset) a un objeto Point
                            val point = Point(offset.x.toInt(), offset.y.toInt())
                            // Usa el Point para obtener las coordenadas LatLng
                            val newLatLng = projection.fromScreenLocation(point)
                            newLatLng?.let {
                                geofencePoints.add(it) // Agrega el punto a la lista
                            }
                        }
                    }
                },
            cameraPositionState = cameraPositionState
        ) {
            // Dibuja el polígono cuando hay más de 2 puntos
            if (geofencePoints.size >= 3) {
                Polygon(
                    points = geofencePoints,
                    strokeColor = Color.Blue,
                    fillColor = Color(0x5500A1FF),
                    strokeWidth = 5f
                )
            }

            // Coloca marcadores en los puntos de la geocerca
            geofencePoints.forEachIndexed { index, point ->
                Marker(
                    state = rememberMarkerState(position = point),
                    draggable = true
                )
            }
        }
    }
}


//Poligonal
@Composable
fun GeoFenceMapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapUiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val cameraPositionState = rememberCameraPositionState()

    // Mutable state for geofence points and center
    val geofencePoints = remember { mutableStateListOf<MutableState<LatLng>>() }
    val centerState = remember { mutableStateOf(LatLng(0.0, 0.0)) } // Initial geofence center

    // State for handling marker drag
    val draggingMarkerIndex = remember { mutableStateOf<Int?>(null) }
    val markerStates = remember { mutableStateListOf<MarkerState>() }

    // On map load, calculate initial geofence
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val center = LatLng(location.latitude, location.longitude)
                centerState.value = center
                geofencePoints.clear()
                geofencePoints.addAll(
                    calculateGeofence(center, 500.0).map { mutableStateOf(it) }
                )
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 15f))
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings
        ) {
            // Draw the geofence polygon
            if (geofencePoints.isNotEmpty()) {
                Polygon(
                    points = geofencePoints.map { it.value },
                    strokeColor = Color.Blue,
                    fillColor = Color(0x5500A1FF),
                    strokeWidth = 5f
                )
            }

            // Draw draggable points
            val originalIcon = BitmapFactory.decodeResource(context.resources, R.drawable.circle) // Cargar el drawable
            val resizedIcon = Bitmap.createScaledBitmap(originalIcon, 32, 32, false) // Redimensionarlo
            val markerIcon = BitmapDescriptorFactory.fromBitmap(resizedIcon)

            geofencePoints.forEachIndexed { index, pointState ->
                val markerState = rememberMarkerState(position = pointState.value)

                // Handle the marker drag
                Marker(
                    state = markerState,
                    draggable = true, // Enable dragging
                    icon = markerIcon
                )

                // Observe marker drag events and update point position
                LaunchedEffect(markerState.position) {
                    pointState.value = markerState.position
                }
            }
        }
    }
}



@Composable
fun PolygonalGeoFenceMapScreen(puntos: Int, token:String, polygonalGeofenceViewModel: PolygonalGeofenceViewModel,
                               usuario:String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapUiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val cameraPositionState = rememberCameraPositionState()

    // Mutable state for geofence points and center
    val geofencePoints = remember { mutableStateListOf<MutableState<LatLng>>() }
    val centerState = remember { mutableStateOf(LatLng(0.0, 0.0)) } // Initial geofence center

    var nombreGeocerca by remember { mutableStateOf("") }
    var descripcionGeocerca by remember { mutableStateOf("") }

    // On map load, get user location and center camera
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val center = LatLng(location.latitude, location.longitude)
                centerState.value = center
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 12f)) // Zoom ajustado
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Mapa (60% del alto)
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            onMapClick = { latLng ->
                if (geofencePoints.size < puntos) {
                    geofencePoints.add(mutableStateOf(latLng)) // Agregar punto al hacer clic
                }
            }
        ) {
            // Si hay puntos, se dibuja la geocerca
            if (geofencePoints.size == puntos) {
                val geofence = geofencePoints.map { it.value }

                Polygon(
                    points = geofence,
                    strokeColor = Color.Blue,
                    fillColor = Color(0x5500A1FF),
                    strokeWidth = 5f
                )
            }

            // Dibujar puntos seleccionados
            geofencePoints.forEach { pointState ->
                Marker(state = rememberMarkerState(position = pointState.value))
            }
        }

        // Texto informativo y campos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Toca el mapa para agregar puntos y formar la geocerca.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = gilroy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto
            TextField(
                value = nombreGeocerca,
                onValueChange = { nombreGeocerca = it },
                label = { Text("Nombre de la geocerca",
                    fontFamily = gilroy) },

                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcionGeocerca,
                onValueChange = { descripcionGeocerca = it },
                label = { Text("Descripción", fontFamily = gilroy) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        Intent(context, MenuGeocercaActivity::class.java).also {
                            context.startActivity(it)
                        }

                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancelar", color = Color.Black,
                        fontFamily = gilroy)
                }
                Button(
                    onClick = {
                        //Solamente cuando se hayan agregado todos los puntos
                        Log.e("GEOFENCE", "${geofencePoints.size} $puntos")
                        if (geofencePoints.size == puntos) {
                            var grupos = mutableListOf<String>()
                            grupos.add(usuario)
                            grupos.add("Vehiculos de Blac")
                            val geocercaPoligonal = GeocercaPoligonal(
                                geofencePoints.map {
                                    "${it.value.latitude},${it.value.longitude}"
                                }
                            )

                            val setGeocercaPolyRequest = SetGeocercaPolyRequest("30","#0000FF",
                                "#0000FF",descripcionGeocerca,geocercaPoligonal, grupos,nombreGeocerca,"100","POLYGON",usuario)
                            polygonalGeofenceViewModel.setPolyGeofence(token,setGeocercaPolyRequest,
                                onSuccess = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            context,
                                            "Geocerca creada exitosamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                        context.startActivity(Intent(context, MisGeocercasActivity::class.java))
                                },
                                onError = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(context, "Error al crear la geocerca $it", Toast.LENGTH_SHORT).show()
                                    }

                                })

                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Guardar", color = Color.White, fontFamily = gilroy)
                }
            }
        }
    }
}

@Composable
fun PolygonalGeoFenceMapScreen(
    nombre: String,
    puntosGeocerca: List<LatLng>,
    modifier: Modifier = Modifier
     // Recibir los puntos de la geocerca
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapUiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val cameraPositionState = rememberCameraPositionState()

    var nombreGeocerca by remember { mutableStateOf(nombre) }
    var descripcionGeocerca by remember { mutableStateOf("") }

    // On map load, get user location and center camera
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val center = LatLng(puntosGeocerca.first().latitude, puntosGeocerca.first().longitude)
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 16f)) // Zoom ajustado
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Mapa (60% del alto)
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings
        ) {
            // Dibujar la geocerca con los puntos pasados
            if (puntosGeocerca.isNotEmpty()) {
                Polygon(
                    points = puntosGeocerca,
                    strokeColor = Color.Blue,
                    fillColor = Color(0x5500A1FF),
                    strokeWidth = 5f
                )
            }
        }

        // Texto informativo y campos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "La geocerca se ha dibujado con los puntos predefinidos.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = gilroy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto
            Text(
                text = nombreGeocerca,
                fontFamily = gilroy,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        Intent(context, MisGeocercasActivity::class.java).also {
                            context.startActivity(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancelar", color = Color.White, fontFamily = gilroy)
                }

            }
        }
    }
}


@Composable
fun GeoFenceCuadrangularMapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapUiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val cameraPositionState = rememberCameraPositionState()

    // Mutable state for geofence points and center
    val geofencePoints = remember { mutableStateListOf<LatLng>() }
    val centerState = remember { mutableStateOf(LatLng(0.0, 0.0)) } // Initial geofence center

    // Function to calculate geofence points
    fun calculateGeofence(points: List<LatLng>): List<LatLng> {
        return points // No change in this function for now; you can adjust the logic as needed
    }

    // On map load, get user location and center camera
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val center = LatLng(location.latitude, location.longitude)
                centerState.value = center
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 12f)) // Zoom ajustado
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            onMapClick = { latLng ->
                // Solo agrega puntos si hay menos de 8 puntos
                if (geofencePoints.size < 4) {
                    geofencePoints.add(latLng) // Agregar punto al hacer clic
                }
            }
        ) {
            // Si hay puntos, se dibuja la geocerca
            if (geofencePoints.size == 4) {
                val geofence = calculateGeofence(geofencePoints)

                Polygon(
                    points = geofence,
                    strokeColor = Color.Blue,
                    fillColor = Color(0x5500A1FF),
                    strokeWidth = 5f
                )
            }

            // Dibujar puntos seleccionados
            val originalIcon = BitmapFactory.decodeResource(context.resources, R.drawable.circle) // Cargar el drawable
            val resizedIcon = Bitmap.createScaledBitmap(originalIcon, 32, 32, false) // Redimensionarlo
            val markerIcon = BitmapDescriptorFactory.fromBitmap(resizedIcon)

            geofencePoints.forEach { point ->
                val markerState = rememberMarkerState(position = point) // Usar MarkerState para definir la posición del marcador

                Marker(
                    state = markerState, // Usar MarkerState aquí
                    icon = markerIcon
                )
            }
        }
    }
}




// Function to calculate a geofence polygon based on a center and distance
fun calculateGeofence(center: LatLng, distanceMeters: Double): List<LatLng> {
    val earthRadius = 6378137.0 // Radio de la Tierra en metros

    // Convertir distancia a grados
    val deltaLat = distanceMeters / earthRadius * (180 / Math.PI)
    val deltaLng = distanceMeters / (earthRadius * cos(Math.toRadians(center.latitude))) * (180 / Math.PI)

    val points = mutableListOf<LatLng>()

    // Punto 1 (superior izquierda)
    points.add(LatLng(center.latitude + deltaLat, center.longitude - deltaLng))

    // Punto 2 (superior central, alineado en Y con el punto 1)
    points.add(LatLng(center.latitude + deltaLat, center.longitude))

    // Punto 3 (superior derecha)
    points.add(LatLng(center.latitude + deltaLat, center.longitude + deltaLng))

    // Punto 4 (derecho central, alineado en X con el punto 3)
    points.add(LatLng(center.latitude, center.longitude + deltaLng))

    // Punto 5 (inferior derecha)
    points.add(LatLng(center.latitude - deltaLat, center.longitude + deltaLng))

    // Punto 6 (inferior central, alineado en Y con el punto 5)
    points.add(LatLng(center.latitude - deltaLat, center.longitude))

    // Punto 7 (inferior izquierda)
    points.add(LatLng(center.latitude - deltaLat, center.longitude - deltaLng))

    // Punto 8 (izquierdo central, alineado en X con el punto 7)
    points.add(LatLng(center.latitude, center.longitude - deltaLng))

    return points
}
