package com.ysraelmorenopkg.storybook.ui.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.compose.adapter.ComposeStory
import com.ysraelmorenopkg.storybook.core.control.BooleanControl
import com.ysraelmorenopkg.storybook.core.control.EnumControl
import com.ysraelmorenopkg.storybook.core.control.PropBinding
import com.ysraelmorenopkg.storybook.core.control.TextControl
import com.ysraelmorenopkg.storybook.core.runtime.StoryRuntimeState
import com.ysraelmorenopkg.storybook.ui.components.ChevronDownIcon
import com.ysraelmorenopkg.storybook.ui.components.ChevronUpIcon
import com.ysraelmorenopkg.storybook.ui.components.SettingsIcon
import com.ysraelmorenopkg.storybook.ui.components.StorybookBodyText
import com.ysraelmorenopkg.storybook.ui.components.StorybookDivider
import com.ysraelmorenopkg.storybook.ui.components.StorybookIconButton
import com.ysraelmorenopkg.storybook.ui.components.StorybookLabel
import com.ysraelmorenopkg.storybook.ui.components.StorybookTitle
import com.ysraelmorenopkg.storybook.ui.theme.StorybookTheme

/**
 * Modern controls panel matching Storybook JS design.
 */
@Composable
fun ControlsPanel(
    story: ComposeStory<*>?,
    runtimeState: StoryRuntimeState<*>?,
    onPropsChange: (Any) -> Unit,
    onCollapse: () -> Unit,
    expanded: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .background(StorybookTheme.colors.backgroundElevated)
    ) {
        StorybookDivider()
        
        // Header - Always visible
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCollapse() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsIcon()
                StorybookTitle("Controls")
            }
            
            if (expanded) ChevronDownIcon() else ChevronUpIcon()
        }
        
        // Controls List - Collapsible
        AnimatedVisibility(visible = expanded) {
            Column {
                StorybookDivider()
                
                story?.let { s ->
                    runtimeState?.let { state ->
                        @Suppress("UNCHECKED_CAST")
                        ControlsList(
                            story = s as ComposeStory<Any>,
                            currentProps = state.props,
                            onPropsChange = onPropsChange,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlsList(
    story: ComposeStory<Any>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val bindings = story.controls
    
    if (bindings.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            StorybookBodyText(
                text = "No controls available",
                color = StorybookTheme.colors.textTertiary
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .padding(
                    vertical = 16.dp
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = bindings,
                key = { it.key }
            ) { binding ->
                @Suppress("UNCHECKED_CAST")
                ControlItem(
                    binding = binding as PropBinding<Any, Any>,
                    currentProps = currentProps,
                    onPropsChange = onPropsChange
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ControlItem(
    binding: PropBinding<Any, Any>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StorybookLabel(text = binding.key.uppercase())
        
        when (val control = binding.control) {
            is TextControl -> {
                @Suppress("UNCHECKED_CAST")
                TextControlRenderer(
                    control = control,
                    binding = binding as PropBinding<Any, String>,
                    currentProps = currentProps,
                    onPropsChange = onPropsChange
                )
            }
            is BooleanControl -> {
                @Suppress("UNCHECKED_CAST")
                BooleanControlRenderer(
                    control = control,
                    binding = binding as PropBinding<Any, Boolean>,
                    currentProps = currentProps,
                    onPropsChange = onPropsChange
                )
            }
            is EnumControl<*> -> {
                EnumControlRendererAny(
                    control = control,
                    binding = binding,
                    currentProps = currentProps,
                    onPropsChange = onPropsChange
                )
            }
        }
    }
}

@Composable
private fun TextControlRenderer(
    control: TextControl,
    binding: PropBinding<Any, String>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit
) {
    var text by remember(currentProps) { mutableStateOf(binding.getter(currentProps)) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(StorybookTheme.colors.surface)
            .padding(12.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                onPropsChange(binding.setter(currentProps, newValue))
            },
            textStyle = TextStyle(
                color = StorybookTheme.colors.textPrimary,
                fontSize = StorybookTheme.typography.bodyMedium.fontSize
            ),
            cursorBrush = SolidColor(StorybookTheme.colors.accent),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BooleanControlRenderer(
    control: BooleanControl,
    binding: PropBinding<Any, Boolean>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit
) {
    var checked by remember(currentProps) { mutableStateOf(binding.getter(currentProps)) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StorybookBodyText(
            text = control.label,
            color = StorybookTheme.colors.textSecondary
        )
        
        Switch(
            checked = checked,
            onCheckedChange = { newValue ->
                checked = newValue
                onPropsChange(binding.setter(currentProps, newValue))
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = StorybookTheme.colors.accent,
                checkedTrackColor = StorybookTheme.colors.accent.copy(alpha = 0.5f),
                uncheckedThumbColor = StorybookTheme.colors.textTertiary,
                uncheckedTrackColor = StorybookTheme.colors.surface
            )
        )
    }
}

@Composable
private fun EnumControlRendererAny(
    control: EnumControl<*>,
    binding: PropBinding<Any, Any>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit
) {
    var selectedValue by remember(currentProps) { mutableStateOf(binding.getter(currentProps)) }
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Dropdown button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(StorybookTheme.colors.surface)
                .clickable { expanded = !expanded }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StorybookBodyText(
                    text = selectedValue.toString(),
                    color = StorybookTheme.colors.textPrimary
                )
                if (expanded) {
                    ChevronUpIcon()
                } else {
                    ChevronDownIcon()
                }
            }
        }
        
        // Options
        if (expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = control.values.toList(),
                    key = { it.toString() }
                ) { option ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (option == selectedValue) {
                                    StorybookTheme.colors.surfaceSelected
                                } else {
                                    StorybookTheme.colors.surface
                                }
                            )
                            .clickable {
                                selectedValue = option
                                onPropsChange(binding.setter(currentProps, option))
                                expanded = false
                            }
                            .padding(12.dp)
                    ) {
                        StorybookBodyText(
                            text = option.toString(),
                            color = if (option == selectedValue) {
                                StorybookTheme.colors.accent
                            } else {
                                StorybookTheme.colors.textSecondary
                            }
                        )
                    }
                }
            }
        }
    }
}
