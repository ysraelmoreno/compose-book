package com.ysraelmorenopkg.storybook.core.api

import com.ysraelmorenopkg.storybook.core.control.PropBinding
import com.ysraelmorenopkg.storybook.core.control.PropControl
import com.ysraelmorenopkg.storybook.core.model.StoryId

/**
 * Builder for creating stories using a DSL.
 * 
 * Provides a clean, type-safe API for defining stories without
 * directly implementing the Story interface.
 */
class StoryBuilder<Props : Any>(
    private val id: StoryId,
    private val name: String,
    private val defaultProps: Props
) {
    private val bindings = mutableListOf<PropBinding<Props, *>>()
    private var renderer: ((Props, StoryContext) -> Unit)? = null
    
    /**
     * Adds a control binding for a prop.
     * 
     * @param key Unique key for this prop (typically the field name)
     * @param control The control type (Text, Boolean, Enum)
     * @param getter Function to extract the value from Props
     * @param setter Function to create new Props with updated value (must maintain immutability)
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
     * Defines the render function for this story.
     * 
     * This is NOT a Composable function - it's a regular function that
     * will be adapted by UI layer implementations.
     */
    fun render(block: (Props, StoryContext) -> Unit) {
        renderer = block
    }
    
    /**
     * Builds the Story instance.
     * Internal - should only be called by the story() helper function.
     */
    internal fun build(): Story<Props> {
        val finalRenderer = renderer ?: error("Story must have a render function")
        
        return object : Story<Props> {
            override val id: StoryId = this@StoryBuilder.id
            override val name: String = this@StoryBuilder.name
            override val defaultProps: Props = this@StoryBuilder.defaultProps
            override val controls: List<PropBinding<Props, *>> = bindings.toList()
            
            override fun render(props: Props, context: StoryContext) {
                finalRenderer(props, context)
            }
        }
    }
}

/**
 * Creates a story using a DSL.
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
 *     render { props, _ ->
 *         // Adapter will convert this to actual UI
 *         Button(text = props.text)
 *     }
 * }
 * ```
 */
fun <Props : Any> story(
    id: String,
    name: String,
    defaultProps: Props,
    block: StoryBuilder<Props>.() -> Unit
): Story<Props> {
    val builder = StoryBuilder(StoryId(id), name, defaultProps)
    builder.block()
    return builder.build()
}
