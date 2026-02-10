# Using Storybook Kotlin in Your App

Quick guide for integrating Storybook Kotlin into your existing app with your own theme.

## Basic Integration

### 1. Add Dependencies

```kotlin
// In your app/build.gradle.kts
dependencies {
    implementation(project(":composebook-core"))
    implementation(project(":composebook-compose"))
}
```

### 2. Create an Activity

```kotlin
class StorybookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val registry = InMemoryStoryRegistry()
        registerYourStories(registry)
        
        setContent {
            ComposeBookApp(
                registry = registry,
                theme = { content ->
                    YourAppTheme {  // Your existing theme!
                        content()
                    }
                }
            )
        }
    }
}
```

### 3. Create Your Stories

```kotlin
// In your design-system module
data class YourButtonProps(
    val label: String,
    val variant: ButtonVariant,
    val enabled: Boolean
)

val YourButtonStory = composeStory(
    id = "yourapp.button",
    name = "Components / Button",
    defaultProps = YourButtonProps("Click Me", ButtonVariant.Primary, true)
) {
    control(
        key = "label",
        control = TextControl("Label"),
        getter = { it.label },
        setter = { props, value -> props.copy(label = value) }
    )
    
    control(
        key = "variant",
        control = EnumControl("Variant", ButtonVariant.entries),
        getter = { it.variant },
        setter = { props, value -> props.copy(variant = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    render { props, _ ->
        YourButton(  // Your actual component
            label = props.label,
            variant = props.variant,
            enabled = props.enabled,
            onClick = { }
        )
    }
}
```

### 4. Register Stories

```kotlin
fun registerYourStories(registry: StoryRegistry) {
    registry.register(YourButtonStory)
    registry.register(YourCardStory)
    registry.register(YourInputStory)
    // ... all your components
}
```

## Theme Customization

The `theme` parameter accepts any `@Composable` wrapper:

```kotlin
// Example 1: Material 3 with custom colors
ComposeBookApp(
    registry = registry,
    theme = { content ->
        MaterialTheme(
            colorScheme = yourCustomColorScheme,
            typography = yourCustomTypography
        ) {
            content()
        }
    }
)

// Example 2: Your design system theme
ComposeBookApp(
    registry = registry,
    theme = { content ->
        YourDesignSystemTheme(
            colors = YourColors.Light,
            spacing = YourSpacing.Default
        ) {
            content()
        }
    }
)

// Example 3: Multiple theme layers
ComposeBookApp(
    registry = registry,
    theme = { content ->
        YourAppTheme {
            YourCustomProviders {
                content()
            }
        }
    }
)
```

## What Gets Themed?

When you provide a custom theme, these Storybook UI elements will use it:

- ✅ Stories header (background, text colors)
- ✅ Controls panel (background, text colors)
- ✅ Buttons, text fields, switches
- ✅ Dropdown menus
- ✅ Typography styles
- ✅ Surface colors
- ✅ Dividers

The **Canvas area** (where your components render) is separate and controlled by `StoryEnvironment`, so your components maintain their intended appearance.

## Best Practices

### 1. Separate Activity for Storybook

Create a dedicated activity for Storybook (not your main app):

```kotlin
// In debug builds only
class StorybookActivity : ComponentActivity() {
    // ... Storybook setup
}
```

### 2. Debug-Only Dependency

```kotlin
dependencies {
    debugImplementation(project(":composebook-core"))
    debugImplementation(project(":composebook-compose"))
}
```

### 3. Organized Story Files

```
your-app/
├── design-system/
│   ├── components/
│   │   ├── Button.kt
│   │   ├── Card.kt
│   │   └── Input.kt
│   └── stories/
│       ├── ButtonStory.kt
│       ├── CardStory.kt
│       └── InputStory.kt
```

### 4. Reusable Props

Make your component props the same as your story props:

```kotlin
// Component
@Composable
fun Button(props: ButtonProps) { }

// Story uses the same props!
val ButtonStory = composeStory(
    defaultProps = ButtonProps(...)
) { ... }
```

## Advanced: Multiple Registries

You can create different registries for different sections:

```kotlin
val componentsRegistry = InMemoryStoryRegistry()
val layoutsRegistry = InMemoryStoryRegistry()
val iconsRegistry = InMemoryStoryRegistry()

// Show different tabs or screens
ComposeBookApp(registry = componentsRegistry)
```

## Benefits

1. ✅ **Consistent theming** - Storybook matches your app's look
2. ✅ **No conflicts** - Your theme's colors, typography, spacing all work
3. ✅ **Familiar UI** - Developers see your design system in action
4. ✅ **Easy integration** - One parameter, no configuration
5. ✅ **Flexible** - Wrap with any Compose theme you want
