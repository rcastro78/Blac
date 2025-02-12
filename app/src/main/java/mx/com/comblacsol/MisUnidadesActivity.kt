package mx.com.comblacsol

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.com.comblacsol.composables.StatusCard
import mx.com.comblacsol.model.Device
import mx.com.comblacsol.model.Evento
import mx.com.comblacsol.model.EventosResponse
import mx.com.comblacsol.model.GetVehicleRequest
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.MisUnidadesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MisUnidadesActivity : ComponentActivity() {
    var lstUnidades = mutableStateListOf<Evento>()
    val sharedPreferences: SharedPreferences by inject()
    private val unidadesViewModel: MisUnidadesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val token = sharedPreferences.getString("token", "")
        Log.d("Token", token!!)
        val usuario = sharedPreferences.getString("username", "")
        val fecha = obtenerFechaActual()
        val getVehicleRequest = GetVehicleRequest(usuario!!,fecha+"000000",fecha+"235959")
        unidadesViewModel.getUnidades(token!!, getVehicleRequest, onSuccess = {
              lstUnidades.clear()
              lstUnidades.addAll(it.eventos)
        },
            onError = {
                Log.d("Error", it.toString())
            },
            onRenewToken = {
                Intent(this@MisUnidadesActivity, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        )

        setContent {
            MxcomblacsolTheme {
                UnidadesScreen(onWhatsAppClick = {
                })
            }
        }
    }


    @Composable
    @Preview(showBackground = true)
    fun UnidadesScreen(onWhatsAppClick: () -> Unit = {}) {
        var searchText by remember { mutableStateOf("") }
        val filteredList:List<Evento>
        if(searchText.isNotEmpty()) {
            filteredList = lstUnidades.filter {
                it.nombre_vehiculo.contains(searchText, ignoreCase = true)
            }
        }else{
            filteredList = lstUnidades
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 48.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Mis unidades",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = gilroy,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text(text = "Buscar unidad") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Permite que la lista ocupe todo el espacio disponible
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredList) { unidad ->
                        StatusCard(
                            code = unidad.nombre_vehiculo,
                            dateTime = unidad.userLocalTime,
                            status = unidad.status_vehicule,
                            location = unidad.address,
                            speed = unidad.speed,
                            onStatusCardClick = {
                                Intent(this@MisUnidadesActivity, MapsActivity::class.java).also {
                                    it.putExtra("latitud", unidad.latitude)
                                    it.putExtra("longitud", unidad.longitude)
                                    it.putExtra("code", unidad.nombre_vehiculo)
                                    it.putExtra("dateTime", unidad.userLocalTime)
                                    it.putExtra("status", if (unidad.status_vehicule == "STOPPED") unidad.status_vehicule else "${unidad.speed} Km/h")
                                    it.putExtra("location", unidad.address)
                                    sharedPreferences.edit().putString("deviceId", unidad.deviceID).apply()
                                    startActivity(it)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 56.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    FloatingActionButton(
                        onClick = {
                            Intent(this@MisUnidadesActivity, PrincipalActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier.padding(end = 16.dp), // Espaciado entre botones
                        shape = CircleShape,
                        containerColor = Color.Black
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            Intent(
                                this@MisUnidadesActivity,
                                MisGeocercasActivity::class.java
                            ).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier, // Sin `size(48.dp)` para que use el tamaño por defecto (56.dp)
                        shape = CircleShape,
                        containerColor = Color.Black
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fence),
                            contentDescription = "Geocercas",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(12.dp),

                            tint = Color.White
                        )
                    }
                }




            }

            // Ícono de Home flotante



        }


    }


    fun convertirFecha(fechaOriginal: String): String {
        // Formato de entrada (el formato de la fecha original)
        val formatoEntrada = SimpleDateFormat("yyyy/MM/dd hh:mm:ss a", Locale.getDefault())

        // Formato de salida (el formato deseado)
        val formatoSalida = SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.getDefault())

        return try {
            // Parsear la fecha de entrada
            val fecha = formatoEntrada.parse(fechaOriginal)

            // Formatear la fecha al nuevo formato
            formatoSalida.format(fecha!!)
        } catch (e: Exception) {
            // Manejar errores, devolver un valor por defecto o relanzar
            "Formato inválido"
        }
    }


    fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val fechaActual = Date()
        return formato.format(fechaActual)
    }

}

