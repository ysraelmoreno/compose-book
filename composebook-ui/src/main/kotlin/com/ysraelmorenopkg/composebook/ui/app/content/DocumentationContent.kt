package com.ysraelmorenopkg.composebook.ui.app.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.docs.DocumentationView

/**
 * Documentation content area that displays story documentation.
 * Shows markdown-formatted documentation for the selected story.
 * 
 * @param selectedStory The currently selected story to show documentation for
 */
@Composable
fun DocumentationContent(
    selectedStory: ComposeStory<*>?
) {
    selectedStory?.let { story ->
        DocumentationView(
            documentation = story.documentation,
            modifier = Modifier.fillMaxSize()
        )
    } ?: EmptyStateContent()
}
