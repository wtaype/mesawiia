package com.mesawii.feature.empresas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mesawii.core.data.supabase.api.EmpresasApi
import com.mesawii.core.data.supabase.api.SunatRucResult
import com.mesawii.core.data.supabase.modelo.Empresa
import com.mesawii.core.kidev.wiStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmpresaUiState(
    val isLoading: Boolean = false,
    val isBuscandoSunat: Boolean = false,
    val empresas: List<Empresa> = emptyList(),
    val empresaActiva: Empresa? = null,
    val datosSunat: SunatRucResult? = null,
    val error: String? = null,
    val exitoMensaje: String? = null
)

class EmpresaViewModel(application: Application) : AndroidViewModel(application) {
    private val store = wiStore(application)

    private val _uiState = MutableStateFlow(EmpresaUiState())
    val uiState: StateFlow<EmpresaUiState> = _uiState.asStateFlow()

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
                    val activa = lista.firstOrNull()
                    if (activa != null) {
                        store.save("empresa", activa.nombreComercial)
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
            val res = EmpresasApi.consultarRucSunat(ruc)
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
            val nueva = Empresa(
                smileId = smileId,
                ruc = ruc.trim(),
                razonSocial = razonSocial.trim(),
                nombreComercial = nombreComercial.trim(),
                direccion = direccion.trim(),
                telefono = telefono.trim(),
                moneda = moneda,
                ubigeo = ubigeo?.trim(),
                pinSol = pinSol?.trim()
            )

            val res = EmpresasApi.crearEmpresa(nueva)
            res.fold(
                onSuccess = { creada ->
                    store.save("empresa", creada.nombreComercial)
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

    fun seleccionarEmpresa(empresa: Empresa) {
        store.save("empresa", empresa.nombreComercial)
        _uiState.value = _uiState.value.copy(empresaActiva = empresa)
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(error = null, exitoMensaje = null)
    }
}
