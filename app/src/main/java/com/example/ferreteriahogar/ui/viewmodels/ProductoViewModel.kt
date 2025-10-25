package com.example.ferreteriahogar.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ferreteriahogar.data.Producto

class ProductoViewModel: ViewModel() {
    private val _productos = mutableStateListOf<Producto>()// Lista mutable de productos
    //se usa el (_) por algo que se llama backing property solo el viewmodel puuede agregar
    // o quitar elementos de esta lista en cambio productos es una lista de solo lectura

    val productos: List<Producto> get() = _productos

    private var nextId = 1

    fun agregar(nombre: String, precio: Double) {
        val nuevo = Producto(nextId++, nombre, precio)
        _productos.add(nuevo)
    }

    fun eliminar(producto: Producto) {
        _productos.remove(producto)
    }

    fun actualizar(id: Int, nombre: String, precio: Double) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            _productos[index] = _productos[index].copy(nombre = nombre, precio = precio)
        }
    }

    fun obtenerPorId(id: Int): Producto? {
        return _productos.find { it.id == id }
    }
}