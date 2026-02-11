package com.ysraelmorenopkg.composebook.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Tab bar for switching between Canvas and Documentation views.
 * 
 * Displays two tabs with clear visual feedback for the selected tab.
 * 
 * @param selectedTab Currently selected tab
 * @param onTabSelected Callback when a tab is clicked
 * @param modifier Modifier for the tab bar
 */
@Composable
fun TabBar(
    selectedTab: StoryTab,
    onTabSelected: (StoryTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Tab(
            label = "Canvas",
            isSelected = selectedTab == StoryTab.Canvas,
            onClick = { onTabSelected(StoryTab.Canvas) },
            modifier = Modifier.weight(1f)
        )
        
        Tab(
            label = "Docs",
            isSelected = selectedTab == StoryTab.Docs,
            onClick = { onTabSelected(StoryTab.Docs) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Tab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (isSelected) {
                    ComposeBookTheme.colors.backgroundElevated
                } else {
                    ComposeBookTheme.colors.surface
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        ComposeBookBodyText(
            text = label,
            color = if (isSelected) {
                ComposeBookTheme.colors.textPrimary
            } else {
                ComposeBookTheme.colors.textTertiary
            }
        )
    }
}

/**
 * Represents the available tabs in the story viewer.
 */
enum class StoryTab {
    Canvas,
    Docs
}
