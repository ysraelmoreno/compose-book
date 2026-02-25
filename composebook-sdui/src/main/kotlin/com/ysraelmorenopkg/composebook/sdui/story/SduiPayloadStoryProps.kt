package com.ysraelmorenopkg.composebook.sdui.story

import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry

/**
 * Props for a payload-driven SDUI story.
 *
 * Unlike [SduiStoryProps] which holds a pre-built component,
 * this holds a raw [SduiPayload] that must be mapped at render time.
 * The mapping pipeline (payload → mapper → component → renderer) runs
 * inside the story's Content composable.
 *
 * @property payload The raw BFF payload to map and render
 * @property registry The registry containing mappers and renderers
 */
data class SduiPayloadStoryProps(
    val payload: SduiPayload,
    val registry: SduiRegistry,
)
