package com.example.ecomarketmovil.data.models


import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("celsius")
    val temperature: Int

)