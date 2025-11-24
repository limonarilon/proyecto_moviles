package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.UsuarioRegistro
import com.example.ecomarketmovil.data.UsuarioRespuesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApiService {

    @GET("usuarios")
    suspend fun getAllUsuarios(): Response<List<UsuarioRespuesta>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<UsuarioRespuesta>

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioRegistro): Response<UsuarioRespuesta>

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioRegistro): Response<UsuarioRespuesta>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<String> // <-- Cambiado de Void a String
}
