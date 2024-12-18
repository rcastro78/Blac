package mx.com.comblacsol.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.comblacsol.model.LoginRequest
import mx.com.comblacsol.model.LoginResponse
import mx.com.comblacsol.model.User
import mx.com.comblacsol.networking.IBlacAPI
import mx.com.comblacsol.util.Constants
import mx.com.comblacsol.worker.TokenRenewalWorker
import retrofit2.awaitResponse
import java.util.concurrent.TimeUnit

class LoginViewModel(private val iBlacAPI: IBlacAPI, private val context: Context):ViewModel() {

    //Ya que el token solo vive 10 minutos se debe renovar cada 9:55 minutos
    fun scheduleTokenRenewal() {
        val workRequest = OneTimeWorkRequestBuilder<TokenRenewalWorker>()
            .setInitialDelay(Constants.RENEWAL_TOKEN_INTERVAL_SECONDS, TimeUnit.SECONDS) // 9 minutos y 55 segundos
            .build()

        // Encolar el Worker
        WorkManager.getInstance(context).enqueueUniqueWork(
            "TokenRenewalWork",
            ExistingWorkPolicy.REPLACE, // Reemplaza si ya está en progreso
            workRequest
        )
    }

    fun login(
        username: String,
        password: String,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = LoginRequest(username, password)
                val response = iBlacAPI.login(body).awaitResponse()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.token!!.isNotEmpty()) {
                        Log.d("LoginViewModel", "Login successful")
                        scheduleTokenRenewal()
                        onSuccess(responseBody)
                    } else {
                        onError("Login failed: No token found")
                    }
                } else {
                    when (response.code()) {
                        401 -> {
                            Log.d("LoginViewModel", "Unauthorized - Error 401")
                            onError("No autorizado")
                        }
                        400 -> {
                            Log.d("LoginViewModel", "Bad Request - Error 400")
                            onError("Bad Request")
                        }
                        else -> {
                            Log.d("LoginViewModel", "Error: ${response.code()} - ${response.message()}")
                            onError("Error ${response.code()}: ${response.message()}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception occurred: ${e.message}", e)
                onError("An error occurred: ${e.message}")
            }
        }
    }

}