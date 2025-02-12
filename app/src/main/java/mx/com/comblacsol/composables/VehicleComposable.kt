package mx.com.comblacsol.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation


import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import mx.com.comblacsol.util.gilroy


@Composable
@Preview(showBackground = true)
fun VehicleCard(
    imageUrl: String?="",
    vehicleName: String?="Honda CRV 2018",
    vehicleDetails: String?="Touring 1.5 Turbo",
    licensePlate: String?="MKG141A",
    onVehicleCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(16.dp),
        onClick = onVehicleCardClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del vehículo
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .width(280.dp)
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del vehículo
            Text(
                text = vehicleName!!,
                style = MaterialTheme.typography.h6,
                fontFamily = gilroy,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Detalles del vehículo
            Text(
                text = vehicleDetails!!,
                style = MaterialTheme.typography.caption,
                fontFamily = gilroy,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Placa del vehículo
            Text(
                text = "Placas: $licensePlate",
                style = MaterialTheme.typography.body2,
                fontFamily = gilroy,
                textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun StatusCard(
    code: String?="TR-304",
    dateTime: String?="25/Nov/2024 17:15",
    status: String?="Detenido",
    location: String?="Supercarretera México - Querétaro",
    speed: String?="0.0 Km/h",
    onStatusCardClick: () -> Unit = {}
) {
    var st:String=""
    if(status == "STOPPED"){
        st = "Detenido"
    }

    if(status == "MOVING"){
        st = speed!!+" Km/h"
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors =  if(isSystemInDarkTheme()) CardColors(Color.White,Color.White, Color.White, Color.White)
        else
            CardColors(Color.White,Color.Black, Color.White, Color.White),
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onStatusCardClick() }

    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
        ) {
            // Línea superior
            Text(
                text = code!!,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = gilroy,
                    color = if(isSystemInDarkTheme()) Color.Black else Color.Blue
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Segunda línea: Fecha y estado
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dateTime!!,
                    fontFamily = gilroy,
                    style = MaterialTheme.typography.body2.copy(
                        color = if(isSystemInDarkTheme()) Color.Black else Color.Gray
                    )
                )
                Text(
                    text = st,
                    fontFamily = gilroy,
                    style = MaterialTheme.typography.body2.copy(
                        color = if(isSystemInDarkTheme()) Color.Black else Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tercera línea: Ubicación
            Text(
                text = location!!,
                style = MaterialTheme.typography.body2.copy(
                    color = if(isSystemInDarkTheme()) Color.Black else Color.Gray
                ),
                fontFamily = gilroy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    
}
