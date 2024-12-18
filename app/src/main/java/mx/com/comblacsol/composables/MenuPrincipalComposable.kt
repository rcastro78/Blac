package mx.com.comblacsol.composables

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.com.comblacsol.R
import mx.com.comblacsol.util.gilroy




@Composable
@Preview(showBackground = true)
fun LocalizaMiVehiculoComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Cyan, Color.Blue), // De celeste a azul
                    start = Offset(0f, 0f), // Esquina superior izquierda
                    end = Offset.Infinite // Esquina inferior derecha
                )
            )
            .clickable { onItemClick() }
    ) {

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.weight(0.3f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            )
            {
                Text(
                    text = "Localiza mi vehículo",
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    color = Color.White,
                    maxLines = 1,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(modifier = Modifier.weight(0.7f)) {

            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.home_locate),
                contentDescription = "camion"
            )

        }


    }
}


@Composable
@Preview(showBackground = true)
fun MisVehiculosComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Blue, Color.Black), // De celeste a azul
                    start = Offset(0f, 0f), // Esquina superior izquierda
                    end = Offset.Infinite // Esquina inferior derecha
                )
            )
            .clickable { onItemClick() }
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.weight(0.3f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            )
            {
                Text(
                    text = "Mis Vehículos",
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    color = Color.White,
                    maxLines = 1,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(modifier = Modifier.weight(0.7f)) {

            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.car),
                contentDescription = "camion"
            )

        }


    }
}


@Composable
@Preview(showBackground = true)
fun HistorialComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Black, Color.Cyan), // De celeste a azul
                    start = Offset(0f, 0f), // Esquina superior izquierda
                    end = Offset.Infinite // Esquina inferior derecha
                )
            )
            .clickable { onItemClick() }
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.weight(0.3f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            )
            {
                Text(
                    text = "Historial",
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    color = Color.White,
                    maxLines = 1,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(modifier = Modifier.weight(0.7f)) {

            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.clock),
                contentDescription = "camion"
            )

        }


    }

}

@Composable
@Preview(showBackground = true)
fun LimitesVirtualesComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Red, Color.Blue), // De celeste a azul
                    start = Offset(0.5f, -0.5f), // Esquina superior izquierda
                    end = Offset.Infinite // Esquina inferior derecha
                )
            )
            .clickable { onItemClick() }
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.weight(0.3f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            )
            {
                Text(
                    text = "Límites Virtuales",
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    color = Color.White,
                    maxLines = 1,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(modifier = Modifier.weight(0.7f)) {

            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.location),
                contentDescription = "camion"
            )

        }


    }
}
