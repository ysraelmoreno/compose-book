package com.ysraelmorenopkg.storybook.samples.button

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.ysraelmorenopkg.composebook.ui.story
import com.ysraelmorenopkg.composebook.core.control.BooleanControl
import com.ysraelmorenopkg.composebook.core.control.TextControl

// Example Props for demonstration
data class DocumentedButtonProps(
    val text: String,
    val enabled: Boolean
)

/**
 * Example story demonstrating the documentation feature.
 * 
 * This story showcases how to add comprehensive documentation
 * to your components using the documentation DSL.
 */
val DocumentedButtonStory = story(
    id = "button.documented",
    name = "Button / With Documentation",
    defaultProps = DocumentedButtonProps(
        text = "Save Changes",
        enabled = true
    )
) {
    // Documentation tab content
    documentation {
        description("""
            The primary button component is used for the most important action on a screen.
            
            It should be used sparingly - typically only one primary button should be visible
            at a time to avoid decision paralysis for users.
            
            Primary buttons use high contrast colors to draw attention and signal importance.
        """.trimIndent())
        
        usage("""
            // Basic usage
            Button(
                text = "Save Changes",
                enabled = true
            )
            
            // Disabled state
            Button(
                text = "Processing...",
                enabled = false
            )
            
            // With icon
            Button(
                text = "Upload",
                enabled = true,
                icon = Icons.Upload
            )
        """.trimIndent())
        
        props("""
            • text: String (required)
              The label text displayed on the button
              
            • enabled: Boolean (default: true)
              Controls whether the button is interactive
              When false, the button appears grayed out and clicks are ignored
              
            • icon: ImageVector? (optional)
              Optional icon to display before the text
              
            • onClick: () -> Unit (required)
              Callback invoked when the button is clicked
        """.trimIndent())
        
        notes("""
            ⚠️ IMPORTANT: Always provide meaningful button text
            - Avoid generic labels like "Click here" or "Submit"
            - Use action-oriented text like "Save changes" or "Create account"
            
            ✅ Accessibility
            - Button includes proper touch target size (min 48dp)
            - Disabled state is announced to screen readers
            - Text contrast meets WCAG AA standards
            
            💡 Best Practices
            - Limit to one primary button per screen
            - Place primary buttons at the end of forms
            - Consider loading states for async operations
        """.trimIndent())
    }
    
    // Canvas tab controls
    control(
        key = "text",
        control = TextControl(
            label = "Text",
            description = "Button label text"
        ),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl(
            label = "Enabled",
            description = "Whether the button is interactive"
        ),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    // Canvas tab rendering
    render { props, _ ->
        Button(
            onClick = { /* No action in storybook */ },
            enabled = props.enabled
        ) {
            Text(props.text)
        }
    }
}
