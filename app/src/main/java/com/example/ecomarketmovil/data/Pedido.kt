package com.example.ecomarketmovil.data

import com.google.gson.annotations.SerializedName

// Clases para la respuesta anidada de la API de Pedidos
data class PedidoEmbeddedResponse(
    @SerializedName("_embedded")
    val embedded: PedidoList
)

data class PedidoList(
    @SerializedName("pedidoModelList")
    val pedidos: List<Pedido>
)

// Modelo principal del Pedido
data class Pedido(
    @SerializedName("idPedido")
    val idPedido: Int,
    @SerializedName("fecha")
    val fecha: String?, // Se usará String para simplificar el parsing de la fecha
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("productos")
    val productos: List<Producto>
)

// Clase para registrar o actualizar un pedido
data class PedidoRegistro(
    val fecha: String?,
    val estado: String?,
    val total: Int,
    val productos: List<Producto>
)
