package com.mesawii.feature.lab.tabs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiMain
import com.mesawii.feature.auth.data.CacheSmile
import com.mesawii.feature.auth.data.SmileModelo

/**
 * 🧪 Lab1Tab.kt — Sub-pantalla (Pestaña Lab 1): Revisión de sesión Auth y consulta de datos wiSmile en Tabla.
 */
@Composable
fun Lab1Tab(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var smileDatos by remember { mutableStateOf<SmileModelo?>(CacheSmile.getInstance(context).getSmileGuardado()) }
    var consultado by remember { mutableStateOf(false) }

    fun consultarAuth() {
        smileDatos = CacheSmile.getInstance(context).getSmileGuardado()
        consultado = true
    }

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
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Lab 1: Revisar Auth & WiSmile",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (smileDatos != null) {
                    GoldPill("SESIÓN ACTIVA")
                }
            }

            Text(
                text = "Consulta el estado actual de la sesión local wiSmile guardada en caché:",
                style = WiText.small,
                color = WiCss.tx3
            )

            WiButton(
                text = "Revisar Auth / Consultar wiSmile",
                onClick = { consultarAuth() },
                icon = Icons.Rounded.Refresh,
                modifier = Modifier.fillMaxWidth()
            )

            val datos = smileDatos
            if (datos == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(WiCss.inp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (consultado) "No se encontró ninguna sesión wiSmile activa." else "Presiona 'Revisar Auth' para consultar la tabla de sesión.",
                        style = WiText.body,
                        color = WiCss.tx2
                    )
                }
            } else {
                // 📊 TABLA DE DATOS WISMILE
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
                        text = "📊 Tabla de Datos de Sesión wiSmile",
                        style = WiText.small,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    FilaTabla(campo = "ID de Usuario", valor = datos.id)
                    FilaTabla(campo = "Nombre de Usuario", valor = datos.usuario.ifBlank { "-" })
                    FilaTabla(campo = "Correo Electrónico", valor = datos.email.ifBlank { "-" })
                    FilaTabla(campo = "Nombre Completo", valor = "${datos.nombre} ${datos.apellidos}".trim().ifBlank { "-" })
                    FilaTabla(campo = "URL de Avatar", valor = datos.avatar ?: "Sin Avatar")
                }
            }
        }
    }
}

@Composable
private fun FilaTabla(campo: String, valor: String) {
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
            modifier = Modifier.weight(1.5f)
        )
    }
}
