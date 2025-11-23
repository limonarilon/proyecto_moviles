package com.example.ecomarketmovil.data.remote

import com.example.ecomarketmovil.data.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {
    @GET("usuarios")
    suspend fun getAllUsuarios(): Response<List<Usuario>>

    @GET("usuarios/{rut}")
    suspend fun getUsuarioByRut(@Path("rut") rut: String): Response<Usuario>

    @POST("productos")
    suspend fun createUsuario(@Body usuario: Usuario): Response<Usuario>

    @PUT("usuarios/{rut}")
    suspend fun updateUsuario(@Path("rut") rut: String, @Body usuario: Usuario): Response<Usuario>

    @DELETE("usuarios/{rut}")
    suspend fun deleteUsuario(@Path("rut") rut: String): Response<Void>
}