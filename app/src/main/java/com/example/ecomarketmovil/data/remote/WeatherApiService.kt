package com.example.ecomarketmovil.data.remote

import com.example.ecomarketmovil.data.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Response

interface WeatherService {

    @GET("weather.json")
    suspend fun getWeather(): Response<WeatherResponse>
}