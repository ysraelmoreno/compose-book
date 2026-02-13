package com.ysraelmorenopkg.composebook.ui.app.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.canvas.StoryCanvas
import com.ysraelmorenopkg.composebook.core.runtime.StoryRuntimeState

/**
 * Canvas content area that renders the selected story.
 * Displays the story's UI component with current props and state.
 * 
 * @param selectedStory The currently selected story to render
 * @param runtimeState The current runtime state of the story
 */
@Composable
fun CanvasContent(
    selectedStory: ComposeStory<*>?,
    runtimeState: StoryRuntimeState<*>?
) {
    selectedStory?.let { story ->
        runtimeState?.let { state ->
            @Suppress("UNCHECKED_CAST")
            StoryCanvas(
                story = story as ComposeStory<Any>,
                state = state as StoryRuntimeState<Any>,
                modifier = Modifier.fillMaxSize()
            )
        }
    } ?: EmptyStateContent()
}
