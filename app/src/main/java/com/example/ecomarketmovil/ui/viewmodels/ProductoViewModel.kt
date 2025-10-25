package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProductoViewModel : ViewModel() {

    // --- Estados Internos ---
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    // --- Estados Públicos para la UI ---
    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    // Flow que combina la lista de productos y el texto de búsqueda para producir la lista filtrada
    val productosFiltrados = _textoBusqueda
        .combine(_productos) { texto, productos ->
            if (texto.isBlank()) {
                productos
            } else {
                productos.filter {
                    it.nombre.contains(texto, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _productos.value
        )

    private var nextId = 1

    // --- Lógica de Negocio ---

    fun onTextoBusquedaChange(texto: String) {
        _textoBusqueda.value = texto
    }

    fun validarYGuardar(id: Int, nombre: String, precioStr: String, stockStr: String) {
        _mensajeError.value = null // Limpiar error anterior

        if (nombre.isBlank() || precioStr.isBlank() || stockStr.isBlank()) {
            _mensajeError.value = "Todos los campos son obligatorios."
            return
        }

        val precio = precioStr.toIntOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || stock == null) {
            _mensajeError.value = "Precio y stock deben ser números válidos."
            return
        }

        if (precio <= 0 || stock <= 0) {
            _mensajeError.value = "Precio y stock no pueden ser negativos ni 0."
            return
        }

        if (id != -1) {
            actualizar(id, nombre, precio, stock)
        } else {
            agregar(nombre, precio, stock)
        }
        _navegacionExitosa.value = true // Indicar navegación exitosa
    }

    fun onNavegacionCompleta() {
        _navegacionExitosa.value = false
    }

    fun cargarDatosDeEjemplo() {
        if (_productos.value.isNotEmpty()) return // Evita duplicar datos
        val listaEjemplo = listOf(
            Producto(nextId++, "Jugo de naranja", 1500, 10),
            Producto(nextId++, "Arroz Integral", 1990, 20),
            Producto(nextId++, "Mix frutos secos", 2450, 15)
        )
        _productos.value = listaEjemplo
    }

    private fun agregar(nombre: String, precio: Int, stock: Int) {
        val nuevo = Producto(nextId++, nombre, precio, stock)
        _productos.update { it + nuevo }
    }

    fun eliminar(producto: Producto) {
        _productos.update { it - producto }
    }

    private fun actualizar(id: Int, nombre: String, precio: Int, stock: Int) {
        _productos.update { listaActual ->
            listaActual.map {
                if (it.id == id) it.copy(nombre = nombre, precio = precio, stock = stock) else it
            }
        }
    }

    fun obtenerPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }
}