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
import androidx.compose.foundation.lazy.LazyColumn
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
                    polyGeofenceViewModel)
            }
        }
    }


    @Composable
    fun MisLimitesVirtualesScreen(circularGeofenceViewModel: CircularGeofenceViewModel,
                                  polyGeofenceViewModel: PolygonalGeofenceViewModel,
                                  ){
        val geocercasCirculares by circularGeofenceViewModel.geocercas.collectAsState()
        val geocercasPoligonales by polyGeofenceViewModel.geocercas.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
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
                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(4.dp)

                )
            }
            LazyColumn( modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(12.dp)){

                items(geocercasCirculares.size){it->
                    Spacer(modifier = Modifier.height(12.dp))
                    ItemGeocercaComposable(1,geocercasCirculares[it].nombre_geocerca,
                        onItemClick = {
                            Intent(this@MisGeocercasActivity, GeocercaActivity::class.java).also { intent->
                                intent.putExtra("forma",1)
                                intent.putExtra("modo",1)
                                //Datos para la geocerca
                                intent.putExtra("name",geocercasCirculares[it].nombre_geocerca)
                                intent.putExtra("colorBorde",geocercasCirculares[it].color_borde)
                                intent.putExtra("colorRelleno",geocercasCirculares[it].color_relleno)
                                intent.putExtra("anchoTraza",geocercasCirculares[it].ancho_traza)
                                intent.putExtra("radio",geocercasCirculares[it].radio_circle)
                                intent.putExtra("centro",geocercasCirculares[it].coords_circle)

                                startActivity(intent)
                            }
                        },
                        onBasureroClick = {
                            val eliminarGeocercaRequest = EliminarGeocercaRequest(usuario,"CIRCLE",geocercasCirculares[it].nombre_geocerca)
                            circularGeofenceViewModel.deleteGeocercas(token,eliminarGeocercaRequest, onSuccess = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(
                                        this@MisGeocercasActivity,
                                        "Geocerca eliminada exitosamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, onError = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(
                                        this@MisGeocercasActivity,
                                        "Error al eliminar la geocerca",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })

                            val geo = GeocercaCircleRequest(usuario)
                            val poly = GeocercaPolygonRequest(usuario)
                            circularGeofenceViewModel.getCircleGeofence(token,geo, onError = {
                                Log.e("ERROR",it)
                            })
                            polyGeofenceViewModel.getPolygonalGeofence(token,poly, onError = {
                                Log.e("ERROR",it)
                            })

                        })
                }
                items(geocercasPoligonales.size) {it->
                    Spacer(modifier = Modifier.height(12.dp))
                    ItemGeocercaComposable(2,geocercasPoligonales[it].nombre_geocerca,
                        onItemClick = {
                            Intent(this@MisGeocercasActivity, GeocercaActivity::class.java).also {intent->
                                intent.putExtra("forma",2)
                                intent.putExtra("modo",1)
                                intent.putExtra("name",geocercasPoligonales[it].nombre_geocerca)
                                intent.putExtra("colorBorde",geocercasPoligonales[it].color_borde)
                                intent.putExtra("colorRelleno",geocercasPoligonales[it].color_relleno)
                                intent.putExtra("anchoTraza",geocercasPoligonales[it].ancho_traza)
                                val coordsPolygon: List<String> = geocercasPoligonales[it].coords_polygon
                                val coordsPolygonArrayList = ArrayList(coordsPolygon)
                                intent.putStringArrayListExtra("puntos",coordsPolygonArrayList)



                                startActivity(intent)
                            }
                        },
                        onBasureroClick = {})
                }




            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 24.dp, end = 24.dp), // Establece la altura de la fila
                    horizontalArrangement = Arrangement.SpaceBetween, // Distribuye los íconos a los extremos
                    verticalAlignment = Alignment.CenterVertically // Centra los íconos verticalmente
                ) {
                    // Icono Home
                    IconButton(
                        onClick = {
                            Intent(this@MisGeocercasActivity,PrincipalActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier.size(48.dp) // Tamaño del icono
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.home), // Reemplaza con tu recurso home
                            contentDescription = "Home",
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // Icono Add
                    IconButton(
                        onClick = {
                            Intent(this@MisGeocercasActivity,MenuGeocercaActivity::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier.size(48.dp) // Tamaño del icono
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add), // Reemplaza con tu recurso add
                            contentDescription = "Add",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }

}

