package com.ysraelmorenopkg.storybook.samples.sdui.mapper

import com.ysraelmorenopkg.composebook.sdui.api.SduiComponentMapper
import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiButtonComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiCardComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiSpacerComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiTextComponent

/**
 * Maps a "text" payload into [SduiTextComponent].
 *
 * Expected parameters:
 * - "value" (String): the text content
 * - "color" (String, optional): text color
 */
val TextMapper = SduiComponentMapper<SduiTextComponent> { payload, _ ->
    SduiTextComponent(
        text = payload.stringParam("value"),
        color = payload.stringParam("color"),
        id = payload.id.ifEmpty { "text" },
    )
}

/**
 * Maps a "button" payload into [SduiButtonComponent].
 *
 * Expected parameters:
 * - "value" (String): button label text
 * - "enabled" (Boolean, optional): defaults to true
 * - "style" (String, optional): "primary", "secondary", or "destructive"
 */
val ButtonMapper = SduiComponentMapper<SduiButtonComponent> { payload, _ ->
    val styleString = payload.stringParam("style", "primary")
    val style = when (styleString.lowercase()) {
        "secondary" -> SduiButtonComponent.ButtonStyle.SECONDARY
        "destructive" -> SduiButtonComponent.ButtonStyle.DESTRUCTIVE
        else -> SduiButtonComponent.ButtonStyle.PRIMARY
    }

    SduiButtonComponent(
        label = SduiTextComponent(text = payload.stringParam("value", "Button")),
        enabled = payload.boolParam("enabled", true),
        style = style,
        id = payload.id.ifEmpty { "button" },
    )
}

/**
 * Maps a "spacer" payload into [SduiSpacerComponent].
 *
 * Expected parameters:
 * - "height" (Number, optional): height in dp, defaults to 8
 */
val SpacerMapper = SduiComponentMapper<SduiSpacerComponent> { payload, _ ->
    val height = (payload.param<Number>("height"))?.toFloat() ?: 8f
    SduiSpacerComponent(
        heightDp = height,
        id = payload.id.ifEmpty { "spacer" },
    )
}

/**
 * Maps a "card" payload into [SduiCardComponent].
 *
 * Expected parameters:
 * - "title" (String): card title
 * - "subtitle" (String, optional): card subtitle
 *
 * Children are recursively mapped via [mapChildren].
 */
val CardMapper = SduiComponentMapper<SduiCardComponent> { payload, mapChildren ->
    val mappedChildren = mapChildren(payload.children)

    SduiCardComponent(
        title = SduiTextComponent(text = payload.stringParam("title", "Card")),
        subtitle = payload.param<String>("subtitle")?.let { SduiTextComponent(text = it) },
        children = mappedChildren,
        id = payload.id.ifEmpty { "card" },
    )
}
