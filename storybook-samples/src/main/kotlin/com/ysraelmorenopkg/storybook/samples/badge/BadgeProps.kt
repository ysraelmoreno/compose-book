package com.ysraelmorenopkg.storybook.samples.badge

/**
 * Badge color variants.
 */
enum class BadgeVariant {
    Primary,
    Secondary,
    Success,
    Error,
    Warning,
    Info
}

/**
 * Badge size options.
 */
enum class BadgeSize {
    Small,
    Medium,
    Large
}

/**
 * Props for the Badge component.
 * 
 * Demonstrates multiple controls for testing the UI.
 */
data class BadgeProps(
    val text: String,
    val variant: BadgeVariant,
    val size: BadgeSize,
    val outlined: Boolean,
    val dismissible: Boolean,
    val showIcon: Boolean
)
