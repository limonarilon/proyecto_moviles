package com.example.ecomarketmovil.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Pedido
import com.example.ecomarketmovil.data.PedidoRegistro
import com.example.ecomarketmovil.remote.RetrofitClient
import com.example.ecomarketmovil.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidoViewModel(private val repository: PedidoRepository = PedidoRepository(RetrofitClient.apiPedido)) : ViewModel() {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    val pedidosFiltrados = textoBusqueda.combine(_pedidos) { texto, pedidos ->
        if (texto.isBlank()) {
            pedidos
        } else {
            pedidos.filter {
                it.idPedido.toString().contains(texto, ignoreCase = true) ||
                        it.estado?.contains(texto, ignoreCase = true) ?: false
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _pedidos.value)

    fun cargarPedidos() {
        viewModelScope.launch {
            val result = repository.getAllPedidos()
            result.onSuccess {
                _pedidos.value = it
            }.onFailure {
                _mensajeError.value = "Error al cargar pedidos: ${it.message}"
            }
        }
    }

    fun obtenerPedidoPorIdLocal(id: Int): Pedido? = _pedidos.value.find { it.idPedido == id }

    fun createPedido(pedido: PedidoRegistro) {
        viewModelScope.launch {
            val result = repository.createPedido(pedido)
            result.onSuccess {
                cargarPedidos() // Recargar la lista
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al crear el pedido: ${it.message}"
            }
        }
    }

    fun updatePedido(id: Int, pedido: PedidoRegistro) {
        viewModelScope.launch {
            val result = repository.updatePedido(id, pedido)
            result.onSuccess {
                cargarPedidos() // Recargar la lista
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al actualizar el pedido: ${it.message}"
            }
        }
    }

    fun deletePedido(id: Int) {
        viewModelScope.launch {
            val result = repository.deletePedido(id)
            result.onSuccess {
                cargarPedidos() // Recargar la lista
            }.onFailure {
                _mensajeError.value = "Error al eliminar el pedido: ${it.message}"
            }
        }
    }

    fun addProductoToPedido(idPedido: Int, idProducto: Int) {
        viewModelScope.launch {
            val result = repository.addProductoToPedido(idPedido, idProducto)
            result.onSuccess {
                cargarPedidos() // Recargar para reflejar el cambio
            }.onFailure {
                _mensajeError.value = "Error al agregar producto: ${it.message}"
            }
        }
    }

    fun removeProductoFromPedido(idPedido: Int, idProducto: Int) {
        viewModelScope.launch {
            val result = repository.removeProductoFromPedido(idPedido, idProducto)
            result.onSuccess {
                cargarPedidos() // Recargar para reflejar el cambio
            }.onFailure {
                _mensajeError.value = "Error al quitar producto: ${it.message}"
            }
        }
    }

    fun onTextoBusquedaChange(texto: String) {
        _textoBusqueda.value = texto
    }

    fun onNavegacionCompleta() {
        _navegacionExitosa.value = false
    }
}