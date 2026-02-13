package com.ysraelmorenopkg.composebook.ui.app.header

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.components.BookIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronUpIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookDivider
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Main header component displaying the Stories title and expandable story list.
 * 
 * @param stories List of all available stories
 * @param selectedStory Currently selected story
 * @param expanded Whether the story list is expanded
 * @param onExpandedChange Callback when expand state changes
 * @param onStorySelected Callback when a story is selected
 * @param onOpenSearchDialog Callback to open the search dialog (long press)
 * @param modifier Optional modifier
 */
@Composable
fun StoriesHeader(
    stories: List<ComposeStory<*>>,
    selectedStory: ComposeStory<*>?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onStorySelected: (ComposeStory<*>) -> Unit,
    onOpenSearchDialog: () -> Unit,
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
                .pointerInput(expanded) {
                    detectTapGestures(
                        onTap = { onExpandedChange(!expanded) },
                        onLongPress = { onOpenSearchDialog() }
                    )
                }
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
