package mx.com.comblacsol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.EliminarGeocercaRequest
import mx.com.comblacsol.model.GeocercaCircleItem
import mx.com.comblacsol.model.GeocercaCircleRequest
import mx.com.comblacsol.model.GeocercaPolygonItem
import mx.com.comblacsol.model.GeocercaPolygonRequest

import mx.com.comblacsol.model.SetGeocercaCircleRequest
import mx.com.comblacsol.networking.IBlacAPI
import retrofit2.awaitResponse

class CircularGeofenceViewModel(private val iBlacAPI: IBlacAPI) : ViewModel() {
    //Set geocerca circular
    fun setCircleGeofence(token: String, setCircleGeofenceRequest: SetGeocercaCircleRequest,
                          onSuccess:(String) -> Unit, onError:(String) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = iBlacAPI.setGeocerca(token, setCircleGeofenceRequest).awaitResponse()
                if(response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it)
                    }
                }else{
                    onError(response.message())
                }
            }catch (e: Exception){
                onError("Error ${e.message}")

            }
        }
    }
    /*
    *  private val _historial = MutableStateFlow<List<EventosHistory>>(emptyList())
    val historial: StateFlow<List<EventosHistory>> = _historial
    * */
    //Get geocerca
    private val _geocercas = MutableStateFlow<List<GeocercaCircleItem>>(emptyList())
    val geocercas: MutableStateFlow<List<GeocercaCircleItem>> = _geocercas

    private val _pgeocercas = MutableStateFlow<List<GeocercaPolygonItem>>(emptyList())
    val pgeocercas: MutableStateFlow<List<GeocercaPolygonItem>> = _pgeocercas

    fun getCircleGeofence(token: String, geocercaCircleRequest: GeocercaCircleRequest, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = iBlacAPI.getCircleGeocercas(token, geocercaCircleRequest).awaitResponse()
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


    fun deleteGeocercas(token: String, eliminarGeocercaRequest: EliminarGeocercaRequest, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = iBlacAPI.deleteGeocercas(token, eliminarGeocercaRequest).awaitResponse()
                if(response.isSuccessful){
                    val result = response.body()!!.message
                    onSuccess(result)
                }else{
                    onError("Error al eliminar las geocercas")
                }
            }catch (e: Exception){
                onError("Error en la petición: ${e.message}")
            }


        }
    }


}