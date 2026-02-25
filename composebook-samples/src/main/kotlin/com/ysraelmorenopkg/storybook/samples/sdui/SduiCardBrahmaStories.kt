package com.ysraelmorenopkg.storybook.samples.sdui

import android.content.res.AssetManager
import com.ysraelmorenopkg.composebook.sdui.parser.SduiPayloadParser
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.story.sduiPayloadStory
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory

/**
 * SDUI story for a Brahma Extra Lager product card.
 *
 * Loads from `assets/sdui_card_brahma_extra_lager.json`.
 * Validates the full JSON pipeline: file → parser → mapper → renderer.
 */
class SduiCardBrahmaStories(assetManager: AssetManager, sduiRegistry: SduiRegistry) {

    private val payloads = loadPayloads(assetManager)

    val Default = payloads["product-card-001"]?.let { payload ->
        sduiPayloadStory(
            id = "sdui.json.card.brahma-extra-lager",
            name = "Card / JSON (Brahma Extra Lager)",
            defaultPayload = payload,
            sduiRegistry = sduiRegistry,
        )
    }

    val all: List<ComposeStory<*>> = listOfNotNull(Default)

    private companion object {
        fun loadPayloads(assetManager: AssetManager) = runCatching {
            val json = assetManager.open("sdui_card_brahma_extra_lager.json")
                .bufferedReader().use { it.readText() }
            SduiPayloadParser.parseSection(json).associateBy { it.id }
        }.getOrDefault(emptyMap())
    }
}
