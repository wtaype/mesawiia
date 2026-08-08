package com.mesawii.core.kidev

import com.mesawii.core.kicss.*

import java.time.LocalDateTime

/** Saludo según hora del día */
fun saludar(): String = when (LocalDateTime.now().hour) {
    in 0..11 -> "Buenos días"
    in 12..17 -> "Buenas tardes"
    else -> "Buenas noches"
}
