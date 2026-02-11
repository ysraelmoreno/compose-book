package com.ysraelmorenopkg.composebook.ui.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.model.Documentation
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Displays story documentation in a clean, readable format.
 * 
 * Shows structured information about a component including description,
 * usage examples, props documentation, and additional notes.
 * 
 * @param documentation The documentation to display
 * @param modifier Modifier for the documentation view
 */
@Composable
fun DocumentationView(
    documentation: Documentation,
    modifier: Modifier = Modifier
) {
    if (documentation.isEmpty()) {
        EmptyDocumentation(modifier = modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Description Section
            documentation.description?.let { desc ->
                DocumentationSection(
                    title = "Description",
                    content = desc
                )
            }
            
            // Usage Section
            documentation.usage?.let { usage ->
                DocumentationSection(
                    title = "Usage",
                    content = usage,
                    isCode = true
                )
            }
            
            // Props Section
            documentation.props?.let { props ->
                DocumentationSection(
                    title = "Props",
                    content = props
                )
            }
            
            // Notes Section
            documentation.notes?.let { notes ->
                DocumentationSection(
                    title = "Notes",
                    content = notes
                )
            }
        }
    }
}

@Composable
private fun DocumentationSection(
    title: String,
    content: String,
    isCode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ComposeBookLabel(text = title.uppercase())
        
        if (isCode) {
            CodeBlock(content = content)
        } else {
            ComposeBookBodyText(
                text = content,
                color = ComposeBookTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun CodeBlock(
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(16.dp)
    ) {
        ComposeBookBodyText(
            text = content.trimIndent(),
            color = ComposeBookTheme.colors.textPrimary
        )
    }
}

@Composable
private fun EmptyDocumentation(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComposeBookTitle("No Documentation")
            ComposeBookBodyText(
                text = "This story doesn't have documentation yet",
                color = ComposeBookTheme.colors.textTertiary
            )
        }
    }
}
