package com.mesawii.app

import androidx.compose.runtime.Composable
import com.mesawii.app.components.Modulo
import com.mesawii.core.kicss.WiTemaColors
import com.mesawii.feature.auth.AuthPantalla
import com.mesawii.feature.bienvenida.BienvenidaScreen
import com.mesawii.feature.cuenta.CuentaPantalla
import com.mesawii.feature.empresas.EmpresaPantalla
import com.mesawii.feature.lab.LabPantalla

/**
 * 🧭 Navegar.kt — Enrutador Composable Sincrónico Ultra-Rápido (< 0.1ms).
 * Conmuta entre módulos feature aceptando tabActivaIndex dinámico por página.
 */
@Composable
fun Navegar(
    rutasState: RutasState,
    tabActivaIndex: Int = rutasState.tabActivaIndex,
    onTemaCambiado: (WiTemaColors) -> Unit = {}
) {
    when (rutasState.rutaActual) {
        "bienvenida" -> {
            BienvenidaScreen(
                onComenzar = {
                    rutasState.navegarA("auth")
                }
            )
        }
        "auth" -> {
            AuthPantalla(
                onAuthExitosa = {
                    rutasState.navegarA("empresas")
                }
            )
        }
        "empresas" -> {
            EmpresaPantalla(
                tabActivaIndex = tabActivaIndex,
                onEmpresaSeleccionada = {
                    rutasState.navegarA("mesas")
                },
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                }
            )
        }
        "cuenta" -> {
            CuentaPantalla(
                tabActivaIndex = tabActivaIndex,
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                },
                onCerrarSesion = {
                    rutasState.navegarA("auth")
                },
                onTemaCambiado = onTemaCambiado
            )
        }
        "lab" -> {
            LabPantalla(
                tabActivaIndex = tabActivaIndex,
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                }
            )
        }
        else -> {
            Modulo(
                rutasState = rutasState,
                tabActivaIndex = tabActivaIndex
            )
        }
    }
}
