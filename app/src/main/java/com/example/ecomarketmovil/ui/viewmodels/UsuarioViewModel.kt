package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.UsuarioRegistro
import com.example.ecomarketmovil.data.UsuarioRespuesta
import com.example.ecomarketmovil.remote.RetrofitClient
import com.example.ecomarketmovil.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository = UsuarioRepository(RetrofitClient.apiUsuario)) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<UsuarioRespuesta>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    val usuariosFiltrados = textoBusqueda.combine(_usuarios) { texto, usuarios ->
        if (texto.isBlank()) {
            usuarios
        } else {
            usuarios.filter { it.nombre.contains(texto, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _usuarios.value)

    fun cargarUsuarios() {
        viewModelScope.launch {
            val result = repository.getAll()
            result.onSuccess {
                _usuarios.value = it
            }.onFailure {
                _mensajeError.value = "Error al cargar usuarios: ${it.message}"
            }
        }
    }

    fun obtenerPorIdLocal(id: Int): UsuarioRespuesta? = _usuarios.value.find { it.id == id }

    fun validarYGuardar(
        id: Int?,
        rut: String,
        nombre: String,
        email: String,
        password: String,
        rol: String
    ) {
        _mensajeError.value = null
        if (rut.isBlank() || nombre.isBlank() || email.isBlank() || rol.isBlank() || (id == null && password.isBlank())) {
            _mensajeError.value = "Todos los campos son obligatorios (la contraseña solo es obligatoria al crear)."
            return
        }

        val registro = UsuarioRegistro(
            nombre = nombre,
            email = email,
            password = password,
            rut = rut,
            rol = rol
        )

        viewModelScope.launch {
            val result = if (id == null) repository.create(registro) else repository.update(id, registro)
            result.onSuccess {
                cargarUsuarios() // Recargar la lista de usuarios
                _navegacionExitosa.value = true
            }.onFailure {
                _mensajeError.value = "Error al guardar: ${it.message}"
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            val result = repository.delete(id)
            result.onSuccess {
                cargarUsuarios() // Recargar la lista de usuarios
            }.onFailure {
                _mensajeError.value = "Error al eliminar: ${it.message}"
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
