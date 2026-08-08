// WiMainState.kt — Contrato de estado global entre :app y los módulos :feature:*
package com.mesawii.core.kidev

import com.mesawii.core.kicss.WiTemaColors
import kotlinx.coroutines.flow.StateFlow

/**
 * WiMainState — Interfaz de contrato que cada feature recibe desde :app.
 *
 * Uso en una nueva feature:
 *   @Composable fun MiFeature(state: WiMainState) {
 *       val tema    by state.currentTheme.collectAsState()
 *       val perfil  by state.sessionProfile.collectAsState()
 *   }
 */
interface WiMainState {
    val sessionProfile:  StateFlow<WiPerfil?>
    val currentTheme:    StateFlow<WiTemaColors>
    val fontScale:       StateFlow<Float>
    val fontFamilyName:  StateFlow<String>

    fun onThemeChange(tema: WiTemaColors)
    fun onFontScaleChange(scale: Float)
    fun onFontFamilyChange(name: String)
    fun logout()
}
