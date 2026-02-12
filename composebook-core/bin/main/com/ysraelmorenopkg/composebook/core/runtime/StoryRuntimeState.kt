package com.ysraelmorenopkg.composebook.core.runtime

import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment

/**
 * Represents the current runtime state of a story.
 * 
 * Combines props and environment into a single immutable state object.
 * This enables features like reset, undo/redo, and snapshots in the future.
 * 
 * @param Props The props type for this story
 * @property props The current props values
 * @property environment The current environment configuration
 */
data class StoryRuntimeState<Props : Any>(
    val props: Props,
    val environment: StoryEnvironment
) {
    /**
     * Creates a new state with updated props.
     */
    fun withProps(newProps: Props): StoryRuntimeState<Props> {
        return copy(props = newProps)
    }
    
    /**
     * Creates a new state with updated environment.
     */
    fun withEnvironment(newEnvironment: StoryEnvironment): StoryRuntimeState<Props> {
        return copy(environment = newEnvironment)
    }
}
