package mx.com.comblacsol.model

data class SetGeocercaPolyRequest(
    val ancho_traza: String,
    val color_borde: String,
    val color_relleno: String,
    val descripcion_geocerca: String,
    val geocerca_polygon: GeocercaPoligonal,
    val grupos_geocerca: List<String>,
    val name_geocerca: String,
    val opacidad_relleno: String,
    val typeGeocerca: String,
    val usuario: String
)

data class GeocercaPoligonal(
    val coords_polygon: List<String>
)