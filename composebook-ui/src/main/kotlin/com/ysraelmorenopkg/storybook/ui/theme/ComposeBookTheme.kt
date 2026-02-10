package com.ysraelmorenopkg.storybook.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Composition locals for ComposeBook theme.
 */
val LocalComposeBookColors = staticCompositionLocalOf { ComposeBookColors.Dark }
val LocalComposeBookTypography = staticCompositionLocalOf { ComposeBookTypography.Default }

/**
 * Custom theme for ComposeBook UI matching Storybook JS design.
 * 
 * This replaces MaterialTheme with a custom design system optimized
 * for developer tools and component showcases.
 * 
 * @param darkTheme Whether to use dark theme (default: true)
 * @param content The content to wrap with theme
 */
@Composable
fun ComposeBookTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) ComposeBookColors.Dark else ComposeBookColors.Light
    val typography = ComposeBookTypography.Default
    
    CompositionLocalProvider(
        LocalComposeBookColors provides colors,
        LocalComposeBookTypography provides typography,
        content = content
    )
}

/**
 * Access to current theme colors.
 */
object ComposeBookTheme {
    val colors: ComposeBookColors
        @Composable
        get() = LocalComposeBookColors.current
    
    val typography: ComposeBookTypography
        @Composable
        get() = LocalComposeBookTypography.current
}
