package com.mesawii.app.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesawii.app.Rutas
import com.mesawii.app.RutasState
import com.mesawii.app.components.Header
import com.mesawii.app.components.Sidebar
import com.mesawii.app.components.Tabs
import com.mesawii.core.kicss.WiCss
import kotlinx.coroutines.launch

/**
 * 🏰 MainLayout.kt — Contenedor maestro con Header y Pestañas 100% Ancho (0 Radius, fondo WiCss.wb unificado con StatusBar).
 */
@Composable
fun MainLayout(
    rutasState: RutasState,
    content: @Composable () -> Unit
) {
    val meta = Rutas.getMeta(rutasState.rutaActual)
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    if (!meta.requiereLayout) {
        // 🌌 Vista limpia a pantalla completa sin cromo de interfaz (Auth / Bienvenida)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WiCss.bg)
                .statusBarsPadding()
        ) {
            content()
        }
    } else {
        // 🏰 ModalNavigationDrawer Flotante Overlay
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                Sidebar(
                    rutaActiva = rutasState.rutaActual,
                    onSeleccionarRuta = { ruta ->
                        rutasState.navegarA(ruta)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WiCss.bg)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 1. Bloque Superior 100% Ancho (Header + Sub-pestañas unificados en fondo WiCss.wb plano 0 Radius)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WiCss.wb)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                        ) {
                            Header(
                                meta = meta,
                                onToggleSidebar = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                },
                                onClickAvatar = {
                                    // Reserva para navegación a cuenta/perfil
                                }
                            )
                        }

                        // Sub-pestañas 100% Ancho pegadas al borde inferior exacto de WiCss.wb
                        if (meta.tabs.isNotEmpty()) {
                            Tabs(
                                tabsList = meta.tabs,
                                tabActivaIndex = rutasState.tabActivaIndex,
                                onSeleccionarTab = { index ->
                                    rutasState.seleccionarTab(index)
                                }
                            )
                        }
                    }

                    // 2. Contenido Central con padding reactivo desde RutasState/Layout
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = rutasState.paddingHorizontal,
                                vertical = rutasState.paddingVertical
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // VISTA CENTRAL
                        Box(modifier = Modifier.weight(1f)) {
                            content()
                        }
                    }
                }
            }
        }
    }
}
