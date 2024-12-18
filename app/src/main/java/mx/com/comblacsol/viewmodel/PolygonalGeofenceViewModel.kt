package mx.com.comblacsol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.GeocercaPolygonItem
import mx.com.comblacsol.model.GeocercaPolygonRequest
import mx.com.comblacsol.model.SetGeocercaPolyRequest
import mx.com.comblacsol.networking.IBlacAPI
import retrofit2.awaitResponse

class PolygonalGeofenceViewModel(private val iBlacAPI: IBlacAPI) : ViewModel() {
    //Set geocerca circular
    fun setPolyGeofence(token: String, setGeocercaPolyRequest: SetGeocercaPolyRequest,
                          onSuccess:(String) -> Unit, onError:(String) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = iBlacAPI.setPolyGeocerca(token, setGeocercaPolyRequest).awaitResponse()
                if(response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it)
                    }
                }else{
                    onError(response.message())
                }
            }catch (e: Exception){
                onError("Error en la petición ${e.message}")

            }
        }
    }
    /*
    *  private val _historial = MutableStateFlow<List<EventosHistory>>(emptyList())
    val historial: StateFlow<List<EventosHistory>> = _historial
    * */
    //Get geocerca
    private val _geocercas = MutableStateFlow<List<GeocercaPolygonItem>>(emptyList())
    val geocercas: MutableStateFlow<List<GeocercaPolygonItem>> = _geocercas


    fun getPolygonalGeofence(token: String, geocercaPolygonRequest: GeocercaPolygonRequest, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = iBlacAPI.getPolyGeocercas(token, geocercaPolygonRequest).awaitResponse()
                if(response.isSuccessful){
                    response.body()?.let {
                        geocercas.value = it.geocercas
                    }
                }else{
                    onError("Error al obtener las geocercas")
                }
            }catch (e: Exception){
                onError("Error en la petición: ${e.message}")
            }
        }
    }



}