package com.ysraelmorenopkg.composebook.sdui.render

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Fallback renderer for components without a registered [SduiComponentRenderer].
 *
 * Displays the component's class name and a warning so the developer knows
 * a renderer is missing — rather than silently swallowing the component.
 */
@Composable
internal fun SduiFallbackRender(
    component: Any,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(8.dp),
    ) {
        Text(
            text = "No renderer: ${component::class.simpleName}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
}
