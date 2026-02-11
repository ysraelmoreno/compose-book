package com.ysraelmorenopkg.composebook.compose.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.compose.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.compose.canvas.StoryCanvas
import com.ysraelmorenopkg.composebook.compose.controls.ControlsPanel
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment
import com.ysraelmorenopkg.composebook.core.registry.StoryRegistry
import com.ysraelmorenopkg.composebook.core.runtime.StoryRuntimeState

/**
 * Main ComposeBook application UI shell.
 * 
 * Provides a Scaffold-based layout with:
 * - TopBar: Retractable Stories header
 * - BottomBar: Retractable Controls panel
 * - Content: Canvas (main area)
 * 
 * MVP implementation with local state management using remember.
 * No deep linking or persistence in this version.
 * 
 * Only works with ComposeStory instances - filters out non-Compose stories.
 * 
 * @param registry The story registry containing all registered stories
 * @param modifier Modifier for the root composable
 * @param theme Optional theme wrapper for customizing the Storybook UI.
 *              By default uses MaterialTheme. Pass your app's theme to customize colors, typography, etc.
 *              Example: `theme = { MyAppTheme { it() } }`
 */
@Composable
fun ComposeBookApp(
    registry: StoryRegistry,
    modifier: Modifier = Modifier,
    theme: @Composable (content: @Composable () -> Unit) -> Unit = { content ->
        MaterialTheme { content() }
    }
) {
    // Filter for ComposeStory instances only
    val stories = registry.getAll().filterIsInstance<ComposeStory<*>>()
    
    // Selected story state
    var selectedStory by remember { mutableStateOf<ComposeStory<*>?>(stories.firstOrNull()) }
    
    // UI state
    var storiesExpanded by remember { mutableStateOf(false) }
    var controlsExpanded by remember { mutableStateOf(true) }
    
    // Runtime state for the selected story
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
    
    // Wrap everything in the provided theme
    theme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
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
                            storiesExpanded = false // Auto-collapse after selection
                        }
                    )
                }
            },
            bottomBar = {
                if (stories.isNotEmpty()) {
                    ControlsPanelRetractable(
                        story = selectedStory,
                        runtimeState = runtimeState,
                        expanded = controlsExpanded,
                        onExpandedChange = { controlsExpanded = it },
                        onPropsChange = { newProps ->
                            runtimeState = runtimeState?.withProps(newProps)
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if (stories.isEmpty()) {
                    EmptyState()
                } else {
                    StoryContent(
                        selectedStory = selectedStory,
                        runtimeState = runtimeState
                    )
                }
            }
        }
    }
}

@Composable
private fun StoryContent(
    selectedStory: ComposeStory<*>?,
    runtimeState: StoryRuntimeState<*>?
) {
    selectedStory?.let { story ->
        runtimeState?.let { state ->
            @Suppress("UNCHECKED_CAST")
            StoryCanvas(
                story = story as ComposeStory<Any>,
                state = state as StoryRuntimeState<Any>,
                modifier = Modifier.fillMaxSize()
            )
        }
    } ?: run {
        // Show message if no story selected
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No story selected",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Tap the Stories header above to select a story",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StoriesHeader(
    stories: List<ComposeStory<*>>,
    selectedStory: ComposeStory<*>?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onStorySelected: (ComposeStory<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    // Group stories by category (part before " / ")
    val groupedStories = remember(stories) {
        stories.groupBy { story ->
            story.name.substringBefore(" / ", story.name)
        }.mapValues { (_, storiesInGroup) ->
            storiesInGroup.sortedBy { it.name }
        }
    }
    
    // Track expanded categories
    val expandedCategories = remember { mutableStateOf(setOf<String>()) }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Header with toggle button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!expanded) },
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stories",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedStory?.let {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = if (expanded) "▲" else "▼",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        
        // Expandable story list with categories
        AnimatedVisibility(visible = expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                groupedStories.forEach { (category, storiesInCategory) ->
                    // Category header
                    item(key = "category_$category") {
                        StoryCategoryHeader(
                            category = category,
                            isExpanded = expandedCategories.value.contains(category),
                            onClick = {
                                expandedCategories.value = if (expandedCategories.value.contains(category)) {
                                    expandedCategories.value - category
                                } else {
                                    expandedCategories.value + category
                                }
                            }
                        )
                    }
                    
                    // Stories in this category (if expanded)
                    if (expandedCategories.value.contains(category)) {
                        items(
                            items = storiesInCategory,
                            key = { story -> story.id.value }
                        ) { story ->
                            val storyName = story.name.substringAfter(" / ", story.name)
                            StoryListItemIndented(
                                storyName = storyName,
                                isSelected = story.id == selectedStory?.id,
                                onClick = { onStorySelected(story) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryCategoryHeader(
    category: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isExpanded) "▼" else "▶",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StoryListItemIndented(
    storyName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = storyName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun ControlsPanelRetractable(
    story: ComposeStory<*>?,
    runtimeState: StoryRuntimeState<*>?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onPropsChange: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Header with toggle button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!expanded) },
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Controls",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = if (expanded) "▼" else "▲",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        
        // Expandable controls panel
        AnimatedVisibility(visible = expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                story?.let { s ->
                    runtimeState?.let { state ->
                        @Suppress("UNCHECKED_CAST")
                        ControlsPanel(
                            story = s as ComposeStory<Any>,
                            currentProps = state.props,
                            onPropsChange = onPropsChange
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No controls available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryListItem(
    story: ComposeStory<*>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = backgroundColor
    ) {
        Text(
            text = story.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No Stories Found",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Register ComposeStory instances using StoryRegistry",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
