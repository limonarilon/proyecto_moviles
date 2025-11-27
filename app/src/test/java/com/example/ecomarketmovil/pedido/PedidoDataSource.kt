package com.example.ecomarketmovil.pedido

interface PedidoDataSource {
    suspend fun obtenerPedidos(): List<String>
    suspend fun crearPedido(pedido: String): Boolean
}
