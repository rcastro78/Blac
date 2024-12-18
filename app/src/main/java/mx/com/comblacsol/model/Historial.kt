package mx.com.comblacsol.model

data class HistorialResponse(
    val eventosHistory: List<EventosHistory>,
    val grupo_dispositivos: String
)

data class EventosHistory(
    val address: String,
    val azimuth: String,
    val deviceID: String,
    val event: String,
    val latitude: String,
    val longitude: String,
    val nombre_vehiculo: String,
    val speed: String,
    val userLocalTime: String,
    val userLocalTimeFormat: String,
    val usuario: String
)