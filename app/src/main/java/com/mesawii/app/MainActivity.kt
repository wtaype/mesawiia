package com.mesawii.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mesawii.core.kicss.WiTemaApp
import com.mesawii.feature.hola.HolaScreen

/**
 * ⚡ MainActivity — Actividad principal ultra-delgada
 */
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentTema by mainViewModel.currentTema.collectAsState()

            WiTemaApp(themeColors = currentTema) {
                HolaScreen(
                    currentTema = currentTema,
                    onTemaSelected = { nuevoTema -> mainViewModel.setTema(nuevoTema) }
                )
            }
        }
    }
}
