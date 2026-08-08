package com.mesawii.feature.hola

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.Wii
import com.mesawii.core.kicss.*

@Composable
fun HolaScreen(
    currentTema: WiTemaColors = LocalWiTemaColors.current,
    onTemaSelected: (WiTemaColors) -> Unit = {}
) {
    val theme     = LocalWiTemaColors.current
    val temasList = remember { MesaWiTemas }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WiCss.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .background(WiCss.wb)
                .padding(28.dp)
        ) {
            // Título principal
            Text(
                text = "${Wii.app} 🍽️",
                style = WiText.h2,
                color = WiCss.tx
            )

            Spacer(Modifier.height(8.dp))

            // Descripción
            Text(
                text = Wii.descri,
                style = WiText.body,
                color = WiCss.tx3
            )

            Spacer(Modifier.height(24.dp))

            // Tema activo
            Text(
                text = "Tema: ${theme.name} 🌿",
                style = WiText.label,
                color = WiCss.mco,
                fontFamily = fPoppins,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            // Selector de temas
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                temasList.forEach { tema ->
                    val isSelected = tema.name == theme.name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) tema.mco else tema.bg)
                            .clickable { onTemaSelected(tema) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = tema.name,
                            style = WiText.small,
                            color = if (isSelected) tema.txa else tema.tx,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
