package mx.com.comblacsol

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.com.comblacsol.composables.VehicleCard
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.MGFMisVehiculosViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MiVehiculoActivity : ComponentActivity() {
    private val mgfMisVehiculosViewModel: MGFMisVehiculosViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mgfMisVehiculosViewModel.getMisVehiculos(3558)

        enableEdgeToEdge()
        setContent {
            MxcomblacsolTheme {
                VehicleCardPreview(mgfMisVehiculosViewModel)
            }
        }
    }



    @Composable
    fun VehicleCardPreview(mgfMisVehiculosViewModel: MGFMisVehiculosViewModel) {
        val vehiculos = mgfMisVehiculosViewModel.misVehiculos.collectAsState()
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)){

            Text(
                modifier = Modifier.padding(12.dp),
                text = "Mis vehículos",
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = gilroy
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp) // Espaciado entre elementos
            ) {
                items(vehiculos.value) {vehiculo ->

                        VehicleCard(
                            imageUrl = vehiculo.rutaimagen,
                            vehicleName = vehiculo.marca + " " +
                                    vehiculo.modelo + " " +
                                    vehiculo.anio,
                            vehicleDetails = vehiculo.version,
                            licensePlate = vehiculo.placas,
                            onVehicleCardClick = {
                                //Al dar click, mostrar dialogo que lleve: VIN, placa (editable), inicio serv, fin service
                                //Log.d("MGFMisVehiculosViewModel", "Response: ${vehiculo.placas} ${vehiculo.vin} ${vehiculo.fechainiciovigencia} ${vehiculo.fechafinvigencia}")
                            }
                        )

                }
            }





            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { /* Acción para botón 1 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home), // Reemplazar con tu ícono
                        contentDescription = "Botón 1",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { /* Acción para botón 2 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home), // Reemplazar con tu ícono
                        contentDescription = "Botón 2",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { /* Acción para botón 3 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home), // Reemplazar con tu ícono
                        contentDescription = "Botón 3",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }



    }



}

