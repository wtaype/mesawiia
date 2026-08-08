package com.mesawii.core.kidev

import com.mesawii.core.kicss.*

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

/**
 * Storage.kt — Motor de almacenamiento persistente local para MesaWii (WiStore)
 */
class WiStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mesawii_store", Context.MODE_PRIVATE)

    fun save(key: String, value: String): Boolean {
        return prefs.edit().putString(key, value).commit()
    }

    fun get(key: String, fallback: String = ""): String {
        return prefs.getString(key, fallback) ?: fallback
    }

    fun saveBool(key: String, value: Boolean): Boolean {
        return prefs.edit().putBoolean(key, value).commit()
    }

    fun getBool(key: String, fallback: Boolean = false): Boolean {
        return prefs.getBoolean(key, fallback)
    }

    fun saveLong(key: String, value: Long): Boolean {
        return prefs.edit().putLong(key, value).commit()
    }

    fun getLong(key: String, fallback: Long = 0L): Long {
        return prefs.getLong(key, fallback)
    }

    /** Guarda un valor JSON con tiempo de expiración (TTL en horas) */
    fun savels(key: String, jsonValue: String, horas: Long = 24): Boolean {
        val expiryMs = System.currentTimeMillis() + (horas * 3600000)
        val json = JSONObject().apply {
            put("v", jsonValue)
            put("exp", expiryMs)
        }
        return save(key, json.toString())
    }

    /** Obtiene un valor guardado con TTL; retorna null si ya expiró */
    fun getls(key: String): String? {
        val raw = get(key, "")
        if (raw.isEmpty()) return null
        return try {
            val json = JSONObject(raw)
            val exp = json.optLong("exp", 0L)
            if (exp > 0 && System.currentTimeMillis() > exp) {
                remove(key)
                null
            } else {
                json.optString("v", null)
            }
        } catch (e: Exception) {
            raw
        }
    }

    fun remove(vararg keys: String) {
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}

fun wiStore(context: Context): WiStore = WiStore(context)
