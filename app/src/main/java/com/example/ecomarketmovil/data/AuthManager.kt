package com.example.ecomarketmovil.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.jsonwebtoken.Jwts
import java.lang.Exception

class AuthManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        "secure_auth_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_ROLES = "user_roles"
        private const val ROLE_PREFIX = "ROLE_"
        private const val TAG = "AuthManager"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        decodeAndSaveRoles(token)
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getRoles(): List<String> {
        val rolesJson = prefs.getString(KEY_ROLES, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return try {
            Gson().fromJson(rolesJson, type) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse roles from JSON", e)
            emptyList()
        }
    }

    fun hasRole(role: String): Boolean {
        return getRoles().contains(role.removePrefix(ROLE_PREFIX))
    }

    fun hasAnyRole(vararg roles: String): Boolean {
        val userRoles = getRoles()
        return roles.any { role -> userRoles.contains(role.removePrefix(ROLE_PREFIX)) }
    }

    fun clearToken() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_ROLES)
            .apply()
    }

    private fun decodeAndSaveRoles(token: String) {
        try {
            // Parse the JWT without verifying the signature. This is safe on the client-side
            // as we are only reading claims. The server is responsible for signature validation.
            val unsignedToken = token.substring(0, token.lastIndexOf('.') + 1)
            val claims = Jwts.parserBuilder().build().parseClaimsJwt(unsignedToken).body

            // As per backend spec, roles are in "authorities" or "roles" claim.
            val rolesFromClaim = claims.get("authorities", List::class.java)
                ?: claims.get("roles", List::class.java)
                ?: emptyList<String>()

            @Suppress("UNCHECKED_CAST")
            val normalizedRoles = (rolesFromClaim as List<String>).map { it.removePrefix(ROLE_PREFIX) }

            val rolesJson = Gson().toJson(normalizedRoles)
            prefs.edit().putString(KEY_ROLES, rolesJson).apply()
            Log.d(TAG, "Successfully decoded and saved roles: $normalizedRoles")

        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode JWT or save roles.", e)
            // If decoding fails, ensure any stale roles are cleared.
            prefs.edit().remove(KEY_ROLES).apply()
        }
    }
}
