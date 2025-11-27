package com.example.ecomarketmovil.validation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductValidatorTest {

    @Test
    fun `validacion falla si el nombre esta vacio`() {
        val nombre = ""
        val precio = "2000"
        val stock = "5"

        val esValido = TestProductValidator.validar(nombre, precio, stock)

        assertFalse(esValido, "La validación debería fallar cuando el nombre está vacío")
    }
}
