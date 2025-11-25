package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.EcoMarketApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Crear el AuthInterceptor usando el AuthManager de la aplicación
    private val authInterceptor = AuthInterceptor(EcoMarketApp.authManager)

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor) // Añadir el interceptor de autenticación
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio para Productos
    val apiProduct: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    // Servicio para Usuarios
    val apiUsuario: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    // Servicio para Autenticación
    val apiAuth: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Servicio para Pedidos
    val apiPedido: PedidoApiService by lazy {
        retrofit.create(PedidoApiService::class.java)
    }
}
