package mx.com.comblacsol.di

import android.content.Context
import android.content.SharedPreferences
import mx.com.comblacsol.R
import mx.com.comblacsol.networking.BlacAPI
import mx.com.comblacsol.networking.IBlacAPI
import mx.com.comblacsol.networking.IMGFinderAPI
import mx.com.comblacsol.networking.MGFinderAPI
import mx.com.comblacsol.viewmodel.*
import mx.com.comblacsol.worker.TokenRenewalWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {
    val appModule = module {
        single<SharedPreferences> {
            androidContext().getSharedPreferences(
                androidContext().getString(R.string.spref),
                Context.MODE_PRIVATE
            )
        }
        single<IBlacAPI> {
            BlacAPI.getBlacAPIService()!!
        }

        single {
            TokenRenewalWorker(
                get(), // Contexto de la aplicación
                get(), // WorkerParameters (inyectado por Koin)
                get()  // IBlacAPI para la renovación del token
            )
        }

        viewModel {LoginViewModel(get(),androidContext())}
        viewModel {UserViewModel(get())}
        viewModel {MisUnidadesViewModel(get())}
        viewModel {HistorialMovimientoViewModel(get())}
        viewModel {CircularGeofenceViewModel(get())}
        viewModel {PolygonalGeofenceViewModel(get())}

        single<IMGFinderAPI> {
            MGFinderAPI.getMGFinderAPIService()!!
        }
        viewModel {MGFMisVehiculosViewModel(get())}

    }

}