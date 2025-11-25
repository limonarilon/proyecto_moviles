package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.data.ProductoEmbeddedResponse
import com.example.ecomarketmovil.data.ProductoRegistro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApiService {

    @GET("productos")
    suspend fun getAllProductos(): Response<ProductoEmbeddedResponse>

    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): Response<Producto>

    @POST("productos")
    suspend fun createProducto(@Body producto: ProductoRegistro): Response<Producto>

    @PUT("productos/{id}")
    suspend fun updateProducto(@Path("id") id: Int, @Body producto: ProductoRegistro): Response<Producto>

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Unit>
}