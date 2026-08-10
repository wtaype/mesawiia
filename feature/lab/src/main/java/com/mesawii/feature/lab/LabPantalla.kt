package com.mesawii.feature.lab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesawii.core.kidev.FadeMain
import com.mesawii.feature.lab.tabs.Lab1Tab
import com.mesawii.feature.lab.tabs.Lab2Tab
import com.mesawii.feature.lab.tabs.Lab3Tab

/**
 * 🧪 LabPantalla.kt — Orquestador UI Principal del Módulo Laboratorio con FadeMain.
 */
@Composable
fun LabPantalla(
    tabActivaIndex: Int = 0,
    onCambiarTab: (Int) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val paddingModifier = Modifier.padding(vertical = 4.dp)
        FadeMain(targetState = tabActivaIndex) { page ->
            when (page) {
                0 -> Lab1Tab(modifier = paddingModifier)
                1 -> Lab2Tab(modifier = paddingModifier)
                2 -> Lab3Tab(modifier = paddingModifier)
                else -> Lab1Tab(modifier = paddingModifier)
            }
        }
    }
}
