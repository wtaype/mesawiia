package com.mesawii.feature.empresas.api

import com.mesawii.core.data.supabase.Cliente
import com.mesawii.feature.empresas.data.ModeloEmpresa
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 🏢 EmpresasApi.kt — Servicio remoto PostgREST oficial para public.empresas en Supabase exclusivo de feature/empresas.
 * Utiliza el DSL nativo `update { set(...) }` de Supabase Kotlin SDK para garantizar tipos booleanos strictly en PostgreSQL.
 */
object EmpresasApi {
    private val client get() = Cliente.instancia

    suspend fun obtenerEmpresasPorSmile(smileId: String): Result<List<ModeloEmpresa>> = withContext(Dispatchers.IO) {
        try {
            if (smileId.isBlank()) return@withContext Result.success(emptyList())
            val lista = client.postgrest["empresas"]
                .select { filter { eq("userId", smileId) } }
                .decodeList<ModeloEmpresa>()
            Result.success(lista)
        } catch (e: RestException) {
            Result.failure(Exception("Error de consulta en Supabase (${e.statusCode}): ${e.error}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearEmpresa(empresa: ModeloEmpresa): Result<ModeloEmpresa> = withContext(Dispatchers.IO) {
        try {
            val creada = client.postgrest["empresas"]
                .insert(empresa) { select() }
                .decodeSingle<ModeloEmpresa>()
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

    suspend fun actualizarEmpresa(empresa: ModeloEmpresa): Result<ModeloEmpresa> = withContext(Dispatchers.IO) {
        try {
            val idActualizar = empresa.id ?: return@withContext Result.failure(IllegalArgumentException("ID de empresa nulo"))
            
            // ⚡ DSL Nativo Oficial de Supabase Kotlin SDK `update { set(...) }`
            val desdedb = client.postgrest["empresas"].update({
                set("empresa", empresa.nombreComercial)
                set("razon_social", empresa.razonSocial)
                set("empresa_ruc", empresa.ruc)
                set("direccion", empresa.direccion ?: "")
                set("telefono", empresa.telefono ?: "")
                set("ubigeo", empresa.ubigeo ?: "")
                set("logo", empresa.logo ?: "")
                set("activo", empresa.activo)
                set("estado", empresa.estado ?: if (empresa.activo) "activo" else "inactivo")
                set("principal", empresa.principal)
                set("nota_venta", empresa.notaVenta)
                set("boleta", empresa.boleta)
                set("factura", empresa.factura)
            }) {
                filter { eq("id", idActualizar) }
                select()
            }.decodeSingle<ModeloEmpresa>()
            
            Result.success(desdedb)
        } catch (e: RestException) {
            Result.failure(Exception("Error de actualización en Supabase (${e.statusCode}): ${e.error}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarCampoBoolean(empresaId: String, campo: String, valor: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (empresaId.isBlank() || campo.isBlank()) return@withContext Result.failure(IllegalArgumentException("ID o campo vacío"))
            
            client.postgrest["empresas"].update({
                set(campo, valor)
            }) {
                filter { eq("id", empresaId) }
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun marcarEmpresaPrincipal(smileId: String, empresaId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (smileId.isBlank() || empresaId.isBlank()) return@withContext Result.failure(IllegalArgumentException("ID nulo"))
            
            // 1. Desmarcar todas las empresas del usuario
            client.postgrest["empresas"].update({
                set("principal", false)
            }) {
                filter { eq("userId", smileId) }
            }
            
            // 2. Marcar exclusivamente la seleccionada
            client.postgrest["empresas"].update({
                set("principal", true)
            }) {
                filter { eq("id", empresaId) }
            }
            Result.success(true)
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
