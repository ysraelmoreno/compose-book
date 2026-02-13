package com.ysraelmorenopkg.composebook.ui.app.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.ysraelmorenopkg.composebook.ui.components.ComposeBookBodyText
import com.ysraelmorenopkg.composebook.ui.theme.ComposeBookTheme

/**
 * Custom search text field with placeholder and theming.
 * 
 * @param value Current text value
 * @param onValueChange Callback when text changes
 * @param focusRequester Focus requester for auto-focus
 * @param modifier Optional modifier
 */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = ComposeBookTheme.colors.textPrimary
        ),
        cursorBrush = SolidColor(ComposeBookTheme.colors.accent),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    ComposeBookBodyText(
                        text = "Search stories...",
                        color = ComposeBookTheme.colors.textTertiary
                    )
                }
                innerTextField()
            }
        }
    )
}
