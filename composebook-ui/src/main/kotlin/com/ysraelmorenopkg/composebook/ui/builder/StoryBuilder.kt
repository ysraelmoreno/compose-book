package com.ysraelmorenopkg.composebook.ui.builder

import androidx.compose.runtime.Composable
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.core.api.StoryContext
import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.control.PropControl
import com.ysraelmorenopkg.composebook.core.model.Documentation
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
    private var docs: Documentation = Documentation.Empty
    
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
     * Defines documentation for this story.
     * 
     * Provides additional information about the component that will be
     * displayed in a separate documentation tab.
     * 
     * @param block Builder for documentation content
     */
    fun documentation(block: StoryDocumentationBuilder.() -> Unit) {
        docs = StoryDocumentationBuilder().apply(block).build()
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
        val finalDocs = docs
        
        return object : ComposeStory<Props> {
            override val id: StoryId = this@StoryBuilder.id
            override val name: String = this@StoryBuilder.name
            override val defaultProps: Props = this@StoryBuilder.defaultProps
            override val controls: List<PropBinding<Props, *>> = bindings.toList()
            override val documentation: Documentation = finalDocs
            
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
 * Builder for documentation content in the UI module.
 * 
 * This is a public API wrapper that creates Documentation instances
 * without exposing core internals.
 */
class StoryDocumentationBuilder {
    private var description: String? = null
    private var usage: String? = null
    private var props: String? = null
    private var notes: String? = null
    
    /**
     * Sets the main description of the component.
     * 
     * Explains what the component is, its purpose, and when to use it.
     */
    fun description(text: String) {
        description = text
    }
    
    /**
     * Sets usage examples with code snippets.
     * 
     * Shows how to use the component in practice with real examples.
     */
    fun usage(text: String) {
        usage = text
    }
    
    /**
     * Sets props documentation.
     * 
     * Describes available props, their types, and effects on the component.
     */
    fun props(text: String) {
        props = text
    }
    
    /**
     * Sets additional notes.
     * 
     * Includes warnings, best practices, accessibility notes, or other important information.
     */
    fun notes(text: String) {
        notes = text
    }
    
    internal fun build() = Documentation(
        description = description,
        usage = usage,
        props = props,
        notes = notes
    )
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
 *     documentation {
 *         description("Primary action button for important user actions")
 *         usage("""
 *             Button(
 *                 text = "Save Changes",
 *                 enabled = true
 *             )
 *         """)
 *         props("text: String - The button label\nenabled: Boolean - Whether the button is interactive")
 *         notes("Use primary buttons for the main action on a screen")
 *     }
 * 
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
