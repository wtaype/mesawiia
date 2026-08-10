package com.mesawii.feature.cuenta.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiField
import com.mesawii.core.kidev.WiPassword
import com.mesawii.feature.cuenta.CuentaUiState
import com.mesawii.feature.cuenta.CuentaViewModel

@Composable
fun Contrasena(
    uiState: CuentaUiState,
    viewModel: CuentaViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mensajes de Alerta
        if (!uiState.mensajeExitoPass.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(WiCss.success.copy(alpha = 0.15f))
                    .padding(12.dp)
            ) {
                Text(
                    text = uiState.mensajeExitoPass,
                    style = WiText.body,
                    color = WiCss.success,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (!uiState.mensajeErrorPass.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(WiCss.error.copy(alpha = 0.15f))
                    .padding(12.dp)
            ) {
                Text(
                    text = uiState.mensajeErrorPass,
                    style = WiText.body,
                    color = WiCss.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Formulario Cambio de Contraseña
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.wb)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Seguridad de la Cuenta",
                        style = WiText.h4,
                        color = WiCss.tx1,
                        fontWeight = FontWeight.Bold
                    )
                }

                WiField(
                    value = uiState.emailEdit,
                    onValueChange = { },
                    label = "Correo Electrónico (Solo Lectura)",
                    leadingIcon = Icons.Rounded.Lock,
                    singleLine = true
                )

                WiPassword(
                    value = uiState.passNueva,
                    onValueChange = { viewModel.onPassNuevaChange(it) },
                    label = "Nueva Contraseña"
                )

                WiPassword(
                    value = uiState.passConfirmar,
                    onValueChange = { viewModel.onPassConfirmarChange(it) },
                    label = "Confirmar Nueva Contraseña"
                )

                WiButton(
                    text = "Cambiar Contraseña",
                    onClick = { viewModel.cambiarContrasena() },
                    icon = Icons.Rounded.Lock,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
