package mx.com.comblacsol.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.EventosHistory
import mx.com.comblacsol.model.GetHistoVehicleRequest
import mx.com.comblacsol.model.HistorialResponse
import mx.com.comblacsol.networking.IBlacAPI
import retrofit2.awaitResponse

class HistorialMovimientoViewModel(private val iBlacAPI: IBlacAPI) : ViewModel() {
    private val _historial = MutableStateFlow<List<EventosHistory>>(emptyList())
    val historial: StateFlow<List<EventosHistory>> = _historial

    // Función para obtener el historial del dispositivo
    fun getHistorialDispositivo(token: String, getHistoVehicleRequest: GetHistoVehicleRequest, onRenewToken: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = iBlacAPI.getHistoricoUnidades(token, getHistoVehicleRequest).awaitResponse()
                if(response.code()>=400){
                    onRenewToken()
                }
                if (response.isSuccessful) {
                    response.body()?.let {
                        _historial.value = it.eventosHistory
                    }
                } else {
                    Log.e("HistorialViewModel", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Error: ${e.message}")
            }
        }
    }
}