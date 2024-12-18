package mx.com.comblacsol.model

import com.google.android.gms.maps.model.LatLng

data class Geocerca(
    val nombreGeocerca: String,
    val colorBorde: String,
    val colorRelleno: String,
    val anchoTraza: String,
    val opacidadRelleno: String,
    val coordsCircle: String,
    val radioCircle: String,
    val coordsPolygon: List<String>,

    // Tipo Polyline
    val rutaBuffer: String,
    val direccionSuperiorP1: String,
    val direccionInferiorP1: String,
    val direccionSuperiorP2: String,
    val direccionInferiorP2: String,
    val coordsPolyline: List<String>,

    // UI helper properties
    val tipoGeocerca: String,
    val icono: String,
    val editable: Boolean,
    val draggable: Boolean,
    val lat: Double,
    val lng: Double,

    // Circle helper properties
    val latCircle: Double,
    val lngCircle: Double,
    val radioCircleNumber: Double, // Cambiado a evitar confusión con `radioCircle` (String)

    // Polygon helper properties
    val polygonVertices: List<LatLng>, // LatLng equivalente en Kotlin

    // Polyline helper properties
    val polylineVertices: List<LatLng>,

    val anchoTrazaNumber: Double,
    val opacidadRellenoNumber: Double
)

data class GeocercaResponse(
    val geocercas: List<Geocerca>
)

data class GetGeofencesRequest(
    val usuario: String,
    val typeGeocerca: String
)

data class SetGeofenceRequest(
    val usuario: String,
    val typeGeocerca: String,
    val nameGeocerca: String,
    val descripcionGeocerca: String,
    val colorBorde: String,
    val colorRelleno: String,
    val anchoTraza: String,
    val opacidadRelleno: String,
    val gruposGeocerca: List<String>,
    val geocercaCircle: GeocercaCircle,
    val geocercaPolygon: GeocercaPolygon,
    val geocercaPolyline: GeocercaPolyline
)

data class GeocercaCircle(
    val coordsCircle: String,
    val radioCircle: String
)

data class GeocercaPolygon(
    val coordsPolygon: List<String>
)

data class GeocercaPolyline(
    val rutaBuffer: String,
    val distBetweenPoints: List<String>,
    val direccionDistP1: List<String>,
    val direccionDistP2: List<String>,
    val coordsPolyline: List<String>
)





data class EliminarGeocercaRequest(
    val usuario: String,
    val typeGeocerca: String,
    val nameGeocerca: String
)

data class EliminarGeocercaResponse(
    val responseCode: String,
    val message: String
)
