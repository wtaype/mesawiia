package com.mesawii.feature.lab.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiField
import com.mesawii.core.kidev.WiMain

/**
 * 🧪 Lab2Tab.kt — Sub-pantalla (Pestaña Lab 2): Playground interactivo de componentes UI/UX.
 */
@Composable
fun Lab2Tab(
    modifier: Modifier = Modifier
) {
    var textoPrueba by remember { mutableStateOf("") }
    var estaCargando by remember { mutableStateOf(false) }

    WiMain(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Lab 2: Playground Componentes",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                }

                GoldPill("INTERACTIVO")
            }

            Text(
                text = "Entorno de prueba en vivo para componentes del sistema MesaWii:",
                style = WiText.small,
                color = WiCss.tx3
            )

            WiField(
                value = textoPrueba,
                onValueChange = { textoPrueba = it },
                label = "Campo de entrada interactivo",
                leadingIcon = Icons.Rounded.Search,
                modifier = Modifier.fillMaxWidth()
            )

            WiButton(
                text = if (estaCargando) "Simulando Carga..." else "Probar Acción de Botón",
                onClick = { estaCargando = !estaCargando },
                loading = estaCargando,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
