package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.WiField
import com.mesawii.core.kidev.WiMain
import com.mesawii.core.kidev.WiSelect
import com.mesawii.core.kidev.WiSwitch
import com.mesawii.feature.empresas.data.ModeloEmpresa

/**
 * ⚙️ AjustesEmpresaTab.kt — Sub-pantalla (Pestaña 2): Ajustes y Configuración de Empresa con WiSelect y Switches Apple Pro Reactivos.
 */
@Composable
fun AjustesEmpresaTab(
    empresas: List<ModeloEmpresa>,
    empresaSeleccionada: ModeloEmpresa?,
    onSeleccionarEmpresaParaAjustes: (ModeloEmpresa) -> Unit,
    onGuardarAjustes: (
        empresa: ModeloEmpresa,
        nombreComercial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String?,
        pinSol: String?,
        logoUrl: String?,
        aceptaNotaVenta: Boolean,
        aceptaBoleta: Boolean,
        aceptaFactura: Boolean,
        formatoTicketera: String
    ) -> Unit,
    onToggleCampo: (empresa: ModeloEmpresa, campo: String, nuevoValor: Boolean) -> Unit = { _, _, _ -> },
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (empresas.isEmpty()) {
        WiMain(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Registra una empresa primero para configurar sus ajustes y facturación",
                    style = WiText.body,
                    color = WiCss.tx3
                )
            }
        }
        return
    }

    val empresaActual = empresaSeleccionada ?: empresas.first()
    val opcionesEmpresas = empresas.map { it.nombreComercial.ifBlank { "Empresa sin nombre" } }

    var nombreComercial by remember(empresaActual) { mutableStateOf(empresaActual.nombreComercial) }
    var direccion by remember(empresaActual) { mutableStateOf(empresaActual.direccion ?: "") }
    var telefono by remember(empresaActual) { mutableStateOf(empresaActual.telefono ?: "") }
    var moneda by remember(empresaActual) { mutableStateOf("PEN") }
    var ubigeo by remember(empresaActual) { mutableStateOf(empresaActual.ubigeo ?: "") }
    var pinSol by remember(empresaActual) { mutableStateOf("") }
    var logoUrl by remember(empresaActual) { mutableStateOf(empresaActual.logo ?: "") }

    // 🎚️ Switches de Comprobantes Reactivos (Guardan al instante en Supabase + Feedback)
    val aceptaNotaVenta = empresaActual.notaVenta
    val aceptaBoleta = empresaActual.boleta
    val aceptaFactura = empresaActual.factura

    // 🖨️ Formato de Ticketera POS
    var formatoTicketera by remember(empresaActual) { mutableStateOf("80 mm (Térmica Estándar)") }
    val opcionesTicketera = listOf("58 mm (Móvil / Bluetooth)", "80 mm (Térmica Estándar)")

    WiMain(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 🔽 Selector de Empresa activa a configurar (WiSelect)
            Text(
                text = "Empresa a Configurar",
                style = WiText.tiny,
                color = WiCss.tx3,
                fontWeight = FontWeight.Bold
            )
            WiSelect(
                selectedOption = empresaActual.nombreComercial,
                options = opcionesEmpresas,
                onOptionSelected = { nombreSeleccionado ->
                    val encontrada = empresas.firstOrNull { it.nombreComercial == nombreSeleccionado }
                    if (encontrada != null) {
                        onSeleccionarEmpresaParaAjustes(encontrada)
                    }
                },
                label = "Seleccionar empresa",
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = null,
                    tint = WiCss.mco,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Ajustes de ${empresaActual.nombreComercial}",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RUC: ${empresaActual.ruc} • ${empresaActual.razonSocial}",
                        style = WiText.small,
                        color = WiCss.tx3
                    )
                }
            }

            WiField(
                value = nombreComercial,
                onValueChange = { nombreComercial = it },
                label = "Nombre Comercial",
                leadingIcon = Icons.Rounded.Person,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth()
            )

            WiField(
                value = direccion,
                onValueChange = { direccion = it },
                label = "Dirección Fiscal / Local",
                leadingIcon = Icons.Rounded.Home,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            WiField(
                value = telefono,
                onValueChange = { telefono = it },
                label = "Teléfono de Contacto",
                leadingIcon = Icons.Rounded.Call,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // 📑 Sección Comprobantes & Facturación (Switches Apple Pro Reactivos)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = WiCss.success,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Comprobantes de Venta Permitidos",
                    style = WiText.h4,
                    color = WiCss.tx,
                    fontWeight = FontWeight.Bold
                )
            }

            WiSwitch(
                checked = aceptaNotaVenta,
                onCheckedChange = { nuevoValor ->
                    onToggleCampo(empresaActual, "nota_venta", nuevoValor)
                },
                label = "Nota de Venta (Comprobante Interno)",
                sublabel = "Serie predeterminada: NV01 • Emisión sin envío SUNAT",
                activeTrackColor = WiCss.success
            )

            WiSwitch(
                checked = aceptaBoleta,
                onCheckedChange = { nuevoValor ->
                    onToggleCampo(empresaActual, "boleta", nuevoValor)
                },
                label = "Boleta de Venta Electrónica",
                sublabel = "Serie predeterminada: B001 • Envío directo a SUNAT",
                activeTrackColor = WiCss.success
            )

            WiSwitch(
                checked = aceptaFactura,
                onCheckedChange = { nuevoValor ->
                    onToggleCampo(empresaActual, "factura", nuevoValor)
                },
                label = "Factura Electrónica RUC",
                sublabel = "Serie predeterminada: F001 • Emisión obligatoria con RUC",
                activeTrackColor = WiCss.mco
            )

            // 🖨️ Sección Ticketera POS
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = null,
                    tint = WiCss.warning,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Configuración de Impresora & Ticketera",
                    style = WiText.h4,
                    color = WiCss.tx,
                    fontWeight = FontWeight.Bold
                )
            }

            WiSelect(
                selectedOption = formatoTicketera,
                options = opcionesTicketera,
                onOptionSelected = { formatoTicketera = it },
                label = "Formato de Papel Térmico POS",
                modifier = Modifier.fillMaxWidth()
            )

            WiField(
                value = logoUrl,
                onValueChange = { logoUrl = it },
                label = "URL del Logo de la Empresa",
                leadingIcon = Icons.Rounded.Share,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth()
            )

            WiField(
                value = ubigeo,
                onValueChange = { ubigeo = it },
                label = "Ubigeo Fiscal (6 dígitos)",
                leadingIcon = Icons.Rounded.Place,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            WiField(
                value = pinSol,
                onValueChange = { pinSol = it },
                label = "PIN / Clave SOL SUNAT",
                leadingIcon = Icons.Rounded.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            WiButton(
                text = if (isLoading) "Guardando Cambios..." else "Guardar Ajustes y Facturación",
                onClick = {
                    onGuardarAjustes(
                        empresaActual,
                        nombreComercial,
                        direccion,
                        telefono,
                        moneda,
                        ubigeo,
                        pinSol,
                        logoUrl,
                        aceptaNotaVenta,
                        aceptaBoleta,
                        aceptaFactura,
                        formatoTicketera
                    )
                },
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
