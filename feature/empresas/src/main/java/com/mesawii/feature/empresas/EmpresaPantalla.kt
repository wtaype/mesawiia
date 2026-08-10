package com.mesawii.feature.empresas

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mesawii.feature.empresas.tabs.AjustesEmpresaTab
import com.mesawii.feature.empresas.tabs.MisEmpresasTab
import com.mesawii.feature.empresas.tabs.NuevoEmpresaTab

/**
 * 🏢 EmpresaPantalla.kt — Orquestador UI Principal de Feature Empresas (Conectado a pestañas reactivas).
 */
@Composable
fun EmpresaPantalla(
    tabActivaIndex: Int = 0,
    viewModel: EmpresaViewModel = viewModel(),
    onEmpresaSeleccionada: () -> Unit = {},
    onCambiarTab: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val paddingModifier = Modifier.padding(vertical = 4.dp)

    when (tabActivaIndex) {
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
            onCrear = { nombreComercial, ruc, razonSocial, direccion, telefono, moneda, ubigeo, pinSol ->
                viewModel.crearEmpresa(
                    nombreComercial = nombreComercial,
                    ruc = ruc,
                    razonSocial = razonSocial,
                    direccion = direccion,
                    telefono = telefono,
                    moneda = moneda,
                    ubigeo = ubigeo,
                    pinSol = pinSol,
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
            onGuardarAjustes = { empresa, nombre, direccion, telefono, moneda, ubigeo, pinSol ->
                viewModel.guardarAjustesEmpresa(
                    empresa = empresa,
                    nombreComercial = nombre,
                    direccion = direccion,
                    telefono = telefono,
                    moneda = moneda,
                    ubigeo = ubigeo,
                    pinSol = pinSol,
                    onExito = {
                        onCambiarTab(0)
                        onEmpresaSeleccionada()
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
                onEmpresaSeleccionada()
            },
            onIrANuevo = { onCambiarTab(1) },
            modifier = paddingModifier
        )
    }
}
