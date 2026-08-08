// Actualizar.kt — Motor de actualización directa OTA desde Cloudflare R2
package com.mesawii.core.kidev

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.mesawii.core.kicss.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Modelo de datos de la versión remota
 */
data class WiVersionInfo(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val releaseNotes: String,
    val isMandatory: Boolean
)

/**
 * Actualizar — Motor centralizado de actualizaciones OTA para MesaWii
 */
object Actualizar {
    const val MANIFEST_URL = "https://mesawii.amorwii.com/version.json"

    /**
     * Obtiene dinámicamente el versionCode real instalado en Android
     */
    fun getInstalledVersionCode(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: Exception) {
            1
        }
    }

    /**
     * Obtiene dinámicamente el versionName real instalado (ej. "2.0.0")
     */
    fun getInstalledVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * Consulta version.json y compara por versionCode O por versionName (soporta parches 2.1.0, 2.0.1)
     */
    suspend fun checkUpdate(context: Context): WiVersionInfo? = withContext(Dispatchers.IO) {
        val currentCode = getInstalledVersionCode(context)
        val currentName = getInstalledVersionName(context)
        
        try {
            val url = URL(MANIFEST_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "GET"

            if (conn.responseCode == 200) {
                val jsonString = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(jsonString)
                val serverCode = json.getInt("versionCode")
                val serverName = json.optString("versionName", "1.0.0")

                val isNewerCode = serverCode > currentCode
                val isNewerName = isVersionNameGreater(serverName, currentName)

                // Detecta actualización si aumenta el versionCode O si aumenta el versionName (ej. 2.1.0)
                if (isNewerCode || isNewerName) {
                    return@withContext WiVersionInfo(
                        versionCode = serverCode,
                        versionName = serverName,
                        apkUrl = json.getString("apkUrl"),
                        releaseNotes = json.optString("releaseNotes", "Nuevas mejoras y optimizaciones."),
                        isMandatory = json.optBoolean("isMandatory", false)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    /**
     * Compara nombres de versión semánticos (ej. "2.1.0" > "2.0.0")
     */
    private fun isVersionNameGreater(serverVersion: String, currentVersion: String): Boolean {
        try {
            val serverParts = serverVersion.split(".").map { it.toIntOrNull() ?: 0 }
            val currentParts = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

            val maxLength = maxOf(serverParts.size, currentParts.size)
            for (i in 0 until maxLength) {
                val s = serverParts.getOrElse(i) { 0 }
                val c = currentParts.getOrElse(i) { 0 }
                if (s > c) return true
                if (s < c) return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Descarga el archivo APK en segundo plano notificando el progreso
     */
    suspend fun downloadApk(
        context: Context,
        apkUrl: String,
        onProgress: (Float) -> Unit
    ): File? = withContext(Dispatchers.IO) {
        try {
            val url = URL(apkUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 10000
            conn.readTimeout = 30000
            conn.connect()

            val fileLength = conn.contentLength
            val apkFile = File(context.cacheDir, "update.apk")
            if (apkFile.exists()) apkFile.delete()

            conn.inputStream.use { input ->
                FileOutputStream(apkFile).use { output ->
                    val data = ByteArray(4096)
                    var total: Long = 0
                    var count: Int
                    while (input.read(data).also { count = it } != -1) {
                        total += count.toLong()
                        if (fileLength > 0) {
                            onProgress(total.toFloat() / fileLength.toFloat())
                        }
                        output.write(data, 0, count)
                    }
                    output.flush()
                }
            }
            return@withContext apkFile
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Lanza el instalador NATIVO DIRECTO de Android (sin mostrar el menú "Open with")
     */
    fun installApk(context: Context, apkFile: File) {
        try {
            val apkUri = getUriForFile(context, apkFile)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val packageManager = context.packageManager
            val installerApps = listOf(
                "com.google.android.packageinstaller",
                "com.android.packageinstaller"
            )

            for (pkg in installerApps) {
                try {
                    packageManager.getPackageInfo(pkg, 0)
                    intent.setPackage(pkg)
                    break
                } catch (e: PackageManager.NameNotFoundException) {
                }
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(getUriForFile(context, apkFile), "application/vnd.android.package-archive")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(fallbackIntent)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun getUriForFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }
}

/**
 * 🚀 WiUpdateDialog — Popup Glassmorphic elegante para notificar actualizaciones oficiales
 */
@Composable
fun WiUpdateDialog(
    show: Boolean,
    versionInfo: WiVersionInfo,
    isDownloading: Boolean,
    downloadProgress: Float,
    onConfirmUpdate: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    WiDialog(
        show = show,
        title = "🚀 Actualización Disponible v${versionInfo.versionName}",
        text = versionInfo.releaseNotes,
        onConfirm = onConfirmUpdate,
        onDismiss = onDismiss,
        confirmText = if (isDownloading) "Descargando ${(downloadProgress * 100).toInt()}%..." else "Actualizar Ahora",
        dismissText = if (versionInfo.isMandatory) "" else "Más tarde"
    )
}
