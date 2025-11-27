package com.example.ecomarketmovil.pedido

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PedidoViewModelTest {

    @Test
    fun `crear pedido agrega un nuevo elemento`() = runTest {
        val fake = PedidoFakeDataSource()
        val vm = PedidoViewModelTestable(fake)

        vm.crearPedido("Pedido 1")

        val resultado = vm.listaPedidos.value
        assertEquals(1, resultado.size)
        assertEquals("Pedido 1", resultado[0])
    }

    @Test
    fun `cargar pedidos obtiene los pedidos existentes`() = runTest {
        val fake = PedidoFakeDataSource()
        fake.crearPedido("A")
        fake.crearPedido("B")

        val vm = PedidoViewModelTestable(fake)
        vm.cargarPedidos()

        assertEquals(listOf("A", "B"), vm.listaPedidos.value)
    }
}
