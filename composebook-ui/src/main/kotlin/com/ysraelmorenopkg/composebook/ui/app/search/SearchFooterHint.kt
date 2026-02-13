package com.ysraelmorenopkg.composebook.ui.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Footer hint displayed at the bottom of the search dialog.
 * Provides usage tips for the user.
 */
@Composable
fun SearchFooterHint() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ComposeBookTheme.colors.background)
            .padding(12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ComposeBookLabel(
            text = "Tip: Long press the Stories header to open search",
            color = ComposeBookTheme.colors.textTertiary
        )
    }
}
