package com.mesawii.core.data.supabase

import com.mesawii.core.data.BuildConfig

/**
 * ⚙️ Config.kt — Lee credenciales seguras de local.properties inyectadas en BuildConfig.
 */
object Config {
    val URL: String get() = BuildConfig.SUPABASE_URL
    val ANON_KEY: String get() = BuildConfig.SUPABASE_ANON_KEY
}
