package com.example.ecomarketmovil.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.data.ProductoRegistro
import com.example.ecomarketmovil.remote.RetrofitClient
import com.example.ecomarketmovil.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository = ProductoRepository(RetrofitClient.apiProduct)) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    val productosFiltrados = textoBusqueda.combine(_productos) { texto, productos ->
        if (texto.isBlank()) {
            productos
        } else {
            productos.filter { it.nombre?.contains(texto, ignoreCase = true) ?: false } // Corregido: Manejo de null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _productos.value)

    fun cargarProductos() {
        viewModelScope.launch {
            val result = repository.getAllProductos()
            result.onSuccess {
                _productos.value = it
            }.onFailure {
                _mensajeError.value = "Error al cargar productos: ${it.message}"
            }
        }
    }

    fun obtenerPorIdLocal(id: Int): Producto? = _productos.value.find { it.id == id }

    fun validarYGuardar(
        id: Int?,
        nombre: String,
        precioStr: String,
        stockStr: String,
        img: String?,
        expirationDate: String?
    ) {
        _mensajeError.value = null
        if (nombre.isBlank() || precioStr.isBlank() || stockStr.isBlank()) {
            _mensajeError.value = "Los campos nombre, precio y stock son obligatorios."
            return
        }

        val precio = precioStr.toIntOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null || precio <= 0 || stock < 0) {
            _mensajeError.value = "Precio y stock deben ser números válidos. El precio debe ser mayor a 0."
            return
        }

        val registro = ProductoRegistro(
            nombre = nombre,
            precio = precio,
            stock = stock,
            img = img,
            expirationDate = expirationDate
        )

        viewModelScope.launch {
            val result = if (id == null) repository.createProducto(registro) else repository.updateProducto(id, registro)
            result.onSuccess {
                cargarProductos() // Recargar la lista
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al guardar el producto: ${it.message}"
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            val result = repository.deleteProducto(id)
            result.onSuccess {
                cargarProductos() // Recargar la lista
            }.onFailure {
                _mensajeError.value = "Error al eliminar el producto: ${it.message}"
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
