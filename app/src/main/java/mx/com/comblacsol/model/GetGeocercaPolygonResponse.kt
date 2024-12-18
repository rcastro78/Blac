package mx.com.comblacsol.model

data class GetGeocercaPolygonResponse(
    val geocercas: List<GeocercaPolygonItem>
)

data class GeocercaPolygonItem(
    val ancho_traza: String,
    val color_borde: String,
    val color_relleno: String,
    val coords_polygon: List<String>,
    val nombre_geocerca: String,
    val opacidad_relleno: String
)

data class GeocercaPolygonRequest(
    val usuario:String,
    val typeGeocerca:String="POLYGON"
)