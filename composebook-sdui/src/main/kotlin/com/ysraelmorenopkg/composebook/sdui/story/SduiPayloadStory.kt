package com.ysraelmorenopkg.composebook.sdui.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.core.api.StoryContext
import com.ysraelmorenopkg.composebook.core.control.PropBinding
import com.ysraelmorenopkg.composebook.core.environment.ThemeMode
import com.ysraelmorenopkg.composebook.core.model.StoryId
import com.ysraelmorenopkg.composebook.sdui.controls.PayloadEditor
import com.ysraelmorenopkg.composebook.sdui.mapper.MappingResult
import com.ysraelmorenopkg.composebook.sdui.mapper.SduiPayloadMapper
import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry
import com.ysraelmorenopkg.composebook.sdui.render.SduiMainRender
import com.ysraelmorenopkg.composebook.sdui.render.SduiMappingError
import com.ysraelmorenopkg.composebook.sdui.render.SduiMappingLoading
import com.ysraelmorenopkg.composebook.ui.adapter.ComposeStory
import com.ysraelmorenopkg.composebook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronUpIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookDivider
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.components.SettingsIcon
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Creates a payload-driven ComposeBook story with an embedded editor.
 *
 * Unlike [sduiStory] which renders a pre-built component, this story
 * takes a raw [SduiPayload] and runs the full mapping pipeline:
 *
 * ```
 * SduiPayload → SduiComponentMapper.map() → Component → SduiMainRender → Compose UI
 * ```
 *
 * The canvas renders in a split layout:
 * - **Top**: Rendered component preview (auto-remaps on every edit)
 * - **Bottom**: Collapsible PayloadEditor (structured & raw JSON modes)
 *
 * Example:
 * ```
 * val story = sduiPayloadStory(
 *     id = "sdui.button.from-payload",
 *     name = "SDUI / Button / From Payload",
 *     defaultPayload = SduiPayload(
 *         type = "button",
 *         parameters = mapOf("value" to "Click Me", "enabled" to true),
 *     ),
 *     sduiRegistry = registry,
 * )
 * ```
 */
fun sduiPayloadStory(
    id: String,
    name: String,
    defaultPayload: SduiPayload,
    sduiRegistry: SduiRegistry,
): ComposeStory<SduiPayloadStoryProps> {
    val storyId = StoryId(id)
    val defaultProps = SduiPayloadStoryProps(
        payload = defaultPayload,
        registry = sduiRegistry,
    )

    return object : ComposeStory<SduiPayloadStoryProps> {
        override val id: StoryId = storyId
        override val name: String = name
        override val defaultProps: SduiPayloadStoryProps = defaultProps
        override val controls: List<PropBinding<SduiPayloadStoryProps, *>> = emptyList()

        override fun render(props: SduiPayloadStoryProps, context: StoryContext) {
            // No-op: rendering happens via Content()
        }

        @Composable
        override fun Content(props: SduiPayloadStoryProps, context: StoryContext) {
            SduiPayloadCanvasWithEditor(
                initialPayload = props.payload,
                registry = props.registry,
                context = context,
            )
        }
    }
}

/**
 * Split-layout canvas: component preview on top, collapsible editor on bottom.
 *
 * Owns the editable payload state. When the user edits a parameter (structured
 * or raw JSON), the new [SduiPayload] triggers [produceState] to re-map,
 * producing an immediate UI update.
 *
 * The shell uses ComposeBook theme; the preview area wraps in Material
 * theme for SDUI component rendering.
 */
@Composable
private fun SduiPayloadCanvasWithEditor(
    initialPayload: SduiPayload,
    registry: SduiRegistry,
    context: StoryContext,
    modifier: Modifier = Modifier,
) {
    var editablePayload by remember(initialPayload) {
        mutableStateOf(initialPayload)
    }
    var editorVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComposeBookTheme.colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            SduiPayloadPreview(
                payload = editablePayload,
                registry = registry,
                context = context,
            )
        }

        EditorToggleBar(
            expanded = editorVisible,
            payloadType = editablePayload.type,
            onToggle = { editorVisible = !editorVisible },
        )

        AnimatedVisibility(
            visible = editorVisible,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
            ) {
                PayloadEditor(
                    payload = editablePayload,
                    onPayloadChange = { editablePayload = it },
                )
            }
        }
    }
}

/**
 * Toggle bar matching the ControlsPanel header pattern:
 * icon + title on the left, chevron on the right.
 */
@Composable
private fun EditorToggleBar(
    expanded: Boolean,
    payloadType: String,
    onToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ComposeBookTheme.colors.backgroundElevated),
    ) {
        ComposeBookDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SettingsIcon()
                ComposeBookTitle("Payload Editor")
                ComposeBookLabel(
                    text = payloadType,
                    color = ComposeBookTheme.colors.accent,
                )
            }

            if (expanded) ChevronDownIcon() else ChevronUpIcon()
        }
    }
}

/**
 * Maps [payload] and renders the result inside a Material theme
 * (SDUI components use Material3 composables). Re-maps whenever [payload] changes.
 */
@Composable
private fun SduiPayloadPreview(
    payload: SduiPayload,
    registry: SduiRegistry,
    context: StoryContext,
    modifier: Modifier = Modifier,
) {
    val mapper = remember(registry) { SduiPayloadMapper(registry) }

    val mappingState = produceState<MappingUiState>(
        initialValue = MappingUiState.Loading,
        key1 = payload,
    ) {
        value = MappingUiState.Loading
        val result = mapper.map(payload)
        value = when (result) {
            is MappingResult.Success -> MappingUiState.Success(result.component)
            is MappingResult.Error -> MappingUiState.Error(result.message)
        }
    }

    val colorScheme = when (context.environment.theme) {
        ThemeMode.Light -> lightColorScheme()
        ThemeMode.Dark -> darkColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            when (val state = mappingState.value) {
                is MappingUiState.Loading -> SduiMappingLoading()

                is MappingUiState.Error -> SduiMappingError(
                    message = state.message,
                    payloadType = payload.type,
                )

                is MappingUiState.Success -> SduiMainRender(
                    component = state.component,
                    registry = registry,
                )
            }
        }
    }
}

private sealed interface MappingUiState {
    data object Loading : MappingUiState
    data class Success(val component: Any) : MappingUiState
    data class Error(val message: String) : MappingUiState
}
