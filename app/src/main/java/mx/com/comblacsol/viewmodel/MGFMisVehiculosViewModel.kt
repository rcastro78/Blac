package mx.com.comblacsol.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.MGFinderMisVehiculosResponseItem
import mx.com.comblacsol.networking.IMGFinderAPI
import retrofit2.awaitResponse

class MGFMisVehiculosViewModel(private val iMGFinderAPI: IMGFinderAPI) : ViewModel() {
    private val _misVehiculos = MutableStateFlow<List<MGFinderMisVehiculosResponseItem>>(emptyList())
    val misVehiculos: StateFlow<List<MGFinderMisVehiculosResponseItem>> = _misVehiculos

    fun getMisVehiculos(userId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = iMGFinderAPI.mgfMisVehiculos(userId).awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("MGFMisVehiculosViewModel", "Response: ${it.size}")
                        _misVehiculos.value = it
                    }
            }else{
                Log.e("MGFMisVehiculosViewModel", "Error: ${response.toString()}")
            }
            } catch (e: Exception) {
                Log.e("MGFMisVehiculosViewModel", "Error: ${e.message}")
            }
        }

    }


}