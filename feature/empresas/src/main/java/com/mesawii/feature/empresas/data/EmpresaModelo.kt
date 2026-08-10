package com.mesawii.feature.empresas.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 EmpresaModelo.kt — Modelo de dominio 1:1 de la entidad public.empresas Supabase.
 * Contiene la propiedad computada `esEmpresaActiva` que evalúa sincronizadamente `activo` (Boolean) y `estado` (String).
 */
@Serializable
data class EmpresaModelo(
    val id: String? = null,
    @SerialName("userId") val smileId: String? = null,
    @SerialName("empresa") val nombreComercial: String = "",
    @SerialName("razon_social") val razonSocial: String = "",
    @SerialName("empresa_ruc") val ruc: String = "",
    val direccion: String? = null,
    val telefono: String? = null,
    val celular: String? = null,
    val ubigeo: String? = null,
    val logo: String? = null,
    val pin: Boolean = false,
    val principal: Boolean = false,
    val activo: Boolean = true,
    val estado: String? = "activo",
    val creado: String? = null,
    val actualizado: String? = null
) {
    val esEmpresaActiva: Boolean
        get() = activo && (estado == null || estado.equals("activo", ignoreCase = true))
}
