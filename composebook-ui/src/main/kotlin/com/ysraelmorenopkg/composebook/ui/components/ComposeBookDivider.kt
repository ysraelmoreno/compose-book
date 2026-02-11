package com.ysraelmorenopkg.composebook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Custom divider matching Storybook JS subtle borders.
 */
@Composable
fun ComposeBookDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(ComposeBookTheme.colors.border)
    )
}
