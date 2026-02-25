package com.ysraelmorenopkg.composebook.sdui.render

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry

/**
 * Main SDUI dispatch composable.
 *
 * Looks up the renderer for [component]'s class in the [registry] and invokes it.
 * If no renderer is found, renders [SduiFallbackRender] instead.
 *
 * Child components are rendered recursively through the `renderChild` callback
 * passed to each renderer, enabling support for component trees (e.g., RichText
 * containing Text + Button children).
 *
 * This mirrors the browse-android `MainRender` pattern:
 * ```
 * renderMap[componentProps::class.java]?.invoke(modifierManager, position, componentProps)
 * ```
 */
@Composable
fun SduiMainRender(
    component: Any,
    registry: SduiRegistry,
    modifier: Modifier = Modifier,
) {
    val renderer = registry.findRenderer(component::class)

    if (renderer != null) {
        renderer.Render(
            component = component,
            modifier = modifier,
            renderChild = { child ->
                SduiMainRender(
                    component = child,
                    registry = registry,
                )
            },
        )
    } else {
        SduiFallbackRender(component, modifier)
    }
}
