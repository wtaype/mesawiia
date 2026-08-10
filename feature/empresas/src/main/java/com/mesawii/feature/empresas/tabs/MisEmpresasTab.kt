package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiMain
import com.mesawii.core.kidev.wiSwipe
import com.mesawii.feature.empresas.cards.EmpresaCard
import com.mesawii.feature.empresas.data.EmpresaModelo

/**
 * 🏢 MisEmpresasTab.kt — Sub-pantalla (Pestaña 0): Lista de Empresas Registradas con Pull-to-Refresh exclusivo.
 */
@Composable
fun MisEmpresasTab(
    empresas: List<EmpresaModelo>,
    empresaActiva: EmpresaModelo?,
    onSeleccionar: (EmpresaModelo) -> Unit,
    onEditar: (EmpresaModelo) -> Unit = {},
    onEliminar: (EmpresaModelo) -> Unit = {},
    onRefrescar: () -> Unit = {},
    isRefreshing: Boolean = false,
    onIrANuevo: () -> Unit,
    modifier: Modifier = Modifier
) {
    WiMain(
        modifier = modifier.wiSwipe(
            onDown = { _, _ -> onRefrescar() }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mis Empresas Registradas",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Desliza hacia abajo para actualizar o selecciona la empresa activa:",
                        style = WiText.small,
                        color = WiCss.tx3
                    )
                }

                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = WiCss.mco,
                        strokeWidth = 2.5.dp
                    )
                }
            }

            if (empresas.isEmpty()) {
                Text(
                    text = "Aún no tienes empresas registradas.",
                    style = WiText.body,
                    color = WiCss.tx2,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                empresas.forEach { empresa ->
                    val esActiva = empresaActiva?.id == empresa.id
                    EmpresaCard(
                        empresa = empresa,
                        esActiva = esActiva,
                        onSeleccionar = { onSeleccionar(empresa) },
                        onEditar = { onEditar(empresa) },
                        onEliminar = { onEliminar(empresa) }
                    )
                }
            }

            WiButton(
                text = "+ Registrar Nueva Empresa",
                onClick = onIrANuevo,
                icon = Icons.Rounded.Add,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
