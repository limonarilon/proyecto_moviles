package com.example.ecomarketmovil.repository

import com.example.ecomarketmovil.data.UsuarioRegistro
import com.example.ecomarketmovil.data.UsuarioRespuesta
import com.example.ecomarketmovil.remote.UsuarioApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UsuarioRepository(private val apiService: UsuarioApiService) {

    suspend fun getAll(): Result<List<UsuarioRespuesta>> {
        return try {
            val response = apiService.getAllUsuarios()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getById(id: Int): Result<UsuarioRespuesta> {
        return try {
            val response = apiService.getUsuarioById(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun create(usuario: UsuarioRegistro): Result<UsuarioRespuesta> {
        return try {
            val response = apiService.createUsuario(usuario)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val type = object : TypeToken<Map<String, Any>>() {}.type
                        val errorMap: Map<String, Any> = Gson().fromJson(errorBody, type)
                        // Corregido: buscar la clave "error" en lugar de "message"
                        errorMap["error"] as? String ?: "Error al crear usuario: ${response.code()}"
                    } catch (e: Exception) {
                        "Error al procesar la respuesta de error: ${response.code()}"
                    }
                } else {
                    "Error al crear usuario: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(id: Int, usuario: UsuarioRegistro): Result<UsuarioRespuesta> {
        return try {
            val response = apiService.updateUsuario(id, usuario)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(id: Int): Result<String> { // <-- Cambiado a Result<String>
        return try {
            val response = apiService.deleteUsuario(id)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Usuario eliminado") // <-- Devolver el mensaje del body
            } else {
                Result.failure(Exception("Error al eliminar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
