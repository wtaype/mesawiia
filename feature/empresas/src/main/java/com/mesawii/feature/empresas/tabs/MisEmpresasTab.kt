package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.feature.empresas.cards.EmpresaCard
import com.mesawii.feature.empresas.data.EmpresaModelo

/**
 * 🏢 MisEmpresasTab.kt — Sub-pantalla (Pestaña 0): Lista de Empresas Registradas y Selección.
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
                    text = "+ Nueva",
                    onClick = onIrANuevo,
                    icon = Icons.Rounded.Add
                )
            }
        }

        items(empresas) { empresa ->
            val esActiva = empresaActiva?.id == empresa.id
            EmpresaCard(
                empresa = empresa,
                esActiva = esActiva,
                onSeleccionar = { onSeleccionar(empresa) }
            )
        }
    }
}
