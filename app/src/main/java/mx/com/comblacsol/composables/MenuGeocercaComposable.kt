package mx.com.comblacsol.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun ItemGeocercaComposable(forma:Int = 1,
    nombreGeocerca:String = "mi geocerca",
    onItemClick: () -> Unit = {},
    onBasureroClick: () -> Unit = {}
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // Altura ligeramente mayor para mejor espacio visual
            .padding(4.dp) // Espaciado alrededor del card
            .clickable { onItemClick() }, // Hacer todo el card clickeable
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
        ) {

            Column(modifier = Modifier
                .weight(0.15f)
                .fillMaxSize()
                .clickable { onItemClick() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center) // Centra el ícono
                ) {
                    val iconRes = if (forma == 1) {
                        R.drawable.circle // Drawable para el círculo
                    } else {
                        R.drawable.square // Drawable para el cuadrado
                    }

                    Icon(
                        painter = painterResource(id = iconRes),
                        tint = Color.Blue,
                        contentDescription = "Forma",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)// Ajusta el tamaño del ícono
                    )
                }
            }

            Column(modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .clickable { onItemClick() }) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight() // Llena el espacio vertical disponible
                        .wrapContentSize(Alignment.CenterStart) // Centra verticalmente y alinea a la izquierda
                ) {
                    Text(
                        text = nombreGeocerca,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp),
                        color = Color.Black,
                        maxLines = 1,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .clickable { onBasureroClick() }// Centra el ícono
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.basurero),
                        tint = Color.Red,
                        contentDescription = "Forma",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(14.dp)// Ajusta el tamaño del ícono
                    )
                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun GeocercaCircularComposable(
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
                    text = "Geocerca circular",
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
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.circle2),
                contentDescription = "camion"
            )

        }


    }
}

@Composable
@Preview(showBackground = true)
fun GeocercaCuadradaComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Black, Color.Blue), // De celeste a azul
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
                    text = "Geocerca rectangular",
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
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.square),
                contentDescription = "camion"
            )

        }


    }
}

@Composable
@Preview(showBackground = true)
fun GeocercaPentagonalComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Blue, Color.Yellow), // De celeste a azul
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
                    text = "Geocerca pentagonal",
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
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.pentagon),
                contentDescription = "camion"
            )

        }


    }
}

@Composable
@Preview(showBackground = true)
fun GeocercaHexagonalComposable(
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Black, Color.Red), // De celeste a azul
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
                    text = "Geocerca hexagonal",
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
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.hexagon),
                contentDescription = "camion"
            )

        }


    }
}