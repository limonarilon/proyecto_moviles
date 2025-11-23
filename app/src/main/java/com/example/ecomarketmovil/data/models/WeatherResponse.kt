package com.example.ecomarketmovil.data.models


// Clase principal que representa toda la respuesta de la API
data class WeatherResponse(
    val current: CurrentWeather? // Objeto que contiene los datos del clima actual
)

// Clase que representa el objeto "current" en el JSON
data class CurrentWeather(
    val temperature: Float // La temperatura en grados Celsius
)
