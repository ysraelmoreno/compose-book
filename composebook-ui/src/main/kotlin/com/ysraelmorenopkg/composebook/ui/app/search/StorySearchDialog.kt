package com.ysraelmorenopkg.composebook.ui.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookDivider
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Command-palette style search dialog for quickly finding and selecting stories.
 * Mimics the macOS Spotlight / Command+Space behavior.
 * 
 * @param stories List of all available stories to search through
 * @param onStorySelected Callback when a story is selected from search results
 * @param onDismiss Callback when the dialog should be dismissed
 */
@Composable
fun StorySearchDialog(
    stories: List<ComposeStory<*>>,
    onStorySelected: (ComposeStory<*>) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    
    // Auto-focus the search field when dialog opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    // Filter stories based on search query
    val filteredStories = remember(searchQuery, stories) {
        if (searchQuery.isEmpty()) {
            stories
        } else {
            stories.filter { story ->
                story.name.contains(searchQuery, ignoreCase = true) ||
                story.id.value.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ComposeBookTheme.colors.backgroundElevated)
        ) {
            // Search Input Field
            SearchTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                focusRequester = focusRequester,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            ComposeBookDivider()
            
            // Search Results
            if (filteredStories.isEmpty()) {
                SearchEmptyState(searchQuery)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(
                        items = filteredStories,
                        key = { story -> story.id.value }
                    ) { story ->
                        SearchResultItem(
                            story = story,
                            searchQuery = searchQuery,
                            onClick = { onStorySelected(story) }
                        )
                    }
                }
            }
            
            // Footer hint
            SearchFooterHint()
        }
    }
}
