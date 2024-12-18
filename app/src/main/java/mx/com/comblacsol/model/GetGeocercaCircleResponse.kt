package mx.com.comblacsol.model

data class GetGeocercaCircleResponse(
    val geocercas: List<GeocercaCircleItem>
)

data class GeocercaCircleItem(
    val ancho_traza: String,
    val color_borde: String,
    val color_relleno: String,
    val coords_circle: String,
    val nombre_geocerca: String,
    val opacidad_relleno: String,
    val radio_circle: String
)

data class GeocercaCircleRequest(
    val usuario:String,
    val typeGeocerca:String="CIRCLE"
)
