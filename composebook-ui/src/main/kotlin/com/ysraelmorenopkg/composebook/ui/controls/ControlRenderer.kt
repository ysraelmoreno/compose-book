package com.ysraelmorenopkg.composebook.ui.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.control.BooleanControl
import com.ysraelmorenopkg.composebook.core.control.EnumControl
import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.control.TextControl

/**
 * Renders a single control for editing a prop value.
 * 
 * Maps core control types to Compose UI components:
 * - TextControl -> TextField
 * - BooleanControl -> Switch
 * - EnumControl -> Dropdown
 * 
 * @param binding The prop binding containing the control and getter/setter
 * @param currentProps The current props instance
 * @param onPropsChange Callback when props are updated
 */
@Composable
fun <Props : Any, T : Any> ControlRenderer(
    binding: PropBinding<Props, T>,
    currentProps: Props,
    onPropsChange: (Props) -> Unit
) {
    val currentValue = binding.getValue(currentProps)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        when (val control = binding.control) {
            is TextControl -> {
                TextControlRenderer(
                    label = control.label,
                    description = control.description,
                    value = currentValue as String,
                    onValueChange = { newValue ->
                        @Suppress("UNCHECKED_CAST")
                        val updatedProps = binding.updateValue(currentProps, newValue as T)
                        onPropsChange(updatedProps)
                    }
                )
            }
            
            is BooleanControl -> {
                BooleanControlRenderer(
                    label = control.label,
                    description = control.description,
                    value = currentValue as Boolean,
                    onValueChange = { newValue ->
                        @Suppress("UNCHECKED_CAST")
                        val updatedProps = binding.updateValue(currentProps, newValue as T)
                        onPropsChange(updatedProps)
                    }
                )
            }
            
            is EnumControl<*> -> {
                @Suppress("UNCHECKED_CAST")
                EnumControlRenderer(
                    label = control.label,
                    description = control.description,
                    values = control.values as List<T>,
                    currentValue = currentValue,
                    displayName = { it.toString() },
                    onValueChange = { newValue ->
                        val updatedProps = binding.updateValue(currentProps, newValue)
                        onPropsChange(updatedProps)
                    }
                )
            }
            
            else -> {
                Text(
                    text = "Unsupported control: ${control::class.simpleName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun TextControlRenderer(
    label: String,
    description: String?,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Composable
private fun BooleanControlRenderer(
    label: String,
    description: String?,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = value,
                onCheckedChange = onValueChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> EnumControlRenderer(
    label: String,
    description: String?,
    values: List<T>,
    currentValue: T,
    displayName: (T) -> String = { it.toString() },
    onValueChange: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = displayName(currentValue),
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        text = { Text(displayName(value)) },
                        onClick = {
                            onValueChange(value)
                            expanded = false
                        }
                    )
                }
            }
        }
        
        description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}
