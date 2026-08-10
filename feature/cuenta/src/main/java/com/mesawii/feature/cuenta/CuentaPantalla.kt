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
import com.mesawii.feature.cuenta.tabs.AjustesTab
import com.mesawii.feature.cuenta.tabs.ContrasenaTab
import com.mesawii.feature.cuenta.tabs.GeneralTab
import com.mesawii.feature.cuenta.tabs.PerfilTab

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
            0 -> GeneralTab(
                uiState = uiState,
                onSeleccionarTema = { nombreTema ->
                    viewModel.cambiarTema(nombreTema, onTemaCambiado)
                },
                onCerrarSesion = {
                    viewModel.cerrarSesion(onCerrarSesion)
                }
            )
            1 -> PerfilTab(
                uiState = uiState,
                viewModel = viewModel
            )
            2 -> ContrasenaTab(
                uiState = uiState,
                viewModel = viewModel
            )
            3 -> AjustesTab(
                uiState = uiState,
                viewModel = viewModel
            )
            else -> GeneralTab(
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
