package com.ysraelmorenopkg.storybook.samples.dropdown

/**
 * Dropdown options list item.
 */
data class DropdownOption(
    val id: String,
    val label: String
)

/**
 * Dropdown size variants.
 */
enum class DropdownSize {
    Small,
    Medium,
    Large
}

/**
 * Props for the Dropdown component.
 * 
 * Demonstrates a dropdown with selectable options and various configurations.
 */
data class DropdownProps(
    val label: String,
    val placeholder: String,
    val selectedOptionId: String?,
    val options: List<DropdownOption>,
    val enabled: Boolean,
    val required: Boolean,
    val size: DropdownSize,
    val helperText: String?
)
