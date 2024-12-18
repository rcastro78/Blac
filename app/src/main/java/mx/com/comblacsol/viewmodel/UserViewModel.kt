package mx.com.comblacsol.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.User
import mx.com.comblacsol.networking.IBlacAPI
import retrofit2.awaitResponse
import androidx.compose.runtime.State

class UserViewModel(private val iBlacAPI: IBlacAPI) : ViewModel() {

    private val _userBitmap = mutableStateOf<Bitmap?>(null)
    val userBitmap: State<Bitmap?> = _userBitmap

    // Obtener datos del usuario
    fun getUserData(token: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = iBlacAPI.getUser("Bearer $token").awaitResponse()
            if (response.code() == 200) {
                onSuccess(response.body()!!)
            } else {
                onError(response.toString())
            }
        }
    }

    // Descargar imagen y convertir el ByteArray a Bitmap
    fun downloadImage(token: String, onSuccess: (ByteArray) -> Unit, onError: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = iBlacAPI.downloadImage("Bearer $token").awaitResponse()
            if (response.code() == 200) {
                val byteArray = response.body()!!.bytes()
                onSuccess(byteArray)  // Paso el ByteArray a onSuccess
                // Convertir el ByteArray a Bitmap y actualizar el estado
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                _userBitmap.value = bitmap
            } else {
                onError(response.toString())
            }
        }
    }
}