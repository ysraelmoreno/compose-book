package com.ysraelmorenopkg.composebook.core.api

import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.control.PropControl
import com.ysraelmorenopkg.composebook.core.model.Documentation
import com.ysraelmorenopkg.composebook.core.model.StoryId

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
    private var docs: Documentation = Documentation.Empty
    
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
     * Defines documentation for this story.
     * 
     * Provides additional information about the component that will be
     * displayed in a separate documentation tab.
     * 
     * @param block Builder for documentation content
     */
    fun documentation(block: DocumentationBuilder.() -> Unit) {
        docs = DocumentationBuilder().apply(block).build()
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
        val finalDocs = docs
        
        return object : Story<Props> {
            override val id: StoryId = this@StoryBuilder.id
            override val name: String = this@StoryBuilder.name
            override val defaultProps: Props = this@StoryBuilder.defaultProps
            override val controls: List<PropBinding<Props, *>> = bindings.toList()
            override val documentation: Documentation = finalDocs
            
            override fun render(props: Props, context: StoryContext) {
                finalRenderer(props, context)
            }
        }
    }
}

/**
 * Builder for documentation content.
 */
class DocumentationBuilder {
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
 * Creates a story using a DSL.
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
 *     }
 * 
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
