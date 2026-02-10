# ComposeBook Kotlin

A Kotlin-native library for showcasing Design System components, inspired by Storybook JS.

## Status: MVP (Minimum Viable Product)

This is an MVP implementation focused on **validating the core conceptual model**. The goal is not visual polish, but proving that:
- The core is solid and UI-independent
- The Story API is usable day-to-day
- Components can be rendered and edited predictably

## What This Is

A library for:
- **Design System component showcasing**
- **UI state exploration** - See all variations of a component
- **Internal product team usage** - Shared component library

## What This Is NOT

- ❌ A visual regression testing tool
- ❌ A Kotlin port of Storybook JS
- ❌ A navigation or routing framework
- ❌ A production-ready library (yet - this is MVP)

## UI Options

ComposeBook provides two UI implementations:

### 1. Modern Custom Design System (`composebook-ui` module)
- Custom design system inspired by Storybook JS 7+
- Dark/Light theme support
- Professional developer tool aesthetics
- No Material Design dependencies

### 2. Classic Material Theme (`composebook-compose` module)
- Traditional Material Design 3 look
- Familiar Android UI patterns
- Lighter weight option

## Architecture

### Modules

```
composebook-kotlin/
├── composebook-core/       # Pure Kotlin, UI-independent core
├── composebook-compose/    # Jetpack Compose adapter
├── composebook-ui/         # Modern UI with custom design system
└── composebook-samples/    # Example stories
```

### Core Principles

1. **Core is UI-independent** - No Compose, no Android, no UI frameworks
2. **Stories are data, not screens** - Describes what to render, not how to navigate
3. **No reflection magic** - Everything is explicit
4. **Immutability by default** - All state changes create new instances
5. **Minimal public API** - Open-source requires stability

## Quick Start

### 1. Add Dependencies

```kotlin
dependencies {
    implementation(project(":composebook-core"))
    implementation(project(":composebook-compose"))
}
```

### 2. Create Props

```kotlin
data class ButtonProps(
    val text: String,
    val enabled: Boolean
)
```

### 3. Create a Story

```kotlin
import com.ysraelmorenopkg.storybook.compose.adapter.composeStory
import com.ysraelmorenopkg.storybook.core.control.TextControl
import com.ysraelmorenopkg.storybook.core.control.BooleanControl

val ButtonStory = composeStory(
    id = "button.primary",
    name = "Button / Primary",
    defaultProps = ButtonProps("Click Me", true)
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl("Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    render { props, _ ->
        Button(
            onClick = { },
            enabled = props.enabled
        ) {
            Text(props.text)
        }
    }
}
```

### 4. Register Stories

```kotlin
val registry = InMemoryStoryRegistry()
registry.register(ButtonStory)
```

### 5. Launch ComposeBook

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val registry = InMemoryStoryRegistry()
        registerSampleStories(registry)
        
        setContent {
            // Modern UI with custom design system (Recommended)
            ModernComposeBookApp(
                registry = registry,
                darkTheme = true
            )
            
            // OR: Classic UI with Material Theme
            ComposeBookApp(
                registry = registry,
                theme = { content ->
                    MyAppTheme {
                        content()
                    }
                }
            )
        }
    }
}
```

## UI Options

### Modern UI (Recommended)

Professional interface inspired by Storybook JS 7+ with:
- Custom design system (no Material Theme dependency)
- Dark theme by default
- Retractable sidebar and controls panel
- Professional color palette and typography
- Custom icons and components

```kotlin
import com.ysraelmorenopkg.storybook.ui.app.ModernComposeBookApp

ModernComposeBookApp(
    registry = registry,
    darkTheme = true // or false for light theme
)
```

**Features:**
- 🎨 Custom color system (#1A1A1A background, #029CFD accent)
- 📝 Optimized typography for technical content
- 📚 Collapsible story categories
- ⚙️ Modern control renderers
- 🔄 Smooth animations

See [Modern UI Documentation](docs/modern_ui_implementation.md) for details.

### Classic UI

Simple Material Theme-based interface:

```kotlin
import com.ysraelmorenopkg.storybook.compose.app.ComposeBookApp

ComposeBookApp(
    registry = registry,
    theme = { content ->
        MyAppTheme { content() }
    }
)
```

## Customization

### Custom Theme

The ComposeBook UI can be themed to match your app's design system:

```kotlin
// Use your app's theme for the ComposeBook UI
ComposeBookApp(
    registry = registry,
    theme = { content ->
        MyDesignSystemTheme(
            darkTheme = false,
            dynamicColor = true
        ) {
            content()
        }
    }
)
```

**What gets themed:**
- Stories header background and text colors
- Controls panel background and text colors
- Dropdown menus, text fields, switches
- Dividers and surface colors
- Typography styles

**Note:** The canvas area renders your components with their own theme (via StoryEnvironment), so your components maintain their original appearance while the ComposeBook UI uses your theme.

## MVP Features

### ✅ Included

- Pure Kotlin core module
- Jetpack Compose adapter
- Manual story registration
- Basic controls (Text, Boolean, Enum)
- Theme switching (Light/Dark)
- 3-panel UI (Stories, Canvas, Controls)
- Sample implementations (Button, Card)

### ❌ Not Included (Post-MVP)

- State persistence
- Visual snapshots
- Code generation
- Gradle plugin
- Auto-discovery of stories
- Multiplatform support
- Advanced controls (Color, Number, Date)
- Deep linking
- UI polish

## Success Criteria

The MVP is successful if:

1. ✅ An external dev can create a story without reading internal code
2. ✅ Props can be modified at runtime
3. ✅ The same component supports multiple variations
4. ✅ The core has zero Compose/Android dependencies
5. ✅ Zero reflection usage

## Project Structure

### composebook-core

Pure Kotlin module with clean architecture:

```
composebook-core/
├── api/          # Story, StoryContext
├── model/        # StoryId
├── control/      # PropControl, PropBinding
├── environment/  # StoryEnvironment, ThemeMode
├── registry/     # StoryRegistry, InMemoryStoryRegistry
└── runtime/      # StoryRuntimeState
```

**Key constraint**: No UI framework dependencies.

### composebook-compose

Jetpack Compose implementation:

```
composebook-compose/
├── adapter/      # ComposeStory, ComposeStoryBuilder
├── canvas/       # StoryCanvas
├── controls/     # Control renderers (TextField, Switch, etc.)
└── app/          # Classic ComposeBookApp
```

### composebook-ui

Modern UI with custom design system:

```
composebook-ui/
├── theme/        # ComposeBookColors, ComposeBookTypography, ComposeBookTheme
├── components/   # Custom buttons, text, icons, dividers
└── app/          # ModernComposeBookApp, ControlsPanel
```

## Examples

See `composebook-samples/` for complete examples:
- Button (Primary, Disabled)
- Card (Default, With Image)

## Documentation

- [Architecture & Contracts](docs/storybook_kotlin_arquitetura_e_contratos.md)
- [MVP Specification](docs/storybook_kotlin_especificacao_do_mvp.md)
- [Technical Backlog](docs/storybook_kotlin_backlog_tecnico_do_mvp.md)
- [Modern UI Implementation](docs/modern_ui_implementation.md)
- [Visual Comparison](docs/visual_comparison.md)
- [Customization Guide](docs/customization_guide.md)

## Building

```bash
# Build core (pure Kotlin)
./gradlew :composebook-core:build

# Build Compose adapter
./gradlew :composebook-compose:build

# Build samples
./gradlew :composebook-samples:build

# Build and run app
./gradlew :app:assembleDebug
```

## Requirements

- JDK 17+
- Android SDK 33+
- Kotlin 2.0+

## License

TBD (Open-source license to be determined post-MVP)

## Contributing

This is currently an MVP for internal validation. External contributions will be welcome once the API is stable and the repository is public.

## Next Steps

After MVP validation:
1. Freeze core API
2. Mark experimental APIs
3. Collect feedback
4. Plan roadmap based on real usage

**Without validation, there is no roadmap.**
