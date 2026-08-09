package com.mesawii.app

/**
 * 🗺️ Rutas.kt — Router dinámico autogenerado derivado de Seo.kt.
 * Ubicado en la raíz com.mesawii.app junto a MainViewModel.kt
 */
object Rutas {
    /** Lista de rutas navegables principales para la Sidebar */
    val RUTAS_SIDEBAR: List<MetaRuta> = Seo.METADATOS.values.filter { it.esNavPrincipal }

    /** Obtiene los metadatos de una ruta o retorna el valor por defecto */
    fun getMeta(key: String): MetaRuta = Seo.METADATOS[key] ?: Seo.DEFAULT
}
