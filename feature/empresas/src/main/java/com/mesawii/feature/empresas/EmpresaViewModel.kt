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
    val isBuscandoSunat: Boolean = false,
    val empresas: List<EmpresaModelo> = emptyList(),
    val empresaActiva: EmpresaModelo? = null,
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

    fun cargarEmpresas() {
        val smileJson = store.getSmileJson()
        val smileId = smileJson?.optString("id", "") ?: store.get("wiToken")
        if (smileId.isBlank()) return

        _uiState.value = _uiState.value.copy(isLoading = true)

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
                        empresas = lista,
                        empresaActiva = activa
                    )
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al cargar empresas"
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
        onExito: () -> Unit
    ) {
        val smileJson = store.getSmileJson()
        val smileId = smileJson?.optString("id", "") ?: store.get("wiToken")
        if (smileId.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No se encontró sesión activa de usuario")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val nueva = EmpresaModelo(
                smileId = smileId,
                ruc = ruc.trim(),
                razonSocial = razonSocial.trim(),
                nombreComercial = nombreComercial.trim(),
                direccion = direccion.trim(),
                telefono = telefono.trim(),
                moneda = moneda,
                ubigeo = ubigeo?.trim(),
                pinSol = pinSol?.trim(),
                logoUrl = logoUrl?.trim()
            )

            val res = EmpresasApi.crearEmpresa(nueva)
            res.fold(
                onSuccess = { creada ->
                    cacheEmpresa.guardarEmpresaActiva(creada)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresaActiva = creada,
                        exitoMensaje = "¡Empresa '${creada.nombreComercial}' creada con éxito!"
                    )
                    cargarEmpresas()
                    onExito()
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Error al registrar la empresa"
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
        onExito: () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val actualizada = empresa.copy(
                nombreComercial = nombreComercial.trim(),
                direccion = direccion.trim(),
                telefono = telefono.trim(),
                moneda = moneda,
                ubigeo = ubigeo?.trim(),
                pinSol = pinSol?.trim(),
                logoUrl = logoUrl?.trim()
            )

            val res = EmpresasApi.actualizarEmpresa(actualizada)
            res.fold(
                onSuccess = { ok ->
                    cacheEmpresa.guardarEmpresaActiva(ok)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        empresaActiva = ok,
                        exitoMensaje = "¡Ajustes de la empresa actualizados!"
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

    fun seleccionarEmpresa(empresa: EmpresaModelo) {
        cacheEmpresa.guardarEmpresaActiva(empresa)
        _uiState.value = _uiState.value.copy(empresaActiva = empresa)
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(error = null, exitoMensaje = null)
    }
}
