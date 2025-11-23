package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Usuario
import com.example.ecomarketmovil.utils.ValidationUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.ecomarketmovil.data.remote.RetrofitClient
import com.example.ecomarketmovil.data.Repository.UsuarioRepository
import kotlin.collections.filter

class UsuarioViewModel : ViewModel() {

    private val repository = UsuarioRepository(RetrofitClient.apiUsuario)
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    // --- Estados Públicos para la UI ---
    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    val usuariosFiltrados = _textoBusqueda.combine(_usuarios) { texto, usuarios ->
        if (texto.isBlank()) usuarios
        else usuarios.filter { it.nombre.contains(texto, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _usuarios.value)


    fun cargarUsuarios() {
        viewModelScope.launch {
            val result = repository.getAll()
            result.onSuccess { _usuarios.value = it }
                .onFailure { _mensajeError.value = "Error al cargar: ${it.message}" }
        }
    }

    fun validarYGuardar(rut: String, nombre: String, email: String, contrasena: String, direccion: String) {
        _mensajeError.value = null
        if (rut.isBlank() || nombre.isBlank() || email.isBlank() || contrasena.isBlank() || direccion.isBlank()) {
            _mensajeError.value = "Todos los campos son obligatorios."
            return
        }



        val usuario = Usuario(rut, nombre, email, contrasena, direccion)
        viewModelScope.launch {
            val result = if (rut.isBlank()) repository.create(usuario) else repository.update(rut, usuario)
            result.onSuccess {
                cargarUsuarios()
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al guardar: ${it.message}"
            }
        }
    }

    fun eliminar(usuario:Usuario) {
        viewModelScope.launch {
            val result = repository.delete(usuario.rut)
            result.onSuccess { cargarUsuarios() }
                .onFailure { _mensajeError.value = "Error al eliminar: ${it.message}" }
        }
    }

    fun onTextoBusquedaChange(texto: String) {
        _textoBusqueda.value = texto
    }

    fun onNavegacionCompleta() {
        _navegacionExitosa.value = false
    }

    fun obtenerPorRut(rut: String): Usuario? = _usuarios.value.find { it.rut == rut }

 }

