package com.example.ferreteriahogar.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FormularioViewModel : ViewModel() {

    // 1. ESTADO: Variables que guardan los datos del formulario
    var nombre by mutableStateOf("")
        private set// Solo el ViewModel puede modificar este estado


    var email by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var rut by mutableStateOf("")
        private set

    var direccion by mutableStateOf("")
        private set




    // 2. EVENTOS: Función para actualizar el estado desde la UI
    fun onEvent(event: FormularioEvent) {
        when (event) {
            is FormularioEvent.OnNombreChanged -> {
                nombre = event.nombre
            }
            is FormularioEvent.OnEmailChanged -> {
                email = event.email
            }
            is FormularioEvent.OnContrasenaChanged -> {
                contrasena = event.contrasena
            }
            is FormularioEvent.OnRutChanged -> {
                rut = event.rut
            }
            is FormularioEvent.OnDireccionChanged -> {
                direccion = event.direccion
            }
            is FormularioEvent.OnSubmit -> {
                // Aquí irá la lógica para registrar al usuario
                println("Registrando usuario: Nombre=$nombre, Email=$email")
                // Más adelante conectaremos esto a un repositorio
            }
        }
    }
}

// 3. DEFINICIÓN DE EVENTOS: Acciones que la UI puede enviar al ViewModel
sealed class FormularioEvent {
    data class OnNombreChanged(val nombre: String) : FormularioEvent()
    data class OnEmailChanged(val email: String) : FormularioEvent()
    data class OnContrasenaChanged(val contrasena: String) : FormularioEvent()

    data class OnRutChanged(val rut: String) : FormularioEvent()

    data class OnDireccionChanged(val direccion: String) : FormularioEvent()

    object OnSubmit : FormularioEvent()
}