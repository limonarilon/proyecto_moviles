package com.example.ecomarketmovil.remote

import com.example.ecomarketmovil.data.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = authManager.getToken()

        val requestBuilder = originalRequest.newBuilder()
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
