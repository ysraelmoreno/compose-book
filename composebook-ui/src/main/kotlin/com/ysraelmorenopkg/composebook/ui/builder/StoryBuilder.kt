package com.ysraelmorenopkg.composebook.ui.builder

import androidx.compose.runtime.Composable
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.core.api.StoryContext
import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.control.PropControl
import com.ysraelmorenopkg.composebook.core.model.StoryId

/**
 * Builder for creating Compose stories using a DSL.
 * 
 * This is the modern UI module's story builder that extends the core functionality.
 */
class StoryBuilder<Props : Any>(
    private val id: StoryId,
    private val name: String,
    private val defaultProps: Props
) {
    private val bindings = mutableListOf<PropBinding<Props, *>>()
    private var composeContent: (@Composable (Props, StoryContext) -> Unit)? = null
    
    /**
     * Adds a control binding for a prop.
     */
    fun <T : Any> control(
        key: String,
        control: PropControl<T>,
        getter: (Props) -> T,
        setter: (Props, T) -> Props
    ) {
        bindings += PropBinding(key, getter, setter, control)
    }
    
    /**
     * Defines the Composable render function for this story.
     */
    fun render(content: @Composable (Props, StoryContext) -> Unit) {
        composeContent = content
    }
    
    /**
     * Builds the ComposeStory instance.
     */
    internal fun build(): ComposeStory<Props> {
        val finalContent = composeContent ?: error("Story must have a render function")
        
        return object : ComposeStory<Props> {
            override val id: StoryId = this@StoryBuilder.id
            override val name: String = this@StoryBuilder.name
            override val defaultProps: Props = this@StoryBuilder.defaultProps
            override val controls: List<PropBinding<Props, *>> = bindings.toList()
            
            override fun render(props: Props, context: StoryContext) {
                // No-op in core layer - actual rendering happens via Content()
            }
            
            @Composable
            override fun Content(props: Props, context: StoryContext) {
                finalContent(props, context)
            }
        }
    }
}

/**
 * Creates a Compose story using a DSL.
 *
 * This is the modern, simplified API for creating stories in ComposeBook UI.
 *
 * Example usage:
 * ```
 * val buttonStory = story(
 *     id = "button.primary",
 *     name = "Button / Primary",
 *     defaultProps = ButtonProps("Click", true)
 * ) {
 *     control(
 *         key = "text",
 *         control = TextControl("Text"),
 *         getter = { it.text },
 *         setter = { p, v -> p.copy(text = v) }
 *     )
 *
 *     render { props, context ->
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
