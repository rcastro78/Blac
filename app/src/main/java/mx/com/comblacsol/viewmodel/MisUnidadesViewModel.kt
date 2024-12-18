package mx.com.comblacsol.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.Evento
import mx.com.comblacsol.model.EventosResponse
import mx.com.comblacsol.model.GetVehicleRequest
import mx.com.comblacsol.networking.IBlacAPI
import retrofit2.awaitResponse

class MisUnidadesViewModel(private val iBlacAPI: IBlacAPI) : ViewModel() {
    fun getUnidades(token: String,
                    getVehicleRequest: GetVehicleRequest,
                    onSuccess: (EventosResponse) -> Unit,
                    onError: (String) -> Unit,
                    onRenewToken: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Token _vm_", token)
            val response = iBlacAPI.getUnidades(token, getVehicleRequest).awaitResponse()
            Log.d("Response", response.toString())
            if (response.code()>=400){
                onRenewToken(response.toString())
            }
            if (response.code() == 200) {
                try {
                    onSuccess(response.body()!!)
                }catch (e: Exception){
                    onError(e.toString())
                }
            } else {
                onError(response.toString())
            }

        }
    }
}