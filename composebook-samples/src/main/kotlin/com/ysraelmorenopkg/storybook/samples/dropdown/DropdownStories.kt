package com.ysraelmorenopkg.storybook.samples.dropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.compose.adapter.composeStory
import com.ysraelmorenopkg.storybook.core.control.BooleanControl
import com.ysraelmorenopkg.storybook.core.control.EnumControl
import com.ysraelmorenopkg.storybook.core.control.TextControl

// Sample options for stories
private val defaultOptions = listOf(
    DropdownOption("opt1", "Option 1"),
    DropdownOption("opt2", "Option 2"),
    DropdownOption("opt3", "Option 3"),
    DropdownOption("opt4", "Option 4")
)

private val fruitOptions = listOf(
    DropdownOption("apple", "Apple"),
    DropdownOption("banana", "Banana"),
    DropdownOption("orange", "Orange"),
    DropdownOption("grape", "Grape"),
    DropdownOption("mango", "Mango")
)

/**
 * Story for a basic Dropdown.
 * 
 * Demonstrates standard dropdown with selectable options.
 */
val DropdownDefaultStory = composeStory(
    id = "dropdown.default",
    name = "Dropdown / Default",
    defaultProps = DropdownProps(
        label = "Select an option",
        placeholder = "Choose one...",
        selectedOptionId = null,
        options = defaultOptions,
        enabled = true,
        required = false,
        size = DropdownSize.Medium,
        helperText = null
    )
) {
    control(
        key = "label",
        control = TextControl("Label", "Label text above the dropdown"),
        getter = { it.label },
        setter = { props, value -> props.copy(label = value) }
    )
    
    control(
        key = "placeholder",
        control = TextControl("Placeholder", "Text shown when no option is selected"),
        getter = { it.placeholder },
        setter = { props, value -> props.copy(placeholder = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled", "Enable/disable interaction"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    control(
        key = "required",
        control = BooleanControl("Required", "Show required indicator"),
        getter = { it.required },
        setter = { props, value -> props.copy(required = value) }
    )
    
    control(
        key = "size",
        control = EnumControl("Size", DropdownSize.entries, "Dropdown size variant"),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    render { props, _ ->
        DropdownComponent(props)
    }
}

/**
 * Story for a Dropdown with pre-selected value.
 */
val DropdownWithSelectionStory = composeStory(
    id = "dropdown.with-selection",
    name = "Dropdown / With Selection",
    defaultProps = DropdownProps(
        label = "Favorite Fruit",
        placeholder = "Select your favorite",
        selectedOptionId = "banana",
        options = fruitOptions,
        enabled = true,
        required = true,
        size = DropdownSize.Medium,
        helperText = "This helps us personalize your experience"
    )
) {
    control(
        key = "label",
        control = TextControl("Label"),
        getter = { it.label },
        setter = { props, value -> props.copy(label = value) }
    )
    
    control(
        key = "placeholder",
        control = TextControl("Placeholder"),
        getter = { it.placeholder },
        setter = { props, value -> props.copy(placeholder = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    control(
        key = "required",
        control = BooleanControl("Required"),
        getter = { it.required },
        setter = { props, value -> props.copy(required = value) }
    )
    
    control(
        key = "size",
        control = EnumControl("Size", DropdownSize.entries),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    render { props, _ ->
        DropdownComponent(props)
    }
}

/**
 * Story for a disabled Dropdown.
 */
val DropdownDisabledStory = composeStory(
    id = "dropdown.disabled",
    name = "Dropdown / Disabled",
    defaultProps = DropdownProps(
        label = "Country",
        placeholder = "Not available",
        selectedOptionId = null,
        options = defaultOptions,
        enabled = false,
        required = false,
        size = DropdownSize.Large,
        helperText = "This field is currently unavailable"
    )
) {
    control(
        key = "label",
        control = TextControl("Label"),
        getter = { it.label },
        setter = { props, value -> props.copy(label = value) }
    )
    
    control(
        key = "placeholder",
        control = TextControl("Placeholder"),
        getter = { it.placeholder },
        setter = { props, value -> props.copy(placeholder = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    control(
        key = "required",
        control = BooleanControl("Required"),
        getter = { it.required },
        setter = { props, value -> props.copy(required = value) }
    )
    
    control(
        key = "size",
        control = EnumControl("Size", DropdownSize.entries),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    render { props, _ ->
        DropdownComponent(props)
    }
}

@Composable
private fun DropdownComponent(props: DropdownProps) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember(props.selectedOptionId) {
        mutableStateOf(props.options.find { it.id == props.selectedOptionId })
    }
    
    val height = when (props.size) {
        DropdownSize.Small -> 40.dp
        DropdownSize.Medium -> 48.dp
        DropdownSize.Large -> 56.dp
    }
    
    val textStyle = when (props.size) {
        DropdownSize.Small -> MaterialTheme.typography.bodySmall
        DropdownSize.Medium -> MaterialTheme.typography.bodyMedium
        DropdownSize.Large -> MaterialTheme.typography.bodyLarge
    }
    
    val backgroundColor = if (props.enabled) Color.White else Color(0xFFF5F5F5)
    val borderColor = if (props.enabled) Color(0xFF757575) else Color(0xFFBDBDBD)
    val textColor = if (props.enabled) Color.Black else Color(0xFF9E9E9E)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label with required indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = props.label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
            if (props.required) {
                Text(
                    text = "*",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFB00020)
                )
            }
        }
        
        // Dropdown box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(backgroundColor, RoundedCornerShape(4.dp))
                    .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(4.dp))
                    .clickable(enabled = props.enabled) { expanded = !expanded }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedOption?.label ?: props.placeholder,
                        style = textStyle,
                        color = if (selectedOption != null) textColor else Color(0xFF9E9E9E)
                    )
                    Text(
                        text = if (expanded) "▲" else "▼",
                        style = textStyle,
                        color = textColor
                    )
                }
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                props.options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                style = textStyle,
                                color = if (option.id == selectedOption?.id) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Black
                                }
                            )
                        },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
        
        // Helper text
        props.helperText?.let { helperText ->
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF757575)
            )
        }
    }
}
