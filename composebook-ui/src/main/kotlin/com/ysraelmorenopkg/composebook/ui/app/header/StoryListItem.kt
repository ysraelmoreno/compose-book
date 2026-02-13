package com.ysraelmorenopkg.composebook.ui.app.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Represents an individual story item in the story list.
 * Shows the story name with visual feedback for selection.
 * 
 * @param storyName Name of the story to display
 * @param isSelected Whether this story is currently selected
 * @param onClick Callback when the story is clicked
 */
@Composable
fun StoryListItem(
    storyName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        ComposeBookTheme.colors.surfaceSelected
    } else {
        ComposeBookTheme.colors.backgroundElevated
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(start = 40.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
    ) {
        ComposeBookBodyText(
            text = storyName,
            color = if (isSelected) {
                ComposeBookTheme.colors.accent
            } else {
                ComposeBookTheme.colors.textSecondary
            }
        )
    }
}
