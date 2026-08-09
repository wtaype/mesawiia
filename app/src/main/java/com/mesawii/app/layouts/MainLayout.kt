package com.mesawii.app.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesawii.app.NavegadorState
import com.mesawii.app.Rutas
import com.mesawii.app.components.Header
import com.mesawii.app.components.Sidebar
import com.mesawii.app.components.Tabs
import com.mesawii.core.kicss.WiCss
import kotlinx.coroutines.launch

/**
 * 🏰 MainLayout.kt — Contenedor maestro envolvente inteligente.
 * Si requiereLayout == false (Bienvenida o Auth), muestra únicamente la pantalla limpia a pantalla completa.
 * Si requiereLayout == true, envuelve la vista con Header + Sidebar + Tabs + Footer.
 */
@Composable
fun MainLayout(
    navegadorState: NavegadorState,
    content: @Composable () -> Unit
) {
    val meta = Rutas.getMeta(navegadorState.rutaActual)
    val scope = rememberCoroutineScope()
    var isSidebarOpen by remember { mutableStateOf(false) }

    if (!meta.requiereLayout) {
        // 🌌 Vista limpia a pantalla completa sin barras ni cromo de interfaz
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WiCss.bg)
        ) {
            content()
        }
    } else {
        // 🏰 Layout Maestro para módulos operativos (Mesas, Pagar, Inventario, Reportes)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WiCss.bg)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar desplegable / fijo
                AnimatedVisibility(
                    visible = isSidebarOpen,
                    enter = slideInHorizontally(initialOffsetX = { -it }),
                    exit = slideOutHorizontally(targetOffsetX = { -it })
                ) {
                    Sidebar(
                        rutaActiva = navegadorState.rutaActual,
                        onSeleccionarRuta = { ruta ->
                            navegadorState.navegarA(ruta)
                            isSidebarOpen = false
                        }
                    )
                }

                // Área de Contenido Principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header con botón de toggle de Sidebar
                    Header(
                        meta = meta,
                        onToggleSidebar = { isSidebarOpen = !isSidebarOpen }
                    )

                    // Sub-pestañas contextuales
                    if (meta.tabs.isNotEmpty()) {
                        Tabs(
                            tabsList = meta.tabs,
                            tabActivaIndex = navegadorState.tabActivaIndex,
                            onSeleccionarTab = { index ->
                                navegadorState.seleccionarTab(index)
                            }
                        )
                    }

                    // VISTA CENTRAL
                    Box(modifier = Modifier.weight(1f)) {
                        content()
                    }
                }
            }
        }
    }
}
