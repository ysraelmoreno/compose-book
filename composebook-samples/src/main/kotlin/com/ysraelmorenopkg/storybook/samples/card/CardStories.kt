package com.ysraelmorenopkg.storybook.samples.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ysraelmorenopkg.composebook.ui.adapter.composeStory
import com.ysraelmorenopkg.composebook.core.control.BooleanControl
import com.ysraelmorenopkg.composebook.core.control.TextControl

/**
 * Story for a default card without image.
 */
val CardDefaultStory = composeStory(
    id = "card.default",
    name = "Card / Default",
    defaultProps = CardProps(
        title = "Card Title",
        description = "This is a card description with some content.",
        hasImage = false
    )
) {
    control(
        key = "title",
        control = TextControl("Title"),
        getter = { it.title },
        setter = { props, value -> props.copy(title = value) }
    )
    
    control(
        key = "description",
        control = TextControl("Description"),
        getter = { it.description },
        setter = { props, value -> props.copy(description = value) }
    )
    
    control(
        key = "hasImage",
        control = BooleanControl("Has Image"),
        getter = { it.hasImage },
        setter = { props, value -> props.copy(hasImage = value) }
    )
    
    render { props, _ ->
        CardComponent(props)
    }
}

/**
 * Story for a card with image.
 */
val CardWithImageStory = composeStory(
    id = "card.with-image",
    name = "Card / With Image",
    defaultProps = CardProps(
        title = "Card with Image",
        description = "This card includes an image placeholder.",
        hasImage = true
    )
) {
    control(
        key = "title",
        control = TextControl("Title"),
        getter = { it.title },
        setter = { props, value -> props.copy(title = value) }
    )
    
    control(
        key = "description",
        control = TextControl("Description"),
        getter = { it.description },
        setter = { props, value -> props.copy(description = value) }
    )
    
    control(
        key = "hasImage",
        control = BooleanControl("Has Image"),
        getter = { it.hasImage },
        setter = { props, value -> props.copy(hasImage = value) }
    )
    
    render { props, _ ->
        CardComponent(props)
    }
}

@Composable
private fun CardComponent(props: CardProps) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (props.hasImage) {
                // Placeholder for image
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    // Image placeholder
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Text(
                text = props.title,
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = props.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
