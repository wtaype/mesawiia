package com.mesawii.feature.empresas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mesawii.core.kidev.FadeMain
import com.mesawii.core.kidev.WiDialog
import com.mesawii.core.kidev.WiMessengerHost
import com.mesawii.core.kidev.WiMsgType
import com.mesawii.core.kidev.rememberWiMessenger
import com.mesawii.feature.empresas.data.EmpresaModelo
import com.mesawii.feature.empresas.tabs.AjustesEmpresaTab
import com.mesawii.feature.empresas.tabs.MisEmpresasTab
import com.mesawii.feature.empresas.tabs.NuevoEmpresaTab

/**
 * 🏢 EmpresaPantalla.kt — Pantalla Principal del Módulo Empresas & Negocios.
 * Integrada con WiMessengerHost de kidev para notificaciones flotantes premium estilo Apple.
 */
@Composable
fun EmpresaPantalla(
    tabActivaIndex: Int = 0,
    onCambiarTab: (Int) -> Unit = {},
    onEmpresaSeleccionada: () -> Unit = {},
    viewModel: EmpresaViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val messenger = rememberWiMessenger()

    var empresaAEliminar by remember { mutableStateOf<EmpresaModelo?>(null) }

    LaunchedEffect(uiState.exitoMensaje, uiState.error) {
        uiState.exitoMensaje?.let { msg ->
            messenger.Notificacion(msg, type = WiMsgType.Success)
            viewModel.limpiarMensajes()
        }
        uiState.error?.let { err ->
            messenger.Notificacion("❌ $err", type = WiMsgType.Error)
            viewModel.limpiarMensajes()
        }
    }

    if (empresaAEliminar != null) {
        WiDialog(
            show = true,
            title = "Eliminar Empresa",
            text = "¿Estás seguro de que deseas eliminar '${empresaAEliminar?.nombreComercial}'? Esta acción no se puede deshacer.",
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            onConfirm = {
                empresaAEliminar?.let { viewModel.eliminarEmpresa(it) }
                empresaAEliminar = null
            },
            onDismiss = { empresaAEliminar = null }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val paddingModifier = Modifier.padding(vertical = 4.dp)
        FadeMain(targetState = tabActivaIndex) { page ->
            when (page) {
                0 -> MisEmpresasTab(
                    empresas = uiState.empresas,
                    empresaActiva = uiState.empresaActiva,
                    onSeleccionar = { empresa ->
                        viewModel.seleccionarEmpresa(empresa)
                        onEmpresaSeleccionada()
                    },
                    onEditar = { empresa ->
                        viewModel.prepararEdicion(empresa)
                        onCambiarTab(1)
                    },
                    onEliminar = { empresa ->
                        empresaAEliminar = empresa
                    },
                    onRefrescar = {
                        viewModel.cargarEmpresas(isRefreshManual = true)
                    },
                    isRefreshing = uiState.isRefreshing,
                    onIrANuevo = {
                        viewModel.cancelarEdicion()
                        onCambiarTab(1)
                    },
                    modifier = paddingModifier
                )

                1 -> NuevoEmpresaTab(
                    empresaAEditar = uiState.empresaAEditar,
                    onCrear = { nombreComercial, ruc, razonSocial, direccion, telefono, moneda, ubigeo, pinSol, logoUrl, activo ->
                        viewModel.crearEmpresa(
                            nombreComercial = nombreComercial,
                            ruc = ruc,
                            razonSocial = razonSocial,
                            direccion = direccion,
                            telefono = telefono,
                            moneda = moneda,
                            ubigeo = ubigeo,
                            pinSol = pinSol,
                            logoUrl = logoUrl,
                            activo = activo,
                            onExito = {
                                onCambiarTab(0)
                            }
                        )
                    },
                    onGuardarEdicion = { empresaModificada ->
                        viewModel.guardarEdicion(empresaModificada) {
                            onCambiarTab(0)
                        }
                    },
                    onConsultarSunat = { ruc, onExito ->
                        viewModel.consultarSunat(ruc, onExito)
                    },
                    isLoading = uiState.isLoading,
                    isBuscandoSunat = uiState.isBuscandoSunat,
                    modifier = paddingModifier
                )

                2 -> AjustesEmpresaTab(
                    empresas = uiState.empresas,
                    empresaSeleccionada = uiState.empresaActiva,
                    onSeleccionarEmpresaParaAjustes = { empresa ->
                        viewModel.seleccionarEmpresa(empresa)
                    },
                    onGuardarAjustes = { empresa, nombre, direccion, telefono, moneda, ubigeo, pinSol, logoUrl, aceptaNota, aceptaBol, aceptaFac, ticketera ->
                        viewModel.guardarAjustesEmpresa(
                            empresa = empresa,
                            nombreComercial = nombre,
                            direccion = direccion,
                            telefono = telefono,
                            moneda = moneda,
                            ubigeo = ubigeo,
                            pinSol = pinSol,
                            logoUrl = logoUrl,
                            aceptaNotaVenta = aceptaNota,
                            aceptaBoleta = aceptaBol,
                            aceptaFactura = aceptaFac,
                            formatoTicketera = ticketera,
                            onExito = {
                                onCambiarTab(0)
                            }
                        )
                    },
                    onToggleCampo = { empresa, campo, nuevoValor ->
                        viewModel.toggleCampoEmpresa(empresa, campo, nuevoValor)
                    },
                    isLoading = uiState.isLoading,
                    modifier = paddingModifier
                )
                else -> MisEmpresasTab(
                    empresas = uiState.empresas,
                    empresaActiva = uiState.empresaActiva,
                    onSeleccionar = { empresa ->
                        viewModel.seleccionarEmpresa(empresa)
                        onEmpresaSeleccionada()
                    },
                    onEditar = { empresa ->
                        viewModel.prepararEdicion(empresa)
                        onCambiarTab(1)
                    },
                    onEliminar = { empresa ->
                        empresaAEliminar = empresa
                    },
                    onRefrescar = {
                        viewModel.cargarEmpresas(isRefreshManual = true)
                    },
                    isRefreshing = uiState.isRefreshing,
                    onIrANuevo = {
                        viewModel.cancelarEdicion()
                        onCambiarTab(1)
                    },
                    modifier = paddingModifier
                )
            }
        }

        // 🌟 Sistema Premium de Notificación Flotante kidev (remplaza la barra negra SnackbarHost)
        WiMessengerHost(messenger = messenger)
    }
}
