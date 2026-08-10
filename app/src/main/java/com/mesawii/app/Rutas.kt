package com.mesawii.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 🧭 RutasState — Controlador de estado de navegación reactivo y ultra-rápido (< 0.1ms).
 * Fusionado en Rutas.kt para mantener un estado limpio sin sobrecarga.
 */
class RutasState(rutaInicial: String = "empresas") {
    var rutaActual by mutableStateOf(rutaInicial)
        private set

    var tabActivaIndex by mutableStateOf(0)
        private set

    var paddingHorizontal by mutableStateOf(10.dp)
        private set

    var paddingVertical by mutableStateOf(6.dp)
        private set

    fun navegarA(nuevaRuta: String) {
        if (rutaActual != nuevaRuta) {
            rutaActual = nuevaRuta
            tabActivaIndex = 0
        }
    }

    fun seleccionarTab(index: Int) {
        tabActivaIndex = index
    }

    fun setContentPadding(horizontal: Dp = 10.dp, vertical: Dp = 6.dp) {
        paddingHorizontal = horizontal
        paddingVertical = vertical
    }
}

@Composable
fun rememberRutas(rutaInicial: String = "empresas"): RutasState {
    return remember(rutaInicial) { RutasState(rutaInicial) }
}

/**
 * 🗺️ Rutas — Router dinámico derivado de Seo.kt.
 */
object Rutas {
    /** Lista de rutas navegables principales para la Sidebar */
    val RUTAS_SIDEBAR: List<MetaRuta> = Seo.METADATOS.values.filter { it.esNavPrincipal }

    /** Obtiene los metadatos de una ruta o retorna el valor por defecto */
    fun getMeta(key: String): MetaRuta = Seo.METADATOS[key] ?: Seo.DEFAULT
}
