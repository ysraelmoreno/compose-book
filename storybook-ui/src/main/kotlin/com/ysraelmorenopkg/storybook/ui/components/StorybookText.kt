package com.ysraelmorenopkg.storybook.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.ysraelmorenopkg.storybook.ui.theme.StorybookTheme

/**
 * Text components using Storybook typography system.
 */

@Composable
fun StorybookTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = StorybookTheme.colors.textPrimary
) {
    Text(
        text = text,
        style = StorybookTheme.typography.titleMedium,
        color = color,
        modifier = modifier
    )
}

@Composable
fun StorybookBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = StorybookTheme.colors.textSecondary,
    style: TextStyle = StorybookTheme.typography.bodyMedium
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}

@Composable
fun StorybookLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = StorybookTheme.colors.textTertiary
) {
    Text(
        text = text,
        style = StorybookTheme.typography.labelMedium,
        color = color,
        modifier = modifier
    )
}
