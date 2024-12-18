package mx.com.comblacsol

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.com.comblacsol.composables.GeocercaCircularComposable
import mx.com.comblacsol.composables.GeocercaCuadradaComposable
import mx.com.comblacsol.composables.GeocercaHexagonalComposable
import mx.com.comblacsol.composables.GeocercaPentagonalComposable
import mx.com.comblacsol.composables.LocalizaMiVehiculoComposable
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy

class MenuGeocercaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MxcomblacsolTheme {
                MenuGeocercaScreen()
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun MenuGeocercaScreen() {
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
                    text = "Selecciona la forma de tu geocerca",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = gilroy,
                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(4.dp)

                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(0.9f)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GeocercaCircularComposable(onItemClick = {
                        Intent(this@MenuGeocercaActivity, GeocercaActivity::class.java).also {
                            it.putExtra("forma",1)
                            startActivity(it)
                        }
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GeocercaCuadradaComposable(onItemClick = {
                        Intent(this@MenuGeocercaActivity, GeocercaActivity::class.java).also {
                            it.putExtra("forma",4)
                            startActivity(it)
                        }
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GeocercaPentagonalComposable(onItemClick = {
                        Intent(this@MenuGeocercaActivity, GeocercaActivity::class.java).also {
                            it.putExtra("forma",5)
                            startActivity(it)
                        }
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GeocercaHexagonalComposable(onItemClick = {
                        Intent(this@MenuGeocercaActivity, GeocercaActivity::class.java).also {
                            it.putExtra("forma",6)
                            startActivity(it)
                        }
                    })

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                   Intent(this@MenuGeocercaActivity,PrincipalActivity::class.java).also {
                                       startActivity(it)
                                   }
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = "Home",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                }

            }



        }
    }
}

