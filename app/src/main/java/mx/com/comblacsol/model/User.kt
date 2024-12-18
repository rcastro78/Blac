package mx.com.comblacsol.model

import java.util.Date

data class User(
    val iduser: Int,
    val provider: String,
    val email: String,
    val nombre: String,
    val segundonombre: String,
    val apellidopaterno: String,
    val apellidomaterno: String,
    val fechanacimiento: String, // Cambié a String porque Date requiere una conversión adicional si usas JSON
    val genero: String,
    val telefono: String,
    val tiposangre: String,
    val nombrecontactoemergencia: String,
    val telcontactoemergencia: String,
    val parentescocontactoemergencia: String,
    val imagensegurovehiculo: String,
    val imagensgmm: String,
    val telreportesiniestro: String,
    val telaseguradoragmm: String,
    val urlFoto: String,
    val password: String,
    val esusuariolocaliza: Boolean = false
)
