package mx.com.comblacsol

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy
import mx.com.comblacsol.viewmodel.LoginViewModel
import mx.com.comblacsol.viewmodel.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }



        setContent {
            MxcomblacsolTheme {
               LoginScreen()
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun LoginScreen() {

        var username by remember {
            mutableStateOf(
                ""
            )
        }
        var password by remember {
            mutableStateOf("")
        }
        var passwordVisible by remember { mutableStateOf(false) }


        val gradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFffffff),
                colorResource(id = R.color.black),


                 // Color de inicio
                // Color de fin
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Image(
                    painter = painterResource(id = R.drawable.loginavatar),
                    contentDescription = "MG Finder Logo",
                    modifier = Modifier
                        .width(92.dp)
                        .height(92.dp)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header Text
            Text(
                text = "Inicia Sesión",
                fontSize = 24.sp,
                fontFamily = gilroy,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username TextField
            TextField(
                value = username,
                onValueChange = { username = it },
                label = {
                    Text(
                        "Usuario",
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Normal
                    )
                }, // Aplicar la fuente a la etiqueta
                textStyle = TextStyle(fontFamily = gilroy, fontWeight = FontWeight.Normal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle, // Icono de usuario de Material Icons
                        contentDescription = "Usuario",
                        tint = colorResource(id = R.color.black)
                    )
                },
                colors = TextFieldDefaults.colors(
                    if (isSystemInDarkTheme()) Color.White else Color.Black, // Fondo transparente
                    Color.Black, // Texto en negro
                    focusedIndicatorColor = Color.Transparent, // Sin línea inferior al enfocarse
                    unfocusedIndicatorColor = Color.Transparent, // Sin línea inferior sin enfocar
                    disabledIndicatorColor = Color.Transparent // Sin línea inferior cuando está deshabilitado
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Password TextField
            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Contraseña",
                        fontFamily = gilroy,
                        fontWeight = FontWeight.Normal
                    )
                }, // Aplicar la fuente a la etiqueta
                textStyle = TextStyle(fontFamily = gilroy, fontWeight = FontWeight.Normal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock, // Icono de candado
                        contentDescription = "Usuario",
                        tint = colorResource(id = R.color.black)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = colorResource(id = R.color.black)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Alternar entre texto visible y oculto
                colors = TextFieldDefaults.colors(
                    if (isSystemInDarkTheme()) Color.White else Color.Black, // Fondo transparente
                    Color.Black, // Texto en negro
                    focusedIndicatorColor = Color.Transparent, // Sin línea inferior al enfocarse
                    unfocusedIndicatorColor = Color.Transparent, // Sin línea inferior sin enfocar
                    disabledIndicatorColor = Color.Transparent // Sin línea inferior cuando está deshabilitado
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        Log.d("LoginScreen", "Username: $username, Password: $password")
                        loginViewModel.login(username,password, onSuccess = {
                            val token = it.token
                            Log.d("LoginScreen", "$token")
                            Intent(applicationContext, PrincipalActivity::class.java).also {
                                sharedPreferences.edit().putString("username", username).apply()
                                sharedPreferences.edit().putString("password", password).apply()
                                sharedPreferences.edit().putString("token", token).apply()
                                sharedPreferences.edit().putLong("token_expiration", System.currentTimeMillis() + 10 * 60 * 1000).apply() // 10 minutos
                                startActivity(it)
                                finish()
                            }
                        },
                            onError = {
                                CoroutineScope(Dispatchers.Main).launch{
                                    Toast.makeText(applicationContext, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                                }

                            })
                    }else{
                        Toast.makeText(applicationContext, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.black),  // Color de fondo del botón
                    contentColor = Color.White     // Color del texto o ícono del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)

            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Normal)
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                Intent(this@MainActivity, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.white),  // Color de fondo del botón
                    contentColor = Color.Black     // Color del texto o ícono del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)) {
                Text(
                    text = "Registrarse",
                    fontSize = 18.sp,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Normal)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.white),  // Color de fondo del botón
                    contentColor = Color.Black     // Color del texto o ícono del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)) {
                Text(
                    text = "Olvidé mi contraseña",
                    fontSize = 18.sp,
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Normal)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Otros elementos del Row (pueden ir aquí si es necesario)

                Spacer(modifier = Modifier.weight(1f)) // Empuja el FAB a la derecha

                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .width(64.dp)
                        .padding(end = 16.dp) // Espacio a la derecha
                        .clip(CircleShape) // Hace que el FAB sea circular
                        .background(Color.Transparent) // Fondo transparente
                        .clickable {
                            openWhatsApp("+525510481042")
                        } // Acción al hacer clic
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_whatsapp), // Aquí pones tu drawable
                        contentDescription = "Imagen",
                        modifier = Modifier.fillMaxSize() // Asegura que la imagen ocupe todo el espacio circular
                    )
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
        }


    }


    fun openWhatsApp(phoneNumber: String) {
        val url = "https://wa.me/$phoneNumber" // URL para abrir WhatsApp con el número
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        try {
            ContextCompat.startActivity(this, intent, null)
        } catch (e: Exception) {
            // Manejo de errores si WhatsApp no está instalado
            Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
        }
    }



}

