package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.Producto
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {
    @GET("productos")
    suspend fun getAllProducts(): Response<List<Producto>>

    @GET("productos/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Producto>

    @POST("productos")
    suspend fun createProduct(@Body producto: Producto): Response<Producto>

    @PUT("productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    @DELETE("productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}








