package com.mesawii.feature.empresas.api

import com.mesawii.core.data.supabase.Cliente
import com.mesawii.feature.empresas.data.EmpresaModelo
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 🏢 EmpresasApi.kt — Servicio remoto PostgREST para la tabla public.empresas en Supabase exclusivo de feature/empresas.
 * Captura excepciones RLS y de red para presentar mensajes amigables en español.
 */
object EmpresasApi {
    private val client get() = Cliente.instancia

    suspend fun obtenerEmpresasPorSmile(smileId: String): Result<List<EmpresaModelo>> = withContext(Dispatchers.IO) {
        try {
            if (smileId.isBlank()) return@withContext Result.success(emptyList())
            val lista = client.postgrest["empresas"]
                .select { filter { eq("userId", smileId) } }
                .decodeList<EmpresaModelo>()
            Result.success(lista)
        } catch (e: RestException) {
            Result.failure(Exception("Error de consulta en Supabase (${e.statusCode}): ${e.error}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearEmpresa(empresa: EmpresaModelo): Result<EmpresaModelo> = withContext(Dispatchers.IO) {
        try {
            val creada = client.postgrest["empresas"]
                .insert(empresa) { select() }
                .decodeSingle<EmpresaModelo>()
            Result.success(creada)
        } catch (e: RestException) {
            Result.failure(Exception("Error RLS en Supabase (${e.statusCode}): La tabla public.empresas rechazó el registro por políticas RLS."))
        } catch (e: Exception) {
            val rawMsg = e.localizedMessage ?: ""
            val msgLimpio = if (rawMsg.contains("249") || rawMsg.contains("Expected string") || rawMsg.contains("JSON token")) {
                "Error RLS 42501: Supabase rechazó la inserción en la tabla empresas. Revisa las políticas RLS en Supabase."
            } else {
                rawMsg.ifBlank { "Error al registrar la empresa en Supabase" }
            }
            Result.failure(Exception(msgLimpio))
        }
    }

    suspend fun actualizarEmpresa(empresa: EmpresaModelo): Result<EmpresaModelo> = withContext(Dispatchers.IO) {
        try {
            val idActualizar = empresa.id ?: return@withContext Result.failure(IllegalArgumentException("ID de empresa nulo"))
            val actualizada = client.postgrest["empresas"]
                .update(empresa) {
                    filter { eq("id", idActualizar) }
                    select()
                }
                .decodeSingle<EmpresaModelo>()
            Result.success(actualizada)
        } catch (e: RestException) {
            Result.failure(Exception("Error de actualización en Supabase (${e.statusCode}): ${e.error}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarEmpresa(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (id.isBlank()) return@withContext Result.failure(IllegalArgumentException("ID de empresa vacío"))
            client.postgrest["empresas"].delete {
                filter { eq("id", id) }
            }
            Result.success(true)
        } catch (e: RestException) {
            Result.failure(Exception("Error al eliminar empresa en Supabase (${e.statusCode}): ${e.error}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
