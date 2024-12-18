package mx.com.comblacsol.worker

import android.content.Context
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import mx.com.comblacsol.model.LoginRequest
import mx.com.comblacsol.networking.IBlacAPI
import mx.com.comblacsol.util.Constants
import java.util.concurrent.TimeUnit

class TokenRenewalWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val blacAPI: IBlacAPI
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            // Lógica de renovación del token
            val prefs = applicationContext.getSharedPreferences("mx.com.comblacsol.spref", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "") ?: ""
            val password = prefs.getString("password", "") ?: ""

            val loginRequest = LoginRequest(username, password)
            val response = blacAPI.login(loginRequest).execute()

            if (response.isSuccessful) {
                val newToken = response.body()?.token
                saveToken(newToken)

                // Reprogramar el worker para que se ejecute de nuevo
                scheduleNextTokenRenewal()

                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun scheduleNextTokenRenewal() {
        val workRequest = OneTimeWorkRequestBuilder<TokenRenewalWorker>()
            .setInitialDelay(Constants.RENEWAL_TOKEN_INTERVAL_SECONDS, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    private fun saveToken(token: String?) {
        val prefs = applicationContext.getSharedPreferences("mx.com.comblacsol.spref", Context.MODE_PRIVATE)
        Log.d("TokenRenewalWorker", "Saving token: $token")
        prefs.edit().putString("token", token).apply()
    }
}


class TokenRenewalWorkerFactory(
    private val blacAPI: IBlacAPI
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (Class.forName(workerClassName).kotlin) {
            TokenRenewalWorker::class -> TokenRenewalWorker(appContext, workerParameters, blacAPI)
            else -> null
        }
    }
}
