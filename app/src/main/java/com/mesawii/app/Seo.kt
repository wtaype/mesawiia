package com.mesawii.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.mesawii.core.kicss.WiIcons

/**
 * 📜 Seo.kt — Single Source of Truth para metadatos y navegación en MesaWii.
 * Ubicado en la raíz com.mesawii.app junto a MainViewModel.kt
 */
data class MetaRuta(
    val key: String,
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val tabs: List<String> = emptyList(),
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
            icono = WiIcons.Building, // Edificio (Building)
            tabs = listOf("Mis Empresas", "Crear Nueva", "Ajustes"),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "mesas" to MetaRuta(
            key = "mesas",
            titulo = "Ventas y Control de Mesas",
            subtitulo = "Salón en tiempo real · Hawka Cafetería",
            icono = WiIcons.Restaurant,
            tabs = listOf("Mesas", "Venta Rápida", "Reservas"),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "pagar" to MetaRuta(
            key = "pagar",
            titulo = "Caja & Flujo de Pago",
            subtitulo = "Resumen de consumo y métodos de cobro",
            icono = WiIcons.PointOfSale,
            tabs = listOf("Resumen Hoy", "Ventas Ayer", "Arqueo de Caja"),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "inventario" to MetaRuta(
            key = "inventario",
            titulo = "Inventario de Insumos",
            subtitulo = "Control de stock y recetario por dosis",
            icono = WiIcons.Inventory,
            tabs = listOf("Insumos", "Recetario", "Alertas Stock"),
            requiereLayout = true,
            esNavPrincipal = true
        ),
        "reportes" to MetaRuta(
            key = "reportes",
            titulo = "Analíticas & Reportes",
            subtitulo = "Dashboard de ventas y métricas del dueño",
            icono = WiIcons.BarChart,
            tabs = listOf("Dashboard", "Ventas", "Meseros"),
            requiereLayout = true,
            esNavPrincipal = true
        )
    )

    val DEFAULT = METADATOS["empresas"]!!
}
