package com.ysraelmorenopkg.storybook.compose.adapter

import androidx.compose.runtime.Composable
import com.ysraelmorenopkg.storybook.core.api.StoryBuilder
import com.ysraelmorenopkg.storybook.core.api.StoryContext
import com.ysraelmorenopkg.storybook.core.control.PropBinding
import com.ysraelmorenopkg.storybook.core.control.PropControl
import com.ysraelmorenopkg.storybook.core.model.StoryId

/**
 * Builder for creating Compose stories using a DSL.
 * 
 * Extends the core StoryBuilder to support Composable content.
 */
class ComposeStoryBuilder<Props : Any>(
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
     * Alternative render API that takes ComposeRenderData.
     */
    fun render(renderData: ComposeRenderData<Props>) {
        composeContent = renderData.content
    }
    
    /**
     * Builds the ComposeStory instance.
     */
    internal fun build(): ComposeStory<Props> {
        val finalContent = composeContent ?: error("ComposeStory must have a render function")
        
        return object : ComposeStory<Props> {
            override val id: StoryId = this@ComposeStoryBuilder.id
            override val name: String = this@ComposeStoryBuilder.name
            override val defaultProps: Props = this@ComposeStoryBuilder.defaultProps
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
 * Example usage:
 * ```
 * val buttonStory = composeStory(
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
fun <Props : Any> composeStory(
    id: String,
    name: String,
    defaultProps: Props,
    block: ComposeStoryBuilder<Props>.() -> Unit
): ComposeStory<Props> {
    val builder = ComposeStoryBuilder(StoryId(id), name, defaultProps)
    builder.block()
    return builder.build()
}
