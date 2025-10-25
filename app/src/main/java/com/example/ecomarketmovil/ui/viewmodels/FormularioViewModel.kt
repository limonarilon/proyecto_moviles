package com.example.ecomarketmovil.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 1. DATA CLASS PARA EL ESTADO
data class FormularioState(
    val nombre: String = "",
    val nombreError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val contrasena: String = "",
    val contrasenaError: String? = null,
    val rut: String = "",
    val rutError: String? = null,
    val direccion: String = "",
    val direccionError: String? = null,
    val cargo: String = "",
    val cargoError: String? = null,
    val genero: String = "",
    val generoError: String? = null,
)

class FormularioViewModel : ViewModel() {

    // 2. ÚNICO MUTABLESTATEOF
    var state by mutableStateOf(FormularioState())
        private set

    private fun validarRutChileno(rut: String): Boolean {
        if (rut.isBlank()) return false
        try {
            var rutLimpio = rut.replace(".", "").replace("-", "")
            if (rutLimpio.length < 2) return false

            val dv = rutLimpio.last().uppercaseChar()
            val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1).toInt()

            var suma = 0
            var multiplo = 2
            var rutTemp = cuerpo

            while (rutTemp > 0) {
                suma += (rutTemp % 10) * multiplo
                rutTemp /= 10
                multiplo++
                if (multiplo > 7) multiplo = 2
            }

            val dvEsperado = when (val res = 11 - (suma % 11)) {
                11 -> '0'
                10 -> 'K'
                else -> res.toString().first()
            }

            return dv == dvEsperado
        } catch (e: Exception) {
            return false
        }
    }

    private fun validate(): Boolean {
        val nombreResult = if (state.nombre.isBlank()) "El nombre no puede estar vacío" else null
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val emailResult = when {
            state.email.isBlank() -> "El email no puede estar vacío"
            !emailRegex.matches(state.email) -> "El formato del email no es válido"
            else -> null
        }
        val contrasenaResult = if (state.contrasena.length < 8) "La contraseña debe tener al menos 8 caracteres" else null
        val rutResult = when {
            state.rut.isBlank() -> "El RUT no puede estar vacío"
            !validarRutChileno(state.rut) -> "El RUT no es válido"
            else -> null
        }
        val direccionResult = if (state.direccion.isBlank()) "La dirección no puede estar vacía" else null
        val cargoResult = if (state.cargo.isBlank()) "Debes seleccionar un cargo" else null
        val generoResult = if (state.genero.isBlank()) "Debes seleccionar un género" else null

        val hasError = listOf(
            nombreResult,
            emailResult,
            contrasenaResult,
            rutResult,
            direccionResult,
            cargoResult,
            generoResult
        ).any { it != null }

        if (hasError) {
            state = state.copy(
                nombreError = nombreResult,
                emailError = emailResult,
                contrasenaError = contrasenaResult,
                rutError = rutResult,
                direccionError = direccionResult,
                cargoError = cargoResult,
                generoError = generoResult
            )
        }

        return !hasError
    }
    fun onEvent(event: FormularioEvent) {
        when (event) {
            is FormularioEvent.OnNombreChanged -> {
                state = state.copy(nombre = event.nombre, nombreError = null)
            }
            is FormularioEvent.OnEmailChanged -> {
                state = state.copy(email = event.email, emailError = null)
            }
            is FormularioEvent.OnContrasenaChanged -> {
                state = state.copy(contrasena = event.contrasena, contrasenaError = null)
            }
            is FormularioEvent.OnRutChanged -> {
                state = state.copy(rut = event.rut, rutError = null)
            }
            is FormularioEvent.OnDireccionChanged -> {
                state = state.copy(direccion = event.direccion, direccionError = null)
            }
            is FormularioEvent.OnCargoChanged -> {
                state = state.copy(cargo = event.cargo, cargoError = null)
            }
            is FormularioEvent.OnGeneroChanged -> {
                state = state.copy(genero = event.genero, generoError = null)
            }
            is FormularioEvent.OnSubmit -> {
                if (validate()){
                    println("¡Validación exitosa! Registrando usuario: ${state}")
                }else{
                    println("Validación fallida.")
                }
            }
        }
    }
}



sealed class FormularioEvent {
    data class OnNombreChanged(val nombre: String) : FormularioEvent()
    data class OnEmailChanged(val email: String) : FormularioEvent()
    data class OnContrasenaChanged(val contrasena: String) : FormularioEvent()
    data class OnRutChanged(val rut: String) : FormularioEvent()
    data class OnDireccionChanged(val direccion: String) : FormularioEvent()
    data class OnCargoChanged(val cargo: String) : FormularioEvent()
    data class OnGeneroChanged(val genero: String) : FormularioEvent()
    object OnSubmit : FormularioEvent()
}
