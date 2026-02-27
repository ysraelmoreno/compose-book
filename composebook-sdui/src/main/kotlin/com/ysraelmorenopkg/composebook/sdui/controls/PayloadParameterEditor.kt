package com.ysraelmorenopkg.composebook.sdui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Renders editable fields for each parameter in the payload's parameter map.
 *
 * Dispatches by value type:
 * - Boolean → Switch (matching ControlsPanel style)
 * - String / Number / other → BasicTextField (matching ControlsPanel style)
 */
@Composable
internal fun PayloadParameterEditor(
    parameters: Map<String, Any?>,
    onParameterChange: (key: String, value: Any?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        parameters.forEach { (key, value) ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                ComposeBookLabel(text = key.uppercase())

                when (value) {
                    is Boolean -> BooleanParameterField(
                        key = key,
                        value = value,
                        onValueChange = { onParameterChange(key, it) },
                    )

                    is Number -> NumberParameterField(
                        value = value,
                        onValueChange = { onParameterChange(key, it) },
                    )

                    else -> TextParameterField(
                        value = value?.toString() ?: "",
                        onValueChange = { newText ->
                            onParameterChange(key, coerceType(value, newText))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TextParameterField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(12.dp),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = ComposeBookTheme.colors.textPrimary,
                fontSize = ComposeBookTheme.typography.bodyMedium.fontSize,
            ),
            cursorBrush = SolidColor(ComposeBookTheme.colors.accent),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun NumberParameterField(
    value: Number,
    onValueChange: (Number) -> Unit,
) {
    val allowDecimal = value is Float || value is Double
    var text by remember(value) { mutableStateOf(value.toString()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(12.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = { raw ->
                val filtered = raw.filter { c ->
                    c.isDigit() || c == '-' || (allowDecimal && c == '.')
                }
                text = filtered
                val parsed = parseNumber(value, filtered) ?: return@BasicTextField
                onValueChange(parsed)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (allowDecimal) KeyboardType.Decimal else KeyboardType.Number,
            ),
            textStyle = TextStyle(
                color = ComposeBookTheme.colors.textPrimary,
                fontSize = ComposeBookTheme.typography.bodyMedium.fontSize,
            ),
            cursorBrush = SolidColor(ComposeBookTheme.colors.accent),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun BooleanParameterField(
    key: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ComposeBookBodyText(
            text = key,
            color = ComposeBookTheme.colors.textSecondary,
        )
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ComposeBookTheme.colors.accent,
                checkedTrackColor = ComposeBookTheme.colors.accent.copy(alpha = 0.5f),
                uncheckedThumbColor = ComposeBookTheme.colors.textTertiary,
                uncheckedTrackColor = ComposeBookTheme.colors.surface,
            ),
        )
    }
}

/**
 * Parses [text] into the same [Number] subtype as [reference].
 * Returns null when [text] is empty or not a valid number,
 * so callers can skip the update and keep the local buffer intact.
 */
private fun parseNumber(reference: Number, text: String): Number? = when (reference) {
    is Int -> text.toIntOrNull()
    is Long -> text.toLongOrNull()
    is Double -> text.toDoubleOrNull()
    is Float -> text.toFloatOrNull()
    else -> text.toDoubleOrNull()
}

/**
 * Attempts to preserve the original type when editing a text field.
 * Only used by the generic [TextParameterField] fallback.
 */
private fun coerceType(originalValue: Any?, newText: String): Any? = newText
