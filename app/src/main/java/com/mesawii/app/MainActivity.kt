package com.mesawii.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mesawii.app.layouts.MainLayout
import com.mesawii.core.kicss.WiTemaApp
import com.mesawii.core.kidev.WiMessengerHost
import com.mesawii.core.kidev.WiMessengerProvider
import com.mesawii.core.kidev.rememberWiMessenger

/**
 * ⚡ MainActivity — Actividad principal ultra-delgada conectada a Navegar, RutasState y WiMessenger.
 */
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentTema by mainViewModel.currentTema.collectAsState()
            val rutasState = rememberRutas(rutaInicial = mainViewModel.rutaInicial)
            val messenger = rememberWiMessenger()

            WiTemaApp(themeColors = currentTema) {
                WiMessengerProvider(messenger = messenger) {
                    WiMessengerHost(messenger = messenger)
                    MainLayout(rutasState = rutasState) {
                        Navegar(rutasState = rutasState)
                    }
                }
            }
        }
    }
}
