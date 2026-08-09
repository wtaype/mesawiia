package com.mesawii.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 🧭 NavegadorState.kt — Controlador de estado de navegación y diseño reactivo.
 * Permite controlar la ruta activa y el padding de contenido central desde layout/mainViewModel.
 */
class NavegadorState(rutaInicial: String = "mesas") {
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
fun rememberNavegador(rutaInicial: String = "mesas"): NavegadorState {
    return remember(rutaInicial) { NavegadorState(rutaInicial) }
}
