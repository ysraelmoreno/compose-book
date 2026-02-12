package com.ysraelmorenopkg.composebook.core.control

/**
 * Control for editing enum properties.
 * 
 * Renders as a dropdown in the UI.
 * 
 * @param T The enum type
 * @property values All possible values for the enum
 */
data class EnumControl<T : Enum<T>>(
    override val label: String,
    val values: List<T>,
    override val description: String? = null
) : PropControl<T> {
    init {
        require(values.isNotEmpty()) { "EnumControl must have at least one value" }
    }
}
