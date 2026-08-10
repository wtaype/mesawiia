package com.mesawii.feature.empresas.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 EmpresaModelo.kt — Modelo de dominio 1:1 de la entidad Empresa (public.empresas Supabase).
 */
@Serializable
data class EmpresaModelo(
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
