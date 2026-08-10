package com.mesawii.feature.empresas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mesawii.core.kidev.wiStore
import com.mesawii.feature.empresas.api.EmpresasApi
import com.mesawii.feature.empresas.api.SunatApi
import com.mesawii.feature.empresas.api.SunatRucResult
import com.mesawii.feature.empresas.data.CacheEmpresa
import com.mesawii.feature.empresas.data.ModeloEmpresa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmpresaUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isBuscandoSunat: Boolean = false,
    val empresas: List<ModeloEmpresa> = emptyList(),
    val empresaActiva: ModeloEmpresa? = null,
    val empresaAEditar: ModeloEmpresa? = null,
    val datosSunat: SunatRucResult? = null,
    val error: String? = null,
    val exitoMensaje: String? = null
)

class EmpresaViewModel(application: Application) : AndroidViewModel(application) {
    private val store = wiStore(application)
    private val cacheEmpresa = CacheEmpresa.getInstance(application)

    private val _uiState = MutableStateFlow(EmpresaUiState())
    val uiState: StateFlow<EmpresaUiState> = _uiState.asStateFlow()

    val nombreEmpresaActivaFlow: StateFlow<String> = cacheEmpresa.empresaActivaNombreFlow

    init {
        cargarEmpresas()
    }

    private fun obtenerSmileIdActivo(): String {
        val smileJson = store.getSmileJson()
        val smileIdFromObject = smileJson?.optString("id", "")
        if (!smileIdFromObject.isNullOrBlank()) {
            return smileIdFromObject
        }
        return store.get("wiToken")
    }

    /**
     * ⚡ Local-First 0ms Architecture:
     * 1. Lee la caché local `empresas_lista_$smileId` inmediatamente en < 1ms (sin spinner).
     * 2. Lanza sincronización en segundo plano con Supabase y actualiza la caché silenciosamente.
     */
    fun cargarEmpresas(isRefreshManual: Boolean = false) {
        val smileId = obtenerSmileIdActivo()
        if (smileId.isBlank()) return

        // ⚡ 1. Carga Local Instantánea (0ms Latencia - Cero Pantalla de Carga)
        val listaCache = cacheEmpresa.obtenerListaEmpresas(smileId)
        if (listaCache.isNotEmpty()) {
            val activaCache = cacheEmpresa.obtenerEmpresaActiva(smileId)
                ?: listaCache.firstOrNull { it.principal }
                ?: listaCache.firstOrNull { it.nombreComercial == cacheEmpresa.getNombreEmpresaActiva() }
                ?: listaCache.firstOrNull()

            if (activaCache != null) {
                cacheEmpresa.guardarEmpresaActiva(activaCache, smileId)
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isRefreshing = isRefreshManual,
                empresas = listaCache,
                empresaActiva = activaCache
            )
        } else {
            if (!isRefreshManual) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }

        // ⚡ 2. Sincronización en Segundo Plano con Supabase
        viewModelScope.launch {
            val res = EmpresasApi.obtenerEmpresasPorSmile(smileId)
            res.fold(
                onSuccess = { listaFresca ->
                    cacheEmpresa.guardarListaEmpresas(smileId, listaFresca)

                    val activaFresca = listaFresca.firstOrNull { it.principal }
                        ?: listaFresca.firstOrNull { it.nombreComercial == cacheEmpresa.getNombreEmpresaActiva() }
                        ?: listaFresca.firstOrNull()

                    if (activaFresca != null) {
                        cacheEmpresa.guardarEmpresaActiva(activaFresca, smileId)
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        empresas = listaFresca,
                        empresaActiva = activaFresca,
                        exitoMensaje = if (isRefreshManual) "¡Lista de empresas actualizada!" else _uiState.value.exitoMensaje
                    )
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            )
        }
    }

    fun consultarSunat(ruc: String, onResultado: (SunatRucResult) -> Unit) {
        if (ruc.length != 11) return
        _uiState.value = _uiState.value.copy(isBuscandoSunat = true)

        viewModelScope.launch {
            val res = SunatApi.consultarRucSunat(ruc)
            _uiState.value = _uiState.value.copy(isBuscandoSunat = false)
            res.onSuccess { data ->
                _uiState.value = _uiState.value.copy(datosSunat = data)
                onResultado(data)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    error = err.localizedMessage ?: "No se pudieron obtener datos del RUC en SUNAT"
                )
            }
        }
    }

    fun crearEmpresa(
        nombreComercial: String,
        ruc: String,
        razonSocial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String? = null,
        pinSol: String? = null,
        logoUrl: String? = null,
        activo: Boolean = true,
        onExito: () -> Unit
    ) {
        val smileId = obtenerSmileIdActivo()
        if (smileId.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No se encontró una sesión activa de usuario wiSmile")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val nueva = ModeloEmpresa(
                id = null,
                smileId = smileId,
                ruc = ruc.trim(),
                razonSocial = razonSocial.trim(),
                nombreComercial = nombreComercial.trim(),
                direccion = direccion.trim(),
                telefono = telefono.trim(),
                ubigeo = ubigeo?.trim(),
                logo = logoUrl?.trim(),
                activo = activo,
                estado = if (activo) "activo" else "inactivo",
                principal = _uiState.value.empresas.isEmpty(),
                notaVenta = true,
                boleta = true,
                factura = true
            )

            val res = EmpresasApi.crearEmpresa(nueva)
            res.fold(
                onSuccess = { creada ->
                    val listaActualizada = _uiState.value.empresas + creada
                    cacheEmpresa.guardarListaEmpresas(smileId, listaActualizada)
                    cacheEmpresa.guardarEmpresaActiva(creada, smileId)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaActualizada,
                        empresaActiva = creada,
                        exitoMensaje = "¡Empresa '${creada.nombreComercial}' registrada con éxito!"
                    )
                    cargarEmpresas()
                    onExito()
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al registrar la empresa en Supabase"
                    )
                }
            )
        }
    }

    fun prepararEdicion(empresa: ModeloEmpresa) {
        _uiState.value = _uiState.value.copy(empresaAEditar = empresa)
    }

    fun cancelarEdicion() {
        _uiState.value = _uiState.value.copy(empresaAEditar = null)
    }

    fun guardarEdicion(empresa: ModeloEmpresa, onExito: () -> Unit) {
        val smileId = obtenerSmileIdActivo()
        val listaOptimista = _uiState.value.empresas.map {
            if (it.id == empresa.id) empresa else it
        }
        val activaOptimista = if (_uiState.value.empresaActiva?.id == empresa.id) empresa else _uiState.value.empresaActiva

        if (smileId.isNotBlank()) {
            cacheEmpresa.guardarListaEmpresas(smileId, listaOptimista)
            if (activaOptimista != null) {
                cacheEmpresa.guardarEmpresaActiva(activaOptimista, smileId)
            }
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            empresas = listaOptimista,
            empresaActiva = activaOptimista
        )

        viewModelScope.launch {
            val res = EmpresasApi.actualizarEmpresa(empresa)
            res.fold(
                onSuccess = { actualizada ->
                    val listaActualizada = _uiState.value.empresas.map {
                        if (it.id == actualizada.id) actualizada else it
                    }
                    val activaActualizada = if (_uiState.value.empresaActiva?.id == actualizada.id) actualizada else _uiState.value.empresaActiva

                    if (smileId.isNotBlank()) {
                        cacheEmpresa.guardarListaEmpresas(smileId, listaActualizada)
                        if (activaActualizada != null) {
                            cacheEmpresa.guardarEmpresaActiva(activaActualizada, smileId)
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaActualizada,
                        empresaActiva = activaActualizada,
                        empresaAEditar = null,
                        exitoMensaje = "¡Empresa '${actualizada.nombreComercial}' actualizada!"
                    )
                    onExito()
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al actualizar la empresa"
                    )
                }
            )
        }
    }

    fun toggleCampoEmpresa(empresa: ModeloEmpresa, campo: String, nuevoValor: Boolean) {
        val id = empresa.id ?: return
        val smileId = obtenerSmileIdActivo()

        val empresaModificada = when (campo) {
            "nota_venta" -> empresa.copy(notaVenta = nuevoValor)
            "boleta" -> empresa.copy(boleta = nuevoValor)
            "factura" -> empresa.copy(factura = nuevoValor)
            "activo" -> empresa.copy(activo = nuevoValor, estado = if (nuevoValor) "activo" else "inactivo")
            else -> empresa
        }

        val nombreCampo = when (campo) {
            "nota_venta" -> "Nota de Venta"
            "boleta" -> "Boleta Electrónica"
            "factura" -> "Factura Electrónica"
            "activo" -> "Operatividad de Empresa"
            else -> campo
        }

        val estadoTexto = if (nuevoValor) "activada" else "desactivada"

        // ⚡ 1. Actualización Local Instantánea (< 1ms)
        val listaActualizada = _uiState.value.empresas.map {
            if (it.id == id) empresaModificada else it
        }
        val activaActualizada = if (_uiState.value.empresaActiva?.id == id) empresaModificada else _uiState.value.empresaActiva

        if (smileId.isNotBlank()) {
            cacheEmpresa.guardarListaEmpresas(smileId, listaActualizada)
            if (activaActualizada != null) {
                cacheEmpresa.guardarEmpresaActiva(activaActualizada, smileId)
            }
        }

        _uiState.value = _uiState.value.copy(
            empresas = listaActualizada,
            empresaActiva = activaActualizada,
            exitoMensaje = "¡$nombreCampo $estadoTexto para ${empresa.nombreComercial}!"
        )

        // ⚡ 2. Persistencia en Supabase en Segundo Plano
        viewModelScope.launch {
            EmpresasApi.actualizarCampoBoolean(id, campo, nuevoValor)
        }
    }

    fun eliminarEmpresa(empresa: ModeloEmpresa) {
        val id = empresa.id ?: return
        val smileId = obtenerSmileIdActivo()
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val res = EmpresasApi.eliminarEmpresa(id)
            res.fold(
                onSuccess = {
                    val listaFiltrada = _uiState.value.empresas.filter { it.id != id }
                    val nuevaActiva = if (_uiState.value.empresaActiva?.id == id) listaFiltrada.firstOrNull() else _uiState.value.empresaActiva

                    if (smileId.isNotBlank()) {
                        cacheEmpresa.guardarListaEmpresas(smileId, listaFiltrada)
                        if (nuevaActiva != null) {
                            cacheEmpresa.guardarEmpresaActiva(nuevaActiva, smileId)
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaFiltrada,
                        empresaActiva = nuevaActiva,
                        exitoMensaje = "Empresa '${empresa.nombreComercial}' eliminada correctamente"
                    )
                    cargarEmpresas()
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al eliminar empresa"
                    )
                }
            )
        }
    }

    fun guardarAjustesEmpresa(
        empresa: ModeloEmpresa,
        nombreComercial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String?,
        pinSol: String?,
        logoUrl: String?,
        aceptaNotaVenta: Boolean,
        aceptaBoleta: Boolean,
        aceptaFactura: Boolean,
        formatoTicketera: String,
        onExito: () -> Unit
    ) {
        val smileId = obtenerSmileIdActivo()
        val actualizada = empresa.copy(
            nombreComercial = nombreComercial.trim(),
            direccion = direccion.trim(),
            telefono = telefono.trim(),
            ubigeo = ubigeo?.trim(),
            logo = logoUrl?.trim(),
            notaVenta = aceptaNotaVenta,
            boleta = aceptaBoleta,
            factura = aceptaFactura
        )

        val listaOptimista = _uiState.value.empresas.map {
            if (it.id == actualizada.id) actualizada else it
        }
        val activaOptimista = if (_uiState.value.empresaActiva?.id == actualizada.id) actualizada else _uiState.value.empresaActiva

        if (smileId.isNotBlank()) {
            cacheEmpresa.guardarListaEmpresas(smileId, listaOptimista)
            if (activaOptimista != null) {
                cacheEmpresa.guardarEmpresaActiva(activaOptimista, smileId)
            }
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            empresas = listaOptimista,
            empresaActiva = activaOptimista
        )

        viewModelScope.launch {
            val res = EmpresasApi.actualizarEmpresa(actualizada)
            res.fold(
                onSuccess = { ok ->
                    val listaActualizada = _uiState.value.empresas.map {
                        if (it.id == ok.id) ok else it
                    }
                    if (smileId.isNotBlank()) {
                        cacheEmpresa.guardarListaEmpresas(smileId, listaActualizada)
                        cacheEmpresa.guardarEmpresaActiva(ok, smileId)
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaActualizada,
                        empresaActiva = ok,
                        exitoMensaje = "¡Ajustes de facturación de '${ok.nombreComercial}' guardados!"
                    )
                    onExito()
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al actualizar los ajustes de la empresa"
                    )
                }
            )
        }
    }

    fun seleccionarEmpresa(empresa: ModeloEmpresa) {
        val idSeleccionado = empresa.id ?: return
        val smileId = obtenerSmileIdActivo()

        val empresaConPrincipal = empresa.copy(principal = true)
        val listaActualizada = _uiState.value.empresas.map { item ->
            val esSeleccionada = (item.id == idSeleccionado)
            item.copy(principal = esSeleccionada)
        }

        if (smileId.isNotBlank()) {
            cacheEmpresa.guardarListaEmpresas(smileId, listaActualizada)
            cacheEmpresa.guardarEmpresaActiva(empresaConPrincipal, smileId)
        }

        _uiState.value = _uiState.value.copy(
            empresas = listaActualizada,
            empresaActiva = empresaConPrincipal,
            exitoMensaje = "Empresa '${empresa.nombreComercial}' seleccionada como ACTUAL"
        )

        if (smileId.isNotBlank()) {
            viewModelScope.launch {
                EmpresasApi.marcarEmpresaPrincipal(smileId, idSeleccionado)
            }
        }
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(error = null, exitoMensaje = null)
    }
}
