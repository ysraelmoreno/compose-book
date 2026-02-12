package com.ysraelmorenopkg.composebook.core.registry

import com.ysraelmorenopkg.composebook.core.api.Story
import com.ysraelmorenopkg.composebook.core.model.StoryId

/**
 * Registry for discovering and accessing stories.
 * 
 * Provides a central place to register and retrieve stories.
 * No auto-discovery or reflection - all registration is explicit.
 */
interface StoryRegistry {
    /**
     * Registers a story.
     * 
     * @param story The story to register
     * @throws IllegalArgumentException if a story with the same ID already exists
     */
    fun register(story: Story<*>)
    
    /**
     * Returns all registered stories in the order they were registered.
     */
    fun getAll(): List<Story<*>>
    
    /**
     * Finds a story by its ID.
     * 
     * @return The story if found, null otherwise
     */
    fun findById(id: StoryId): Story<*>?
}
