package com.ysraelmorenopkg.composebook.core.control

/**
 * Control for editing boolean properties.
 * 
 * Renders as a switch/checkbox in the UI.
 */
data class BooleanControl(
    override val label: String,
    override val description: String? = null
) : PropControl<Boolean>
