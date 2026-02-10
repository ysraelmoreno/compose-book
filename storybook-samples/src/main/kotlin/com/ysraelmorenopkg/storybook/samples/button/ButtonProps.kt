package com.ysraelmorenopkg.storybook.samples.button

/**
 * Props for the Button component.
 * 
 * Immutable data class as required by the architecture.
 */
data class ButtonProps(
    val text: String,
    val enabled: Boolean
)
