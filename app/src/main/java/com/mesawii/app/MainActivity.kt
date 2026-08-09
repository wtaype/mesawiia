package com.mesawii.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mesawii.app.layouts.MainLayout
import com.mesawii.core.kicss.WiTemaApp
import com.mesawii.feature.auth.AuthPantalla
import com.mesawii.feature.bienvenida.BienvenidaScreen
import com.mesawii.feature.empresas.EmpresaPantalla
import com.mesawii.feature.hola.HolaScreen

/**
 * ⚡ MainActivity — Actividad principal ultra-delgada conectada al MainLayout y Navegador.
 */
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentTema by mainViewModel.currentTema.collectAsState()
            val navegadorState = rememberNavegador(rutaInicial = mainViewModel.rutaInicial)

            WiTemaApp(themeColors = currentTema) {
                MainLayout(navegadorState = navegadorState) {
                    when (navegadorState.rutaActual) {
                        "bienvenida" -> {
                            BienvenidaScreen(
                                onComenzar = {
                                    navegadorState.navegarA("auth")
                                }
                            )
                        }
                        "auth" -> {
                            AuthPantalla(
                                onAuthExitosa = {
                                    navegadorState.navegarA("empresas")
                                }
                            )
                        }
                        "empresas" -> {
                            EmpresaPantalla(
                                onEmpresaSeleccionada = {
                                    navegadorState.navegarA("mesas")
                                }
                            )
                        }
                        else -> {
                            HolaScreen(
                                currentTema = currentTema,
                                onTemaSelected = { nuevoTema -> mainViewModel.setTema(nuevoTema) }
                            )
                        }
                    }
                }
            }
        }
    }
}
