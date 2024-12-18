package mx.com.comblacsol.model

class MGFinderMisVehiculosResponse : ArrayList<MGFinderMisVehiculosResponseItem>()

data class MGFinderMisVehiculosResponseItem(
    val anio: String,
    val codigoasign: String,
    val color: String,
    val fechafinvigencia: String,
    val fechainiciovigencia: String,
    val fechainstalacion: String,
    val idcliente: Int,
    val imei: String,
    val marca: String,
    val modelo: String,
    val placas: String,
    val rutaimagen: String,
    val version: String,
    val vin: String
)