package com.ysraelmorenopkg.composebook.sdui.model

/**
 * Identifies an SDUI component type by its string name.
 *
 * Maps to the `type` field in BFF responses (e.g., "button", "richText", "spacer").
 * Used as a key for mapper and renderer resolution.
 */
@JvmInline
value class SduiComponentType(val value: String)
