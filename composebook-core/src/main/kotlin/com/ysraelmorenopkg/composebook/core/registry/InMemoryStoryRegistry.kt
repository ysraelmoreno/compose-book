package com.ysraelmorenopkg.composebook.core.registry

import com.ysraelmorenopkg.composebook.core.api.Story
import com.ysraelmorenopkg.composebook.core.model.StoryId

/**
 * In-memory implementation of StoryRegistry.
 * 
 * Maintains stories in insertion order and prevents duplicate IDs.
 * Thread-safe for concurrent registration.
 */
class InMemoryStoryRegistry : StoryRegistry {
    private val stories = mutableMapOf<StoryId, Story<*>>()
    private val insertionOrder = mutableListOf<StoryId>()
    
    @Synchronized
    override fun register(story: Story<*>) {
        require(!stories.containsKey(story.id)) {
            "Story with id '${story.id}' is already registered"
        }
        
        stories[story.id] = story
        insertionOrder.add(story.id)
    }
    
    override fun getAll(): List<Story<*>> {
        return insertionOrder.mapNotNull { stories[it] }
    }
    
    override fun findById(id: StoryId): Story<*>? {
        return stories[id]
    }
}
