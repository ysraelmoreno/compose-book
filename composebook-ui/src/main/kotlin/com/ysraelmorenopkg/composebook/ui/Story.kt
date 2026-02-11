package com.ysraelmorenopkg.composebook.ui

import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.builder.StoryBuilder
import com.ysraelmorenopkg.composebook.core.model.StoryId

/**
 * Creates a Compose story using a DSL.
 * 
 * This is the modern, simplified API for creating stories in ComposeBook.
 * 
 * Example usage:
 * ```
 * import com.ysraelmorenopkg.composebook.ui.story
 * import com.ysraelmorenopkg.composebook.core.control.*
 * 
 * val buttonStory = story(
 *     id = "button.primary",
 *     name = "Button / Primary",
 *     defaultProps = ButtonProps("Click", true)
 * ) {
 *     documentation {
 *         description("Primary action button for important user actions")
 *         usage("""
 *             Button(
 *                 text = "Save Changes",
 *                 enabled = true
 *             )
 *         """)
 *         props("text: String - Button label\nenabled: Boolean - Interactive state")
 *         notes("Use sparingly - one primary button per screen")
 *     }
 * 
 *     control(
 *         key = "text",
 *         control = TextControl("Text", "Button label"),
 *         getter = { it.text },
 *         setter = { p, v -> p.copy(text = v) }
 *     )
 *     
 *     render { props, _ ->
 *         Button(text = props.text, enabled = props.enabled)
 *     }
 * }
 * ```
 */
fun <Props : Any> story(
    id: String,
    name: String,
    defaultProps: Props,
    block: StoryBuilder<Props>.() -> Unit
): ComposeStory<Props> {
    val builder = StoryBuilder(StoryId(id), name, defaultProps)
    builder.block()
    return builder.build()
}
