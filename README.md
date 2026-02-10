# ComposeBook

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Compose-1.5+-blue?style=for-the-badge&logo=jetpackcompose)
![Android](https://img.shields.io/badge/Android-8.0+-green?style=for-the-badge&logo=android)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)

**A Kotlin-native library for showcasing Design System components and exploring UI variations**

[Features](#-features) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Architecture](#-architecture) • [Contributing](#-contributing)

</div>

---

## 📖 About

**ComposeBook** is an open-source, Kotlin-native library designed specifically for building component showcases and exploring UI variations in Jetpack Compose applications. Inspired by Storybook JS but built from the ground up for Kotlin, ComposeBook provides a clean, type-safe API for documenting and testing your Design System components in isolation.

Unlike ports or adaptations, ComposeBook embraces Kotlin's strengths: immutability, explicit APIs, and a UI-independent core that works seamlessly with Jetpack Compose.

### 🎯 What Problem Does It Solve?

When building a Design System or component library, you need:
- **Isolated component development** - See components without app complexity
- **State exploration** - Test all variations (loading, error, different props)
- **Documentation** - Visual documentation that stays in sync with code
- **Team collaboration** - Shared component library for designers and developers
- **Quality assurance** - Catch visual regressions early

ComposeBook solves these by providing a structured way to define, organize, and showcase your components.

### ✨ Why ComposeBook?

- **🎨 Two UI Options**: Modern custom design system OR classic Material Theme
- **🏗️ UI-Independent Core**: Works without Compose - future-proof architecture
- **📝 Explicit Story API**: No reflection magic - everything is clear and type-safe
- **🎭 Manual Registration**: Full control over what you showcase
- **🔒 Immutable by Default**: State changes create new instances - predictable behavior
- **📦 Minimal Public API**: Stable, focused API designed for open-source usage
- **🚀 Kotlin-First**: Built specifically for Kotlin - not a JavaScript port

---

## 🎨 Features

### Core Features

- **Story Definition DSL**: Clean, readable syntax for defining component variations
- **Type-Safe Prop Controls**: Edit component props in real-time with type safety
- **Hierarchical Organization**: Group stories by category for easy navigation
- **Runtime State Management**: Modify props and see changes instantly
- **Environment Context**: Test components in different theme/locale configurations
- **Collapsible Panels**: Maximize canvas space with retractable controls

### Control Types

- **TextControl**: Edit string properties
- **BooleanControl**: Toggle boolean properties
- **EnumControl**: Select from predefined values
- _More controls in future releases_

### Two UI Implementations

#### Modern UI (`composebook-ui`)
- Custom design system inspired by Storybook JS 7+
- Dark/Light theme support
- Professional developer tool aesthetics
- Vertical 3-panel layout (Stories | Canvas | Controls)

#### Classic UI (`composebook-compose`)
- Material Design 3 theme
- Familiar Android patterns
- Lightweight alternative

---

## 🚀 Quick Start

### Prerequisites

- **Kotlin**: 2.0 or higher
- **Gradle**: 8.0 or higher
- **Android**: minSdk 25 (Android 8.0) or higher
- **Jetpack Compose**: 1.5 or higher

### Installation

Add ComposeBook to your project:

```kotlin
// settings.gradle.kts
include(":composebook-core")
include(":composebook-compose")
include(":composebook-ui")      // Optional: Modern UI
include(":composebook-samples")  // Optional: Example stories
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(project(":composebook-core"))
    implementation(project(":composebook-compose"))
    
    // Optional: Modern UI with custom design system
    implementation(project(":composebook-ui"))
}
```

### Your First Story

#### 1. Define Props

```kotlin
data class ButtonProps(
    val text: String,
    val enabled: Boolean,
    val variant: ButtonVariant
)

enum class ButtonVariant { Primary, Secondary, Destructive }
```

#### 2. Create the Story

```kotlin
val PrimaryButtonStory = story(
    id = StoryId("button.primary"),
    name = "Button / Primary",
    defaultProps = ButtonProps(
        text = "Click Me",
        enabled = true,
        variant = ButtonVariant.Primary
    )
) {
    // Define editable controls
    control(
        key = "text",
        control = TextControl(label = "Text"),
        getter = { it.text },
        setter = { props, value -> props.copy(text = value) }
    )
    
    control(
        key = "enabled",
        control = BooleanControl(label = "Enabled"),
        getter = { it.enabled },
        setter = { props, value -> props.copy(enabled = value) }
    )
    
    control(
        key = "variant",
        control = EnumControl(
            label = "Variant",
            values = ButtonVariant.entries.toSet()
        ),
        getter = { it.variant },
        setter = { props, value -> props.copy(variant = value) }
    )
    
    // Define how to render the component
    render { props, _ ->
        Button(
            text = props.text,
            enabled = props.enabled,
            variant = props.variant
        )
    }
}
```

#### 3. Register and Launch

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create registry and register stories
        val registry = InMemoryStoryRegistry()
        registry.register(PrimaryButtonStory)
        
        setContent {
            // Option 1: Modern UI
            ModernComposeBookApp(
                registry = registry,
                darkTheme = true
            )
            
            // Option 2: Classic Material UI
            // ComposeBookApp(registry = registry)
        }
    }
}
```

That's it! 🎉 Run your app and explore your component stories.

---

## 📚 Documentation

### Core Concepts

#### Stories
A **Story** represents a single variation of a component. It defines:
- **Props**: The component's configuration
- **Controls**: How to edit props at runtime
- **Render**: How to display the component

```kotlin
val story = story(id, name, defaultProps) {
    control(...)
    render { props, context -> /* Composable */ }
}
```

#### Registry
The **Registry** stores and organizes all your stories:

```kotlin
val registry = InMemoryStoryRegistry()
registry.register(ButtonStory)
registry.register(CardStory)
registry.register(BadgeStory)

// Retrieve stories
val allStories = registry.getAllStories()
val buttonStory = registry.getStory(StoryId("button.primary"))
```

#### Controls & Bindings
**Controls** let you edit props in the UI:

```kotlin
// Text control
control(
    key = "label",
    control = TextControl("Label"),
    getter = { it.label },
    setter = { props, value -> props.copy(label = value) }
)

// Boolean control
control(
    key = "visible",
    control = BooleanControl("Visible"),
    getter = { it.visible },
    setter = { props, value -> props.copy(visible = value) }
)

// Enum control
control(
    key = "size",
    control = EnumControl("Size", Size.entries.toSet()),
    getter = { it.size },
    setter = { props, value -> props.copy(size = value) }
)
```

#### Story Environment
**Environment** provides context like theme and locale:

```kotlin
data class StoryEnvironment(
    val theme: ThemeMode,
    val locale: Locale,
    val device: DeviceProfile
)

// Access in render
render { props, context ->
    val isDarkMode = context.environment.theme == ThemeMode.DARK
    // Render based on environment
}
```

### Advanced Usage

#### Organizing Stories by Category

Use naming convention `"Category / Story Name"`:

```kotlin
story(id = "button.primary", name = "Button / Primary", ...)
story(id = "button.secondary", name = "Button / Secondary", ...)
story(id = "card.default", name = "Card / Default", ...)
story(id = "card.image", name = "Card / With Image", ...)
```

Stories are automatically grouped in the UI.

#### Custom Story Registration

Create a dedicated function for registering stories:

```kotlin
fun registerButtonStories(registry: StoryRegistry) {
    registry.register(PrimaryButtonStory)
    registry.register(SecondaryButtonStory)
    registry.register(DestructiveButtonStory)
}

fun registerCardStories(registry: StoryRegistry) {
    registry.register(DefaultCardStory)
    registry.register(ImageCardStory)
}

// In your app
val registry = InMemoryStoryRegistry()
registerButtonStories(registry)
registerCardStories(registry)
```

---

## 🏗️ Architecture

### Module Structure

```
ComposeBookCompose/
├── composebook-core/        # Pure Kotlin - UI-independent
│   ├── Story API
│   ├── StoryRegistry
│   ├── PropControl & PropBinding
│   ├── StoryEnvironment
│   └── StoryRuntimeState
│
├── composebook-compose/     # Jetpack Compose adapter
│   ├── ComposeStory wrapper
│   ├── StoryCanvas renderer
│   └── Classic Material UI
│
├── composebook-ui/          # Modern custom design system
│   ├── ComposeBookTheme (custom)
│   ├── ModernComposeBookApp
│   ├── Custom components
│   └── Control renderers
│
└── composebook-samples/     # Example stories
    ├── Button stories
    ├── Card stories
    └── Badge stories
```

### Architectural Principles

#### 1. Core is UI-Independent
The `composebook-core` module has **zero UI dependencies**:

```kotlin
// ✅ ALLOWED in core
interface Story<Props : Any> {
    fun render(props: Props, context: StoryContext)
}

// ❌ NOT ALLOWED in core
import androidx.compose.runtime.Composable
import android.view.View
```

**Why?** Future-proof for multiplatform support (iOS, Desktop, Web).

#### 2. Stories are Data, Not Screens
Stories describe **what** to render, not navigation:

```kotlin
// ✅ GOOD - Story renders content
val story = story(...) {
    render { props, _ ->
        Button(text = props.text)
    }
}

// ❌ BAD - Story handling navigation
val story = story(...) {
    render { props, _ ->
        navigateTo(ButtonScreen)
    }
}
```

#### 3. No Reflection Magic
Everything is explicit - no automatic discovery:

```kotlin
// ❌ BAD - Reflection-based
@Story
class ButtonStory { ... }

// ✅ GOOD - Explicit registration
val story = story(...) { ... }
registry.register(story)
```

**Why?** Works in Kotlin/Native, smaller binary size, predictable behavior.

#### 4. Immutability by Default
State changes create new instances:

```kotlin
// ❌ BAD - Mutable props
class ButtonProps(var text: String, var enabled: Boolean)

// ✅ GOOD - Immutable props
data class ButtonProps(val text: String, val enabled: Boolean)
```

#### 5. Minimal Public API
Keep public APIs small and stable:

```kotlin
// Public API (stable)
- Story
- StoryId
- StoryBuilder
- PropControl
- PropBinding
- StoryRegistry
- StoryEnvironment

// Everything else is internal or private
```

---

## 🎨 Customization

### Custom Theme

Create your own theme for `ModernComposeBookApp`:

```kotlin
// Define custom colors
val MyCustomColors = ComposeBookColors(
    background = Color(0xFF1E1E1E),
    accent = Color(0xFFFF6B6B),
    // ... other colors
)

// Use in app
ComposeBookTheme(darkTheme = true) {
    // Override colors if needed
    // Your custom UI here
}
```

### Custom Controls

Create custom control types:

```kotlin
// Define custom control
data class NumberControl(
    override val label: String,
    val min: Int,
    val max: Int
) : PropControl<Int>

// Create renderer
@Composable
fun NumberControlRenderer(
    control: NumberControl,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = control.min.toFloat()..control.max.toFloat()
    )
}
```

---

## 🧪 Examples

Check the `composebook-samples` module for complete examples:

### Button Stories
- Primary, Secondary, Destructive variants
- Enabled/disabled states
- Custom text content

### Card Stories
- Default card layout
- Card with image
- Card with actions

### Badge Stories
- Different badge styles
- Number badges
- Status badges

### Running Examples

```bash
# Clone the repository
git clone https://github.com/yourusername/composebook.git

# Open in Android Studio
open composebook/

# Run the app module to see examples
./gradlew :app:installDebug
```

---

## 🤝 Contributing

We welcome contributions! ComposeBook is an MVP focused on validating the core concept. Here's how you can help:

### Areas for Contribution

- 🐛 **Bug Fixes**: Found a bug? Submit a PR with a fix
- 📚 **Documentation**: Improve docs, add examples, fix typos
- ✨ **New Control Types**: Add NumberControl, ColorControl, DateControl
- 🎨 **UI Improvements**: Enhance the Modern UI design
- 🧪 **Tests**: Add unit tests and UI tests
- 🌍 **Localization**: Help translate the UI

### Development Setup

1. **Fork the repository**
2. **Clone your fork**:
   ```bash
   git clone https://github.com/yourusername/composebook.git
   cd composebook
   ```
3. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```
4. **Make your changes** and test thoroughly
5. **Commit with clear messages**:
   ```bash
   git commit -m "feat: add NumberControl for numeric props"
   ```
6. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```
7. **Open a Pull Request** with a clear description

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused
- Write tests for new features

### Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `refactor:` Code refactoring
- `test:` Adding tests
- `chore:` Maintenance tasks

---

## 🗺️ Roadmap

### MVP (Current)
- ✅ Core Story API
- ✅ Basic controls (Text, Boolean, Enum)
- ✅ Manual registration
- ✅ Two UI options (Modern + Classic)
- ✅ Hierarchical organization

### Phase 2 (Planned)
- ⏳ More control types (Number, Color, Date)
- ⏳ State persistence
- ⏳ Visual snapshot testing
- ⏳ Gradle plugin for code generation
- ⏳ Deep linking to stories

### Phase 3 (Future)
- 🔮 Multiplatform support (iOS, Desktop, Web)
- 🔮 Auto-discovery of stories
- 🔮 Interactive documentation generation
- 🔮 Visual regression testing integration

**Note**: MVP is focused on validation. Features are added based on real user feedback.

---

## 📄 License

```
MIT License

Copyright (c) 2024 ComposeBook Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **Storybook JS**: Inspiration for the concept and design
- **Jetpack Compose Team**: Amazing UI toolkit
- **Kotlin Community**: For the incredible language and ecosystem

---

## 📞 Support & Community

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/yourusername/composebook/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/yourusername/composebook/discussions)
- 📧 **Email**: composebook@example.com
- 🐦 **Twitter**: [@composebook](https://twitter.com/composebook)

---

## 🌟 Show Your Support

If ComposeBook helps your project, consider:
- ⭐ **Star this repository**
- 🐦 **Tweet about it**
- 📝 **Write a blog post**
- 💬 **Share with your team**

---

<div align="center">

**Built with ❤️ for the Kotlin & Compose community**

[⬆ Back to Top](#composebook)

</div>
