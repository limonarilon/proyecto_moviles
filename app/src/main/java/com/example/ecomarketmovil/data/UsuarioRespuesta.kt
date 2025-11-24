package com.example.ecomarketmovil.data

import com.google.gson.annotations.SerializedName

data class UsuarioRespuesta(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("rut")
    val rut: String,
    @SerializedName("rol")
    val rol: String,
    @SerializedName("activo")
    val activo: Int
)
