package com.mesawii.feature.cuenta.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiSwitch
import com.mesawii.feature.cuenta.CuentaUiState
import com.mesawii.feature.cuenta.CuentaViewModel
import kotlin.math.roundToInt

@Composable
fun Ajustes(
    uiState: CuentaUiState,
    viewModel: CuentaViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mensajes de Éxito en Ajustes
        if (!uiState.mensajeExitoAjustes.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(WiCss.success.copy(alpha = 0.15f))
                    .padding(12.dp)
            ) {
                Text(
                    text = uiState.mensajeExitoAjustes,
                    style = WiText.body,
                    color = WiCss.success,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 1. Control de Tamaño de Texto (Range / Slider)
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
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Tamaño de Texto & Legibilidad",
                        style = WiText.h4,
                        color = WiCss.tx1,
                        fontWeight = FontWeight.Bold
                    )
                }

                val porcentaje = (uiState.escalaTexto * 100).roundToInt()
                Text(
                    text = "Ajusta la escala de fuente del sistema ($porcentaje%)",
                    style = WiText.small,
                    color = WiCss.tx3
                )

                Slider(
                    value = uiState.escalaTexto,
                    onValueChange = { viewModel.onEscalaTextoChange(it) },
                    valueRange = 0.8f..1.3f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = WiCss.mco,
                        activeTrackColor = WiCss.mco,
                        inactiveTrackColor = WiCss.brd
                    )
                )

                // Vista previa de texto en vivo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(WiCss.inp)
                        .border(1.dp, WiCss.brd.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Vista Previa del Texto MesaWii",
                            fontSize = (15 * uiState.escalaTexto).sp,
                            color = WiCss.tx1,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Así se visualizarán las descripciones, comprobantes y menús de tu restaurante.",
                            fontSize = (12 * uiState.escalaTexto).sp,
                            color = WiCss.tx3
                        )
                    }
                }
            }
        }

        // 2. Personalización & Opciones Inteligentes de Cuenta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Preferencias Avanzadas",
                    style = WiText.h4,
                    color = WiCss.tx1,
                    fontWeight = FontWeight.Bold
                )

                WiSwitch(
                    checked = uiState.biometriaActivada,
                    onCheckedChange = { viewModel.onBiometriaChange(it) },
                    label = "Autenticación Biométrica",
                    sublabel = "Pedir Huella o FaceID al reabrir la app"
                )

                WiSwitch(
                    checked = uiState.notificacionesActivadas,
                    onCheckedChange = { viewModel.onNotificacionesChange(it) },
                    label = "Notificaciones de Sistema",
                    sublabel = "Alertas de stock crítico y arqueo de caja"
                )
            }
        }

        // 3. Caché Local & Exportación de Datos
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Almacenamiento Local",
                        style = WiText.h4,
                        color = WiCss.tx1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.cacheUsageMb,
                        style = WiText.body,
                        color = WiCss.mco,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Limpia archivos temporales e imágenes cacheadas para liberar almacenamiento interno.",
                    style = WiText.small,
                    color = WiCss.tx3
                )

                WiButton(
                    text = "Limpiar Caché Temporal",
                    onClick = { viewModel.limpiarCacheLocal() },
                    containerColor = WiCss.mco.copy(alpha = 0.85f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
