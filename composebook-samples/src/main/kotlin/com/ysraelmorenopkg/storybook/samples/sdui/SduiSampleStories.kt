package com.ysraelmorenopkg.storybook.samples.sdui

import com.ysraelmorenopkg.composebook.core.registry.StoryRegistry
import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.story.sduiPayloadStory
import com.ysraelmorenopkg.composebook.sdui.story.sduiStory
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiButtonComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiCardComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiSpacerComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiTextComponent

/**
 * Registers sample SDUI stories with the given [StoryRegistry].
 *
 * Demonstrates both static stories (Phase 1) and payload-driven stories (Phase 2).
 */
fun registerSduiSampleStories(
    storyRegistry: StoryRegistry,
    sduiRegistry: SduiRegistry = createSampleSduiRegistry(),
) {
    registerStaticStories(storyRegistry, sduiRegistry)
    registerPayloadStories(storyRegistry, sduiRegistry)
}

/**
 * Phase 1 — Static SDUI stories with pre-built components.
 */
private fun registerStaticStories(
    storyRegistry: StoryRegistry,
    sduiRegistry: SduiRegistry,
) {
    storyRegistry.register(
        sduiStory(
            id = "sdui.text.default",
            name = "SDUI / Text / Default",
            component = SduiTextComponent(text = "Hello from SDUI"),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiStory(
            id = "sdui.button.primary",
            name = "SDUI / Button / Primary",
            component = SduiButtonComponent(
                label = SduiTextComponent(text = "Add to Cart"),
                enabled = true,
                style = SduiButtonComponent.ButtonStyle.PRIMARY,
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiStory(
            id = "sdui.button.secondary",
            name = "SDUI / Button / Secondary",
            component = SduiButtonComponent(
                label = SduiTextComponent(text = "View Details"),
                enabled = true,
                style = SduiButtonComponent.ButtonStyle.SECONDARY,
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiStory(
            id = "sdui.button.disabled",
            name = "SDUI / Button / Disabled",
            component = SduiButtonComponent(
                label = SduiTextComponent(text = "Out of Stock"),
                enabled = false,
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiStory(
            id = "sdui.card.with-children",
            name = "SDUI / Card / With Children",
            component = SduiCardComponent(
                title = SduiTextComponent(text = "Product Section"),
                subtitle = SduiTextComponent(text = "BFF-driven card with nested components"),
                children = listOf(
                    SduiSpacerComponent(heightDp = 8f),
                    SduiButtonComponent(
                        label = SduiTextComponent(text = "Primary Action"),
                        style = SduiButtonComponent.ButtonStyle.PRIMARY,
                    ),
                    SduiSpacerComponent(heightDp = 4f),
                    SduiButtonComponent(
                        label = SduiTextComponent(text = "Secondary Action"),
                        style = SduiButtonComponent.ButtonStyle.SECONDARY,
                    ),
                ),
            ),
            sduiRegistry = sduiRegistry,
        )
    )
}

/**
 * Phase 2 — Payload-driven SDUI stories.
 *
 * These simulate what happens when a BFF response arrives as raw data
 * and must flow through the mapping pipeline before rendering.
 */
private fun registerPayloadStories(
    storyRegistry: StoryRegistry,
    sduiRegistry: SduiRegistry,
) {
    storyRegistry.register(
        sduiPayloadStory(
            id = "sdui.payload.text",
            name = "SDUI / Payload / Text",
            defaultPayload = SduiPayload(
                type = "text",
                parameters = mapOf("value" to "Mapped from payload"),
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiPayloadStory(
            id = "sdui.payload.button",
            name = "SDUI / Payload / Button",
            defaultPayload = SduiPayload(
                type = "button",
                parameters = mapOf(
                    "value" to "Add to Cart",
                    "enabled" to true,
                    "style" to "primary",
                ),
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiPayloadStory(
            id = "sdui.payload.button.destructive",
            name = "SDUI / Payload / Button Destructive",
            defaultPayload = SduiPayload(
                type = "button",
                parameters = mapOf(
                    "value" to "Delete Item",
                    "enabled" to true,
                    "style" to "destructive",
                ),
            ),
            sduiRegistry = sduiRegistry,
        )
    )

    storyRegistry.register(
        sduiPayloadStory(
            id = "sdui.payload.card-with-children",
            name = "SDUI / Payload / Card Tree",
            defaultPayload = SduiPayload(
                type = "card",
                parameters = mapOf(
                    "title" to "BFF Product Card",
                    "subtitle" to "Full pipeline: payload → mapper → renderer",
                ),
                children = listOf(
                    SduiPayload(
                        type = "spacer",
                        parameters = mapOf("height" to 8),
                    ),
                    SduiPayload(
                        type = "button",
                        parameters = mapOf(
                            "value" to "Buy Now",
                            "style" to "primary",
                        ),
                    ),
                    SduiPayload(
                        type = "spacer",
                        parameters = mapOf("height" to 4),
                    ),
                    SduiPayload(
                        type = "button",
                        parameters = mapOf(
                            "value" to "More Info",
                            "style" to "secondary",
                        ),
                    ),
                ),
            ),
            sduiRegistry = sduiRegistry,
        )
    )
}
