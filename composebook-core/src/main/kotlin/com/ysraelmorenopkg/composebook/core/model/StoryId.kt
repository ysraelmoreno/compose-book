package com.ysraelmorenopkg.composebook.core.model

/**
 * Type-safe identifier for a Story.
 * 
 * Prevents the use of raw strings and provides better compile-time safety.
 * Used for navigation, persistence, and deep linking in future implementations.
 */
@JvmInline
value class StoryId(val value: String) {
    init {
        require(value.isNotBlank()) { "StoryId cannot be blank" }
    }
    
    override fun toString(): String = value
}
