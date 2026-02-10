package com.ysraelmorenopkg.storybook.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.ui.theme.StorybookTheme

/**
 * Custom icons matching Storybook JS design.
 * Simple, minimal SVG-style icons.
 */

@Composable
fun ChevronDownIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = StorybookTheme.colors.textSecondary
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 2f
        val path = Path().apply {
            moveTo(size.toPx() * 0.25f, size.toPx() * 0.4f)
            lineTo(size.toPx() * 0.5f, size.toPx() * 0.65f)
            lineTo(size.toPx() * 0.75f, size.toPx() * 0.4f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ChevronRightIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = StorybookTheme.colors.textSecondary
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 2f
        val path = Path().apply {
            moveTo(size.toPx() * 0.4f, size.toPx() * 0.25f)
            lineTo(size.toPx() * 0.65f, size.toPx() * 0.5f)
            lineTo(size.toPx() * 0.4f, size.toPx() * 0.75f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ChevronUpIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = StorybookTheme.colors.textSecondary
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 2f
        val path = Path().apply {
            moveTo(size.toPx() * 0.25f, size.toPx() * 0.6f)
            lineTo(size.toPx() * 0.5f, size.toPx() * 0.35f)
            lineTo(size.toPx() * 0.75f, size.toPx() * 0.6f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun BookIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = StorybookTheme.colors.accent
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 1.5f
        val width = size.toPx()
        val height = size.toPx()
        
        // Book outline
        drawRect(
            color = color,
            topLeft = Offset(width * 0.2f, height * 0.15f),
            size = androidx.compose.ui.geometry.Size(width * 0.6f, height * 0.7f),
            style = Stroke(width = strokeWidth)
        )
        
        // Spine
        drawLine(
            color = color,
            start = Offset(width * 0.5f, height * 0.15f),
            end = Offset(width * 0.5f, height * 0.85f),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = StorybookTheme.colors.textSecondary
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 1.5f
        val centerX = size.toPx() / 2
        val centerY = size.toPx() / 2
        val radius = size.toPx() * 0.25f
        
        // Center circle
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth)
        )
        
        // Sliders
        for (i in 0 until 3) {
            val y = centerY + (i - 1) * size.toPx() * 0.2f
            drawLine(
                color = color,
                start = Offset(size.toPx() * 0.2f, y),
                end = Offset(size.toPx() * 0.8f, y),
                strokeWidth = strokeWidth
            )
        }
    }
}
