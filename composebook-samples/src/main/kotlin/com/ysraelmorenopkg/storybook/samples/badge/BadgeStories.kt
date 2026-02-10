package com.ysraelmorenopkg.storybook.samples.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.compose.adapter.composeStory
import com.ysraelmorenopkg.storybook.core.control.BooleanControl
import com.ysraelmorenopkg.storybook.core.control.EnumControl
import com.ysraelmorenopkg.storybook.core.control.TextControl

/**
 * Story for a Primary Badge.
 * 
 * Tests multiple controls: Text, Enum (Variant), Enum (Size), and 3 Booleans.
 */
val BadgePrimaryStory = composeStory(
    id = "badge.primary",
    name = "Badge / Primary",
    defaultProps = BadgeProps(
        text = "New",
        variant = BadgeVariant.Primary,
        size = BadgeSize.Medium,
        outlined = false,
        dismissible = false,
        showIcon = true
    )
) {
    control(
        key = "text",
        control = TextControl("Text", "Badge label text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "variant",
        control = EnumControl(
            label = "Variant",
            values = BadgeVariant.entries,
            description = "Color variant of the badge"
        ),
        getter = { it.variant },
        setter = { props, value -> props.copy(variant = value) }
    )
    
    control(
        key = "size",
        control = EnumControl(
            label = "Size",
            values = BadgeSize.entries,
            description = "Size of the badge"
        ),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    control(
        key = "outlined",
        control = BooleanControl("Outlined", "Show outline instead of filled"),
        getter = { it.outlined },
        setter = { props, value -> props.copy(outlined = value) }
    )
    
    control(
        key = "dismissible",
        control = BooleanControl("Dismissible", "Show close button"),
        getter = { it.dismissible },
        setter = { props, value -> props.copy(dismissible = value) }
    )
    
    control(
        key = "showIcon",
        control = BooleanControl("Show Icon", "Display icon before text"),
        getter = { it.showIcon },
        setter = { props, value -> props.copy(showIcon = value) }
    )
    
    render { props, _ ->
        BadgeComponent(props)
    }
}

/**
 * Story for an Error Badge with different defaults.
 */
val BadgeErrorStory = composeStory(
    id = "badge.error",
    name = "Badge / Error",
    defaultProps = BadgeProps(
        text = "Error!",
        variant = BadgeVariant.Error,
        size = BadgeSize.Large,
        outlined = true,
        dismissible = true,
        showIcon = true
    )
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "variant",
        control = EnumControl("Variant", BadgeVariant.entries),
        getter = { it.variant },
        setter = { props, value -> props.copy(variant = value) }
    )
    
    control(
        key = "size",
        control = EnumControl("Size", BadgeSize.entries),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    control(
        key = "outlined",
        control = BooleanControl("Outlined"),
        getter = { it.outlined },
        setter = { props, value -> props.copy(outlined = value) }
    )
    
    control(
        key = "dismissible",
        control = BooleanControl("Dismissible"),
        getter = { it.dismissible },
        setter = { props, value -> props.copy(dismissible = value) }
    )
    
    control(
        key = "showIcon",
        control = BooleanControl("Show Icon"),
        getter = { it.showIcon },
        setter = { props, value -> props.copy(showIcon = value) }
    )
    
    render { props, _ ->
        BadgeComponent(props)
    }
}

/**
 * Story for a Success Badge (small, no controls).
 */
val BadgeSuccessStory = composeStory(
    id = "badge.success",
    name = "Badge / Success",
    defaultProps = BadgeProps(
        text = "Success",
        variant = BadgeVariant.Success,
        size = BadgeSize.Small,
        outlined = false,
        dismissible = false,
        showIcon = false
    )
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "variant",
        control = EnumControl("Variant", BadgeVariant.entries),
        getter = { it.variant },
        setter = { props, value -> props.copy(variant = value) }
    )
    
    control(
        key = "size",
        control = EnumControl("Size", BadgeSize.entries),
        getter = { it.size },
        setter = { props, value -> props.copy(size = value) }
    )
    
    control(
        key = "outlined",
        control = BooleanControl("Outlined"),
        getter = { it.outlined },
        setter = { props, value -> props.copy(outlined = value) }
    )
    
    control(
        key = "dismissible",
        control = BooleanControl("Dismissible"),
        getter = { it.dismissible },
        setter = { props, value -> props.copy(dismissible = value) }
    )
    
    control(
        key = "showIcon",
        control = BooleanControl("Show Icon"),
        getter = { it.showIcon },
        setter = { props, value -> props.copy(showIcon = value) }
    )
    
    render { props, _ ->
        BadgeComponent(props)
    }
}

@Composable
private fun BadgeComponent(props: BadgeProps) {
    val colors = when (props.variant) {
        BadgeVariant.Primary -> Pair(Color(0xFF6200EE), Color.White)
        BadgeVariant.Secondary -> Pair(Color(0xFF03DAC5), Color.Black)
        BadgeVariant.Success -> Pair(Color(0xFF4CAF50), Color.White)
        BadgeVariant.Error -> Pair(Color(0xFFB00020), Color.White)
        BadgeVariant.Warning -> Pair(Color(0xFFFF9800), Color.Black)
        BadgeVariant.Info -> Pair(Color(0xFF2196F3), Color.White)
    }
    
    val (backgroundColor, textColor) = if (props.outlined) {
        Pair(Color.Transparent, colors.first)
    } else {
        colors
    }
    
    val padding = when (props.size) {
        BadgeSize.Small -> 4.dp
        BadgeSize.Medium -> 8.dp
        BadgeSize.Large -> 12.dp
    }
    
    val textStyle = when (props.size) {
        BadgeSize.Small -> MaterialTheme.typography.labelSmall
        BadgeSize.Medium -> MaterialTheme.typography.labelMedium
        BadgeSize.Large -> MaterialTheme.typography.labelLarge
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = if (props.outlined) BorderStroke(1.dp, colors.first) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = padding * 2, vertical = padding),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (props.showIcon) {
                Text(
                    text = "●",
                    color = textColor,
                    style = textStyle
                )
            }
            
            Text(
                text = props.text,
                color = textColor,
                style = textStyle
            )
            
            if (props.dismissible) {
                Text(
                    text = "✕",
                    color = textColor,
                    style = textStyle,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
