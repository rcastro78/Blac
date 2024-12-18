package mx.com.comblacsol

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.com.comblacsol.ui.theme.MxcomblacsolTheme
import mx.com.comblacsol.util.gilroy

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MxcomblacsolTheme {
               RegisterScreen()
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun RegisterScreen() {
        var username by remember {
            mutableStateOf(
                ""
            )
        }
        var password by remember {
            mutableStateOf("")
        }
        var passwordVisible by remember { mutableStateOf(false) }
        var repPassword by remember {
            mutableStateOf("")
        }
        var repPasswordVisible by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.registeravatar),
                    contentDescription = "MG Finder Logo",
                    modifier = Modifier
                        .width(92.dp)
                        .height(92.dp)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header Text
            Text(
                text = "Crea tu cuenta",
                fontSize = 20.sp,
                fontFamily = gilroy,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
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
            Spacer(modifier = Modifier.height(16.dp))
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
            TextField(
                value = repPassword,
                onValueChange = { repPassword = it },
                label = {
                    Text(
                        "Verificar contraseña",
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
                            imageVector = if (repPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (repPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
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
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.black),  // Color de fondo del botón
                    contentColor = Color.White     // Color del texto o ícono del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            {
                Text(
                    text = "Continuar",
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Normal
                )
            }



            Button(
                onClick = {
                    Intent(this@RegisterActivity, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.white),  // Color de fondo del botón
                    contentColor = Color.Black     // Color del texto o ícono del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            {
                Text(
                    text = "Ya tienes cuenta? Iniciar sesión",
                    fontFamily = gilroy,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                TermsAndPrivacyText()
            }

            Spacer(modifier = Modifier.height(48.dp))
        }




    }



    @Composable
    fun TermsAndPrivacyText() {
        val context = LocalContext.current

        // Cargar la fuente personalizada
        val customFont = gilroy

        // Crear el texto con partes clickeables
        val annotatedString = buildAnnotatedString {
            // Aplicar la fuente personalizada al texto normal
            withStyle(style = SpanStyle(color = if(isSystemInDarkTheme()) Color.White else Color.Black,fontFamily = customFont)) {
                append("Al registrarte, aceptas los ")
            }

            // Términos y condiciones clickeable
            pushStringAnnotation(tag = "terms", annotation = "terms")
            withStyle(style = SpanStyle(color = Color.Blue, fontFamily = customFont)) {
                append("Términos de servicio")
            }
            pop()

            withStyle(style = SpanStyle(color = if(isSystemInDarkTheme()) Color.White else Color.Black,fontFamily = customFont)) {
                append(" y la ")
            }

            // Política de privacidad clickeable
            pushStringAnnotation(tag = "privacy", annotation = "privacy")
            withStyle(style = SpanStyle(color = Color.Blue, fontFamily = customFont)) {
                append("Política de privacidad")
            }
            pop()

            append(".")
        }

        // Crear un ClickableText con el texto anotado
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                // Detectar qué parte del texto fue clickeada
                annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                    .firstOrNull()?.let {
                        // Redirigir a los Términos y condiciones
                        openTermsAndConditions(context)
                    }

                annotatedString.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                    .firstOrNull()?.let {
                        // Redirigir a la Política de privacidad
                        openPrivacyPolicy(context)
                    }
            }
        )

    }

    fun openTermsAndConditions(context: Context) {
        // Aquí puedes redirigir a la pantalla de Términos y condiciones, o abrir un URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blac.com.mx/avisodeprivacidad/"))
        context.startActivity(intent)
    }

    fun openPrivacyPolicy(context: Context) {
        // Aquí puedes redirigir a la pantalla de Política de privacidad, o abrir un URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blac.com.mx/avisodeprivacidad/"))
        context.startActivity(intent)
    }


}

