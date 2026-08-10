package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiMain
import com.mesawii.feature.empresas.cards.EmpresaCard
import com.mesawii.feature.empresas.data.EmpresaModelo

/**
 * 🏢 MisEmpresasTab.kt — Sub-pantalla (Pestaña 0): Lista de Empresas Registradas enmarcada en WiMain.
 */
@Composable
fun MisEmpresasTab(
    empresas: List<EmpresaModelo>,
    empresaActiva: EmpresaModelo?,
    onSeleccionar: (EmpresaModelo) -> Unit,
    onIrANuevo: () -> Unit,
    modifier: Modifier = Modifier
) {
    WiMain(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mis Empresas Registradas",
                    style = WiText.h4,
                    color = WiCss.tx,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selecciona la empresa con la que deseas operar hoy:",
                    style = WiText.small,
                    color = WiCss.tx3
                )
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
                        onSeleccionar = { onSeleccionar(empresa) }
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
