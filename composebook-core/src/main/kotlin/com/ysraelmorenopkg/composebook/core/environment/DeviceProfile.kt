package com.ysraelmorenopkg.composebook.core.environment

import java.util.Locale

/**
 * Device profile information for rendering stories.
 * 
 * Allows stories to adapt to different screen sizes and densities.
 */
data class DeviceProfile(
    val name: String,
    val widthDp: Int,
    val heightDp: Int,
    val density: Float
) {
    companion object {
        val Default = DeviceProfile(
            name = "Phone",
            widthDp = 360,
            heightDp = 800,
            density = 2.0f
        )
    }
}
