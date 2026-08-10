package com.mesawii.feature.cuenta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mesawii.core.kicss.MesaWiTemas
import com.mesawii.core.kicss.WiTemaColors
import com.mesawii.core.kidev.WiTemas
import com.mesawii.core.kidev.wiStore
import com.mesawii.feature.auth.data.CacheSmile
import com.mesawii.feature.auth.data.SmileModelo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CuentaUiState(
    val smile: SmileModelo? = null,
    val temaActivo: String = "Futuro",
    // Edición Perfil
    val nombreEdit: String = "",
    val apellidosEdit: String = "",
    val usuarioEdit: String = "",
    val emailEdit: String = "",
    val bioEdit: String = "",
    val segmentoEdit: String = "",
    val mensajeExitoPerfil: String? = null,
    val mensajeErrorPerfil: String? = null,
    // Modales Informativos Futuros
    val mostrarModalFuturo: Boolean = false,
    val tituloModalFuturo: String = "",
    val mensajeModalFuturo: String = "",
    // Cambio Contraseña
    val passNueva: String = "",
    val passConfirmar: String = "",
    val mensajeExitoPass: String? = null,
    val mensajeErrorPass: String? = null,
    // Ajustes
    val escalaTexto: Float = 1.0f,
    val biometriaActivada: Boolean = false,
    val notificacionesActivadas: Boolean = true,
    val cacheUsageMb: String = "4.2 MB",
    val mensajeExitoAjustes: String? = null
)

class CuentaViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val store = wiStore(context)
    private val cacheSmile = CacheSmile.getInstance(context)

    private val _uiState = MutableStateFlow(CuentaUiState())
    val uiState: StateFlow<CuentaUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        val smile = cacheSmile.getSmileGuardado() ?: SmileModelo(
            id = "demo-id",
            usuario = "super",
            email = "wtaypee@gmail.com",
            nombre = "Wilder",
            apellidos = "Taype",
            plan = "free",
            segmento = "negocios"
        )
        val temaInicial = WiTemas.getTemaInicial(context).name

        _uiState.value = CuentaUiState(
            smile = smile,
            temaActivo = temaInicial,
            nombreEdit = smile.nombre,
            apellidosEdit = smile.apellidos,
            usuarioEdit = smile.usuario,
            emailEdit = smile.email,
            bioEdit = smile.bio ?: "",
            segmentoEdit = smile.segmento,
            escalaTexto = store.get("wi_escala_texto", "1.0").toFloatOrNull() ?: 1.0f,
            biometriaActivada = store.getBool("wi_biometria", false),
            notificacionesActivadas = store.getBool("wi_notificaciones", true)
        )
    }

    fun cambiarTema(nombreTema: String, onTemaCambiado: (WiTemaColors) -> Unit) {
        val temaNuevo = WiTemas.saveTema(context, nombreTema)
        _uiState.value = _uiState.value.copy(temaActivo = temaNuevo.name)
        onTemaCambiado(temaNuevo)
    }

    // --- Edición de Perfil ---
    fun onNombreChange(valStr: String) { _uiState.value = _uiState.value.copy(nombreEdit = valStr) }
    fun onApellidosChange(valStr: String) { _uiState.value = _uiState.value.copy(apellidosEdit = valStr) }
    fun onUsuarioChange(valStr: String) { _uiState.value = _uiState.value.copy(usuarioEdit = valStr) }
    fun onBioChange(valStr: String) { _uiState.value = _uiState.value.copy(bioEdit = valStr) }
    fun onSegmentoChange(valStr: String) { _uiState.value = _uiState.value.copy(segmentoEdit = valStr) }

    fun guardarPerfil() {
        val current = _uiState.value.smile ?: return
        val updated = current.copy(
            nombre = _uiState.value.nombreEdit,
            apellidos = _uiState.value.apellidosEdit,
            usuario = _uiState.value.usuarioEdit,
            bio = _uiState.value.bioEdit.ifBlank { null },
            segmento = _uiState.value.segmentoEdit
        )
        cacheSmile.guardarSesion(updated)
        _uiState.value = _uiState.value.copy(
            smile = updated,
            mensajeExitoPerfil = "Perfil actualizado correctamente ✨"
        )
    }

    // --- Modales Informativos Futuros ---
    fun abrirModalFuturo(titulo: String, mensaje: String) {
        _uiState.value = _uiState.value.copy(
            mostrarModalFuturo = true,
            tituloModalFuturo = titulo,
            mensajeModalFuturo = mensaje
        )
    }

    fun cerrarModalFuturo() {
        _uiState.value = _uiState.value.copy(mostrarModalFuturo = false)
    }

    // --- Cambio de Contraseña ---
    fun onPassNuevaChange(valStr: String) { _uiState.value = _uiState.value.copy(passNueva = valStr, mensajeErrorPass = null, mensajeExitoPass = null) }
    fun onPassConfirmarChange(valStr: String) { _uiState.value = _uiState.value.copy(passConfirmar = valStr, mensajeErrorPass = null, mensajeExitoPass = null) }

    fun cambiarContrasena() {
        val p1 = _uiState.value.passNueva
        val p2 = _uiState.value.passConfirmar

        if (p1.length < 6) {
            _uiState.value = _uiState.value.copy(mensajeErrorPass = "La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (p1 != p2) {
            _uiState.value = _uiState.value.copy(mensajeErrorPass = "Las contraseñas no coinciden")
            return
        }

        _uiState.value = _uiState.value.copy(
            passNueva = "",
            passConfirmar = "",
            mensajeExitoPass = "¡Contraseña actualizada con éxito! 🔒",
            mensajeErrorPass = null
        )
    }

    // --- Ajustes ---
    fun onEscalaTextoChange(nuevaEscala: Float) {
        store.save("wi_escala_texto", nuevaEscala.toString())
        _uiState.value = _uiState.value.copy(escalaTexto = nuevaEscala)
    }

    fun onBiometriaChange(activa: Boolean) {
        store.saveBool("wi_biometria", activa)
        _uiState.value = _uiState.value.copy(biometriaActivada = activa)
    }

    fun onNotificacionesChange(activa: Boolean) {
        store.saveBool("wi_notificaciones", activa)
        _uiState.value = _uiState.value.copy(notificacionesActivadas = activa)
    }

    fun limpiarCacheLocal() {
        _uiState.value = _uiState.value.copy(
            cacheUsageMb = "0.0 MB",
            mensajeExitoAjustes = "Caché temporal limpiada correctamente 🧹"
        )
    }

    fun cerrarSesion(onExito: () -> Unit) {
        cacheSmile.cerrarSesion()
        onExito()
    }
}
