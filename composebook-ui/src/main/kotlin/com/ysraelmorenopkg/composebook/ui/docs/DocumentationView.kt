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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
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
            
            // Usage Section (default language: Kotlin for syntax highlighting)
            documentation.usage?.let { usage ->
                DocumentationSection(
                    title = "Usage",
                    content = usage,
                    isCode = true,
                    codeLanguage = "kotlin"
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
    codeLanguage: String = "kotlin",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ComposeBookLabel(text = title.uppercase())
        
        if (isCode) {
            CodeBlock(content = content, language = codeLanguage)
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
    language: String = "kotlin",
    modifier: Modifier = Modifier
) {
    val colors = ComposeBookTheme.colors
    val typography = ComposeBookTheme.typography
    val syntaxColors = KotlinSyntaxColors(
        default = colors.textPrimary,
        keyword = colors.accent,
        string = colors.success,
        comment = colors.textTertiary,
        number = colors.info
    )
    val highlighted = when (language.lowercase()) {
        "kotlin" -> KotlinSyntaxHighlighter.highlight(content, syntaxColors)
        else -> AnnotatedString(content.trimIndent())
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surface)
            .padding(16.dp)
    ) {
        Text(
            text = highlighted,
            style = typography.code
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
