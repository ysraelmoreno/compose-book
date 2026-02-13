package com.ysraelmorenopkg.composebook.ui.app.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Empty state displayed when search returns no results.
 * 
 * @param searchQuery The current search query that returned no results
 */
@Composable
fun SearchEmptyState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComposeBookBodyText(
                text = "No stories found",
                color = ComposeBookTheme.colors.textSecondary
            )
            
            if (searchQuery.isNotEmpty()) {
                ComposeBookLabel(
                    text = "Try a different search term",
                    color = ComposeBookTheme.colors.textTertiary
                )
            }
        }
    }
}
