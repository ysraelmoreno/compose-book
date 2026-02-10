package com.ysraelmorenopkg.storybook.core.control

/**
 * Base interface for prop controls.
 * 
 * Controls define how props can be edited in the Storybook UI.
 * This is a sealed interface to ensure only supported control types exist.
 */
sealed interface PropControl<T : Any> {
    val label: String
    val description: String?
}
