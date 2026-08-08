package com.mesawii.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mesawii.core.kicss.WiTemaApp
import com.mesawii.core.kicss.temaPorDefecto
import com.mesawii.feature.hola.HolaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val initialTema = remember { temaPorDefecto() }
            var currentTema by remember { mutableStateOf(initialTema) }

            WiTemaApp(themeColors = currentTema) {
                HolaScreen(
                    currentTema = currentTema,
                    onTemaSelected = { nuevoTema -> currentTema = nuevoTema }
                )
            }
        }
    }
}
