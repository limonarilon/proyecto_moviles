package com.example.ecomarketmovil.user

import com.example.ecomarketmovil.data.UsuarioRegistro

/**
 * Mapper SOLO para uso en pruebas.
 * No afecta al código del main.
 */
object TestUsuarioMapper {
    fun toApiMap(usuario: UsuarioRegistro): Map<String, Any?> {
        return mapOf(
            "nombre" to usuario.nombre,
            "email" to usuario.email,
            "password" to usuario.password,
            "rut" to usuario.rut,
            "rol" to usuario.rol
        )
    }
}
