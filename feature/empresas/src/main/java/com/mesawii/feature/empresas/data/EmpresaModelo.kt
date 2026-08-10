package com.mesawii.feature.empresas.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 🏢 EmpresaModelo.kt — Modelo de dominio 1:1 de la entidad public.empresas Supabase.
 * Mapea los nombres de columna de C:\mipro\mesawii\recursos-mesawii\tablas\empresa-tabla.txt
 */
@Serializable
data class EmpresaModelo(
    val id: String? = null,                       // 📌 null al crear para que Postgres ejecute gen_random_uuid()
    @SerialName("userId") val smileId: String? = null,
    @SerialName("empresa") val nombreComercial: String = "",
    @SerialName("razon_social") val razonSocial: String = "",
    @SerialName("empresa_ruc") val ruc: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val celular: String = "",
    val ubigeo: String? = null,
    val logo: String? = null,
    val pin: Boolean = false,
    val principal: Boolean = false,
    val activo: Boolean = true,
    val estado: String = "activo",
    val creado: String? = null,
    val actualizado: String? = null
)
