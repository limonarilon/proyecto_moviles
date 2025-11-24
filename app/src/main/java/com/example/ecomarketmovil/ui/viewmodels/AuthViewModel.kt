package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.EcoMarketApp
import com.example.ecomarketmovil.data.LoginRequest
import com.example.ecomarketmovil.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val request = LoginRequest(email, password)
            val result = repository.login(request)

            result.onSuccess { response ->
                // Guardar el token usando el AuthManager
                EcoMarketApp.authManager.saveToken(response.token)
                _loginState.value = LoginState.Success(response.role)
            }.onFailure { error ->
                _loginState.value = LoginState.Error(error.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}

// Sealed class para representar los estados del proceso de login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val role: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
