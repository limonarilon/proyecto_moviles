package com.example.ecomarketmovil.pedido

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PedidoViewModelTestable(
    private val dataSource: PedidoDataSource
) : ViewModel() {

    private val _listaPedidos = MutableStateFlow<List<String>>(emptyList())
    val listaPedidos = _listaPedidos.asStateFlow()

    suspend fun cargarPedidos() {
        _listaPedidos.value = dataSource.obtenerPedidos()
    }

    suspend fun crearPedido(p: String) {
        dataSource.crearPedido(p)
        _listaPedidos.value = dataSource.obtenerPedidos()
    }
}
