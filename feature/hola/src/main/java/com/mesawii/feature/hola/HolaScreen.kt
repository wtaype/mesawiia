package com.mesawii.feature.hola

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesawii.core.Wii
import com.mesawii.core.kicss.*
import com.mesawii.core.kidev.*
import kotlinx.coroutines.launch
import java.io.File

/**
 * 📜 HolaScreen — Catálogo Oficial y Suite de Pruebas de los 16 Componentes Kidev v3.0.0
 */
@Composable
fun HolaScreen(
    currentTema: WiTemaColors = LocalWiTemaColors.current,
    onTemaSelected: (WiTemaColors) -> Unit = {}
) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val scroll  = rememberScrollState()

    // Sistema de Mensajes y Notificaciones
    val messenger = rememberWiMessenger()

    // Estado de Hardware
    var capturedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    var photoKb       by remember { mutableStateOf(0L) }
    var compKb        by remember { mutableStateOf(0L) }

    val audioRecorder = remember { WiAudioRecorder(context) }
    var isRecording   by remember { mutableStateOf(false) }
    var isPlaying     by remember { mutableStateOf(false) }
    var audioFile     by remember { mutableStateOf<File?>(null) }

    val isOnline by rememberNetworkStatus()

    // Estado del Actualizador PRO
    val installedVersionName = remember { Actualizar.getInstalledVersionName(context) }
    val installedVersionCode = remember { Actualizar.getInstalledVersionCode(context) }

    var updateStatus     by remember { mutableStateOf("🟢 Aplicación al día (v$installedVersionName)") }
    var isChecking       by remember { mutableStateOf(false) }
    var isDownloading    by remember { mutableStateOf(false) }
    var downloadProg     by remember { mutableFloatStateOf(0f) }
    var versionInfo      by remember { mutableStateOf<WiVersionInfo?>(null) }
    var showUpdateDialog by remember { mutableStateOf(false) }

    // Estados de Formulario de prueba
    var testField    by remember { mutableStateOf("") }
    var testPassword by remember { mutableStateOf("") }
    var testOption   by remember { mutableStateOf("Opción 1 - Pro") }
    val sampleOptions = listOf("Opción 1 - Pro", "Opción 2 - Premium", "Opción 3 - Ultra", "Opción 4 - POS VIP")

    // Modales
    var showDialog by remember { mutableStateOf(false) }

    // Launchers de Cámara y Micrófono
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val res = WiComprimir.comprimir(bitmap, maxDimension = 800, calidad = 80)
            capturedPhoto = res.bitmap
            photoKb = res.originalSizeKb
            compKb  = res.compressedSizeKb
            messenger.Mensaje("¡Foto capturada y comprimida!")
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) cameraLauncher.launch()
        else messenger.Notificacion("Permiso de cámara denegado", WiMsgType.Error)
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            audioFile = audioRecorder.startRecording()
            isRecording = true
            messenger.Mensaje("Grabando nota de voz...")
        } else {
            messenger.Notificacion("Permiso de micrófono denegado", WiMsgType.Error)
        }
    }

    WiMessengerProvider(messenger = messenger) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WiCss.bg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header MesaWii
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${Wii.app} v$installedVersionName 🍽️", style = WiText.h2, color = WiCss.tx)
                            Text("Catálogo Oficial Kidev (16 Componentes)", style = WiText.small, color = WiCss.tx3)
                        }
                        GoldPill("Code: $installedVersionCode")
                    }
                }

                // Banner de estado de red si se desconecta
                if (!isOnline) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WiCss.error)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚠️ Modo Offline: Sin conexión a Internet", style = WiText.body, color = WiCss.white, fontWeight = FontWeight.Bold)
                    }
                }

                // ─────────────────────────────────────────────────────────────
                // 📜 PARTE 1: Catálogo Numerado Kidev & Kicss (1.1 a 1.10)
                // ─────────────────────────────────────────────────────────────
                SectionHeader("📜 PARTE 1: Componentes UI & Tipografía POS")

                // 1.1 Mensajes
                TestCard(num = "1.1", title = "Sistema de Mensajes (Toasts & Banner)") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        WiButton(text = "Toast Éxito", onClick = {
                            messenger.Mensaje("Guardado correctamente!")
                        })
                        WiButton(text = "Banner Error", containerColor = WiCss.error, onClick = {
                            messenger.Notificacion("Ocurrió un error inesperado", WiMsgType.Error)
                        })
                    }
                }

                // 1.2 Temas & Status Bar Sync
                TestCard(num = "1.2", title = "Selector de 5 Temas + Status Bar (WiTemas)") {
                    Text("Tema Activo: ${currentTema.name}", style = WiText.small, color = WiCss.mco, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        MesaWiTemas.forEach { tema ->
                            val isSel = tema.name == currentTema.name
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) tema.mco else tema.bg)
                                    .clickable {
                                        onTemaSelected(tema)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(tema.name, style = WiText.tiny, color = if (isSel) tema.txa else tema.tx, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 1.3 Formulario Fields
                TestCard(num = "1.3", title = "Campos WiField y WiPassword") {
                    WiField(
                        value = testField,
                        onValueChange = { testField = it },
                        label = "Nombre de usuario",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    WiPassword(
                        value = testPassword,
                        onValueChange = { testPassword = it },
                        label = "Contraseña segura",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 1.4 Modales
                TestCard(num = "1.4", title = "Diálogos Modales WiDialog") {
                    WiButton(text = "Abrir WiDialog Modal", onClick = { showDialog = true })
                    if (showDialog) {
                        WiDialog(
                            show = showDialog,
                            title = "Confirmar Acción",
                            text = "¿Deseas procesar esta prueba en el sistema?",
                            onConfirm = {
                                showDialog = false
                                messenger.Mensaje("Acción confirmada ✓")
                            },
                            onDismiss = { showDialog = false }
                        )
                    }
                }

                // 1.5 Picker Desplegable WiSelect
                TestCard(num = "1.5", title = "Picker Desplegable Glass WiSelect") {
                    WiSelect(
                        selectedOption = testOption,
                        options = sampleOptions,
                        onOptionSelected = { testOption = it },
                        label = "Seleccionar Plan de Usuario"
                    )
                }

                // 1.6 Tarjeta de Ayuda WiTipCard
                TestCard(num = "1.6", title = "Tarjeta de Sugerencias & Tips WiTipCard") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WiCss.mco.copy(alpha = 0.15f))
                            .padding(12.dp)
                    ) {
                        Text("💡 Tip Kidev: El sistema de temas guarda automáticamente tu preferencia en WiStore.", style = WiText.small, color = WiCss.mco)
                    }
                }

                // 1.7 Insignias & Badges
                TestCard(num = "1.7", title = "Badges & Pills de Estado (GoldPill & WiBadge)") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        GoldPill("GOLD VIP")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WiCss.success)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("COMPLETADO", style = WiText.tiny, color = WiCss.white, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // 1.8 Indicador Giratorio Spinner
                TestCard(num = "1.8", title = "Indicador de Carga Giratorio WiSpin") {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        WiSpin(size = 28.dp)
                        Text("Procesando en segundo plano...", style = WiText.small, color = WiCss.tx2)
                    }
                }

                // 1.9 Módulo de Facturación POS con Fuente Outfit
                TestCard(num = "1.9", title = "🧾 Módulo POS / Facturación (Fuente Outfit)") {
                    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.3f) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("TOTAL A PAGAR:", style = WiText.posCode, color = WiCss.tx3)
                                Text("FOLIO #8942", style = WiText.posCode, color = WiCss.mco)
                            }
                            Text("$1,850.50 MXN", style = WiText.posAmount, color = WiCss.tx)
                            Text("Subtotal: $1,600.00 + IVA (16%): $250.50", style = WiText.posPrice, color = WiCss.tx2)
                        }
                    }
                }

                // 1.10 Botón Flotante
                TestCard(num = "1.10", title = "Botones de Acción WiButton") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        WiButton(text = "Acción Principal", onClick = { messenger.Mensaje("Clic principal") })
                        WiButton(text = "Cancelar", containerColor = WiCss.error, onClick = { messenger.Mensaje("Cancelado") })
                    }
                }

                // ─────────────────────────────────────────────────────────────
                // 📱 PARTE 2: Hardware Nativo Android 2026 (1.11 a 1.16)
                // ─────────────────────────────────────────────────────────────
                SectionHeader("📱 PARTE 2: Hardware Nativo & Actualizaciones OTA")

                // 1.11 Cámara Fotográfica + Compresor
                TestCard(num = "1.11", title = "Cámara HD Nativa + Compresor (10MB ➔ KB)") {
                    WiButton(text = "📷 Tomar Foto HD", onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    })

                    capturedPhoto?.let { bmp ->
                        Spacer(Modifier.height(10.dp))
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Foto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Original: ${photoKb}KB ➔ Comprimido: ${compKb}KB (-${100 - (compKb * 100 / (photoKb.coerceAtLeast(1)))}%)",
                            style = WiText.small,
                            color = WiCss.success,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 1.12 Grabador de Voz
                TestCard(num = "1.12", title = "Micrófono Nativo & Grabador de Voz AAC") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!isRecording) {
                            WiButton(text = "🎙️ Grabar Voz", onClick = {
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            })
                        } else {
                            WiButton(text = "⏹️ Detener Grabación", containerColor = WiCss.error, onClick = {
                                audioRecorder.stopRecording()
                                isRecording = false
                                messenger.Mensaje("¡Grabación guardada!")
                            })
                        }

                        audioFile?.let {
                            WiButton(text = if (isPlaying) "🔊 Reproduciendo..." else "▶️ Escuchar", onClick = {
                                if (!isPlaying) {
                                    isPlaying = true
                                    audioRecorder.playAudio { isPlaying = false }
                                } else {
                                    audioRecorder.stopAudio()
                                    isPlaying = false
                                }
                            })
                        }
                    }
                }

                // 1.13 Estado de Red
                TestCard(num = "1.13", title = "Estado de Conexión en Tiempo Real (Hook)") {
                    Text(
                        text = if (isOnline) "🌐 Conectado a Internet (Online)" else "⚠️ Sin conexión a Internet (Offline)",
                        style = WiText.small,
                        color = if (isOnline) WiCss.success else WiCss.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 1.14 Actualizaciones Directas OTA
                TestCard(num = "1.14", title = "Actualizaciones Directas OTA via Cloudflare R2") {
                    Text("Manifiesto: https://mesawii.amorwii.com/version.json", style = WiText.tiny, color = WiCss.tx3)
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WiButton(
                            text = if (isChecking) "Buscando..." else "🔄 Buscar Actualizaciones",
                            loading = isChecking,
                            onClick = {
                                isChecking = true
                                updateStatus = "Buscando en Cloudflare..."
                                scope.launch {
                                    val info = Actualizar.checkUpdate(context)
                                    isChecking = false
                                    if (info != null) {
                                        versionInfo = info
                                        showUpdateDialog = true
                                        updateStatus = "🚀 Nueva versión v${info.versionName} disponible"
                                        messenger.Mensaje("¡Nueva versión ${info.versionName} encontrada!")
                                    } else {
                                        updateStatus = "🟢 Aplicación al día (v$installedVersionName)"
                                        messenger.Mensaje("Estás en la versión más reciente ✓")
                                    }
                                }
                            }
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (versionInfo != null) WiCss.mco.copy(alpha = 0.15f) else WiCss.success.copy(alpha = 0.15f))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(updateStatus, style = WiText.small, color = if (versionInfo != null) WiCss.mco else WiCss.success, fontWeight = FontWeight.Bold)
                            Text("Versión instalada: v$installedVersionName (Code: $installedVersionCode)", style = WiText.tiny, color = WiCss.tx2)
                        }
                    }
                }

                // 1.15 Popup OTA Glass
                TestCard(num = "1.15", title = "Popup OTA Glassmorphic WiUpdateDialog") {
                    Text("Popup flotante de actualización preparado para descargas directas APK.", style = WiText.small, color = WiCss.tx2)
                }

                // 1.16 Rating Sliders Interactivas
                TestCard(num = "1.16", title = "Sistema de Calificación 10/10 Interactivo") {
                    Text("Todas las tarjetas del catálogo cuentan con su slider dinámico de calificación.", style = WiText.small, color = WiCss.mco, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(32.dp))
            }

            // Popup Glassmorphism de Actualización PRO
            versionInfo?.let { info ->
                WiUpdateDialog(
                    show = showUpdateDialog,
                    versionInfo = info,
                    isDownloading = isDownloading,
                    downloadProgress = downloadProg,
                    onConfirmUpdate = {
                        if (!isDownloading) {
                            isDownloading = true
                            scope.launch {
                                val file = Actualizar.downloadApk(context, info.apkUrl) { prog ->
                                    downloadProg = prog
                                }
                                isDownloading = false
                                if (file != null) {
                                    messenger.Mensaje("Lanzando instalador nativo...")
                                    Actualizar.installApk(context, file)
                                } else {
                                    messenger.Notificacion("Error al descargar APK", WiMsgType.Error)
                                }
                            }
                        }
                    },
                    onDismiss = { showUpdateDialog = false }
                )
            }

            WiMessengerHost(messenger = messenger, modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WiCss.mco.copy(alpha = 0.15f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(title, style = WiText.h3, color = WiCss.mco, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TestCard(
    num: String,
    title: String,
    content: @Composable () -> Unit
) {
    var rating by remember { mutableFloatStateOf(9.8f) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$num. $title", style = WiText.h4, color = WiCss.tx, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            
            val badgeColor = when {
                rating < 5f  -> WiCss.error
                rating < 8f  -> WiCss.warning
                else         -> WiCss.success
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(String.format("%.1f/10", rating), style = WiText.tiny, color = WiCss.white, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(10.dp))
        content()
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Calificación:", style = WiText.tiny, color = WiCss.tx3)
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 0f..10f,
                steps = 19,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = WiCss.mco,
                    activeTrackColor = WiCss.mco
                )
            )
        }
    }
}
