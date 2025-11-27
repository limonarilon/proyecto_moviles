package com.example.ecomarketmovil.auth

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class AuthRepositoryKotestTest : ShouldSpec({

    should("dejar el token nulo despues de logout") {
        val repo = AuthRepositoryFake()

        repo.token = "abc123"
        repo.logout()

        repo.token shouldBe null
    }
})

// Fake simple para simular logout
class AuthRepositoryFake {
    var token: String? = null

    fun logout() {
        token = null
    }
}

// PRUEBA N° 1 DE KOTEST