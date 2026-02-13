package com.ysraelmorenopkg.composebook.ui.app.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.BookIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Empty state content displayed when no story is selected or available.
 * Shows a centered message with icon and instructions.
 */
@Composable
fun EmptyStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookIcon(size = 48.dp, color = ComposeBookTheme.colors.textTertiary)
            
            ComposeBookTitle(
                text = "No Stories Found",
                color = ComposeBookTheme.colors.textPrimary
            )
            
            ComposeBookBodyText(
                text = "Register ComposeStory instances using StoryRegistry",
                color = ComposeBookTheme.colors.textTertiary
            )
        }
    }
}
