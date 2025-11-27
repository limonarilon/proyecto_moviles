package com.example.ecomarketmovil.validation

object TestProductValidator {

    fun validar(nombre: String, precio: String, stock: String): Boolean {
        val precioInt = precio.toIntOrNull()
        val stockInt = stock.toIntOrNull()

        return nombre.isNotBlank() &&
                precioInt != null && precioInt > 0 &&
                stockInt != null && stockInt >= 0
    }
}
