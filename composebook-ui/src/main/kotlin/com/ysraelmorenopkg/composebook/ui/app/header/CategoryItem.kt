package com.ysraelmorenopkg.composebook.ui.app.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronRightIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Represents a category item in the expandable story list.
 * Shows a chevron icon and category name.
 * 
 * @param category Category name to display
 * @param isExpanded Whether the category is currently expanded
 * @param onClick Callback when the category is clicked
 */
@Composable
fun CategoryItem(
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
