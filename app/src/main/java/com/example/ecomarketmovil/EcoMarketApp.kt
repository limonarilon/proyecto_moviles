package com.example.ecomarketmovil

import android.app.Application
import com.example.ecomarketmovil.data.AuthManager

class EcoMarketApp : Application() {

    companion object {
        lateinit var authManager: AuthManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        authManager = AuthManager(applicationContext)
    }
}
