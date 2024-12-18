package mx.com.comblacsol.networking

import mx.com.comblacsol.model.EliminarGeocercaRequest
import mx.com.comblacsol.model.EliminarGeocercaResponse
import mx.com.comblacsol.model.Evento
import mx.com.comblacsol.model.EventosResponse
import mx.com.comblacsol.model.GeocercaCircleRequest
import mx.com.comblacsol.model.GeocercaPolygonRequest
import mx.com.comblacsol.model.GetGeocercaCircleResponse
import mx.com.comblacsol.model.GetGeocercaPolygonResponse
import mx.com.comblacsol.model.GetGeofencesRequest
import mx.com.comblacsol.model.GetHistoVehicleRequest
import mx.com.comblacsol.model.GetVehicleRequest
import mx.com.comblacsol.model.HistorialResponse
import mx.com.comblacsol.model.LoginRequest
import mx.com.comblacsol.model.LoginResponse
import mx.com.comblacsol.model.SetGeocercaCircleRequest
import mx.com.comblacsol.model.SetGeocercaPolyRequest
import mx.com.comblacsol.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface IBlacAPI {
    @POST("login")
    @Headers("credential: f0ef25e588f380942beeadfc4a5016199e3a849a")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/blacservices/api/usuario/consultarusuario")
    fun getUser(@Header("Authorization") token: String): Call<User>

    @GET("/blacservices/api/usuario/downloadImagenPerfil")
    fun downloadImage(@Header("Authorization") token: String): Call<ResponseBody>

    @POST("get/eventos")
    fun getUnidades(@Header("token") token: String, @Body getVehicleRequest: GetVehicleRequest):Call<EventosResponse>

    @POST("get/eventos/history")
    fun getHistoricoUnidades(@Header("token") token: String, @Body getHistoVehicleRequest: GetHistoVehicleRequest):Call<HistorialResponse>

    //Geocercas
    @POST("set/geocerca")
    fun setGeocerca(@Header("token") token: String, @Body setGeocercaCircleRequest: SetGeocercaCircleRequest):Call<String>

    @POST("set/geocerca")
    fun setPolyGeocerca(@Header("token") token: String, @Body setGeocercaPolyRequest: SetGeocercaPolyRequest):Call<String>

    @POST("get/geocercas")
    fun getPolyGeocercas(@Header("token") token: String, @Body geoCercaPolygonRequest: GeocercaPolygonRequest):Call<GetGeocercaPolygonResponse>

    @POST("get/geocercas")
    fun getCircleGeocercas(@Header("token") token: String,@Body geocercaCircleRequest: GeocercaCircleRequest):Call<GetGeocercaCircleResponse>

    @POST("delete/geocerca")
    fun deleteGeocercas(@Header("token") token: String,@Body eliminarGeocercaRequest: EliminarGeocercaRequest):Call<EliminarGeocercaResponse>
}