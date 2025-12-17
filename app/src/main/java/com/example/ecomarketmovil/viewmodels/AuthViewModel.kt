package com.example.ecomarketmovil.viewmodels

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
                // Guardar el token. AuthManager se encargará de decodificar y guardar los roles.
                EcoMarketApp.authManager.saveToken(response.token)
                // El estado de éxito ya no necesita el rol, solo notifica que el login fue correcto.
                _loginState.value = LoginState.Success
            }.onFailure { error ->
                _loginState.value = LoginState.Error(error.message ?: "Ocurrió un error")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    // Sealed class para representar los estados del proceso de login
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState() // Ya no contiene el rol
        data class Error(val message: String) : LoginState()
    }

    //sealed class para el olvido de contraseña
    sealed class PasswordResetState {
        object Idle : PasswordResetState()
        object Loading : PasswordResetState()
        object Success : PasswordResetState()
        data class Error(val message: String) : PasswordResetState()
    }
    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState = _passwordResetState.asStateFlow()
    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _passwordResetState.value = PasswordResetState.Error("Por favor, ingresa un email válido.")
                return@launch
            }

            _passwordResetState.value = PasswordResetState.Loading
            try {	//llamada al backend

                kotlinx.coroutines.delay(2000)

                _passwordResetState.value = PasswordResetState.Success

            } catch (e: Exception) {
                //errores de red o de la API
                _passwordResetState.value = PasswordResetState.Error(e.message ?: "Ocurrió un error desconocido.")
            }
        }
    }

    fun resetPasswordState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

}
