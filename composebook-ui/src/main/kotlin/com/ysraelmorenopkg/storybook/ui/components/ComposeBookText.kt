package com.ysraelmorenopkg.storybook.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.ysraelmorenopkg.storybook.ui.theme.ComposeBookTheme

/**
 * Text components using ComposeBook typography system.
 */

@Composable
fun ComposeBookTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ComposeBookTheme.colors.textPrimary
) {
    Text(
        text = text,
        style = ComposeBookTheme.typography.titleMedium,
        color = color,
        modifier = modifier
    )
}

@Composable
fun ComposeBookBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ComposeBookTheme.colors.textSecondary,
    style: TextStyle = ComposeBookTheme.typography.bodyMedium
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}

@Composable
fun ComposeBookLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ComposeBookTheme.colors.textTertiary
) {
    Text(
        text = text,
        style = ComposeBookTheme.typography.labelMedium,
        color = color,
        modifier = modifier
    )
}
