package mx.com.comblacsol.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212), // Fondo oscuro
    surface = Color(0xFF121212), // Superficie oscura
    onPrimary = Color.White, // Texto en colores primarios
    onSecondary = Color.White, // Texto en colores secundarios
    onBackground = Color.White, // Texto sobre fondo
    onSurface = Color.White // Texto sobre superficie
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFFFF), // Fondo claro
    surface = Color(0xFFFFFFFF), // Superficie clara
    onPrimary = Color.Black, // Texto en colores primarios
    onSecondary = Color.Black, // Texto en colores secundarios
    onBackground = Color.Black, // Texto sobre fondo
    onSurface = Color.Black // Texto sobre superficie
)


@Composable
fun MxcomblacsolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}