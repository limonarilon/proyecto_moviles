package com.example.ecomarketmovil.data

import com.google.gson.annotations.SerializedName

// Clase para la respuesta anidada de la API
data class ProductoEmbeddedResponse(
    @SerializedName("_embedded")
    val embedded: ProductoList
)

data class ProductoList(
    @SerializedName("productoModelList")
    val productos: List<Producto>
)

// Modelo principal del Producto
data class Producto(
    val id: Int,
    val nombre: String?,
    val precio: Int,
    val stock: Int,
    val img: String?,
    val expirationDate: String? // Formato yyyy-MM-dd
)

// Clase para registrar o actualizar un producto
data class ProductoRegistro(
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val img: String?,
    val expirationDate: String?
)
