package com.mesawii.feature.empresas.data

import android.content.Context
import com.mesawii.core.kidev.wiStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ⚡ CacheEmpresa.kt — Motor de caché local ultrarrápido (< 2ms) y emisión StateFlow del nombre de la empresa activa.
 */
class CacheEmpresa private constructor(context: Context) {
    private val store = wiStore(context)

    private val _empresaActivaNombreFlow = MutableStateFlow(store.getEmpresaNombre())
    val empresaActivaNombreFlow: StateFlow<String> = _empresaActivaNombreFlow.asStateFlow()

    private val _empresaActivaFlow = MutableStateFlow<EmpresaModelo?>(null)
    val empresaActivaFlow: StateFlow<EmpresaModelo?> = _empresaActivaFlow.asStateFlow()

    fun guardarEmpresaActiva(empresa: EmpresaModelo) {
        store.save("empresa", empresa.nombreComercial)
        _empresaActivaNombreFlow.value = empresa.nombreComercial.ifBlank { "Empresa" }
        _empresaActivaFlow.value = empresa
    }

    fun getNombreEmpresaActiva(): String {
        return store.getEmpresaNombre()
    }

    companion object {
        @Volatile
        private var INSTANCE: CacheEmpresa? = null

        fun getInstance(context: Context): CacheEmpresa {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CacheEmpresa(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
