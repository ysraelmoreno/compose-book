package com.ysraelmorenopkg.storybook.samples.sdui.components

/**
 * Sample SDUI components simulating a BFF-driven component hierarchy.
 *
 * In a real app (e.g., browse-android), these would be the sealed class
 * subtypes of ComponentProps coming from the BFF layer. Here they're
 * standalone data classes to demonstrate the pattern without external deps.
 */

data class SduiTextComponent(
    val text: String,
    val color: String = "",
    val id: String = "text",
)

data class SduiButtonComponent(
    val label: SduiTextComponent,
    val enabled: Boolean = true,
    val style: ButtonStyle = ButtonStyle.PRIMARY,
    val id: String = "button",
) {
    enum class ButtonStyle { PRIMARY, SECONDARY, DESTRUCTIVE }
}

data class SduiSpacerComponent(
    val heightDp: Float = 8f,
    val id: String = "spacer",
)

data class SduiCardComponent(
    val title: SduiTextComponent,
    val subtitle: SduiTextComponent? = null,
    val children: List<Any> = emptyList(),
    val id: String = "card",
)
