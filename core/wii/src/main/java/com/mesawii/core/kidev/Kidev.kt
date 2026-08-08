package com.mesawii.core.kidev

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.FzSmart
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiIcons
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kicss.dpSmart
import com.mesawii.core.kicss.fPoppins
import com.mesawii.core.kicss.softGlassShadow

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    intensity: Float = 0.55f,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = WiCss.glassShape(intensity)
    val cardContent: @Composable () -> Unit = {
        Column(Modifier.padding(FzSmart.cardPad)) {
            content()
        }
    }
    if (onClick == null) {
        Card(
            modifier = modifier.softGlassShadow(),
            shape = shape,
            colors = WiCss.glassColors(intensity),
            border = WiCss.glassBorder(intensity),
            content = { cardContent() },
        )
    } else {
        Card(
            onClick = onClick,
            modifier = modifier.softGlassShadow(),
            shape = shape,
            colors = WiCss.glassColors(intensity),
            border = WiCss.glassBorder(intensity),
            content = { cardContent() },
        )
    }
}

@Composable
fun GoldPill(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(WiCss.mco.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(text.uppercase(), style = WiText.label, color = WiCss.mco, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun WiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    loading: Boolean = false,
    containerColor: Color? = null
) {
    val alpha = if (loading) 0.5f else 1.0f
    val backgroundModifier = if (containerColor != null) {
        Modifier.background(containerColor.copy(alpha = alpha))
    } else {
        Modifier.background(
            Brush.linearGradient(
                listOf(
                    WiCss.mco.copy(alpha = alpha),
                    WiCss.hva.copy(alpha = alpha)
                )
            )
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .then(backgroundModifier)
            .clickable(enabled = !loading, onClick = onClick)
            .padding(horizontal = dpSmart(15f, 1.6f, 20f), vertical = dpSmart(8f, 1.0f, 12f)),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(color = WiCss.white, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(icon, null, tint = WiCss.white, modifier = Modifier.size(FzSmart.buttonIcon))
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = WiCss.txa,
                    fontFamily = fPoppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = FzSmart.button
                )
            }
        }
    }
}

@Composable
fun WiField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = fPoppins, fontSize = FzSmart.field) },
        leadingIcon = leadingIcon?.let { { Icon(it, null, tint = WiCss.mco, modifier = Modifier.size(FzSmart.fieldIcon)) } },
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = wiFieldColors(),
    )
}

@Composable
fun WiPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = fPoppins, fontSize = FzSmart.field) },
        leadingIcon = { Icon(Icons.Rounded.Lock, null, tint = WiCss.mco, modifier = Modifier.size(FzSmart.fieldIcon)) },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) WiIcons.VisibilityOff else WiIcons.Visibility,
                    contentDescription = null,
                    tint = WiCss.mco
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = wiFieldColors(),
    )
}

@Composable
private fun wiFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = WiCss.mco,
    unfocusedBorderColor = WiCss.brd.copy(alpha = 0.60f),
    focusedContainerColor = WiCss.inp.copy(alpha = 0.80f),
    unfocusedContainerColor = WiCss.inp.copy(alpha = 0.50f),
    focusedTextColor = WiCss.tx1,
    unfocusedTextColor = WiCss.tx1,
)
