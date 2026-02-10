package com.ysraelmorenopkg.storybook.core.environment

import java.util.Locale

/**
 * Environment configuration for rendering a story.
 * 
 * Defines the context in which a story is rendered, including theme, locale, and device.
 */
data class StoryEnvironment(
    val theme: ThemeMode,
    val locale: Locale,
    val device: DeviceProfile
) {
    companion object {
        val Default = StoryEnvironment(
            theme = ThemeMode.Light,
            locale = Locale.getDefault(),
            device = DeviceProfile.Default
        )
    }
}
