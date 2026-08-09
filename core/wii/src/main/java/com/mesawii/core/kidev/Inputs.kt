package com.mesawii.core.kidev

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.FzSmart
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiIcons
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kicss.fPoppins

/**
 * ✏️ Inputs.kt — Campos de Entrada y Formularios Atómicos de MesaWii.
 */
@Composable
fun WiField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    isSuccess: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontFamily = fPoppins, fontSize = FzSmart.field) },
            leadingIcon = leadingIcon?.let { { Icon(it, null, tint = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.mco, modifier = Modifier.size(FzSmart.fieldIcon)) } },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLines = maxLines,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = wiFieldColors(isSuccess = isSuccess, isError = isError),
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = WiText.small,
                color = WiCss.error,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun WiPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var visible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontFamily = fPoppins, fontSize = FzSmart.field) },
            leadingIcon = { Icon(Icons.Rounded.Lock, null, tint = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.mco, modifier = Modifier.size(FzSmart.fieldIcon)) },
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) WiIcons.VisibilityOff else WiIcons.Visibility,
                        contentDescription = null,
                        tint = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.mco
                    )
                }
            },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = wiFieldColors(isSuccess = isSuccess, isError = isError),
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = WiText.small,
                color = WiCss.error,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun wiFieldColors(isSuccess: Boolean = false, isError: Boolean = false) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.mco,
    unfocusedBorderColor = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.brd.copy(alpha = 0.60f),
    focusedContainerColor = WiCss.inp.copy(alpha = 0.90f),
    unfocusedContainerColor = WiCss.inp.copy(alpha = 0.60f),
    focusedTextColor = WiCss.tx,
    unfocusedTextColor = WiCss.tx,
    errorTextColor = WiCss.tx,
    errorBorderColor = WiCss.error,
    errorLeadingIconColor = WiCss.error,
    errorTrailingIconColor = WiCss.error,
    errorLabelColor = WiCss.error,
    focusedLabelColor = if (isError) WiCss.error else if (isSuccess) WiCss.success else WiCss.mco,
    unfocusedLabelColor = WiCss.tx3
)
