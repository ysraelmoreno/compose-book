package com.ysraelmorenopkg.storybook.samples.sdui

import com.ysraelmorenopkg.composebook.sdui.registry.InMemorySduiRegistry
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.registry.registerMapper
import com.ysraelmorenopkg.composebook.sdui.registry.registerRenderer
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiButtonComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiCardComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiSpacerComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiTextComponent
import com.ysraelmorenopkg.storybook.samples.sdui.mapper.ButtonMapper
import com.ysraelmorenopkg.storybook.samples.sdui.mapper.CardMapper
import com.ysraelmorenopkg.storybook.samples.sdui.mapper.SpacerMapper
import com.ysraelmorenopkg.storybook.samples.sdui.mapper.TextMapper
import com.ysraelmorenopkg.storybook.samples.sdui.render.SduiButtonRender
import com.ysraelmorenopkg.storybook.samples.sdui.render.SduiCardRender
import com.ysraelmorenopkg.storybook.samples.sdui.render.SduiSpacerRender
import com.ysraelmorenopkg.storybook.samples.sdui.render.SduiTextRender

/**
 * Creates and configures the SDUI registry with sample renderers and mappers.
 *
 * In a real app, this would register the existing renderers from
 * the app's render map (e.g., MainRenderMap entries) and mappers
 * from the component mapper DI module.
 */
fun createSampleSduiRegistry(): SduiRegistry {
    val registry = InMemorySduiRegistry()

    // Renderers: ComponentClass → @Composable
    registry.registerRenderer<SduiTextComponent> { component, modifier, _ ->
        SduiTextRender(component, modifier)
    }

    registry.registerRenderer<SduiButtonComponent> { component, modifier, _ ->
        SduiButtonRender(component, modifier)
    }

    registry.registerRenderer<SduiSpacerComponent> { component, _, _ ->
        SduiSpacerRender(component)
    }

    registry.registerRenderer<SduiCardComponent> { component, modifier, renderChild ->
        SduiCardRender(component, modifier, renderChild)
    }

    // Mappers: type string → ComponentClass
    registry.registerMapper<SduiTextComponent>("text", TextMapper)
    registry.registerMapper<SduiButtonComponent>("button", ButtonMapper)
    registry.registerMapper<SduiSpacerComponent>("spacer", SpacerMapper)
    registry.registerMapper<SduiCardComponent>("card", CardMapper)

    return registry
}
