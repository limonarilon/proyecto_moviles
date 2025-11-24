package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.remote.RetrofitClient
import com.example.ecomarketmovil.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    private val repository = ProductRepository(RetrofitClient.apiProduct)

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    val productosFiltrados = _textoBusqueda.combine(_productos) { texto, productos ->
        if (texto.isBlank()) productos
        else productos.filter { it.nombre.contains(texto, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _productos.value)


    fun cargarProductos() {
        viewModelScope.launch {
            val result = repository.getAll()
            result.onSuccess { _productos.value = it }
                .onFailure { _mensajeError.value = "Error al cargar: ${it.message}" }
        }
    }

    fun validarYGuardar(id: Int, nombre: String, precioStr: String, stockStr: String) {
        _mensajeError.value = null
        if (nombre.isBlank() || precioStr.isBlank() || stockStr.isBlank()) {
            _mensajeError.value = "Todos los campos son obligatorios."
            return
        }

        val precio = precioStr.toIntOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null || precio <= 0 || stock <= 0) {
            _mensajeError.value = "Precio y stock deben ser válidos y mayores a 0."
            return
        }

        val producto = Producto(id, nombre, precio, stock)
        viewModelScope.launch {
            val result = if (id == -1) repository.create(producto) else repository.update(id, producto)
            result.onSuccess {
                cargarProductos()
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al guardar: ${it.message}"
            }
        }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch {
            val result = repository.delete(producto.id)
            result.onSuccess { cargarProductos() }
                .onFailure { _mensajeError.value = "Error al eliminar: ${it.message}" }
        }
    }

    fun onTextoBusquedaChange(texto: String) {
        _textoBusqueda.value = texto
    }

    fun onNavegacionCompleta() {
        _navegacionExitosa.value = false
    }

    fun obtenerPorId(id: Int): Producto? = _productos.value.find { it.id == id }
}
