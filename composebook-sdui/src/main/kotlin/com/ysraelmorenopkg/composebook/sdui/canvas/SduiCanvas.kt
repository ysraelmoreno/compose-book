package com.ysraelmorenopkg.composebook.sdui.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment
import com.ysraelmorenopkg.composebook.core.environment.ThemeMode
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.render.SduiMainRender
import com.ysraelmorenopkg.composebook.sdui.story.DefaultSduiThemeWrapper

/**
 * Canvas that renders an SDUI component tree inside ComposeBook.
 *
 * Applies the [StoryEnvironment] theme and delegates rendering
 * to [SduiMainRender], which dispatches to the appropriate
 * registered renderer for each component in the tree.
 *
 * @param themeWrapper Replaces the default Material3 theme wrapping.
 *   Consumers can provide their own design system theme (e.g., BeesTheme).
 */
@Composable
fun SduiCanvas(
    component: Any,
    registry: SduiRegistry,
    environment: StoryEnvironment,
    modifier: Modifier = Modifier,
    themeWrapper: @Composable (ThemeMode, @Composable () -> Unit) -> Unit = DefaultSduiThemeWrapper,
) {
    themeWrapper(environment.theme) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                SduiMainRender(
                    component = component,
                    registry = registry,
                )
            }
        }
    }
}
