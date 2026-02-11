package com.ysraelmorenopkg.composebook.ui.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.canvas.StoryCanvas
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment
import com.ysraelmorenopkg.composebook.core.registry.StoryRegistry
import com.ysraelmorenopkg.composebook.core.runtime.StoryRuntimeState
import com.ysraelmorenopkg.composebook.ui.components.BookIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronRightIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronUpIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookDivider
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * ComposeBook App with custom design system.
 * 
 * Vertical layout structure:
 * - Top: Stories header (retractable)
 * - Middle: Canvas (component preview)
 * - Bottom: Controls panel (retractable)
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
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .heightIn(max = 250.dp) // Limit header height when expanded
                )
            }
            
            // Middle - Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (stories.isEmpty()) {
                    EmptyStateContent()
                } else {
                    CanvasContent(
                        selectedStory = selectedStory,
                        runtimeState = runtimeState
                    )
                }
            }
            
            // Bottom - Controls Panel
            if (stories.isNotEmpty() && selectedStory != null) {
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

@Composable
private fun StoriesHeader(
    stories: List<ComposeStory<*>>,
    selectedStory: ComposeStory<*>?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onStorySelected: (ComposeStory<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedStories = remember(stories) {
        stories.groupBy { story ->
            story.name.substringBefore(" / ", story.name)
        }.mapValues { (_, storiesInGroup) ->
            storiesInGroup.sortedBy { it.name }
        }
    }
    
    val expandedCategories = remember { mutableStateOf(setOf<String>()) }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Header with toggle button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(ComposeBookTheme.colors.backgroundElevated)
                .clickable { onExpandedChange(!expanded) }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BookIcon()
                ComposeBookTitle("Stories")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedStory?.let {
                    ComposeBookLabel(
                        text = it.name,
                        color = ComposeBookTheme.colors.textSecondary
                    )
                }
                if (expanded) ChevronUpIcon() else ChevronDownIcon()
            }
        }
        
        // Expandable story list
        AnimatedVisibility(visible = expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .background(ComposeBookTheme.colors.backgroundElevated)
            ) {
                groupedStories.forEach { (category, storiesInCategory) ->
                    item(key = "category_$category") {
                        CategoryItem(
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
                    
                    if (expandedCategories.value.contains(category)) {
                        items(
                            items = storiesInCategory,
                            key = { story -> story.id.value }
                        ) { story ->
                            val storyName = story.name.substringAfter(" / ", story.name)
                            StoryListItem(
                                storyName = storyName,
                                isSelected = story.id == selectedStory?.id,
                                onClick = { onStorySelected(story) }
                            )
                        }
                    }
                }
            }
        }
        
        ComposeBookDivider()
    }
}

@Composable
private fun CategoryItem(
    category: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isExpanded) {
            ChevronDownIcon(size = 14.dp)
        } else {
            ChevronRightIcon(size = 14.dp)
        }
        
        ComposeBookBodyText(
            text = category,
            color = ComposeBookTheme.colors.textPrimary
        )
    }
}

@Composable
private fun StoryListItem(
    storyName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        ComposeBookTheme.colors.surfaceSelected
    } else {
        ComposeBookTheme.colors.backgroundElevated
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(start = 40.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
    ) {
        ComposeBookBodyText(
            text = storyName,
            color = if (isSelected) {
                ComposeBookTheme.colors.accent
            } else {
                ComposeBookTheme.colors.textSecondary
            }
        )
    }
}

@Composable
private fun CanvasContent(
    selectedStory: ComposeStory<*>?,
    runtimeState: StoryRuntimeState<*>?
) {
    selectedStory?.let { story ->
        runtimeState?.let { state ->
            @Suppress("UNCHECKED_CAST")
            StoryCanvas(
                story = story as ComposeStory<Any>,
                state = state as StoryRuntimeState<Any>,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    } ?: EmptyStateContent()
}

@Composable
private fun EmptyStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookIcon(size = 48.dp, color = ComposeBookTheme.colors.textTertiary)
            
            ComposeBookTitle(
                text = "No Stories Found",
                color = ComposeBookTheme.colors.textPrimary
            )
            
            ComposeBookBodyText(
                text = "Register ComposeStory instances using StoryRegistry",
                color = ComposeBookTheme.colors.textTertiary
            )
        }
    }
}
