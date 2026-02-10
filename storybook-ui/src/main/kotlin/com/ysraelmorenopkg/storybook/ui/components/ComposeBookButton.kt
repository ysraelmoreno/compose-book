package com.ysraelmorenopkg.storybook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.ui.theme.ComposeBookTheme

/**
 * Custom button component for ComposeBook UI.
 * 
 * Styled to match Storybook JS button design with subtle rounded corners
 * and hover states.
 */
@Composable
fun ComposeBookButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ComposeBookTheme.colors.surface,
    contentColor: Color = ComposeBookTheme.colors.textPrimary,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Icon button variant for toolbar actions.
 */
@Composable
fun ComposeBookIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
