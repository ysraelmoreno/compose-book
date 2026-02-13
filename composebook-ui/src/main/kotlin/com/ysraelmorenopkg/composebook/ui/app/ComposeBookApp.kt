package com.ysraelmorenopkg.composebook.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.app.content.CanvasContent
import com.ysraelmorenopkg.composebook.ui.app.content.DocumentationContent
import com.ysraelmorenopkg.composebook.ui.app.content.EmptyStateContent
import com.ysraelmorenopkg.composebook.ui.app.header.StoriesHeader
import com.ysraelmorenopkg.composebook.ui.app.search.StorySearchDialog
import com.ysraelmorenopkg.composebook.ui.tabs.StoryTab
import com.ysraelmorenopkg.composebook.ui.tabs.TabBar
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment
import com.ysraelmorenopkg.composebook.core.registry.StoryRegistry
import com.ysraelmorenopkg.composebook.core.runtime.StoryRuntimeState
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * ComposeBook App with custom design system.
 * 
 * Vertical layout structure:
 * - Top: Stories header (retractable)
 * - Middle: Tab bar (Canvas/Docs) + Content area
 * - Bottom: Controls panel (retractable, only visible on Canvas tab)
 * 
 * @param registry Story registry containing all stories
 * @param modifier Root modifier
 * @param darkTheme Whether to use dark theme (default: true)
 */
@Composable
fun ComposeBookApp(
    registry: StoryRegistry,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = true
) {
    ComposeBookTheme(darkTheme = darkTheme) {
        val stories = registry.getAll().filterIsInstance<ComposeStory<*>>()
        
        var selectedStory by remember { mutableStateOf<ComposeStory<*>?>(stories.firstOrNull()) }
        var storiesExpanded by remember { mutableStateOf(false) }
        var controlsExpanded by remember { mutableStateOf(true) }
        var selectedTab by remember { mutableStateOf(StoryTab.Canvas) }
        var showSearchDialog by remember { mutableStateOf(false) }
        
        var runtimeState by remember(selectedStory) {
            val story = selectedStory
            mutableStateOf(
                if (story != null) {
                    StoryRuntimeState(
                        props = story.defaultProps,
                        environment = StoryEnvironment.Default
                    )
                } else {
                    null
                }
            )
        }
        
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ComposeBookTheme.colors.background)
        ) {
            // Top - Stories Header
            if (stories.isNotEmpty()) {
                StoriesHeader(
                    stories = stories,
                    selectedStory = selectedStory,
                    expanded = storiesExpanded,
                    onExpandedChange = { storiesExpanded = it },
                    onStorySelected = { story ->
                        selectedStory = story
                        runtimeState = StoryRuntimeState(
                            props = story.defaultProps,
                            environment = StoryEnvironment.Default
                        )
                        storiesExpanded = false
                    },
                    onOpenSearchDialog = { showSearchDialog = true },
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .heightIn(max = 250.dp) // Limit header height when expanded
                )
            }
            
            // Search Dialog
            if (showSearchDialog) {
                StorySearchDialog(
                    stories = stories,
                    onStorySelected = { story ->
                        selectedStory = story
                        runtimeState = StoryRuntimeState(
                            props = story.defaultProps,
                            environment = StoryEnvironment.Default
                        )
                        showSearchDialog = false
                    },
                    onDismiss = { showSearchDialog = false }
                )
            }
            
            // Tab Bar
            if (stories.isNotEmpty() && selectedStory != null) {
                TabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Middle - Content Area (Canvas or Docs)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    stories.isEmpty() -> EmptyStateContent()
                    selectedTab == StoryTab.Canvas -> CanvasContent(
                        selectedStory = selectedStory,
                        runtimeState = runtimeState
                    )
                    selectedTab == StoryTab.Docs -> DocumentationContent(
                        selectedStory = selectedStory
                    )
                }
            }
            
            // Bottom - Controls Panel (only visible on Canvas tab)
            if (stories.isNotEmpty() && selectedStory != null && selectedTab == StoryTab.Canvas) {
                ControlsPanel(
                    story = selectedStory,
                    runtimeState = runtimeState,
                    onPropsChange = { newProps ->
                        runtimeState = runtimeState?.withProps(newProps)
                    },
                    onCollapse = { controlsExpanded = !controlsExpanded },
                    expanded = controlsExpanded,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                )
            }
        }
    }
}
