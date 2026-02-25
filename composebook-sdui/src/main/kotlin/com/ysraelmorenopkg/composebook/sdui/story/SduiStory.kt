package com.ysraelmorenopkg.composebook.sdui.story

import androidx.compose.runtime.Composable
import com.ysraelmorenopkg.composebook.core.api.StoryContext
import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.model.StoryId
import com.ysraelmorenopkg.composebook.sdui.canvas.SduiCanvas
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory

/**
 * Creates a ComposeBook story that renders an SDUI component.
 *
 * The story integrates with ComposeBook's standard story system —
 * it appears in the sidebar, responds to theme changes, and renders
 * in the canvas. Rendering is delegated to [SduiCanvas] which dispatches
 * to the renderer registered in [sduiRegistry] for the component's class.
 *
 * Phase 1: static mode only (no controls, no payload editing).
 *
 * Example:
 * ```
 * val story = sduiStory(
 *     id = "sdui.button.primary",
 *     name = "SDUI / Button / Primary",
 *     component = ButtonComponent(text = "Click Me"),
 *     sduiRegistry = registry,
 * )
 * storyRegistry.register(story)
 * ```
 *
 * @param T The concrete SDUI component type
 * @param id Unique story identifier
 * @param name Human-readable name shown in the sidebar
 * @param component The pre-built component instance to preview
 * @param sduiRegistry Registry containing the renderer for [T]
 */
fun <T : Any> sduiStory(
    id: String,
    name: String,
    component: T,
    sduiRegistry: SduiRegistry,
): ComposeStory<SduiStoryProps<T>> {
    val storyId = StoryId(id)
    val defaultProps = SduiStoryProps(component = component, registry = sduiRegistry)

    return object : ComposeStory<SduiStoryProps<T>> {
        override val id: StoryId = storyId
        override val name: String = name
        override val defaultProps: SduiStoryProps<T> = defaultProps
        override val controls: List<PropBinding<SduiStoryProps<T>, *>> = emptyList()

        override fun render(props: SduiStoryProps<T>, context: StoryContext) {
            // No-op: rendering happens via Content()
        }

        @Composable
        override fun Content(props: SduiStoryProps<T>, context: StoryContext) {
            SduiCanvas(
                component = props.component,
                registry = props.registry,
                environment = context.environment,
            )
        }
    }
}
