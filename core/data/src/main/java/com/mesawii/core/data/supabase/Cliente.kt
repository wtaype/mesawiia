package com.mesawii.core.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

/**
 * ⚡ Cliente.kt — Instancia Singleton de SupabaseClient con Auth, Postgrest y Realtime.
 */
object Cliente {
    val instancia: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = Config.URL,
            supabaseKey = Config.ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }
}
