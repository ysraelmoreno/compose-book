package com.ysraelmorenopkg.storybook.compose.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.compose.adapter.ComposeStory
import com.ysraelmorenopkg.storybook.compose.adapter.DefaultStoryContext
import com.ysraelmorenopkg.storybook.core.environment.ThemeMode
import com.ysraelmorenopkg.storybook.core.runtime.StoryRuntimeState

/**
 * Canvas for rendering a Compose story with its current state.
 * 
 * Applies the environment configuration (theme) and renders the story
 * using the ComposeStory.Content() function.
 * 
 * @param story The ComposeStory to render
 * @param state The current runtime state (props + environment)
 */
@Composable
fun <Props : Any> StoryCanvas(
    story: ComposeStory<Props>,
    state: StoryRuntimeState<Props>,
    modifier: Modifier = Modifier
) {
    val colorScheme = when (state.environment.theme) {
        ThemeMode.Light -> lightColorScheme()
        ThemeMode.Dark -> darkColorScheme()
    }
    
    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                // Create context with current environment
                val context = DefaultStoryContext(
                    environment = state.environment,
                    inspectionMode = true
                )
                
                // Render the Composable content directly
                // The ComposeStory.Content() already handles the composition
                story.Content(props = state.props, context = context)
            }
        }
    }
}
