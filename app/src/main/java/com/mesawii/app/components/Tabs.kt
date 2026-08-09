package com.mesawii.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText

/**
 * 🧩 Tabs.kt — Barra horizontal de sub-pestañas contextuales para el módulo activo.
 */
@Composable
fun Tabs(
    tabsList: List<String>,
    tabActivaIndex: Int,
    onSeleccionarTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tabsList.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(WiCss.wb.copy(alpha = 0.50f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabsList.forEachIndexed { index, tabTitle ->
            val isSelected = index == tabActivaIndex
            val bgColor = if (isSelected) WiCss.mco else WiCss.inp.copy(alpha = 0.3f)
            val textColor = if (isSelected) WiCss.txa else WiCss.tx2

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .clickable { onSeleccionarTab(index) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tabTitle,
                    style = WiText.small,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}
