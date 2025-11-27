package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.UsuarioRespuesta
import com.example.ecomarketmovil.remote.UsuarioApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    private val apiService = mockk<UsuarioApiService>()
    private val repository = UsuarioRepository(apiService)

    @Test
    fun `cuando getById recibe 200 entonces retorna Result success`() = runBlocking {
        // Arrange
        val usuarioMock = UsuarioRespuesta(
            id = 1,
            nombre = "Juan",
            email = "juan@test.com",
            rut = "12345678-9",
            rol = "ADMIN",
            activo = 1
        )

        coEvery { apiService.getUsuarioById(1) } returns Response.success(usuarioMock)

        // Act
        val result = repository.getById(1)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(usuarioMock, result.getOrNull())
    }

    @Test
    fun `cuando getById recibe error HTTP entonces retorna Result failure`() = runBlocking {
        // Arrange
        coEvery { apiService.getUsuarioById(1) } returns Response.error(
            404,
            ResponseBody.create(null, "Not found")
        )

        // Act
        val result = repository.getById(1)

        // Assert
        assertTrue(result.isFailure)
    }


}
// PRUEBA N° 2 DE MOCKK