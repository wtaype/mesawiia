package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mesawii.feature.empresas.api.SunatRucResult
import com.mesawii.feature.empresas.components.FormularioEmpresa

/**
 * 📝 NuevoEmpresaTab.kt — Sub-pantalla (Pestaña 1): Formulario de Registro con Consulta SUNAT.
 */
@Composable
fun NuevoEmpresaTab(
    onCrear: (
        nombreComercial: String,
        ruc: String,
        razonSocial: String,
        direccion: String,
        telefono: String,
        moneda: String,
        ubigeo: String?,
        pinSol: String?
    ) -> Unit,
    onConsultarSunat: (ruc: String, onExito: (SunatRucResult) -> Unit) -> Unit,
    isLoading: Boolean = false,
    isBuscandoSunat: Boolean = false,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            FormularioEmpresa(
                onCrear = onCrear,
                onConsultarSunat = onConsultarSunat,
                isLoading = isLoading,
                isBuscandoSunat = isBuscandoSunat
            )
        }
    }
}
