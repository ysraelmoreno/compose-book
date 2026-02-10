package com.ysraelmorenopkg.storybook.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Composition locals for Storybook theme.
 */
val LocalStorybookColors = staticCompositionLocalOf { StorybookColors.Dark }
val LocalStorybookTypography = staticCompositionLocalOf { StorybookTypography.Default }

/**
 * Custom theme for Storybook UI matching Storybook JS design.
 * 
 * This replaces MaterialTheme with a custom design system optimized
 * for developer tools and component showcases.
 * 
 * @param darkTheme Whether to use dark theme (default: true)
 * @param content The content to wrap with theme
 */
@Composable
fun StorybookTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) StorybookColors.Dark else StorybookColors.Light
    val typography = StorybookTypography.Default
    
    CompositionLocalProvider(
        LocalStorybookColors provides colors,
        LocalStorybookTypography provides typography,
        content = content
    )
}

/**
 * Access to current theme colors.
 */
object StorybookTheme {
    val colors: StorybookColors
        @Composable
        get() = LocalStorybookColors.current
    
    val typography: StorybookTypography
        @Composable
        get() = LocalStorybookTypography.current
}
