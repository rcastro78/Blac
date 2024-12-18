package mx.com.comblacsol.networking

import android.content.Context
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object{
        private var retrofit: Retrofit? = null
        fun getClient(url: String?): Retrofit? {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            if (retrofit == null) {

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS) // Tiempo máximo para establecer la conexión
                    .readTimeout(300, TimeUnit.SECONDS) // Tiempo máximo para leer datos
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
    }
}


class TokenInterceptor(
    private val context: Context,
    private val renewToken: suspend () -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        val expirationTime = prefs.getLong("token_expiration", 0)
        val currentTime = System.currentTimeMillis()

        // Si el token está por expirar, renueva
        if (currentTime + 60 * 1000 > expirationTime) { // 1 minuto antes de expirar
            runBlocking {
                try {
                    renewToken() // Llama al método suspendido para renovar el token
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Agrega el token actualizado al header
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${prefs.getString("token", "")}")
            .build()

        return chain.proceed(request)
    }
}

