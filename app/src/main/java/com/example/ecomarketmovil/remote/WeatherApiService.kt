package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface WeatherService {

    @GET("point")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("sections") sections: String = "current", // Pedimos solo la sección del clima actual
        @Query("key") apiKey: String // Tu clave de API
    ): Response<WeatherResponse>
}