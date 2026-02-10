package com.ysraelmorenopkg.storybook.core.control

/**
 * Control for editing text properties.
 * 
 * Renders as a text input in the UI.
 */
data class TextControl(
    override val label: String,
    override val description: String? = null
) : PropControl<String>
