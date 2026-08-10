package com.mesawii.feature.empresas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mesawii.core.kidev.wiStore
import com.mesawii.feature.empresas.api.EmpresasApi
import com.mesawii.feature.empresas.api.SunatApi
import com.mesawii.feature.empresas.api.SunatRucResult
import com.mesawii.feature.empresas.data.CacheEmpresa
import com.mesawii.feature.empresas.data.EmpresaModelo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmpresaUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isBuscandoSunat: Boolean = false,
    val empresas: List<EmpresaModelo> = emptyList(),
    val empresaActiva: EmpresaModelo? = null,
    val empresaAEditar: EmpresaModelo? = null,
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

    fun cargarEmpresas(isRefreshManual: Boolean = false) {
        val smileId = obtenerSmileIdActivo()
        if (smileId.isBlank()) return

        if (isRefreshManual) {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
        } else {
            _uiState.value = _uiState.value.copy(isLoading = true)
        }

        viewModelScope.launch {
            val res = EmpresasApi.obtenerEmpresasPorSmile(smileId)
            res.fold(
                onSuccess = { lista ->
                    val activa = lista.firstOrNull { it.nombreComercial == cacheEmpresa.getNombreEmpresaActiva() }
                        ?: lista.firstOrNull()

                    if (activa != null) {
                        cacheEmpresa.guardarEmpresaActiva(activa)
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        empresas = lista,
                        empresaActiva = activa,
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
            val nueva = EmpresaModelo(
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
                estado = if (activo) "activo" else "inactivo"
            )

            val res = EmpresasApi.crearEmpresa(nueva)
            res.fold(
                onSuccess = { creada ->
                    cacheEmpresa.guardarEmpresaActiva(creada)
                    val listaActualizada = _uiState.value.empresas + creada
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

    fun prepararEdicion(empresa: EmpresaModelo) {
        _uiState.value = _uiState.value.copy(empresaAEditar = empresa)
    }

    fun cancelarEdicion() {
        _uiState.value = _uiState.value.copy(empresaAEditar = null)
    }

    fun guardarEdicion(empresa: EmpresaModelo, onExito: () -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val res = EmpresasApi.actualizarEmpresa(empresa)
            res.fold(
                onSuccess = { actualizada ->
                    val listaActualizada = _uiState.value.empresas.map {
                        if (it.id == actualizada.id) actualizada else it
                    }
                    val activaActualizada = if (_uiState.value.empresaActiva?.id == actualizada.id) actualizada else _uiState.value.empresaActiva

                    if (activaActualizada != null) {
                        cacheEmpresa.guardarEmpresaActiva(activaActualizada)
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaActualizada,
                        empresaActiva = activaActualizada,
                        empresaAEditar = null,
                        exitoMensaje = "¡Empresa '${actualizada.nombreComercial}' actualizada!"
                    )
                    cargarEmpresas()
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

    fun eliminarEmpresa(empresa: EmpresaModelo) {
        val id = empresa.id ?: return
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val res = EmpresasApi.eliminarEmpresa(id)
            res.fold(
                onSuccess = {
                    val listaFiltrada = _uiState.value.empresas.filter { it.id != id }
                    val nuevaActiva = if (_uiState.value.empresaActiva?.id == id) listaFiltrada.firstOrNull() else _uiState.value.empresaActiva

                    if (nuevaActiva != null) {
                        cacheEmpresa.guardarEmpresaActiva(nuevaActiva)
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
        empresa: EmpresaModelo,
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
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val actualizada = empresa.copy(
                nombreComercial = nombreComercial.trim(),
                direccion = direccion.trim(),
                telefono = telefono.trim(),
                ubigeo = ubigeo?.trim(),
                logo = logoUrl?.trim()
            )

            val res = EmpresasApi.actualizarEmpresa(actualizada)
            res.fold(
                onSuccess = { ok ->
                    cacheEmpresa.guardarEmpresaActiva(ok)
                    val listaActualizada = _uiState.value.empresas.map {
                        if (it.id == ok.id) ok else it
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresas = listaActualizada,
                        empresaActiva = ok,
                        exitoMensaje = "¡Ajustes de facturación de '${ok.nombreComercial}' guardados!"
                    )
                    cargarEmpresas()
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

    fun seleccionarEmpresa(empresa: EmpresaModelo) {
        cacheEmpresa.guardarEmpresaActiva(empresa)
        _uiState.value = _uiState.value.copy(
            empresaActiva = empresa,
            exitoMensaje = "Empresa '${empresa.nombreComercial}' seleccionada como activa"
        )
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(error = null, exitoMensaje = null)
    }
}
