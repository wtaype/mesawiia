package com.mesawii.feature.empresas.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiField
import com.mesawii.core.kidev.WiSwitch
import com.mesawii.feature.empresas.api.SunatRucResult
import com.mesawii.feature.empresas.data.ModeloEmpresa

/**
 * 🏢 FormularioEmpresa.kt — Formulario Reutilizable para Creación & Edición con Switch de Activo/Inactivo y Consulta SUNAT.
 */
@Composable
fun FormularioEmpresa(
    empresaAEditar: ModeloEmpresa? = null,
    onCrear: (
        nombreComercial: String,
        ruc: String,
        razonSocial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String?,
        pinSol: String?,
        logoUrl: String?,
        activo: Boolean
    ) -> Unit,
    onGuardarEdicion: (ModeloEmpresa) -> Unit = {},
    onConsultarSunat: (ruc: String, onExito: (SunatRucResult) -> Unit) -> Unit = { _, _ -> },
    isLoading: Boolean = false,
    isBuscandoSunat: Boolean = false,
    modifier: Modifier = Modifier
) {
    val esModoEdicion = empresaAEditar != null

    var ruc by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.ruc ?: "") }
    var razonSocial by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.razonSocial ?: "") }
    var nombreComercial by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.nombreComercial ?: "") }
    var direccion by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.direccion ?: "") }
    var telefono by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.telefono ?: "") }
    var moneda by remember(empresaAEditar) { mutableStateOf("PEN") }
    var ubigeo by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.ubigeo ?: "") }
    var pinSol by remember(empresaAEditar) { mutableStateOf("") }
    var logoUrl by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.logo ?: "") }
    var activo by remember(empresaAEditar) { mutableStateOf(empresaAEditar?.esEmpresaActiva ?: true) }

    var mostrarAvanzados by remember(empresaAEditar) { mutableStateOf(esModoEdicion) }

    val isRucOk = ruc.trim().length == 11 && ruc.all { it.isDigit() }
    val isNombreOk = nombreComercial.trim().length >= 2 || razonSocial.trim().length >= 2
    val isFormularioValido = isRucOk && isNombreOk

    fun dispararConsultaSunat() {
        if (isRucOk) {
            onConsultarSunat(ruc) { data ->
                if (data.razonSocial.isNotBlank()) razonSocial = data.razonSocial
                if (data.nombreComercial.isNotBlank()) nombreComercial = data.nombreComercial
                if (data.direccion.isNotBlank()) direccion = data.direccion
                if (data.ubigeo.isNotBlank()) ubigeo = data.ubigeo
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WiCss.wb)
            .border(1.dp, WiCss.glassBrd, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (esModoEdicion) "Editar Empresa: ${empresaAEditar?.nombreComercial}" else "Registrar Nueva Empresa",
                style = WiText.h4,
                color = WiCss.tx,
                fontWeight = FontWeight.Bold
            )

            // 1. RUC (11 dígitos) con consulta SUNAT
            WiField(
                value = ruc,
                onValueChange = { input ->
                    if (input.length <= 11 && input.all { it.isDigit() }) {
                        ruc = input
                        if (input.length == 11) {
                            dispararConsultaSunat()
                        }
                    }
                },
                label = "RUC (11 dígitos)",
                leadingIcon = Icons.Rounded.Info,
                trailingIcon = {
                    if (isBuscandoSunat) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = WiCss.mco,
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = { dispararConsultaSunat() }) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Consultar SUNAT",
                                tint = if (isRucOk) WiCss.mco else WiCss.tx3
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isSuccess = isRucOk,
                isError = ruc.isNotBlank() && !isRucOk,
                errorMessage = if (ruc.isNotBlank() && !isRucOk) "El RUC debe tener 11 dígitos" else null,
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Razón Social
            WiField(
                value = razonSocial,
                onValueChange = { razonSocial = it },
                label = "Razón Social",
                leadingIcon = Icons.Rounded.Info,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth()
            )

            // 3. Nombre Comercial
            WiField(
                value = nombreComercial,
                onValueChange = { nombreComercial = it },
                label = "Nombre Comercial",
                leadingIcon = Icons.Rounded.Person,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                isSuccess = isNombreOk,
                modifier = Modifier.fillMaxWidth()
            )

            // 4. Dirección Fiscal / Local
            WiField(
                value = direccion,
                onValueChange = { direccion = it },
                label = "Dirección Fiscal / Local",
                leadingIcon = Icons.Rounded.Home,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            // 5. Teléfono de Contacto
            WiField(
                value = telefono,
                onValueChange = { telefono = it },
                label = "Teléfono de Contacto",
                leadingIcon = Icons.Rounded.Call,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // ⚙️ Toggle de Datos Avanzados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(WiCss.inp)
                    .border(1.dp, WiCss.glassBrd.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .clickable { mostrarAvanzados = !mostrarAvanzados }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = WiCss.mco,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Datos Avanzados (Opcional)",
                        style = WiText.small,
                        color = WiCss.tx2,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = if (mostrarAvanzados) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = WiCss.tx3,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = mostrarAvanzados) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Selección de Moneda (PEN vs USD)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Moneda Principal:",
                            style = WiText.small,
                            color = WiCss.tx2,
                            fontWeight = FontWeight.Medium
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (moneda == "PEN") WiCss.mco else WiCss.inp)
                                    .clickable { moneda = "PEN" }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "PEN (S/)",
                                    style = WiText.tiny,
                                    color = if (moneda == "PEN") WiCss.tx else WiCss.tx3,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (moneda == "USD") WiCss.mco else WiCss.inp)
                                    .clickable { moneda = "USD" }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "USD ($)",
                                    style = WiText.tiny,
                                    color = if (moneda == "USD") WiCss.tx else WiCss.tx3,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Logo URL
                    WiField(
                        value = logoUrl,
                        onValueChange = { logoUrl = it },
                        label = "URL del Logo de la Empresa",
                        leadingIcon = Icons.Rounded.Share,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Ubigeo Fiscal
                    WiField(
                        value = ubigeo,
                        onValueChange = { ubigeo = it },
                        label = "Ubigeo Fiscal (6 dígitos)",
                        leadingIcon = Icons.Rounded.Place,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // PIN / Clave SOL
                    WiField(
                        value = pinSol,
                        onValueChange = { pinSol = it },
                        label = "PIN / Clave SOL SUNAT",
                        leadingIcon = Icons.Rounded.Lock,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 🎚️ Switch de Activar/Desactivar Empresa en el Formulario
            WiSwitch(
                checked = activo,
                onCheckedChange = { activo = it },
                label = "Estado de Operación de la Empresa",
                sublabel = if (activo) "Habilitada para ventas y operaciones" else "Desactivada temporalmente",
                activeTrackColor = WiCss.success
            )

            // Botón Crear / Guardar Edición
            WiButton(
                text = if (isLoading) "Guardando..." else if (esModoEdicion) "Guardar Cambios de Empresa" else "Registrar Empresa y Continuar",
                onClick = {
                    if (isFormularioValido) {
                        if (esModoEdicion && empresaAEditar != null) {
                            val empresaModificada = empresaAEditar.copy(
                                ruc = ruc.trim(),
                                razonSocial = razonSocial.trim(),
                                nombreComercial = nombreComercial.trim(),
                                direccion = direccion.trim(),
                                telefono = telefono.trim(),
                                ubigeo = ubigeo.trim(),
                                logo = logoUrl.trim(),
                                activo = activo,
                                estado = if (activo) "activo" else "inactivo"
                            )
                            onGuardarEdicion(empresaModificada)
                        } else {
                            onCrear(nombreComercial, ruc, razonSocial, direccion, telefono, moneda, ubigeo, pinSol, logoUrl, activo)
                        }
                    }
                },
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
