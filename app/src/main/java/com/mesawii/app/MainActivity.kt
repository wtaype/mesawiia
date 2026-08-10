package com.mesawii.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mesawii.app.layouts.MainLayout
import com.mesawii.core.kicss.WiTemaApp

/**
 * ⚡ MainActivity — Actividad principal ultra-delgada (~25 líneas) conectada a Navegar y RutasState.
 */
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentTema by mainViewModel.currentTema.collectAsState()
            val rutasState = rememberRutas(rutaInicial = mainViewModel.rutaInicial)

            WiTemaApp(themeColors = currentTema) {
                MainLayout(rutasState = rutasState) {
                    Navegar(rutasState = rutasState)
                }
            }
        }
    }
}
