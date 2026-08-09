package com.mesawii.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mesawii.core.kicss.WiTemaColors
import com.mesawii.core.kidev.WiTemas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 🚀 MainViewModel — ViewModel principal ultra-liviano para MesaWii
 * Gestiona la reactividad del tema activo y coordina su persistencia con WiTemas (WiStore).
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentTema = MutableStateFlow<WiTemaColors>(WiTemas.getTemaInicial(application))
    val currentTema: StateFlow<WiTemaColors> = _currentTema.asStateFlow()

    /**
     * Resuelve la ruta inicial sincrónicamente en RAM (< 2ms) para evitar parpadeos
     */
    val rutaInicial: String = run {
        val store = com.mesawii.core.kidev.wiStore(application)
        val isFirstLaunch = store.getBool("is_first_launch", true)
        when {
            isFirstLaunch -> "bienvenida"
            store.hasSesion() -> "mesas"
            else -> "auth"
        }
    }

    /**
     * Sincroniza y guarda la nueva preferencia de tema en WiStore
     */
    fun setTema(tema: WiTemaColors) {
        val nuevoTema = WiTemas.saveTema(getApplication(), tema)
        _currentTema.value = nuevoTema
    }

    /**
     * Permite cambiar de tema especificando únicamente su nombre (ej. "Paz", "Futuro", "Dulce")
     */
    fun setTemaByName(nombreTema: String) {
        val nuevoTema = WiTemas.saveTema(getApplication(), nombreTema)
        _currentTema.value = nuevoTema
    }
}
