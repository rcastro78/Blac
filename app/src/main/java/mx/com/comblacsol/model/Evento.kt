package mx.com.comblacsol.model

data class Evento(
    val accion: String,
    val address: String,
    val altitude: String,
    val azimuth: String,
    val backupBatteryPer: String,
    val backup_volt: String,
    val bloqueo_por_morosidad: String,
    val claveAmago: String,
    val countNumber: String,
    val count_spaces: Int,
    val descrip_code: String,
    val deviceID: String,
    val deviceID_asignado: String,
    val event: String,
    val graphic_event: String,
    val group_device: String,
    val last_report_dif_day: String,
    val last_report_dif_hora: String,
    val last_report_dif_minute: String,
    val latitude: String,
    val listDevices: String,
    val listGroups: String,
    val listTypeDevice: String,
    val localDate: String,
    val longitude: String,
    val mileage: String,
    val movilAlOperador: String,
    val name_hdw_cmd: String,
    val nombreOperador: String,
    val nombre_cliente: String,
    val nombre_conductor: String,
    val nombre_geocerca: String,
    val nombre_vehiculo: String,
    val odometro: String,
    val path_img_cmd: String,
    val pwr_volt: String,
    val speed: String,
    val status_alert: String,
    val status_vehicule: String,
    val total_minutes: String,
    val type_device: String,
    val userLocalTime: String,
    val userLocalTimeFormat: String,
    val userLocalTimeParent: String,
    val usuario: String
)

data class EventosResponse(
    val eventos: List<Evento> // El array de eventos
)

data class GetVehicleRequest(
    val usuario: String,
    val fechaIni: String,
    val fechaFin: String

)

data class GetHistoVehicleRequest(
    val usuario: String,
    val fechaIni: String,
    val fechaFin: String,
    val imei:String

)