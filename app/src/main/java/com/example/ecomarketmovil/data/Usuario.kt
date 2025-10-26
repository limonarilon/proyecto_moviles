package com.example.ecomarketmovil.data

data class Usuario(
    val rut: String,
    val nombre: String,
    val email: String,
    // Considera almacenar la contraseña de forma segura (ej. hasheada)
    val contrasena: String,
    val direccion: String,
)