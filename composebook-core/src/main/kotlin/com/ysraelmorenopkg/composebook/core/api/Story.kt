package com.ysraelmorenopkg.composebook.core.api

import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.model.StoryId

/**
 * Core interface representing a Story.
 * 
 * A Story is a self-contained description of how to render a component
 * with specific props and available controls.
 * 
 * IMPORTANT: Story.render() is NOT a Composable function. This keeps the core
 * independent of UI frameworks and enables future multiplatform support.
 * 
 * @param Props The immutable data class containing all component properties
 */
interface Story<Props : Any> {
    /**
     * Unique identifier for this story.
     */
    val id: StoryId
    
    /**
     * Human-readable name displayed in the UI.
     */
    val name: String
    
    /**
     * Default props used when the story is first loaded.
     */
    val defaultProps: Props
    
    /**
     * List of prop bindings that define which props can be controlled and how.
     */
    val controls: List<PropBinding<Props, *>>
    
    /**
     * Renders the story with the given props and context.
     * 
     * This is NOT a Composable function. UI adapters (like ComposeStoryAdapter)
     * bridge this to actual rendering frameworks.
     * 
     * @param props The current props to render with
     * @param context The story execution context
     */
    fun render(props: Props, context: StoryContext)
}
