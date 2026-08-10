package com.mesawii.app

import androidx.compose.runtime.Composable
import com.mesawii.feature.auth.AuthPantalla
import com.mesawii.feature.bienvenida.BienvenidaScreen
import com.mesawii.feature.empresas.EmpresaPantalla
import com.mesawii.feature.lab.LabPantalla

/**
 * 🧭 Navegar.kt — Enrutador Composable Sincrónico Ultra-Rápido (< 0.1ms).
 * Conmuta entre módulos feature sin sobrecarga de memoria ni reflexión.
 */
@Composable
fun Navegar(rutasState: RutasState) {
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
                tabActivaIndex = rutasState.tabActivaIndex,
                onEmpresaSeleccionada = {
                    rutasState.navegarA("mesas")
                },
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                }
            )
        }
        "lab" -> {
            LabPantalla(
                tabActivaIndex = rutasState.tabActivaIndex,
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                }
            )
        }
        else -> {
            LabPantalla(
                tabActivaIndex = rutasState.tabActivaIndex,
                onCambiarTab = { nuevoIndex ->
                    rutasState.seleccionarTab(nuevoIndex)
                }
            )
        }
    }
}
