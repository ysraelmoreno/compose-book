package com.ysraelmorenopkg.storybook.samples.sdui

import android.content.res.AssetManager
import com.ysraelmorenopkg.composebook.core.registry.StoryRegistry
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry

/**
 * Registers all JSON-driven SDUI sample stories.
 *
 * Each card has its own JSON file and story class:
 * - `sdui_card_brahma_extra_lager.json` → [SduiCardBrahmaStories]
 * - `sdui_card_promocao_relampago.json` → [SduiCardPromocaoStories]
 *
 * Validates the full pipeline:
 * ```
 * JSON file (assets/) → SduiPayloadParser → SduiPayload → Mapper → Component → Renderer
 * ```
 */
fun registerSduiJsonSampleStories(
    storyRegistry: StoryRegistry,
    assetManager: AssetManager,
    sduiRegistry: SduiRegistry = createSampleSduiRegistry(),
) {
    val brahmaStories = SduiCardBrahmaStories(assetManager, sduiRegistry)
    val promocaoStories = SduiCardPromocaoStories(assetManager, sduiRegistry)

    val allStories = brahmaStories.all + promocaoStories.all

    allStories.forEach { story ->
        storyRegistry.register(story)
    }
}
