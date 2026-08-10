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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mesawii.app.Rutas
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.feature.empresas.data.CacheEmpresa

/**
 * 🧩 Sidebar.kt — Barra lateral con fondo WiCss.wb y statusBarsPadding() seguro, suscrita al StateFlow de la empresa activa.
 */
@Composable
fun Sidebar(
    rutaActiva: String,
    onSeleccionarRuta: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cacheEmpresa = remember { CacheEmpresa.getInstance(context) }
    val nombreEmpresa by cacheEmpresa.empresaActivaNombreFlow.collectAsState()

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(250.dp)
            .clip(RectangleShape)
            .background(WiCss.wb)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Header Sidebar: Nombre de Empresa dinámico con Ícono Location (Place)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Place, // Location Icon
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = nombreEmpresa,
                        style = WiText.h3,
                        color = WiCss.mco,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Lista de rutas principales (Empresas usa ícono de Edificio HomeWork)
                Rutas.RUTAS_SIDEBAR.forEach { meta ->
                    val isSelected = rutaActiva == meta.key
                    val bgColor = if (isSelected) WiCss.mco.copy(alpha = 0.2f) else WiCss.inp.copy(alpha = 0.4f)
                    val textColor = if (isSelected) WiCss.mco else WiCss.tx2

                    val tituloMostrar = if (meta.key == "empresas") "Empresas" else meta.titulo.split(" ")[0]

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
                            text = tituloMostrar,
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = WiCss.success,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Online · Hawka Cloud",
                            style = WiText.tiny,
                            color = WiCss.success,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
