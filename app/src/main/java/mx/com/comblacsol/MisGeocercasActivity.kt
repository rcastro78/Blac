package mx.com.comblacsol

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.comblacsol.composables.ItemGeocercaComposable
import mx.com.comblacsol.model.EliminarGeocercaRequest
import mx.com.comblacsol.model.GeocercaCircleRequest
import mx.com.comblacsol.model.GeocercaPolygonRequest
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.CircularGeofenceViewModel
import mx.com.comblacsol.viewmodel.PolygonalGeofenceViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MisGeocercasActivity : ComponentActivity() {
    private val circularGeofenceViewModel: CircularGeofenceViewModel by viewModel()
    private val polyGeofenceViewModel: PolygonalGeofenceViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    var usuario by mutableStateOf("")
    var token by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        token = sharedPreferences.getString("token", "")!!
        usuario = sharedPreferences.getString("username", "")!!
        val geo = GeocercaCircleRequest(usuario)
        val poly = GeocercaPolygonRequest(usuario)
        circularGeofenceViewModel.getCircleGeofence(token,geo, onError = {
            Log.e("ERROR",it)
        })
        polyGeofenceViewModel.getPolygonalGeofence(token,poly, onError = {
            Log.e("ERROR",it)
        })

        setContent {
            MxcomblacsolTheme {
                MisLimitesVirtualesScreen(circularGeofenceViewModel,
                    polyGeofenceViewModel,token, usuario)
            }
        }
    }


    @Composable
    fun MisLimitesVirtualesScreen(
        circularGeofenceViewModel: CircularGeofenceViewModel,
        polyGeofenceViewModel: PolygonalGeofenceViewModel,
        token: String,
        usuario: String
    ) {
        // Obtener las geocercas desde el ViewModel
        val geocercasCirculares by circularGeofenceViewModel.geocercas.collectAsState()
        val geocercasPoligonales by polyGeofenceViewModel.geocercas.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 48.dp, end = 12.dp, start = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            // Título
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f)
            ) {
                Text(
                    text = "Mis límites virtuales",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = gilroy,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                )
            }

            // Mostrar las geocercas circulares
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(12.dp)
            ) {
                items(geocercasCirculares.size) { index ->
                    Spacer(modifier = Modifier.height(12.dp))
                    ItemGeocercaComposable(
                        1,
                        geocercasCirculares[index].nombre_geocerca,
                        onItemClick = {
                            // Acciones cuando se hace clic en una geocerca
                            Intent(this@MisGeocercasActivity, GeocercaActivity::class.java).also { intent ->
                                intent.putExtra("forma", 1)
                                intent.putExtra("modo", 1)
                                intent.putExtra("name", geocercasCirculares[index].nombre_geocerca)
                                intent.putExtra("colorBorde", geocercasCirculares[index].color_borde)
                                intent.putExtra("colorRelleno", geocercasCirculares[index].color_relleno)
                                intent.putExtra("anchoTraza", geocercasCirculares[index].ancho_traza)
                                intent.putExtra("radio", geocercasCirculares[index].radio_circle)
                                intent.putExtra("centro", geocercasCirculares[index].coords_circle)
                                startActivity(intent)
                            }
                        },
                        onBasureroClick = {
                            // Eliminar la geocerca
                            val eliminarGeocercaRequest = EliminarGeocercaRequest(
                                usuario,
                                "CIRCLE",
                                geocercasCirculares[index].nombre_geocerca
                            )

                            circularGeofenceViewModel.deleteGeocercas(
                                token,
                                eliminarGeocercaRequest,
                                onSuccess = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            this@MisGeocercasActivity,
                                            "Geocerca eliminada exitosamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    // Después de eliminar, recargar las geocercas
                                    val geo = GeocercaCircleRequest(usuario)
                                    circularGeofenceViewModel.getCircleGeofence(token, geo, onError = {
                                        Log.e("ERROR", it)
                                    })
                                },
                                onError = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            this@MisGeocercasActivity,
                                            "Error al eliminar la geocerca",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                            // Actualizar las geocercas poligonales también
                            val poly = GeocercaPolygonRequest(usuario)
                            polyGeofenceViewModel.getPolygonalGeofence(token, poly, onError = {
                                Log.e("ERROR", it)
                            })
                        }
                    )
                }

                // Mostrar las geocercas poligonales
                items(geocercasPoligonales.size) { index ->
                    Spacer(modifier = Modifier.height(12.dp))
                    ItemGeocercaComposable(
                        2,
                        geocercasPoligonales[index].nombre_geocerca,
                        onItemClick = {
                            // Acciones cuando se hace clic en una geocerca poligonal
                            Intent(this@MisGeocercasActivity, GeocercaActivity::class.java).also { intent ->
                                intent.putExtra("forma", 2)
                                intent.putExtra("modo", 1)
                                intent.putExtra("name", geocercasPoligonales[index].nombre_geocerca)
                                intent.putExtra("colorBorde", geocercasPoligonales[index].color_borde)
                                intent.putExtra("colorRelleno", geocercasPoligonales[index].color_relleno)
                                intent.putExtra("anchoTraza", geocercasPoligonales[index].ancho_traza)
                                val coordsPolygon: List<String> = geocercasPoligonales[index].coords_polygon
                                val coordsPolygonArrayList = ArrayList(coordsPolygon)
                                intent.putStringArrayListExtra("puntos", coordsPolygonArrayList)
                                startActivity(intent)
                            }
                        },
                        onBasureroClick = {
                            // Eliminar geocerca poligonal
                            val eliminarGeocercaRequest = EliminarGeocercaRequest(
                                usuario,
                                "POLYGON",
                                geocercasPoligonales[index].nombre_geocerca
                            )

                            circularGeofenceViewModel.deleteGeocercas(
                                token,
                                eliminarGeocercaRequest,
                                onSuccess = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            this@MisGeocercasActivity,
                                            "Geocerca eliminada exitosamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    // Después de eliminar, recargar las geocercas
                                    val poly = GeocercaPolygonRequest(usuario)
                                    polyGeofenceViewModel.getPolygonalGeofence(token,poly, onError = {
                                        Log.e("ERROR",it)
                                    })
                                    
                                },
                                onError = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            this@MisGeocercasActivity,
                                            "Error al eliminar la geocerca",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                            // Actualizar las geocercas poligonales también
                            val poly = GeocercaPolygonRequest(usuario)
                            polyGeofenceViewModel.getPolygonalGeofence(token, poly, onError = {
                                Log.e("ERROR", it)
                            })
                        }
                    )
                }
            }

            // Parte inferior (botones de navegación)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
            ) {
                Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 56.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.End

                ) {
                    // Botón Home
                    FloatingActionButton(onClick = {
                        Intent(this@MisGeocercasActivity, PrincipalActivity::class.java).also {
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
                    Spacer(modifier = Modifier.width(16.dp))
                    // Botón Add
                    FloatingActionButton(
                        onClick = {
                        Intent(this@MisGeocercasActivity, MenuGeocercaActivity::class.java).also {
                            startActivity(it)
                        }
                    }, modifier = Modifier, // Sin `size(48.dp)` para que use el tamaño por defecto (56.dp)
                        shape = CircleShape,
                        containerColor = Color.Black) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Add",
                            modifier = Modifier.size(48.dp)
                                .padding(12.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

