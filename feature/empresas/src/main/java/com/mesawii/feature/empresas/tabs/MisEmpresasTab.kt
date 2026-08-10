package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.mesawii.feature.empresas.cards.EmpresaCard
import com.mesawii.feature.empresas.data.EmpresaModelo

/**
 * 🏢 MisEmpresasTab.kt — Sub-pantalla (Pestaña 0): Lista de Empresas Registradas con Botón inferior organizado.
 */
@Composable
fun MisEmpresasTab(
    empresas: List<EmpresaModelo>,
    empresaActiva: EmpresaModelo?,
    onSeleccionar: (EmpresaModelo) -> Unit,
    onIrANuevo: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
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
        }

        if (empresas.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Aún no tienes empresas registradas.",
                        style = WiText.body,
                        color = WiCss.tx2
                    )
                }
            }
        } else {
            items(empresas) { empresa ->
                val esActiva = empresaActiva?.id == empresa.id
                EmpresaCard(
                    empresa = empresa,
                    esActiva = esActiva,
                    onSeleccionar = { onSeleccionar(empresa) }
                )
            }
        }

        // Botón organizado a lo ancho al final de la lista
        item {
            Spacer(Modifier.height(8.dp))
            WiButton(
                text = "+ Registrar Nueva Empresa",
                onClick = onIrANuevo,
                icon = Icons.Rounded.Add,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
