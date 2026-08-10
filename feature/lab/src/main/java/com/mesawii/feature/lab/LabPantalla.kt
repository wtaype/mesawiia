package com.mesawii.feature.lab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesawii.feature.lab.tabs.Lab1Tab
import com.mesawii.feature.lab.tabs.Lab2Tab
import com.mesawii.feature.lab.tabs.Lab3Tab

/**
 * 🧪 LabPantalla.kt — Orquestador UI Principal del Módulo Laboratorio con Swipe Horizontal 60fps y 3 Sub-pestañas.
 */
@Composable
fun LabPantalla(
    tabActivaIndex: Int = 0,
    onCambiarTab: (Int) -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = tabActivaIndex, pageCount = { 3 })

    // 1. Clic en pestaña superior -> Scroll suave del Pager
    LaunchedEffect(tabActivaIndex) {
        if (pagerState.currentPage != tabActivaIndex) {
            pagerState.animateScrollToPage(tabActivaIndex)
        }
    }

    // 2. Gesto de Swipe con el dedo -> Sincronización instantánea del estado mediante targetPage
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.targetPage }.collect { page ->
            if (page != tabActivaIndex) {
                onCambiarTab(page)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val paddingModifier = Modifier.padding(vertical = 4.dp)
        when (page) {
            0 -> Lab1Tab(modifier = paddingModifier)
            1 -> Lab2Tab(modifier = paddingModifier)
            2 -> Lab3Tab(modifier = paddingModifier)
        }
    }
}
