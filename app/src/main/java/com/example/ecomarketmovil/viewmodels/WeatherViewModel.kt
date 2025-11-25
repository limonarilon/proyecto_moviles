package com.example.ecomarketmovil.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.models.WeatherResponse
import com.example.ecomarketmovil.remote.RetrofitClientWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Definimos la clave de la API como una constante para facilitar su mantenimiento.
private const val API_KEY = "a576d8557b3263c788623b9c71612e43"

class WeatherViewModel : ViewModel() {

    // Flujo de estado para almacenar y exponer los datos del clima de forma segura para la UI
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    // Flujo de estado para manejar los estados de la UI (Cargando, Éxito, Error)
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    /**
     * Obtiene los datos del clima para una latitud y longitud específicas.
     * La clave de la API está codificada aquí por simplicidad, pero en una aplicación real
     * debería almacenarse de forma más segura.
     */
    fun fetchWeatherData(lat: String, lon: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // Indica que la carga ha comenzado
            try {
                // Llamada a la API para obtener los datos del clima
                val response = RetrofitClientWeather.apiWeather.getWeather(lat, lon, apiKey = API_KEY)
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualiza los datos y el estado de la UI
                    _weatherData.value = response.body()
                    _uiState.value = UiState.Success
                } else {
                    // Si hay un error en la respuesta, actualiza el estado de la UI con el mensaje de error
                    _uiState.value = UiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Si ocurre una excepción durante la llamada a la API, actualiza el estado de la UI
                _uiState.value = UiState.Error("Exception: ${e.message}")
            }
        }
    }

    /**
     * Función para notificar a la UI que no se pudo obtener la ubicación.
     */
    fun onLocationUnavailable() {
        _uiState.value = UiState.Error("Ubicación no disponible")
    }
}

// Clase sellada para representar los diferentes estados de la UI
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}
