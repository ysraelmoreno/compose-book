package com.ysraelmorenopkg.composebook.sdui.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.composebook.sdui.parser.SduiPayloadParser
import com.ysraelmorenopkg.composebook.sdui.parser.SduiPayloadSerializer
import com.ysraelmorenopkg.composebook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.composebook.ui.components.ChevronRightIcon
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookDivider
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookLabel
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookTitle
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

private enum class EditorTab(val label: String) {
    STRUCTURED("Parameters"),
    RAW_JSON("Raw JSON"),
}

/**
 * Full payload editor with two modes:
 *
 * - **Structured**: Key-value parameter editing with typed fields.
 * - **Raw JSON**: Multiline text field showing the full JSON representation.
 *
 * Edits in either mode produce a new [SduiPayload] via [onPayloadChange],
 * triggering live re-mapping in the canvas.
 *
 * Visual design matches ComposeBook's ControlsPanel styling.
 */
@Composable
fun PayloadEditor(
    payload: SduiPayload,
    onPayloadChange: (SduiPayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ComposeBookTheme.colors.backgroundElevated),
    ) {
        ComposeBookDivider()

        EditorTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        ComposeBookDivider()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            when (EditorTab.entries[selectedTab]) {
                EditorTab.STRUCTURED -> StructuredEditorContent(
                    payload = payload,
                    onPayloadChange = onPayloadChange,
                )

                EditorTab.RAW_JSON -> RawJsonEditorContent(
                    payload = payload,
                    onPayloadChange = onPayloadChange,
                )
            }
        }
    }
}

@Composable
private fun EditorTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        EditorTab.entries.forEachIndexed { index, tab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (selectedTab == index) ComposeBookTheme.colors.backgroundElevated
                        else ComposeBookTheme.colors.surface,
                    )
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                ComposeBookBodyText(
                    text = tab.label,
                    color = if (selectedTab == index) ComposeBookTheme.colors.textPrimary
                    else ComposeBookTheme.colors.textTertiary,
                )
            }
        }
    }
}

@Composable
private fun StructuredEditorContent(
    payload: SduiPayload,
    onPayloadChange: (SduiPayload) -> Unit,
) {
    if (payload.id.isNotEmpty()) {
        ComposeBookLabel(text = "ID")
        Spacer(modifier = Modifier.height(4.dp))
        ComposeBookBodyText(
            text = payload.id,
            color = ComposeBookTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (payload.parameters.isNotEmpty()) {
        PayloadParameterEditor(
            parameters = payload.parameters,
            onParameterChange = { key, value ->
                onPayloadChange(payload.withParam(key, value))
            },
        )
    } else {
        ComposeBookBodyText(
            text = "No parameters",
            color = ComposeBookTheme.colors.textTertiary,
        )
    }

    if (payload.children.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        ComposeBookDivider()
        Spacer(modifier = Modifier.height(12.dp))

        ComposeBookLabel(text = "CHILDREN (${payload.children.size})")
        Spacer(modifier = Modifier.height(8.dp))

        payload.children.forEachIndexed { index, child ->
            ChildPayloadSection(
                child = child,
                index = index,
                onChildChange = { updatedChild ->
                    val updatedChildren = payload.children.toMutableList()
                    updatedChildren[index] = updatedChild
                    onPayloadChange(payload.copy(children = updatedChildren))
                },
            )
        }
    }
}

@Composable
private fun ChildPayloadSection(
    child: SduiPayload,
    index: Int,
    onChildChange: (SduiPayload) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (expanded) ComposeBookTheme.colors.surfaceSelected
                    else ComposeBookTheme.colors.surface,
                )
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (expanded) {
                ChevronDownIcon(size = 14.dp)
            } else {
                ChevronRightIcon(size = 14.dp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            ComposeBookBodyText(
                text = "[$index] ${child.type}",
                color = if (expanded) ComposeBookTheme.colors.accent
                else ComposeBookTheme.colors.textPrimary,
            )
            if (child.id.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                ComposeBookBodyText(
                    text = "(${child.id})",
                    color = ComposeBookTheme.colors.textTertiary,
                    style = ComposeBookTheme.typography.bodySmall,
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)) {
                if (child.parameters.isNotEmpty()) {
                    PayloadParameterEditor(
                        parameters = child.parameters,
                        onParameterChange = { key, value ->
                            onChildChange(child.withParam(key, value))
                        },
                    )
                }
                if (child.children.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    child.children.forEachIndexed { childIndex, grandchild ->
                        ChildPayloadSection(
                            child = grandchild,
                            index = childIndex,
                            onChildChange = { updatedGrandchild ->
                                val updated = child.children.toMutableList()
                                updated[childIndex] = updatedGrandchild
                                onChildChange(child.copy(children = updated))
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RawJsonEditorContent(
    payload: SduiPayload,
    onPayloadChange: (SduiPayload) -> Unit,
) {
    var jsonText by remember(payload) {
        mutableStateOf(SduiPayloadSerializer.toJsonString(payload))
    }
    var parseError by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ComposeBookTheme.colors.surface)
                .padding(12.dp),
        ) {
            BasicTextField(
                value = jsonText,
                onValueChange = { newText ->
                    jsonText = newText
                    try {
                        val parsed = SduiPayloadParser.parseComponent(newText)
                        parseError = null
                        onPayloadChange(parsed)
                    } catch (_: Exception) {
                        parseError = "Invalid JSON"
                    }
                },
                textStyle = TextStyle(
                    color = ComposeBookTheme.colors.textPrimary,
                    fontFamily = ComposeBookTheme.typography.code.fontFamily,
                    fontSize = ComposeBookTheme.typography.code.fontSize,
                    lineHeight = ComposeBookTheme.typography.code.lineHeight,
                ),
                cursorBrush = SolidColor(ComposeBookTheme.colors.accent),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (parseError != null) {
            ComposeBookBodyText(
                text = parseError!!,
                color = ComposeBookTheme.colors.error,
                style = ComposeBookTheme.typography.bodySmall,
            )
        }
    }
}
