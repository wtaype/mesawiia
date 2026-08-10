package com.mesawii.feature.empresas.api

import com.mesawii.core.data.supabase.Cliente
import com.mesawii.feature.empresas.data.EmpresaModelo
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 🏢 EmpresasApi.kt — Servicio remoto PostgREST para la tabla public.empresas en Supabase exclusivo de feature/empresas.
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
        } catch (e: Exception) {
            Result.failure(e)
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
