package com.mesawii.feature.empresas.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 EmpresaUpdateDto.kt — Objeto DTO fuertemente tipado para peticiones UPDATE en la tabla public.empresas de Supabase.
 * Garantiza la emisión nativa del tipo booleano en JSON para el campo `activo`.
 */
@Serializable
data class EmpresaUpdateDto(
    @SerialName("empresa") val nombreComercial: String,
    @SerialName("razon_social") val razonSocial: String,
    @SerialName("empresa_ruc") val ruc: String,
    val direccion: String = "",
    val telefono: String = "",
    val ubigeo: String = "",
    val logo: String = "",
    val activo: Boolean = true,
    val estado: String = "activo",
    val principal: Boolean = false
)
