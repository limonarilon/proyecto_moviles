package com.example.ecomarketmovil.data.Repository

import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.data.remote.ProductApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ProductRepository(private val api: ProductApiService) {
    suspend fun getAll(): Result<List<Producto>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllProducts()
            if (response.isSuccessful) Result.success(response.body().orEmpty())
            else Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun create(product: Producto): Result<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = api.createProduct(product)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(id: Int, product: Producto): Result<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateProduct(id, product)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteProduct(id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}