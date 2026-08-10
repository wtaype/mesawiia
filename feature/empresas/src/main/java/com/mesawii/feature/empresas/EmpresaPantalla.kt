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
import com.mesawii.core.kidev.LocalWiMessenger
import com.mesawii.core.kidev.WiDialog
import com.mesawii.core.kidev.WiMsgType
import com.mesawii.feature.empresas.data.EmpresaModelo
import com.mesawii.feature.empresas.tabs.AjustesEmpresaTab
import com.mesawii.feature.empresas.tabs.MisEmpresasTab
import com.mesawii.feature.empresas.tabs.NuevoEmpresaTab

/**
 * 🏢 EmpresaPantalla.kt — Orquestador UI Principal de Empresas.
 * Conectado a FadeMain para transiciones de entrada suave Fade-In (200ms) por sub-pestaña.
 */
@Composable
fun EmpresaPantalla(
    tabActivaIndex: Int = 0,
    viewModel: EmpresaViewModel = viewModel(),
    onEmpresaSeleccionada: () -> Unit = {},
    onCambiarTab: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val messenger = LocalWiMessenger.current

    var empresaAEliminar by remember { mutableStateOf<EmpresaModelo?>(null) }

    // 🔔 NOTIFICACIONES FLOTANTES CON Notificacion() en la parte superior
    LaunchedEffect(uiState.exitoMensaje) {
        uiState.exitoMensaje?.let { msg ->
            messenger.Notificacion(msg, WiMsgType.Success)
            viewModel.limpiarMensajes()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            messenger.Notificacion(err, WiMsgType.Error)
            viewModel.limpiarMensajes()
        }
    }

    // 💬 Modal de Confirmación WiDialog para Eliminar
    WiDialog(
        show = empresaAEliminar != null,
        title = "Eliminar Empresa",
        text = "¿Estás seguro de que deseas eliminar la empresa '${empresaAEliminar?.nombreComercial}'? Esta acción la eliminará de Supabase permanentemente.",
        confirmText = "Sí, Eliminar",
        dismissText = "Cancelar",
        onConfirm = {
            empresaAEliminar?.let { viewModel.eliminarEmpresa(it) }
            empresaAEliminar = null
        },
        onDismiss = { empresaAEliminar = null }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        val paddingModifier = Modifier.padding(vertical = 4.dp)
        FadeMain(targetState = tabActivaIndex) { page ->
            when (page) {
                0 -> MisEmpresasTab(
                    empresas = uiState.empresas,
                    empresaActiva = uiState.empresaActiva,
                    onSeleccionar = { empresa ->
                        viewModel.seleccionarEmpresa(empresa)
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
                    isLoading = uiState.isLoading,
                    modifier = paddingModifier
                )
                else -> MisEmpresasTab(
                    empresas = uiState.empresas,
                    empresaActiva = uiState.empresaActiva,
                    onSeleccionar = { empresa ->
                        viewModel.seleccionarEmpresa(empresa)
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
    }
}
