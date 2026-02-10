package com.ysraelmorenopkg.storybook.compose.adapter

import com.ysraelmorenopkg.storybook.core.api.StoryContext
import com.ysraelmorenopkg.storybook.core.environment.StoryEnvironment

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
