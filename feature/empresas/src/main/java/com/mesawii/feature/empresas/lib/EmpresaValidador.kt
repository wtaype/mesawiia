package com.mesawii.feature.empresas.lib

/**
 * 🛠️ EmpresaValidador.kt — Validaciones puras para RUC 11 dígitos, Ubigeo y Teléfonos.
 */
object EmpresaValidador {
    fun esRucValido(ruc: String): Boolean {
        val r = ruc.trim()
        return r.length == 11 && r.all { it.isDigit() } && (r.startsWith("10") || r.startsWith("20") || r.startsWith("15") || r.startsWith("17"))
    }

    fun esUbigeoValido(ubigeo: String): Boolean {
        val u = ubigeo.trim()
        return u.isBlank() || (u.length == 6 && u.all { it.isDigit() })
    }

    fun esTelefonoValido(telefono: String): Boolean {
        val t = telefono.trim()
        return t.isBlank() || (t.length >= 6 && t.all { it.isDigit() || it == '+' || it == ' ' || it == '-' })
    }
}
