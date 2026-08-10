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
import com.mesawii.feature.empresas.data.EmpresaModelo

/**
 * ⚙️ AjustesEmpresaTab.kt — Sub-pantalla (Pestaña 2): Ajustes y Configuración de la Empresa Activa enmarcada en WiMain.
 */
@Composable
fun AjustesEmpresaTab(
    empresa: EmpresaModelo?,
    onGuardarAjustes: (
        empresa: EmpresaModelo,
        nombreComercial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String?,
        pinSol: String?,
        logoUrl: String?
    ) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (empresa == null) {
        WiMain(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Selecciona una empresa registrada para configurar sus ajustes",
                    style = WiText.body,
                    color = WiCss.tx3
                )
            }
        }
        return
    }

    var nombreComercial by remember(empresa) { mutableStateOf(empresa.nombreComercial) }
    var direccion by remember(empresa) { mutableStateOf(empresa.direccion) }
    var telefono by remember(empresa) { mutableStateOf(empresa.telefono) }
    var moneda by remember(empresa) { mutableStateOf("PEN") }
    var ubigeo by remember(empresa) { mutableStateOf(empresa.ubigeo ?: "") }
    var pinSol by remember(empresa) { mutableStateOf("") }
    var logoUrl by remember(empresa) { mutableStateOf(empresa.logo ?: "") }

    WiMain(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
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
                        text = "Ajustes de ${empresa.nombreComercial}",
                        style = WiText.h4,
                        color = WiCss.tx,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RUC: ${empresa.ruc} • ${empresa.razonSocial}",
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

            Text(
                text = "Configuración Facturación y SOL",
                style = WiText.h4,
                color = WiCss.tx,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
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
                text = if (isLoading) "Guardando Cambios..." else "Guardar Ajustes",
                onClick = {
                    onGuardarAjustes(
                        empresa,
                        nombreComercial,
                        direccion,
                        telefono,
                        moneda,
                        ubigeo,
                        pinSol,
                        logoUrl
                    )
                },
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
