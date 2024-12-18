package mx.com.comblacsol.networking

import mx.com.comblacsol.model.MGFinderLoginResponse
import mx.com.comblacsol.model.MGFinderMisVehiculosResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface IMGFinderAPI {
    @GET("cliente/clienteblac/{user}/{pwd}")
    fun mgfLogin(@Path("user") user: String, @Path("pwd") pwd: String): Call<MGFinderLoginResponse>

    @GET("cliente/vehiculos/{idCliente}")
    fun mgfMisVehiculos(@Path("idCliente") idCliente: Int): Call<MGFinderMisVehiculosResponse>

}