package com.mesawii.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.app.MetaRuta
import com.mesawii.app.Rutas
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kicss.fPoppins

/**
 * 🧩 Sidebar.kt — Barra lateral de navegación principal autogenerada desde Rutas.kt.
 */
@Composable
fun Sidebar(
    rutaActiva: String,
    onSeleccionarRuta: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(220.dp)
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .background(WiCss.wb)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Header Sidebar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("☕ MesaWii", style = WiText.h3, color = WiCss.mco, fontWeight = FontWeight.Bold)
                }

                // Lista de rutas principales
                Rutas.RUTAS_SIDEBAR.forEach { meta ->
                    val isSelected = rutaActiva == meta.key
                    val bgColor = if (isSelected) WiCss.mco.copy(alpha = 0.2f) else WiCss.inp.copy(alpha = 0.4f)
                    val textColor = if (isSelected) WiCss.mco else WiCss.tx2

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .clickable { onSeleccionarRuta(meta.key) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = meta.icono,
                            contentDescription = meta.titulo,
                            tint = textColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = meta.titulo.split(" ")[0], // Primer término corto
                            style = WiText.body,
                            color = textColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Footer Sidebar
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WiCss.inp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "🟢 Online · Hawka Cloud",
                        style = WiText.tiny,
                        color = WiCss.success,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
