package com.ysraelmorenopkg.composebook.sdui.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.environment.StoryEnvironment
import com.ysraelmorenopkg.composebook.core.environment.ThemeMode
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.render.SduiMainRender

/**
 * Canvas that renders an SDUI component tree inside ComposeBook.
 *
 * Applies the [StoryEnvironment] theme and delegates rendering
 * to [SduiMainRender], which dispatches to the appropriate
 * registered renderer for each component in the tree.
 */
@Composable
fun SduiCanvas(
    component: Any,
    registry: SduiRegistry,
    environment: StoryEnvironment,
    modifier: Modifier = Modifier,
) {
    val colorScheme = when (environment.theme) {
        ThemeMode.Light -> lightColorScheme()
        ThemeMode.Dark -> darkColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
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
