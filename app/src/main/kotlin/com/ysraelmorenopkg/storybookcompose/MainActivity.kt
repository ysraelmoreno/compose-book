package com.ysraelmorenopkg.storybookcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ysraelmorenopkg.storybook.core.registry.InMemoryStoryRegistry
import com.ysraelmorenopkg.storybook.samples.registerSampleStories
import com.ysraelmorenopkg.storybook.ui.app.ModernComposeBookApp

/**
 * Main activity that launches the modern ComposeBook application.
 * 
 * This demonstrates the complete MVP with modern UI:
 * - Creates a registry
 * - Manually registers sample stories
 * - Launches ModernComposeBookApp with custom design system
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Create registry and register stories
        val registry = InMemoryStoryRegistry()
        registerSampleStories(registry)
        
        setContent {
            // Modern ComposeBook UI with custom design system
            ModernComposeBookApp(
                registry = registry,
                darkTheme = true // Use dark theme by default
            )
        }
    }
}
