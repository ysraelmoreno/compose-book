package com.ysraelmorenopkg.composebook.sdui.story

import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry

/**
 * Props for an SDUI story.
 *
 * Wraps the SDUI component and its registry so that the story's
 * [Content][com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory.Content]
 * can delegate rendering to [com.ysraelmorenopkg.composebook.sdui.canvas.SduiCanvas].
 *
 * @param T The concrete component type (e.g., ButtonComponent, TextComponent)
 * @property component The SDUI component instance to render
 * @property registry The registry containing renderers for dispatch
 */
data class SduiStoryProps<T : Any>(
    val component: T,
    val registry: SduiRegistry,
)
