package com.mesawii.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
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
    val key: String,
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val tabs: List<MetaTab> = emptyList(),
    val requiereLayout: Boolean = true,
    val esNavPrincipal: Boolean = true
)

object Seo {
    val METADATOS = mapOf(
        "bienvenida" to MetaRuta(
            key = "bienvenida",
            titulo = "Bienvenido a MesaWii",
            subtitulo = "La experiencia moderna para tu cafetería",
            icono = WiIcons.Star,
            requiereLayout = false,
            esNavPrincipal = false
        ),
        "auth" to MetaRuta(
            key = "auth",
            titulo = "Autenticación",
            subtitulo = "Ingreso seguro al sistema",
            icono = WiIcons.Lock,
            requiereLayout = false,
            esNavPrincipal = false
        ),
        "empresas" to MetaRuta(
            key = "empresas",
            titulo = "Mis Empresas & Negocios",
            subtitulo = "Gestión de cafeterías y locales activos",
            icono = WiIcons.Building,
            tabs = listOf(
                MetaTab("Mis Empresas", Icons.Rounded.Home),
                MetaTab("Crear Nueva", Icons.Rounded.Add),
                MetaTab("Ajustes", Icons.Rounded.Settings)
            ),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "mesas" to MetaRuta(
            key = "mesas",
            titulo = "Ventas y Control de Mesas",
            subtitulo = "Salón en tiempo real · Hawka Cafetería",
            icono = WiIcons.Restaurant,
            tabs = listOf(
                MetaTab("Mesas", WiIcons.Restaurant),
                MetaTab("Venta Rápida", WiIcons.PointOfSale),
                MetaTab("Reservas", WiIcons.Star)
            ),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "pagar" to MetaRuta(
            key = "pagar",
            titulo = "Caja & Flujo de Pago",
            subtitulo = "Resumen de consumo y métodos de cobro",
            icono = WiIcons.PointOfSale,
            tabs = listOf(
                MetaTab("Resumen Hoy", WiIcons.PointOfSale),
                MetaTab("Ventas Ayer", Icons.Rounded.Info),
                MetaTab("Arqueo de Caja", Icons.Rounded.Settings)
            ),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "inventario" to MetaRuta(
            key = "inventario",
            titulo = "Inventario de Insumos",
            subtitulo = "Control de stock y recetario por dosis",
            icono = WiIcons.Inventory,
            tabs = listOf(
                MetaTab("Insumos", WiIcons.Inventory),
                MetaTab("Recetario", Icons.Rounded.Info),
                MetaTab("Alertas Stock", WiIcons.Star)
            ),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "reportes" to MetaRuta(
            key = "reportes",
            titulo = "Analíticas & Reportes",
            subtitulo = "Dashboard de ventas y métricas del dueño",
            icono = WiIcons.BarChart,
            tabs = listOf(
                MetaTab("Dashboard", WiIcons.BarChart),
                MetaTab("Ventas", WiIcons.PointOfSale),
                MetaTab("Meseros", Icons.Rounded.Home)
            ),
            requiereLayout = true,
            esNavPrincipal = true
        )
    )

    val DEFAULT = METADATOS["empresas"]!!
}
