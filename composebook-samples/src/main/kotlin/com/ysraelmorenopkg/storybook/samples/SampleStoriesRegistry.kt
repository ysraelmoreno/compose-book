package com.ysraelmorenopkg.storybook.samples

import com.ysraelmorenopkg.storybook.core.registry.StoryRegistry
import com.ysraelmorenopkg.storybook.samples.badge.BadgeErrorStory
import com.ysraelmorenopkg.storybook.samples.badge.BadgePrimaryStory
import com.ysraelmorenopkg.storybook.samples.badge.BadgeSuccessStory
import com.ysraelmorenopkg.storybook.samples.button.ButtonDisabledStory
import com.ysraelmorenopkg.storybook.samples.button.ButtonPrimaryStory
import com.ysraelmorenopkg.storybook.samples.card.CardDefaultStory
import com.ysraelmorenopkg.storybook.samples.card.CardWithImageStory
import com.ysraelmorenopkg.storybook.samples.dropdown.DropdownDefaultStory
import com.ysraelmorenopkg.storybook.samples.dropdown.DropdownDisabledStory
import com.ysraelmorenopkg.storybook.samples.dropdown.DropdownWithSelectionStory

/**
 * Registers all sample stories with the given registry.
 * 
 * This demonstrates explicit, manual registration as required by the MVP.
 * No auto-discovery or reflection.
 */
fun registerSampleStories(registry: StoryRegistry) {
    // Badge stories (testing multiple controls)
    registry.register(BadgePrimaryStory)
    registry.register(BadgeErrorStory)
    registry.register(BadgeSuccessStory)
    
    // Button stories
    registry.register(ButtonPrimaryStory)
    registry.register(ButtonDisabledStory)
    
    // Card stories
    registry.register(CardDefaultStory)
    registry.register(CardWithImageStory)
    
    // Dropdown stories
    registry.register(DropdownDefaultStory)
    registry.register(DropdownWithSelectionStory)
    registry.register(DropdownDisabledStory)
}
