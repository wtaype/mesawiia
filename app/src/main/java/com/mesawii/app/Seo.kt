package com.mesawii.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mesawii.core.kicss.WiIcons

/**
 * 🏷️ MetaTab — Estructura de datos para sub-pestañas con título e ícono opcional.
 */
data class MetaTab(
    val titulo: String,
    val icono: ImageVector? = null
)

/**
 * 📜 Seo.kt — Single Source of Truth para metadatos y navegación en MesaWii.
 * Ubicado en la raíz com.mesawii.app junto a MainViewModel.kt
 */
data class MetaRuta(
    val orden: Int? = null,            // 1. Encima de key: orden numérico explícito (null = no aparece en Sidebar)
    val key: String,                   // 2. Key de la ruta
    val nombre: String? = null,        // 3. Debajo de key: nombre limpio y dedicado para el botón de la Sidebar
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val tabs: List<MetaTab> = emptyList(),
    val requiereLayout: Boolean = true
) {
    val esNavPrincipal: Boolean get() = orden != null
}

object Seo {
    /**
     * 📌 METADATOS: Single Source of Truth ordenado numéricamente mediante `orden`.
     */
    val METADATOS = mapOf(
        "bienvenida" to MetaRuta(
            orden = null,
            key = "bienvenida",
            nombre = "Bienvenida",
            titulo = "Bienvenido a MesaWii",
            subtitulo = "La experiencia moderna para tu cafetería",
            icono = WiIcons.Star,
            requiereLayout = false
        ),
        "auth" to MetaRuta(
            orden = null,
            key = "auth",
            nombre = "Autenticación",
            titulo = "Autenticación",
            subtitulo = "Ingreso seguro al sistema",
            icono = WiIcons.Lock,
            requiereLayout = false
        ),
        "empresas" to MetaRuta(
            orden = 1,
            key = "empresas",
            nombre = "Empresas",
            titulo = "Mis Empresas & Negocios",
            subtitulo = "Gestión de cafeterías y locales activos",
            icono = WiIcons.Building,
            tabs = listOf(
                MetaTab("Mis Empresas", Icons.Rounded.Home),
                MetaTab("Crear Nueva", Icons.Rounded.Add),
                MetaTab("Ajustes", Icons.Rounded.Settings)
            ),
            requiereLayout = true
        ),
        "mesas" to MetaRuta(
            orden = 2,
            key = "mesas",
            nombre = "Mesas",
            titulo = "Ventas y Control de Mesas",
            subtitulo = "Salón en tiempo real · Hawka Cafetería",
            icono = WiIcons.Restaurant,
            tabs = listOf(
                MetaTab("Mesas", WiIcons.Restaurant),
                MetaTab("Venta Rápida", WiIcons.PointOfSale),
                MetaTab("Reservas", WiIcons.Star)
            ),
            requiereLayout = true
        ),
        "pagar" to MetaRuta(
            orden = 3,
            key = "pagar",
            nombre = "Caja",
            titulo = "Caja & Flujo de Pago",
            subtitulo = "Resumen de consumo y métodos de cobro",
            icono = WiIcons.PointOfSale,
            tabs = listOf(
                MetaTab("Resumen Hoy", WiIcons.PointOfSale),
                MetaTab("Ventas Ayer", Icons.Rounded.Info),
                MetaTab("Arqueo de Caja", Icons.Rounded.Settings)
            ),
            requiereLayout = true
        ),
        "inventario" to MetaRuta(
            orden = 4,
            key = "inventario",
            nombre = "Inventario",
            titulo = "Inventario de Insumos",
            subtitulo = "Control de stock y recetario por dosis",
            icono = WiIcons.Inventory,
            tabs = listOf(
                MetaTab("Insumos", WiIcons.Inventory),
                MetaTab("Recetario", Icons.Rounded.Info),
                MetaTab("Alertas Stock", WiIcons.Star)
            ),
            requiereLayout = true
        ),
        "reportes" to MetaRuta(
            orden = 5,
            key = "reportes",
            nombre = "Analíticas",
            titulo = "Analíticas & Reportes",
            subtitulo = "Dashboard de ventas y métricas del dueño",
            icono = WiIcons.BarChart,
            tabs = listOf(
                MetaTab("Dashboard", WiIcons.BarChart),
                MetaTab("Ventas", WiIcons.PointOfSale),
                MetaTab("Meseros", Icons.Rounded.Home)
            ),
            requiereLayout = true
        ),
        "lab" to MetaRuta(
            orden = 6,
            key = "lab",
            nombre = "Laboratorio",
            titulo = "Laboratorio & Pruebas",
            subtitulo = "Verificación de sesión WiSmile y diagnóstico de componentes",
            icono = WiIcons.Star,
            tabs = listOf(
                MetaTab("Lab 1", Icons.Rounded.Lock),
                MetaTab("Lab 2", Icons.Rounded.Settings),
                MetaTab("Lab 3", Icons.Rounded.Info)
            ),
            requiereLayout = true
        )
    )

    val DEFAULT = METADATOS["empresas"]!!
}
