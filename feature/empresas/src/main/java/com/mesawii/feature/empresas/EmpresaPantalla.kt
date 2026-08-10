package com.mesawii.feature.empresas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mesawii.feature.empresas.tabs.AjustesEmpresaTab
import com.mesawii.feature.empresas.tabs.MisEmpresasTab
import com.mesawii.feature.empresas.tabs.NuevoEmpresaTab

/**
 * 🏢 EmpresaPantalla.kt — Orquestador UI Principal con Swipe Horizontal (HorizontalPager) alineado al Top
 * y respuesta instantánea del indicador activo (targetPage).
 */
@Composable
fun EmpresaPantalla(
    tabActivaIndex: Int = 0,
    viewModel: EmpresaViewModel = viewModel(),
    onEmpresaSeleccionada: () -> Unit = {},
    onCambiarTab: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = tabActivaIndex, pageCount = { 3 })

    // 1. Clic en pestaña superior -> Scroll suave del Pager
    LaunchedEffect(tabActivaIndex) {
        if (pagerState.currentPage != tabActivaIndex) {
            pagerState.animateScrollToPage(tabActivaIndex)
        }
    }

    // 2. Gesto de Swipe con el dedo -> Sincronización instantánea mediante targetPage (cero lag)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.targetPage }.collect { page ->
            if (page != tabActivaIndex) {
                onCambiarTab(page)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top, // 📌 Alineación superior directa (evita centrado vertical)
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val paddingModifier = Modifier.padding(vertical = 4.dp)
        when (page) {
            0 -> MisEmpresasTab(
                empresas = uiState.empresas,
                empresaActiva = uiState.empresaActiva,
                onSeleccionar = { empresa ->
                    viewModel.seleccionarEmpresa(empresa)
                    onEmpresaSeleccionada()
                },
                onIrANuevo = { onCambiarTab(1) },
                modifier = paddingModifier
            )

            1 -> NuevoEmpresaTab(
                onCrear = { nombreComercial, ruc, razonSocial, direccion, telefono, moneda, ubigeo, pinSol, logoUrl ->
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
                        onExito = {
                            onCambiarTab(0)
                            onEmpresaSeleccionada()
                        }
                    )
                },
                onConsultarSunat = { ruc, onExito ->
                    viewModel.consultarSunat(ruc, onExito)
                },
                isLoading = uiState.isLoading,
                isBuscandoSunat = uiState.isBuscandoSunat,
                modifier = paddingModifier
            )

            2 -> AjustesEmpresaTab(
                empresa = uiState.empresaActiva,
                onGuardarAjustes = { empresa, nombre, direccion, telefono, moneda, ubigeo, pinSol, logoUrl ->
                    viewModel.guardarAjustesEmpresa(
                        empresa = empresa,
                        nombreComercial = nombre,
                        direccion = direccion,
                        telefono = telefono,
                        moneda = moneda,
                        ubigeo = ubigeo,
                        pinSol = pinSol,
                        logoUrl = logoUrl,
                        onExito = {
                            onCambiarTab(0)
                            onEmpresaSeleccionada()
                        }
                    )
                },
                isLoading = uiState.isLoading,
                modifier = paddingModifier
            )
        }
    }
}
