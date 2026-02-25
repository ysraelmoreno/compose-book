# ComposeBook Plugin Architecture — Expansion Document

> **Status**: Proposal (pre-validation)
> **Audience**: Core maintainers, future contributors
> **Prerequisite**: MVP must be validated before any implementation begins

---

## 1. Why This Document Exists Now

The MVP scope explicitly excludes plugins. So why write this?

Because the core API will freeze after MVP validation. Certain architectural decisions — particularly `sealed interface PropControl` and the absence of lifecycle hooks — become **irreversible** after that point. This document identifies which doors must stay open, not which rooms to build.

**This is NOT a feature spec. It's a constraint analysis.**

---

## 2. The Cost of a Plugin System

Before discussing what plugins *enable*, let's be honest about what they *cost*.

| Cost | Impact | Severity |
|------|--------|----------|
| `PropControl` must become unsealed | Loses exhaustive `when` guarantees; control dispatch becomes runtime-checked | **High** |
| API surface grows | More interfaces = more to document, test, and maintain forever | **High** |
| Type safety trade-offs | Dynamic dispatch replaces static dispatch in control rendering | **Medium** |
| Plugin ordering/conflicts | Plugins may interfere with each other; need conflict resolution | **Medium** |
| Lifecycle complexity | Hooks create implicit execution order dependencies | **Medium** |
| Documentation burden | Every extension point needs examples and contracts | **Low** |

**If these costs aren't acceptable, stop here.** Targeted, one-off extensions (e.g., just adding `ColorControl` to core) may be cheaper than a full plugin system.

---

## 3. Plugin Categories

Not all plugins are the same. The architecture must distinguish between extension *types* because they touch different layers.

### 3.1 Control Plugins
**What**: Add new `PropControl` types (Color, Number, Date, Range, etc.)
**Touches**: `composebook-core` (control model) + `composebook-ui` (control renderer)
**Example**: A `ColorControl` that renders a color picker in the controls panel

### 3.2 Decorator Plugins
**What**: Wrap story rendering with additional behavior (padding, theming, providers)
**Touches**: `composebook-core` (story pipeline) + `composebook-ui` (rendering)
**Example**: An accessibility decorator that adds contrast ratio overlays

### 3.3 Panel Plugins
**What**: Add new tabs/panels to the ComposeBook UI
**Touches**: `composebook-ui` only
**Example**: An "Actions" panel that logs callback invocations

### 3.4 Lifecycle Plugins
**What**: React to story events (selection, prop changes, registration)
**Touches**: `composebook-core` (hooks) + optionally `composebook-ui`
**Example**: Analytics plugin that tracks which stories are viewed most

### 3.5 Source Plugins
**What**: Provide stories from external sources (JSON, server, code generation)
**Touches**: `composebook-core` (registry)
**Example**: Load stories from a remote design system API

---

## 4. Architectural Seams — Current State

The core has both natural extension points and hard barriers. A plugin system must work *with* the existing seams, not against them.

### Already Extensible (No Core Changes Needed)

| Seam | Mechanism | Plugin Potential |
|------|-----------|-----------------|
| `StoryRegistry` | Interface | Source plugins can implement custom registries |
| `StoryContext` | Interface | Lifecycle plugins can enrich context with metadata |
| `Story` | Interface | Decorator plugins can wrap stories |
| `StoryEnvironment` | Data class with `copy()` | Environment dimensions can be extended via composition |

### Blocked (Require Core Changes)

| Barrier | Current State | Required Change | Cost |
|---------|--------------|-----------------|------|
| `PropControl` | `sealed interface` | Unseal to `interface` | Loss of exhaustive `when` |
| Control rendering | Hardcoded `when` in `ControlsPanel` | Dynamic renderer registry | Runtime dispatch, no compile-time safety |
| Story metadata | Fixed fields only | Add `metadata: Map<String, Any>` or typed extensions | API surface growth |
| Lifecycle hooks | None | Add hook interfaces to `StoryRegistry` and runtime | Implicit ordering, complexity |
| `StoryBuilder.build()` | `internal` | Plugin participation in build pipeline | API surface, build order conflicts |
| UI panel system | Fixed tabs (Canvas, Docs) | Dynamic panel registry | UI complexity |

---

## 5. Proposed Architecture

### 5.1 Core Layer — `composebook-core`

#### 5.1.1 Unseal PropControl

This is the most consequential change. Without it, control plugins are impossible.

```kotlin
// BEFORE (MVP)
sealed interface PropControl<T : Any> {
    val label: String
    val description: String?
}

// AFTER (Plugin-ready)
interface PropControl<T : Any> {
    val label: String
    val description: String?
}
```

**Trade-off**: Every `when` statement that matches on `PropControl` subtypes loses exhaustive checking. The UI must handle unknown control types gracefully (fallback renderer).

**Mitigation**: Keep built-in controls as a known set. Provide a `BuiltInControl` sealed subtype for core controls, so the UI can still exhaustively match core types and fall through to a plugin registry for unknown ones.

```kotlin
interface PropControl<T : Any> {
    val label: String
    val description: String?
}

sealed interface BuiltInControl<T : Any> : PropControl<T>

data class TextControl(
    override val label: String,
    override val description: String? = null
) : BuiltInControl<String>

data class BooleanControl(
    override val label: String,
    override val description: String? = null
) : BuiltInControl<Boolean>

data class EnumControl<T : Enum<T>>(
    override val label: String,
    val values: Array<T>,
    override val description: String? = null
) : BuiltInControl<T>
```

This preserves exhaustive matching for built-in controls while allowing external implementations.

#### 5.1.2 Plugin Interface

A single entry point for plugin installation.

```kotlin
interface ComposeBookPlugin {
    val id: String
    val name: String

    fun install(scope: PluginScope)
}
```

`PluginScope` provides the registration surface without exposing internal state:

```kotlin
interface PluginScope {
    fun addDecorator(decorator: StoryDecorator)
    fun addLifecycleHook(hook: StoryLifecycleHook)
    fun <T : Any> registerControl(
        controlClass: KClass<out PropControl<T>>,
        metadata: ControlMetadata<T>
    )
}
```

#### 5.1.3 Story Decorators

Decorators wrap story rendering. They compose, they don't replace.

```kotlin
fun interface StoryDecorator {
    fun decorate(
        story: Story<*>,
        context: StoryContext,
        proceed: () -> Unit
    )
}
```

The `proceed` parameter creates a chain — each decorator calls `proceed()` to invoke the next one (or the actual story). This is the interceptor pattern, familiar from OkHttp and Ktor.

```kotlin
// Example: Padding decorator
class PaddingDecorator(private val dp: Int) : StoryDecorator {
    override fun decorate(story: Story<*>, context: StoryContext, proceed: () -> Unit) {
        // UI layer applies padding around proceed()
        proceed()
    }
}
```

**Note**: The core decorator is UI-agnostic. The Compose layer provides a `ComposeStoryDecorator` adapter that works with `@Composable` content.

#### 5.1.4 Lifecycle Hooks

Minimal set of hooks. Start small, expand based on real demand.

```kotlin
interface StoryLifecycleHook {
    fun onStoryRegistered(story: Story<*>) {}
    fun onStorySelected(story: Story<*>) {}
    fun onPropsChanged(story: Story<*>, oldProps: Any, newProps: Any) {}
    fun onEnvironmentChanged(old: StoryEnvironment, new: StoryEnvironment) {}
}
```

All methods have default empty implementations — plugins override only what they need.

#### 5.1.5 Story Metadata Extension

Instead of adding arbitrary fields to `Story`, use a typed metadata system:

```kotlin
interface MetadataKey<T : Any> {
    val id: String
}

interface Story<Props : Any> {
    // ... existing fields ...
    val metadata: StoryMetadata
}

class StoryMetadata internal constructor(
    private val entries: Map<String, Any>
) {
    operator fun <T : Any> get(key: MetadataKey<T>): T?

    companion object {
        val Empty = StoryMetadata(emptyMap())
    }
}
```

This allows plugins to attach typed data to stories without polluting the core interface:

```kotlin
// Plugin defines its own metadata key
object AccessibilityLevel : MetadataKey<String> {
    override val id = "a11y.level"
}

// Story author uses it
story(...) {
    metadata(AccessibilityLevel, "AA")
    // ...
}
```

### 5.2 UI Layer — `composebook-ui`

#### 5.2.1 Dynamic Control Renderer Registry

Replace hardcoded `when` dispatch with a registry:

```kotlin
interface ControlRendererRegistry {
    fun <T : Any> register(
        controlClass: KClass<out PropControl<T>>,
        renderer: ControlRenderer<T>
    )

    fun <T : Any> findRenderer(control: PropControl<T>): ControlRenderer<T>?
}

fun interface ControlRenderer<T : Any> {
    @Composable
    fun Render(control: PropControl<T>, value: T, onValueChange: (T) -> Unit)
}
```

Built-in controls register their renderers by default. Unknown controls fall back to a `FallbackControlRenderer` that shows the value as read-only text.

#### 5.2.2 Panel Plugin System

Panels are UI-only extensions:

```kotlin
interface PanelPlugin {
    val id: String
    val title: String
    val icon: ImageVector?

    @Composable
    fun Content(story: Story<*>, state: StoryRuntimeState<*>)
}
```

`ComposeBookApp` dynamically renders tabs based on registered panels.

#### 5.2.3 Compose Decorator Adapter

Bridges core decorators to the Compose world:

```kotlin
interface ComposeDecorator {
    @Composable
    fun Decorate(
        story: ComposeStory<*>,
        context: StoryContext,
        content: @Composable () -> Unit
    )
}
```

### 5.3 Plugin Installation

Plugins are installed explicitly (no classpath scanning, no reflection):

```kotlin
val app = ComposeBookApp(
    registry = registry,
    plugins = listOf(
        AccessibilityPlugin(),
        ActionsPlugin(),
        ColorControlPlugin()
    )
)
```

Or via a builder:

```kotlin
composeBook {
    registry(myRegistry)

    install(AccessibilityPlugin())
    install(ActionsPlugin()) 
    install(ColorControlPlugin())
}
```

---

## 6. Plugin Interaction Model

### 6.1 Execution Order

Plugins install in declaration order. Decorators execute in reverse order (last installed = outermost wrapper). Lifecycle hooks execute in installation order.

```
Decorator C → Decorator B → Decorator A → Story.render()
```

### 6.2 Conflict Resolution

**Rule: Last write wins, with warning.**

If two plugins register a renderer for the same `PropControl` subclass, the second registration replaces the first and logs a warning. No silent overrides.

### 6.3 Plugin Dependencies

MVP plugin system does NOT support inter-plugin dependencies. Plugins must be self-contained. If a plugin needs another, the user must install both explicitly.

**Rationale**: Dependency resolution is a rabbit hole. Validate the basic model first.

---

## 7. Concrete Plugin Examples

### 7.1 ColorControl Plugin

```kotlin
// In a separate module: composebook-plugin-color

data class ColorControl(
    override val label: String,
    override val description: String? = null,
    val showAlpha: Boolean = false
) : PropControl<Color>

class ColorControlPlugin : ComposeBookPlugin {
    override val id = "composebook.controls.color"
    override val name = "Color Control"

    override fun install(scope: PluginScope) {
        scope.registerControl(
            controlClass = ColorControl::class,
            metadata = ControlMetadata(
                renderer = ColorControlRenderer()
            )
        )
    }
}

class ColorControlRenderer : ControlRenderer<Color> {
    @Composable
    override fun Render(
        control: PropControl<Color>,
        value: Color,
        onValueChange: (Color) -> Unit
    ) {
        // Color picker UI
    }
}
```

### 7.2 Actions Panel Plugin

```kotlin
class ActionsPlugin : ComposeBookPlugin {
    override val id = "composebook.panels.actions"
    override val name = "Actions"

    private val actions = mutableStateListOf<ActionEvent>()

    override fun install(scope: PluginScope) {
        scope.addPanel(ActionsPanel(actions))
    }

    fun action(name: String): () -> Unit = {
        actions.add(ActionEvent(name, System.currentTimeMillis()))
    }
}

// Usage in a story
val actionsPlugin = ActionsPlugin()

story(...) {
    render { props, _ ->
        Button(onClick = actionsPlugin.action("onClick")) {
            Text(props.text)
        }
    }
}
```

### 7.3 Theming Decorator Plugin

```kotlin
class CustomThemePlugin(
    private val lightColors: ColorScheme,
    private val darkColors: ColorScheme
) : ComposeBookPlugin {
    override val id = "composebook.decorators.theme"
    override val name = "Custom Theme"

    override fun install(scope: PluginScope) {
        scope.addComposeDecorator(object : ComposeDecorator {
            @Composable
            override fun Decorate(
                story: ComposeStory<*>,
                context: StoryContext,
                content: @Composable () -> Unit
            ) {
                val colors = when (context.environment.theme) {
                    ThemeMode.Light -> lightColors
                    ThemeMode.Dark -> darkColors
                }
                MaterialTheme(colorScheme = colors) {
                    content()
                }
            }
        })
    }
}
```

---

## 8. What Must Be Decided BEFORE API Freeze

These decisions affect the core API surface and cannot be retrofitted without breaking changes:

| Decision | Options | Recommendation | Urgency |
|----------|---------|----------------|---------|
| Unseal `PropControl` | (A) Unseal now, (B) Unseal later (breaking change), (C) Add `CustomControl` escape hatch | **(A)** with `BuiltInControl` sealed subtree | **Before freeze** |
| Story metadata | (A) `metadata: Map`, (B) Typed `MetadataKey`, (C) Don't add | **(B)** if plugins are planned; **(C)** if not | **Before freeze** |
| Lifecycle hooks in `StoryRegistry` | (A) Add hook points now, (B) Use decorator/wrapper pattern | **(B)** — wrap the registry, don't pollute it | Can wait |
| `StoryBuilder` extensibility | (A) Open `build()`, (B) Keep internal + add metadata DSL | **(B)** — add `metadata()` to builder, keep build sealed | **Before freeze** |
| Decorator support in `Story` | (A) Add to `Story` interface, (B) External composition | **(B)** — decorators are applied externally, not baked into `Story` | Can wait |

---

## 9. Phasing

### Phase 0 — Pre-freeze (NOW)
**Goal**: Keep doors open without building the plugin system.

- [ ] Unseal `PropControl`, introduce `BuiltInControl` sealed subtree
- [ ] Add `StoryMetadata` with typed keys to `Story` interface
- [ ] Add `metadata()` DSL to `StoryBuilder`
- [ ] Ensure `StoryRegistry`, `StoryContext`, and `Story` remain interfaces (not classes)

**Cost**: Minimal. These are non-breaking additive changes.

### Phase 1 — Foundation (Post-MVP validation)
**Goal**: Minimum viable plugin system.

- [ ] Define `ComposeBookPlugin` and `PluginScope` interfaces
- [ ] Implement `ControlRendererRegistry` in `composebook-ui`
- [ ] Replace hardcoded `when` dispatch with registry-based dispatch
- [ ] Implement one control plugin (ColorControl) as proof-of-concept

### Phase 2 — Decorators & Panels (After Phase 1 validation)
**Goal**: UI extensibility.

- [ ] Implement `StoryDecorator` chain in core
- [ ] Implement `ComposeDecorator` adapter in UI
- [ ] Implement `PanelPlugin` system in `ComposeBookApp`
- [ ] Implement one panel plugin (Actions) as proof-of-concept

### Phase 3 — Lifecycle & Advanced (After real user feedback)
**Goal**: Runtime extensibility.

- [ ] Implement `StoryLifecycleHook` system
- [ ] Implement source plugins (external story loading)
- [ ] Plugin conflict detection and diagnostics
- [ ] Plugin API documentation and migration guide

---

## 10. What This Document Intentionally Omits

- **Plugin versioning/compatibility** — Premature. Versioning matters when you have external consumers. Wait for real adoption.
- **Plugin testing framework** — Build test utilities after the plugin API stabilizes.
- **Plugin marketplace/discovery** — Way beyond current scope.
- **Classpath-based auto-discovery** — Violates the "no reflection, no magic" principle. Plugins are always explicit.
- **Hot-reloading of plugins** — Nice to have, but pure complexity. Manual restart is fine.

---

## 11. Open Questions

1. **Should `PropControl` be unsealed or should we use a `CustomControl<T>` wrapper?**
   Unsealing is cleaner but loses exhaustive checking. A wrapper preserves the sealed hierarchy but adds indirection. The `BuiltInControl` subtree approach is a middle ground — worth validating.

2. **How do decorators interact with `ComposeStory.Content()`?**
   Core decorators are non-Composable. The Compose layer needs its own decorator interface. Should there be one decorator chain or two (core + compose)?

3. **Should plugins have access to `StoryRuntimeState`?**
   Lifecycle hooks receive events, but should plugins be able to read/modify runtime state? This is powerful but dangerous — one rogue plugin could corrupt state for all stories.

4. **Is `PluginScope` the right abstraction, or should plugins register directly with subsystems?**
   A unified scope is simpler for plugin authors but may become a god interface. Targeted registration (control registry, panel registry, hook registry) is more modular but harder to discover.

---

## 12. Decision Criteria

For any plugin-related change, apply the same framework:

```
Does this enable external extensibility without breaking core stability?
├─ Yes → Does it require core API changes?
│   ├─ Yes → Is the API change additive (non-breaking)?
│   │   ├─ Yes → Consider for Phase 0
│   │   └─ No → Phase 1+ only, after validation
│   └─ No → Can be added anytime
└─ No → Reject — plugins should extend, not destabilize
```
