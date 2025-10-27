package com.example.ecomarketmovil.utils

object ValidationUtils {

    fun esEmailValido(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return emailRegex.matches(email)
    }

    fun esRutValido(rut: String): Boolean {
        if (rut.isBlank()) return false
        try {
            var rutLimpio = rut.replace(".", "").replace("-", "")
            if (rutLimpio.length < 2) return false

            val dv = rutLimpio.last().uppercaseChar()
            val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1).toInt()

            var suma = 0
            var multiplo = 2
            var rutTemp = cuerpo

            while (rutTemp > 0) {
                suma += (rutTemp % 10) * multiplo
                rutTemp /= 10
                multiplo++
                if (multiplo > 7) multiplo = 2
            }

            val dvEsperado = when (val res = 11 - (suma % 11)) {
                11 -> '0'
                10 -> 'K'
                else -> res.toString().first()
            }

            return dv == dvEsperado
        } catch (e: Exception) {
            return false
        }
    }
}