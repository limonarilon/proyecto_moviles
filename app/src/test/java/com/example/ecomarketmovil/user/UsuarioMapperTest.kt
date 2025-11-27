package com.example.ecomarketmovil.user

import com.example.ecomarketmovil.data.UsuarioRegistro
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UsuarioMapperTest {

    @Test
    fun `transformar UsuarioRegistro a mapa para API`() {
        // Arrange
        val usuario = UsuarioRegistro(
            nombre = "Ana",
            email = "ana@test.com",
            password = "1234",
            rut = "12345678-9",
            rol = "ADMIN"
        )

        // Act (usamos el mapper SOLO de test)
        val map = TestUsuarioMapper.toApiMap(usuario)

        // Assert
        assertEquals("Ana", map["nombre"])
        assertEquals("ana@test.com", map["email"])
        assertEquals("1234", map["password"])
        assertEquals("12345678-9", map["rut"])
        assertEquals("ADMIN", map["rol"])
    }
}
