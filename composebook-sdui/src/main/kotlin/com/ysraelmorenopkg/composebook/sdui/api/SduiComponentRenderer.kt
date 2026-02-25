package com.ysraelmorenopkg.composebook.sdui.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Renders an SDUI component of type [T] as Compose UI.
 *
 * Mirrors the pattern from SDUI architectures (e.g., browse-android's RenderFunction)
 * but adapted for ComposeBook's registry-based dispatch.
 *
 * @param T The concrete component type this renderer handles
 *
 * @see [com.ysraelmorenopkg.composebook.sdui.render.SduiMainRender] for dispatch logic
 */
fun interface SduiComponentRenderer<T : Any> {

    /**
     * Renders [component] into Compose UI.
     *
     * @param component The typed component instance
     * @param modifier Modifier applied by the parent layout
     * @param renderChild Recursive dispatch — call this to render nested child components.
     *                    Accepts [Any] because children may be of different concrete types.
     */
    @Composable
    fun Render(
        component: T,
        modifier: Modifier,
        renderChild: @Composable (Any) -> Unit,
    )
}
