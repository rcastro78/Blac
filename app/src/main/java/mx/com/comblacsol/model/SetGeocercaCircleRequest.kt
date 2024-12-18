package mx.com.comblacsol.model

data class SetGeocercaCircleRequest(
    val ancho_traza: String,
    val color_borde: String,
    val color_relleno: String,
    val descripcion_geocerca: String,
    val geocerca_circle: GeoCircle,
    val grupos_geocerca: List<String>,
    val name_geocerca: String,
    val opacidad_relleno: String,
    val typeGeocerca: String,
    val usuario: String
)

data class GeoCircle(
    val coords_circle: String,
    val radio_circle: String
)