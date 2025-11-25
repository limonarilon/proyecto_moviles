package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.Pedido
import com.example.ecomarketmovil.data.PedidoRegistro
import com.example.ecomarketmovil.remote.PedidoApiService

class PedidoRepository(private val apiService: PedidoApiService) {

    suspend fun getAllPedidos(): Result<List<Pedido>> {
        return try {
            val response = apiService.getAllPedidos()
            if (response.isSuccessful) {
                val pedidoList = response.body()?.embedded?.pedidos ?: emptyList()
                Result.success(pedidoList)
            } else {
                Result.failure(Exception("Error al obtener pedidos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPedidoById(id: Int): Result<Pedido> {
        return try {
            val response = apiService.getPedidoById(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPedido(pedido: PedidoRegistro): Result<Pedido> {
        return try {
            val response = apiService.createPedido(pedido)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePedido(id: Int, pedido: PedidoRegistro): Result<Pedido> {
        return try {
            val response = apiService.updatePedido(id, pedido)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePedido(id: Int): Result<Unit> {
        return try {
            val response = apiService.deletePedido(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addProductoToPedido(idPedido: Int, idProducto: Int): Result<Pedido> {
        return try {
            val response = apiService.addProductoToPedido(idPedido, idProducto)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al agregar producto al pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeProductoFromPedido(idPedido: Int, idProducto: Int): Result<Unit> {
        return try {
            val response = apiService.removeProductoFromPedido(idPedido, idProducto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al quitar producto del pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
