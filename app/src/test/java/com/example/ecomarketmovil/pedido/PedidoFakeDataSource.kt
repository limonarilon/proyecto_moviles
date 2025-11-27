package com.example.ecomarketmovil.pedido

class PedidoFakeDataSource : PedidoDataSource {

    private val pedidos = mutableListOf<String>()

    override suspend fun obtenerPedidos(): List<String> {
        return pedidos
    }

    override suspend fun crearPedido(pedido: String): Boolean {
        pedidos.add(pedido)
        return true
    }
}
