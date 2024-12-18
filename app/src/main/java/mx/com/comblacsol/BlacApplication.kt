package mx.com.comblacsol

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import mx.com.comblacsol.di.KoinModules
import mx.com.comblacsol.networking.BlacAPI
import mx.com.comblacsol.networking.IBlacAPI
import mx.com.comblacsol.worker.TokenRenewalWorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BlacApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        val blacAPI = BlacAPI.getBlacAPIService()
        val workerFactory = TokenRenewalWorkerFactory(blacAPI!!)
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, config)

        startKoin {
            androidContext(this@BlacApplication)
            modules(KoinModules.appModule)
        }
    }

    fun cancelTokenRenewalWorker() {
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("TokenRenewalWorker")
    }
}