package mx.com.comblacsol.model

import java.util.Date

data class Contacto(
    val id: Int,
    val idUsuarioServicio: Int,
    val tipoContacto: String,
    val nombre: String,
    val segundoNombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val celular: String,
    val telCasa: String,
    val telOficina: String,
    val correo: String,
    val parentesco: String,
    val genero: String,
    val fechanacimiento: Date,
    val username: String,
    val direccion: String,
    val status: String,

    // UI helper properties
    val nombreCompleto: String
)
