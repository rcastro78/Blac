package mx.com.comblacsol.model

data class GetDevicesRequest(
    val usuario: String,
    val imei: String,
    val fechaIni: String,
    val fechaFin: String
)
