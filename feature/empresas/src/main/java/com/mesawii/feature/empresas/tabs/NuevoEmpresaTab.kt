package com.mesawii.feature.empresas.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mesawii.feature.empresas.api.SunatRucResult
import com.mesawii.feature.empresas.components.FormularioEmpresa
import com.mesawii.feature.empresas.data.ModeloEmpresa

/**
 * 📝 NuevoEmpresaTab.kt — Sub-pantalla (Pestaña 1): Formulario Reutilizable de Registro y Edición con Consulta SUNAT.
 */
@Composable
fun NuevoEmpresaTab(
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
    onConsultarSunat: (ruc: String, onExito: (SunatRucResult) -> Unit) -> Unit,
    isLoading: Boolean = false,
    isBuscandoSunat: Boolean = false,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            FormularioEmpresa(
                empresaAEditar = empresaAEditar,
                onCrear = onCrear,
                onGuardarEdicion = onGuardarEdicion,
                onConsultarSunat = onConsultarSunat,
                isLoading = isLoading,
                isBuscandoSunat = isBuscandoSunat
            )
        }
    }
}
