package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.LoginRequest
import com.example.ecomarketmovil.data.LoginResponse
import com.example.ecomarketmovil.remote.AuthApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class AuthRepositoryTest {

    private val apiService = mockk<AuthApiService>()
    private val repository = AuthRepository(apiService)

    @Test
    fun `cuando login recibe 200 entonces retorna Result success`() = runBlocking {
        // Arrange
        val request = LoginRequest(email = "admin@test.com", password = "1234")

        val loginResponse = LoginResponse(
            token = "jwt_123",
            role = "ADMIN"
        )

        coEvery { apiService.login(request) } returns Response.success(loginResponse)

        // Act
        val result = repository.login(request)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("jwt_123", result.getOrNull()!!.token)
        assertEquals("ADMIN", result.getOrNull()!!.role)
    }

    @Test
    fun `cuando login recibe error HTTP entonces retorna Result failure`() = runBlocking {
        // Arrange
        val request = LoginRequest(email = "admin@test.com", password = "wrong")

        coEvery { apiService.login(request) } returns Response.error(
            401,
            "Unauthorized".toResponseBody(null)

        )

        // Act
        val result = repository.login(request)

        // Assert
        assertTrue(result.isFailure)
    }
}
// PRUEBA N° 1 DE MOCKK