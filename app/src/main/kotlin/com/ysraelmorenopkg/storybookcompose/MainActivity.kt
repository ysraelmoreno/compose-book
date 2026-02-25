package com.ysraelmorenopkg.storybookcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ysraelmorenopkg.composebook.core.registry.InMemoryStoryRegistry
import com.ysraelmorenopkg.composebook.ui.app.ComposeBookApp
import com.ysraelmorenopkg.storybook.samples.registerSampleStories
import com.ysraelmorenopkg.storybook.samples.sdui.registerSduiJsonSampleStories

/**
 * Main activity that launches the ComposeBook application.
 * 
 * This demonstrates the complete MVP with modern UI:
 * - Creates a registry
 * - Manually registers sample stories (including SDUI)
 * - Registers SDUI stories parsed from a raw JSON asset file
 * - Launches ComposeBookApp with custom design system
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val registry = InMemoryStoryRegistry()
        registerSampleStories(registry)
        registerSduiJsonSampleStories(registry, assets)
        
        setContent {
            ComposeBookApp(
                registry = registry,
                darkTheme = true,
            )
        }
    }
}
