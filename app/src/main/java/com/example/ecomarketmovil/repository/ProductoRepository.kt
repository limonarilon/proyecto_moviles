package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.data.ProductoRegistro
import com.example.ecomarketmovil.remote.ProductoApiService

class ProductoRepository(private val apiService: ProductoApiService) {

    suspend fun getAllProductos(): Result<List<Producto>> {
        return try {
            val response = apiService.getAllProductos()
            if (response.isSuccessful) {
                // Extraemos la lista de productos de la estructura anidada
                val productoList = response.body()?.embedded?.productos ?: emptyList()
                Result.success(productoList)
            } else {
                Result.failure(Exception("Error al obtener productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductoById(id: Int): Result<Producto> {
        return try {
            val response = apiService.getProductoById(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProducto(producto: ProductoRegistro): Result<Producto> {
        return try {
            val response = apiService.createProducto(producto)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProducto(id: Int, producto: ProductoRegistro): Result<Producto> {
        return try {
            val response = apiService.updateProducto(id, producto)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProducto(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteProducto(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
