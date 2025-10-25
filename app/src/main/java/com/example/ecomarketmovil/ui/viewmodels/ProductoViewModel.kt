package com.example.ecomarketmovil.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ecomarketmovil.data.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductoViewModel: ViewModel() {
    private val _productos = mutableStateListOf<Producto>()// Lista mutable de productos

    // Estado para exponer los mensajes de error a la UI
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError = _mensajeError.asStateFlow()

    // Estado para indicar a la UI que la operación fue exitosa y debe navegar
    private val _navegacionExitosa = MutableStateFlow(false)
    val navegacionExitosa = _navegacionExitosa.asStateFlow()

    val productos: List<Producto> get() = _productos

    private var nextId = 1

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

    // Función pública para cargar datos en Previews
    fun cargarDatosDeEjemplo() {
        if (productos.isNotEmpty()) return // Evita duplicar datos
        agregar("Jugo de naranja", 1500, 10)
        agregar("Arroz Integral", 1990, 20)
        agregar("Mix frutos secos", 2450, 15)
    }

    private fun agregar(nombre: String, precio: Int, stock: Int) {
        val nuevo = Producto(nextId++, nombre, precio, stock)
        _productos.add(nuevo)
    }

    fun eliminar(producto: Producto) {
        _productos.remove(producto)
    }

    private fun actualizar(id: Int, nombre: String, precio: Int, stock: Int) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            _productos[index] = _productos[index].copy(nombre = nombre, precio = precio, stock = stock)
        }
    }

    fun obtenerPorId(id: Int): Producto? {
        return _productos.find { it.id == id }
    }
}
