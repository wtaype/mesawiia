package com.mesawii.core.data.supabase.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 😊 Smile.kt — Dataclass Mapeo 1:1 con la tabla public.smiles en Supabase.
 */
@Serializable
data class Smile(
    val id: String,
    val usuario: String,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val avatar: String? = null,
    val bio: String? = null,
    val estado: String = "activo",
    val activo: Boolean = true,
    val plan: String = "free",
    val rol: String = "todos",
    val segmento: String = "negocios",
    val tema: String = "Futuro",
    val terminos: Boolean = true,
    @SerialName("terminos_fecha") val terminosFecha: String? = null,
    val verificado: Boolean = false,
    @SerialName("registrado_por") val registradoPor: String = "correo",
    val creado: String? = null,
    val actualizado: String? = null
)
