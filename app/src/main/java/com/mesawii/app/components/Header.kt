package com.mesawii.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.app.MetaRuta
import com.mesawii.core.kicss.FzSmart
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiIcons
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kicss.fPoppins
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.GlassCard

/**
 * 🧩 Header.kt — Encabezado dinámico con Menú Hamburguesa, Logo Hawka, Título de Ruta y Perfil Auth.
 */
@Composable
fun Header(
    meta: MetaRuta,
    onToggleSidebar: () -> Unit = {},
    userRole: String = "Dueño (Admin)",
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        intensity = 0.25f,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda: Hamburguesa + Logo Hawka + Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onToggleSidebar,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = WiIcons.Menu,
                        contentDescription = "Menú",
                        tint = WiCss.mco
                    )
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(WiCss.mco.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "☕ Hawka",
                        style = WiText.small,
                        color = WiCss.mco,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = meta.icono,
                            contentDescription = null,
                            tint = WiCss.tx,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = meta.titulo,
                            style = WiText.h4,
                            color = WiCss.tx,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = meta.subtitulo,
                        style = WiText.tiny,
                        color = WiCss.tx3
                    )
                }
            }

            // Derecha: Status GoldPill + Perfil Auth
            Row(verticalAlignment = Alignment.CenterVertically) {
                GoldPill(userRole)

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(WiCss.mco)
                        .border(1.5.dp, WiCss.brd, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "H",
                        style = WiText.body,
                        color = WiCss.txa,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
