package com.mesawii.feature.cuenta.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.MesaWiTemas
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiIcons
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiDialog
import com.mesawii.feature.cuenta.CuentaUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun General(
    uiState: CuentaUiState,
    onSeleccionarTema: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val smile = uiState.smile
    val nombreCompleto = "${smile?.nombre ?: ""} ${smile?.apellidos ?: ""}".ifBlank { "Usuario MesaWii" }
    val usuarioTag = "@${smile?.usuario ?: "super"}"
    val segmentoTag = smile?.segmento?.uppercase() ?: "NEGOCIOS"

    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Card Resumen de Perfil General
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar Circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(WiCss.mco.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = com.mesawii.core.wii.R.drawable.logo_circle),
                        contentDescription = "Avatar",
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = nombreCompleto,
                            style = WiText.h3,
                            color = WiCss.tx1,
                            fontWeight = FontWeight.Bold
                        )
                        GoldPill(text = segmentoTag)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = usuarioTag,
                        style = WiText.body,
                        color = WiCss.mco,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = smile?.email ?: "sin_correo@mesawii.com",
                        style = WiText.small,
                        color = WiCss.tx3
                    )
                }
            }
        }

        // 2. Card Selección de Temas Organizada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = WiIcons.Star,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Tema de la Aplicación",
                        style = WiText.h4,
                        color = WiCss.tx1,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Selecciona tu tema favorito para personalizar la apariencia de MesaWii.",
                    style = WiText.small,
                    color = WiCss.tx3
                )

                Spacer(Modifier.height(4.dp))

                // Selector Grid / FlowRow de Temas Oficiales
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MesaWiTemas.forEach { temaItem ->
                        val isSelected = uiState.temaActivo.equals(temaItem.name, ignoreCase = true)
                        val borderColor = if (isSelected) WiCss.mco else WiCss.brd.copy(alpha = 0.5f)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(temaItem.wb)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onSeleccionarTema(temaItem.name) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Muestra de color principal (MCO)
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(temaItem.mco)
                                )
                                Text(
                                    text = temaItem.name,
                                    style = WiText.body,
                                    color = temaItem.tx,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Activo",
                                        tint = WiCss.mco,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Bloque de Seguridad / Cerrar Sesión
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Sesión Activa",
                    style = WiText.h4,
                    color = WiCss.tx1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Al cerrar sesión deberás volver a ingresar tus credenciales para acceder al sistema.",
                    style = WiText.small,
                    color = WiCss.tx3
                )

                WiButton(
                    text = "Cerrar Sesión",
                    onClick = { mostrarDialogoCerrarSesion = true },
                    containerColor = WiCss.error,
                    icon = WiIcons.Lock,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Modal de confirmación para cerrar sesión
    WiDialog(
        show = mostrarDialogoCerrarSesion,
        title = "Cerrar Sesión",
        text = "¿Estás seguro de que deseas salir de MesaWii?",
        confirmText = "Sí, Cerrar Sesión",
        dismissText = "Cancelar",
        onConfirm = {
            mostrarDialogoCerrarSesion = false
            onCerrarSesion()
        },
        onDismiss = { mostrarDialogoCerrarSesion = false }
    )
}
