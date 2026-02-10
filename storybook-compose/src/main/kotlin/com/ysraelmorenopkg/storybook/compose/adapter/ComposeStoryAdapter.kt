package com.ysraelmorenopkg.storybook.compose.adapter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import com.ysraelmorenopkg.storybook.core.api.Story
import com.ysraelmorenopkg.storybook.core.api.StoryContext

/**
 * Marker interface for Compose-compatible stories.
 * 
 * Stories that can be rendered with Compose implement this interface,
 * providing access to the Composable content.
 */
interface ComposeStory<Props : Any> : Story<Props> {
    /**
     * The Composable content for this story.
     */
    @Composable
    fun Content(props: Props, context: StoryContext)
}

/**
 * Creates a render function that works with ComposeStory.
 * 
 * The render function is a no-op in the core layer.
 * The actual rendering happens when StoryCanvas calls Content().
 * 
 * Example:
 * ```
 * val buttonStory = story(...) {
 *     render(composeRender { props ->
 *         Button(text = props.text)
 *     })
 * }
 * ```
 */
fun <Props : Any> composeRender(
    content: @Composable (Props, StoryContext) -> Unit
): ComposeRenderData<Props> {
    return ComposeRenderData(content)
}

/**
 * Holds the Composable content for a story.
 * Internal - used by the story builder.
 */
class ComposeRenderData<Props : Any>(
    val content: @Composable (Props, StoryContext) -> Unit
) {
    /**
     * Creates a no-op render function for the core Story interface.
     * The actual rendering is handled by accessing the ComposeStory.Content() function.
     */
    fun createCoreRender(): (Props, StoryContext) -> Unit {
        return { _, _ ->
            // No-op: Compose rendering happens through ComposeStory.Content()
        }
    }
    
    /**
     * Creates a Composable function that renders with proper context.
     */
    @Composable
    fun RenderWithContext(props: Props, context: StoryContext) {
        CompositionLocalProvider(
            LocalInspectionMode provides context.inspectionMode
        ) {
            content(props, context)
        }
    }
}
