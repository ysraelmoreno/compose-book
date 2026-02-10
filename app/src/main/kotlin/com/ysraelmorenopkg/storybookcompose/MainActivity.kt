package com.ysraelmorenopkg.storybookcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ysraelmorenopkg.storybook.core.registry.InMemoryStoryRegistry
import com.ysraelmorenopkg.storybook.samples.registerSampleStories
import com.ysraelmorenopkg.storybook.ui.app.ModernStorybookApp

/**
 * Main activity that launches the modern Storybook application.
 * 
 * This demonstrates the complete MVP with modern UI:
 * - Creates a registry
 * - Manually registers sample stories
 * - Launches ModernStorybookApp with custom design system
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Create registry and register stories
        val registry = InMemoryStoryRegistry()
        registerSampleStories(registry)
        
        setContent {
            // Modern Storybook UI with custom design system
            ModernStorybookApp(
                registry = registry,
                darkTheme = true // Use dark theme by default
            )
        }
    }
}
