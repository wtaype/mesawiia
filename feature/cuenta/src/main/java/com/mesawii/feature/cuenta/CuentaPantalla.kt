package com.mesawii.feature.cuenta

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mesawii.core.kicss.WiTemaColors
import com.mesawii.core.kidev.WiDialog
import com.mesawii.feature.cuenta.tabs.Ajustes
import com.mesawii.feature.cuenta.tabs.Contrasena
import com.mesawii.feature.cuenta.tabs.General
import com.mesawii.feature.cuenta.tabs.Perfil

@Composable
fun CuentaPantalla(
    tabActivaIndex: Int = 0,
    onCambiarTab: (Int) -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onTemaCambiado: (WiTemaColors) -> Unit = {},
    viewModel: CuentaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (tabActivaIndex) {
            0 -> General(
                uiState = uiState,
                onSeleccionarTema = { nombreTema ->
                    viewModel.cambiarTema(nombreTema, onTemaCambiado)
                },
                onCerrarSesion = {
                    viewModel.cerrarSesion(onCerrarSesion)
                }
            )
            1 -> Perfil(
                uiState = uiState,
                viewModel = viewModel
            )
            2 -> Contrasena(
                uiState = uiState,
                viewModel = viewModel
            )
            3 -> Ajustes(
                uiState = uiState,
                viewModel = viewModel
            )
            else -> General(
                uiState = uiState,
                onSeleccionarTema = { nombreTema ->
                    viewModel.cambiarTema(nombreTema, onTemaCambiado)
                },
                onCerrarSesion = {
                    viewModel.cerrarSesion(onCerrarSesion)
                }
            )
        }

        // Modal Informativo para Funcionalidades a Futuro (Planes, Pagos, etc.)
        WiDialog(
            show = uiState.mostrarModalFuturo,
            title = uiState.tituloModalFuturo.ifBlank { "Próximamente" },
            text = uiState.mensajeModalFuturo,
            confirmText = "Entendido",
            dismissText = "",
            onConfirm = { viewModel.cerrarModalFuturo() },
            onDismiss = { viewModel.cerrarModalFuturo() }
        )
    }
}
