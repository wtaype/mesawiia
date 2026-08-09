package com.mesawii.feature.empresas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.feature.empresas.cards.EmpresaCard
import com.mesawii.feature.empresas.components.FormularioEmpresa

/**
 * 🏢 EmpresaPantalla.kt — Pantalla Principal del Cliente (Sin Banner de bienvenida, directo a formulario/empresas).
 */
@Composable
fun EmpresaPantalla(
    viewModel: EmpresaViewModel = viewModel(),
    onEmpresaSeleccionada: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarFormulario by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Directo al Formulario si no tiene empresas registradas o decide crear una nueva
        if (uiState.empresas.isEmpty() || mostrarFormulario) {
            item {
                FormularioEmpresa(
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
                                mostrarFormulario = false
                                onEmpresaSeleccionada()
                            }
                        )
                    },
                    onConsultarSunat = { ruc, onExito ->
                        viewModel.consultarSunat(ruc, onExito)
                    },
                    isLoading = uiState.isLoading,
                    isBuscandoSunat = uiState.isBuscandoSunat
                )
            }
        } else {
            // Mis Empresas Creadas
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mis Empresas Registradas",
                            style = WiText.h3,
                            color = WiCss.tx,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Selecciona la empresa con la que deseas operar hoy:",
                            style = WiText.small,
                            color = WiCss.tx3
                        )
                    }

                    WiButton(
                        text = "+ Nueva Empresa",
                        onClick = { mostrarFormulario = true },
                        icon = Icons.Rounded.Add
                    )
                }
            }

            items(uiState.empresas) { empresa ->
                val esActiva = uiState.empresaActiva?.id == empresa.id
                EmpresaCard(
                    empresa = empresa,
                    esActiva = esActiva,
                    onSeleccionar = {
                        viewModel.seleccionarEmpresa(empresa)
                        onEmpresaSeleccionada()
                    }
                )
            }
        }
    }
}
