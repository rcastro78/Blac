package mx.com.comblacsol

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import mx.com.comblacsol.composables.HistorialComposable
import mx.com.comblacsol.composables.LimitesVirtualesComposable
import mx.com.comblacsol.composables.LocalizaMiVehiculoComposable
import mx.com.comblacsol.composables.MisVehiculosComposable
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrincipalActivity : ComponentActivity() {
    val sharedPreferences: SharedPreferences by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = sharedPreferences.getString("username", "")
        val token = sharedPreferences.getString("token", "")
        enableEdgeToEdge()
        setContent {
            MxcomblacsolTheme {
                PrincipalScreen("Hola, $username!",token!!)
            }
        }


    }
    @Composable
    @Preview(showBackground = true)
    fun PrincipalScreen(username: String = "", token: String="") {
        val userViewModel: UserViewModel by viewModel()
        val userBitmap by userViewModel.userBitmap // Obtenemos el Bitmap desde el ViewModel
        // Usamos LaunchedEffect para disparar la descarga de la imagen cuando el token cambie
        LaunchedEffect(token) {
            userViewModel.downloadImage(token, onSuccess = { _ ->
                // Este callback no es necesario aquí porque el Bitmap ya se actualizó
            }, onError = {
                Log.e("UserViewModel", "Error al descargar imagen: $it")
            })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(12.dp)
        ) {
            Spacer(modifier = Modifier.height(92.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.125f)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = username,
                        modifier = Modifier.padding(12.dp),
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    userBitmap?.let {
                        // Si el bitmap está disponible, lo mostramos
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .height(64.dp)
                                .width(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        // Si no hay bitmap, muestra una imagen por defecto o placeholder
                        Image(
                            painter = painterResource(id = R.drawable.loginavatar),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .height(64.dp)
                                .width(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.125f)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)){
                    Image(
                        painter = painterResource(id = R.drawable.estrellas),
                        contentDescription = "Estrellas",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        contentScale = ContentScale.None
                    )
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)){

                    Text(
                        text = "Conecta con tus vehículos y consulta su posición e historial de ruta cuando quieras.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = gilroy,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(4.dp)

                    )

                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(0.75f)
            ) {
                item {
                    LocalizaMiVehiculoComposable(onItemClick = {
                        Intent(this@PrincipalActivity, MisUnidadesActivity::class.java).also {
                            startActivity(it)
                        }
                    })
                }

                /*item {
                    Spacer(modifier = Modifier.height(12.dp))
                    LimitesVirtualesComposable(onItemClick = {
                        Intent(this@PrincipalActivity, MisGeocercasActivity::class.java).also {
                            startActivity(it)
                        }
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    HistorialComposable(onItemClick = {
                        Intent(this@PrincipalActivity, HistorialMovimientoActivity::class.java).also {
                            startActivity(it)
                        }
                    })
                }*/
            }
        }
    }


}




