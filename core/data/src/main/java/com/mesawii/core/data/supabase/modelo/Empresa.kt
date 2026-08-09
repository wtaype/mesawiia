package com.mesawii.core.data.supabase.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 Empresa.kt — Dataclass Mapeo 1:1 con la tabla public.empresas en Supabase.
 */
@Serializable
data class Empresa(
    val id: String = "",
    @SerialName("smile_id") val smileId: String = "",
    val ruc: String = "",
    @SerialName("razon_social") val razonSocial: String = "",
    @SerialName("nombre_comercial") val nombreComercial: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val moneda: String = "PEN",
    val ubigeo: String? = null,
    @SerialName("pin_sol") val pinSol: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    val creado: String? = null
)
