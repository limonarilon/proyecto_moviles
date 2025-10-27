package com.example.ecomarketmovil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarketmovil.data.Usuario
import com.example.ecomarketmovil.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    // --- Estados Internos ---
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    private val _mensajeError = MutableStateFlow<String?>(null)
    private val _navegacionExitosa = MutableStateFlow(false)
    private val _textoBusqueda = MutableStateFlow("")

    // --- Estados Públicos para la UI ---
    val mensajeError = _mensajeError.asStateFlow()
    val navegacionExitosa = _navegacionExitosa.asStateFlow()
    val textoBusqueda = _textoBusqueda.asStateFlow()

    // Flow que combina la lista de productos y el texto de búsqueda para producir la lista filtrada
    val usuariosFiltrados = _textoBusqueda
        .combine(_usuarios) { texto, usuarios ->
            if (texto.isBlank()) {
                usuarios
            } else {
                usuarios.filter {
                    it.nombre.contains(texto, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _usuarios.value
        )

    // --- Lógica de Negocio ---

    fun onTextoBusquedaChange(texto: String) {
        _textoBusqueda.value = texto
    }

    fun validarYGuardar(rut: String, nombre: String, email: String, contrasena: String, direccion: String, esNuevo:Boolean) {
        _mensajeError.value = null // Limpiar error anterior

        if (rut.isBlank() || nombre.isBlank() || email.isBlank() || contrasena.isBlank() || direccion.isBlank()) {
            _mensajeError.value = "Todos los campos son obligatorios."
            return
        }

        if (!ValidationUtils.esRutValido(rut)) {
            _mensajeError.value = "El formato del RUT no es válido."
            return
        }

        if (!ValidationUtils.esEmailValido(email)) {
            _mensajeError.value = "El formato del email no es válido."
            return
        }

        val existeUsuario = _usuarios.value.any { it.rut == rut }

        if (esNuevo && existeUsuario) {
            _mensajeError.value = "El RUT ingresado ya existe."
            return
        }

        if (esNuevo) {
            agregar(rut, nombre, email, contrasena, direccion)
        } else {
            actualizar(rut, nombre, email, contrasena, direccion)
        }
        _navegacionExitosa.value = true // Indicar navegación exitosa
    }

    fun onNavegacionCompleta() {
        _navegacionExitosa.value = false
    }

    fun cargarDatosDeEjemplo() {
        if (_usuarios .value.isNotEmpty()) return // Evita duplicar datos
        val listaEjemplo = listOf(
            Usuario("15023456-9", "Luis Jara", "lu.jara@email.com", "Usser1234*","av Antonio Varas #666"),
            Usuario("9873423-1", "Daniela Pérez", "da.perez@email.com", "Usser5678*","av Antonio Varas #777"),
            Usuario("18938473-4", "Juan González", "ju.gonzalez@email.com", "Usser1234-","av Antonio Varas #888"),
            Usuario("21784763-9", "Maria Ordenes", "ma.ordenes@email.com", "Usser5678-","av Antonio Varas #999")
        )
        _usuarios.value = listaEjemplo
    }

    private fun agregar(rut: String, nombre: String, email: String, contrasena: String, direccion: String) {
        val nuevo = Usuario(rut, nombre, email, contrasena, direccion)
        _usuarios.update { it + nuevo }
    }

    fun eliminar(usuario: Usuario) {
        _usuarios.update { it - usuario }
    }

    private fun actualizar(rut: String, nombre: String, email: String, contrasena: String, direccion: String) {
        _usuarios.update { listaActual ->
            listaActual.map {
                if (it.rut == rut) it.copy(rut=rut, nombre=nombre, email=email, contrasena=contrasena, direccion=direccion) else it
            }
        }
    }

    fun obtenerPorRut(rut: String): Usuario? {
        return _usuarios.value.find { it.rut == rut }
    }
}