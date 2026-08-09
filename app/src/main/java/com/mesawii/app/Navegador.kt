package com.mesawii.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * 🧭 NavegadorState.kt — Controlador de estado de navegación reactivo.
 * Ubicado en la raíz com.mesawii.app junto a MainViewModel.kt
 */
class NavegadorState(rutaInicial: String = "mesas") {
    var rutaActual by mutableStateOf(rutaInicial)
        private set

    var tabActivaIndex by mutableStateOf(0)
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
}

@Composable
fun rememberNavegador(rutaInicial: String = "mesas"): NavegadorState {
    return remember(rutaInicial) { NavegadorState(rutaInicial) }
}
