package com.mesawii.feature.lab.tabs

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
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
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.WiMain

/**
 * 🧪 Lab3Tab.kt — Sub-pantalla (Pestaña Lab 3): Diagnóstico de Sistema y Entorno Android.
 */
@Composable
fun Lab3Tab(
    modifier: Modifier = Modifier
) {
    val isDark = WiCss.isDark

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
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Lab 3: Diagnóstico de Sistema",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                }

                GoldPill("SISTEMA OK")
            }

            Text(
                text = "Métricas de entorno y estado del dispositivo Android:",
                style = WiText.small,
                color = WiCss.tx3
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(WiCss.inp)
                    .border(1.dp, WiCss.glassBrd, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🖥️ Métricas de Dispositivo",
                    style = WiText.small,
                    color = WiCss.tx,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                FilaDiag(campo = "Versión de Android SDK", valor = "${Build.VERSION.SDK_INT} (Android ${Build.VERSION.RELEASE})")
                FilaDiag(campo = "Modelo de Dispositivo", valor = "${Build.MANUFACTURER} ${Build.MODEL}")
                FilaDiag(campo = "Modo de Tema Activo", valor = if (isDark) "Oscuro (Dark)" else "Claro (Light)")
                FilaDiag(campo = "Arquitectura de CPU", valor = Build.SUPPORTED_ABIS.firstOrNull() ?: "Desconocida")
            }
        }
    }
}

@Composable
private fun FilaDiag(campo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(WiCss.wb.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = campo,
            style = WiText.tiny,
            color = WiCss.tx2,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = WiText.tiny,
            color = WiCss.mco,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.2f)
        )
    }
}
