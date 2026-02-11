package com.ysraelmorenopkg.composebook.ui.adapter

import com.ysraelmorenopkg.composebook.core.api.StoryContext
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment

/**
 * Default implementation of StoryContext for Compose.
 * 
 * @property environment The story environment configuration
 * @property inspectionMode Whether the story is running in Storybook (default: true)
 */
data class DefaultStoryContext(
    override val environment: StoryEnvironment,
    override val inspectionMode: Boolean = true
) : StoryContext
