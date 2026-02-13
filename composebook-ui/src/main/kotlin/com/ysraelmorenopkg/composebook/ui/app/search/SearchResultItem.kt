package com.ysraelmorenopkg.composebook.ui.app.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Individual search result item displaying story information.
 * 
 * @param story The story to display
 * @param searchQuery Current search query (unused, available for highlighting)
 * @param onClick Callback when the result is clicked
 */
@Composable
fun SearchResultItem(
    story: ComposeStory<*>,
    searchQuery: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Story name (primary text)
        ComposeBookBodyText(
            text = story.name,
            color = ComposeBookTheme.colors.textPrimary
        )
        
        // Story ID (secondary text)
        ComposeBookLabel(
            text = story.id.value,
            color = ComposeBookTheme.colors.textTertiary
        )
    }
}
