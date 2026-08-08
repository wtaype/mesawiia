// WiPerfil.kt — Modelo de perfil de usuario para MesaWii
package com.mesawii.core.kidev

import com.mesawii.core.kicss.*

import androidx.compose.runtime.Immutable

/**
 * WiPerfil — Datos del usuario activo en sesión.
 * Reemplaza al modelo Smile del proyecto ChatWii.
 */
@Immutable
data class WiPerfil(
    val id:     String = "",
    val nombre: String = "",
    val email:  String = "",
    val rol:    String = "",
    val avatar: String = "",   // URL o iniciales
)
