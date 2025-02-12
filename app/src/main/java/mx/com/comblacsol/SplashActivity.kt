package mx.com.comblacsol

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                // Inicia MainActivity después de 3 segundos
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Cierra la SplashActivity para que no vuelva atrás
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var alpha by remember { mutableStateOf(0f) } // Inicialmente invisible

    LaunchedEffect(Unit) {
        val duration = 2500L // 2.5s para la animación
        val totalTime = 3000L // 3s antes de cambiar de pantalla

        // Animación de fade-in
        val startTime = withFrameNanos { it }
        while (withFrameNanos { it } - startTime < duration * 1_000_000) {
            val progress = ((withFrameNanos { it } - startTime) / (duration * 1_000_000f)).coerceIn(0f, 1f)
            alpha = progress
        }

        // Espera los 3s y ejecuta el Intent
        delay(totalTime - duration)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoblac), // Reemplázalo con tu imagen
            contentDescription = "Splash Image",
            modifier = Modifier
                .size(200.dp) // Ajusta el tamaño según sea necesario
                .alpha(alpha)
        )
    }
}
