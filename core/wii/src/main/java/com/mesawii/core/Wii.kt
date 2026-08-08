// ANDROID - INFORMACIÓN DEL APP
package com.mesawii.core

object Wii {
    const val id = "mesawii"
    const val app = "MesaWii"
    const val icon = "fa-utensils"
    const val titulo = "MesaWii | Sistema de Gestión de Mesas y Caja"
    const val keywii = "mesawii, pos, restaurante, comandas, caja, ticketera, escpos"
    const val descri = "Punto de venta y comandas ultra-veloz para restaurantes y cafeterías con impresión directa a ticketera."
    const val lanzamiento = 2026
    const val by = "@wilder.taype"
    const val linkweb = "https://mesawii.web.app"
    const val linkme = "https://wtaype.github.io/"
    const val packageName = "com.mesawii.app"
    const val version = "v2"
    const val dtema = "paz"
}

/** ACTUALIZAR AL TAG POR SEGURIDAD [TAG NUEVO] (1)
git tag v2 -m "Version v2" ; git push origin v2 

ACTUALIZACIÓN AL MAIN PRINCIPAL DEL PROYECTO [MAIN] (2)
git add . ; git commit -m "Actualizacion Principal v2.0.0" ; git push origin main

// REEMPLAZAR TAG DE SEGURIDAD EXISTENTE [TAG REMPLAZO] (3)
git tag -d v2 ; git tag v2 -m "Version v2 actualizada" ; git push origin v2 --force

// Actualizar versiones de seguridad [COMPILAR, INSTALAR Y EJECUTAR] (4)
.\gradlew.bat assembleDebug ; adb install -r app/build/outputs/apk/debug/app-debug.apk ; adb shell am start -n com.mesawii.app/.MainActivity;
*/
