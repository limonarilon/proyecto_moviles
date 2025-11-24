package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.LoginRequest
import com.example.ecomarketmovil.data.LoginResponse
import com.example.ecomarketmovil.remote.AuthApiService
import com.example.ecomarketmovil.remote.RetrofitClient

class AuthRepository(private val authApi: AuthApiService = RetrofitClient.apiAuth) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = authApi.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error en el login: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
