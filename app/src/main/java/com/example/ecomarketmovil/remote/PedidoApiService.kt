package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.Pedido
import com.example.ecomarketmovil.data.PedidoEmbeddedResponse
import com.example.ecomarketmovil.data.PedidoRegistro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PedidoApiService {

    @GET("pedidos")
    suspend fun getAllPedidos(): Response<PedidoEmbeddedResponse>

    @GET("pedidos/{id}")
    suspend fun getPedidoById(@Path("id") id: Int): Response<Pedido>

    @POST("pedidos")
    suspend fun createPedido(@Body pedido: PedidoRegistro): Response<Pedido>

    @PUT("pedidos/{id}")
    suspend fun updatePedido(@Path("id") id: Int, @Body pedido: PedidoRegistro): Response<Pedido>

    @DELETE("pedidos/{id}")
    suspend fun deletePedido(@Path("id") id: Int): Response<Unit>

    @POST("pedidos/{idPedido}/productos/{idProducto}")
    suspend fun addProductoToPedido(
        @Path("idPedido") idPedido: Int,
        @Path("idProducto") idProducto: Int
    ): Response<Pedido>

    @DELETE("pedidos/{idPedido}/productos/{idProducto}")
    suspend fun removeProductoFromPedido(
        @Path("idPedido") idPedido: Int,
        @Path("idProducto") idProducto: Int
    ): Response<Unit>
}
