package com.ysraelmorenopkg.storybook.core.api

import com.ysraelmorenopkg.storybook.core.environment.StoryEnvironment

/**
 * Context provided to a story during rendering.
 * 
 * Allows stories to know their execution environment without coupling to UI frameworks.
 */
interface StoryContext {
    /**
     * The environment configuration (theme, locale, device).
     */
    val environment: StoryEnvironment
    
    /**
     * Whether the story is running in inspection/preview mode.
     * Useful for components that behave differently in Storybook.
     */
    val inspectionMode: Boolean
}
