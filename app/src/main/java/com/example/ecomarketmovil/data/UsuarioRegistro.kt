package com.example.ecomarketmovil.data

import com.google.gson.annotations.SerializedName

data class UsuarioRegistro(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("rut")
    val rut: String,
    @SerializedName("rol")
    val rol: String? = null
)
