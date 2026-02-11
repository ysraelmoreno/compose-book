package com.ysraelmorenopkg.composebook.compose.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.api.Story
import com.ysraelmorenopkg.composebook.core.control.PropBinding

/**
 * Panel displaying all controls for a story.
 * 
 * Renders each PropBinding as an editable control.
 * 
 * @param story The story whose controls to display
 * @param currentProps The current props values
 * @param onPropsChange Callback when any prop is updated
 */
@Composable
fun <Props : Any> ControlsPanel(
    story: Story<Props>,
    currentProps: Props,
    onPropsChange: (Props) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Controls",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (story.controls.isEmpty()) {
            Text(
                text = "No controls available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            story.controls.forEachIndexed { index, binding ->
                @Suppress("UNCHECKED_CAST")
                ControlRenderer(
                    binding = binding as PropBinding<Props, Any>,
                    currentProps = currentProps,
                    onPropsChange = onPropsChange
                )
                
                if (index < story.controls.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}
