package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.LoginRequest
import com.example.ecomarketmovil.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
