package com.example.ecomarketmovil.data.Repository


import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.data.Usuario
import com.example.ecomarketmovil.data.remote.UsuarioApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UsuarioRepository (private val api: UsuarioApiService){

    suspend fun getAll(): Result<List<Usuario>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllUsuarios()
            if (response.isSuccessful) Result.success(response.body().orEmpty())
            else Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun create(usuario: Usuario): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val response = api.createUsuario(usuario)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(rut: String, usuario: Usuario): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateUsuario(rut, usuario)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(rut: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteUsuario(rut)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}