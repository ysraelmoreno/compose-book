package com.ysraelmorenopkg.storybook.samples.button

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ysraelmorenopkg.composebook.ui.adapter.composeStory
import com.ysraelmorenopkg.composebook.core.control.BooleanControl
import com.ysraelmorenopkg.composebook.core.control.TextControl

/**
 * Story for the primary button variant.
 * 
 * Demonstrates the simplest case: a button with text and enabled state.
 */
val ButtonPrimaryStory = composeStory(
    id = "button.primary",
    name = "Button / Primary",
    defaultProps = ButtonProps(
        text = "Click Me",
        enabled = true
    )
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    render { props, _ ->
        Button(
            onClick = { /* No action in storybook */ },
            enabled = props.enabled
        ) {
            Text(props.text)
        }
    }
}

/**
 * Story for a disabled button.
 * 
 * Shows the button in a disabled state by default.
 */
val ButtonDisabledStory = composeStory(
    id = "button.disabled",
    name = "Button / Disabled",
    defaultProps = ButtonProps(
        text = "Disabled Button",
        enabled = false
    )
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    render { props, _ ->
        Button(
            onClick = { /* No action in storybook */ },
            enabled = props.enabled
        ) {
            Text(props.text)
        }
    }
}
