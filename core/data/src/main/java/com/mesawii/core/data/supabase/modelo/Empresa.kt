package com.mesawii.core.data.supabase.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 Empresa.kt — Dataclass Mapeo 1:1 con la tabla public.empresas en Supabase.
 */
@Serializable
data class Empresa(
    val id: String? = null,
    @SerialName("userId") val userId: String,
    val empresa: String,
    @SerialName("razon_social") val razonSocial: String? = null,
    @SerialName("empresa_ruc") val empresaRuc: String? = null,
    val direccion: String? = null,
    val telefono: String? = null,
    val celular: String? = null,
    val ubigeo: String? = null,
    val logo: String? = null,
    val pin: Boolean = false,
    val principal: Boolean = false,
    val activo: Boolean = true,
    val estado: String = "activo",
    val creado: String? = null,
    val actualizado: String? = null
)
