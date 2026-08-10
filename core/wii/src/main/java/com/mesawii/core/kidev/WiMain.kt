package com.mesawii.core.kidev

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss

/**
 * 📦 WiMain.kt — Contenedor Marco Principal de Contenido con fondo WiCss.wb y borde Glass (WiCss.glassBrd).
 * Ubicado en com.mesawii.core.kidev para consumo limpio desde todos los módulos feature y app.
 */
@Composable
fun WiMain(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WiCss.wb)
            .border(1.dp, WiCss.glassBrd, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}
