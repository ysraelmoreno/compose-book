package com.ysraelmorenopkg.storybook.samples.sdui.render

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiButtonComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiCardComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiSpacerComponent
import com.ysraelmorenopkg.storybook.samples.sdui.components.SduiTextComponent

@Composable
fun SduiTextRender(component: SduiTextComponent, modifier: Modifier = Modifier) {
    Text(
        text = component.text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun SduiButtonRender(component: SduiButtonComponent, modifier: Modifier = Modifier) {
    val content: @Composable () -> Unit = { Text(component.label.text) }

    when (component.style) {
        SduiButtonComponent.ButtonStyle.PRIMARY -> Button(
            onClick = {},
            enabled = component.enabled,
            modifier = modifier,
            content = { content() },
        )

        SduiButtonComponent.ButtonStyle.SECONDARY -> OutlinedButton(
            onClick = {},
            enabled = component.enabled,
            modifier = modifier,
            content = { content() },
        )

        SduiButtonComponent.ButtonStyle.DESTRUCTIVE -> Button(
            onClick = {},
            enabled = component.enabled,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            ),
            content = { content() },
        )
    }
}

@Composable
fun SduiSpacerRender(component: SduiSpacerComponent) {
    Spacer(modifier = Modifier.height(component.heightDp.dp))
}

@Composable
fun SduiCardRender(
    component: SduiCardComponent,
    modifier: Modifier = Modifier,
    renderChild: @Composable (Any) -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = component.title.text,
                style = MaterialTheme.typography.titleMedium,
            )
            component.subtitle?.let { sub ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = sub.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (component.children.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                component.children.forEach { child -> renderChild(child) }
            }
        }
    }
}
