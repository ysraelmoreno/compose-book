# composebook-sdui — Module Architecture Document

> **Status**: Design proposal
> **Dependencies**: `composebook-core`, `composebook-ui`
> **Target**: Applications with BFF/SDUI architectures (e.g., bees-browse-android pattern)

---

## 1. Problem Statement

Teams that use Server-Driven UI (SDUI) architectures have two parallel systems:

1. **Mappers**: Transform BFF responses (`BffModel` / JSON) into typed UI models (`ComponentProps`)
2. **Renderers**: Transform typed UI models into Compose UI (`@Composable` functions)

These teams **cannot** use ComposeBook today because:

- ComposeBook stories require **explicit props** (`data class ButtonProps`), but SDUI props come from **server payloads**
- ComposeBook controls require **typed bindings** (`getter`/`setter`), but SDUI props are **polymorphic** and resolved at runtime
- ComposeBook renders via `story.Content(props, context)`, but SDUI renders via **registry dispatch** (`renderMap[class] → RenderFunction`)

The `composebook-sdui` module bridges this gap: it lets teams register their existing mappers and renderers, feed BFF payloads, and preview SDUI components in the ComposeBook canvas — without rewriting their SDUI infrastructure.

---

## 2. Reference Architecture: bees-browse-android

The module design is grounded in a real-world SDUI architecture. Here's the pattern it must support:

### 2.1 The Data Flow

```
BFF JSON Response
    ↓
BffModel (parsed payload with `type` string + parameters)
    ↓  ComponentMapper.invoke(bffModel, ...) — resolved by type string
ComponentProps (typed sealed class: ButtonComponent, TextComponent, ...)
    ↓  MainRenderMap.renderMap[componentProps::class.java]
RenderFunction (@Composable)
    ↓
Compose UI
```

### 2.2 Key Abstractions

**ComponentProps** — Base type for all SDUI components:

```kotlin
sealed class ComponentProps(
    open val id: String = "",
    open val componentsProps: List<ComponentProps> = emptyList(),
)
```

**ComponentMapper** — Transforms raw BFF models into typed components:

```kotlin
interface ComponentMapper {
    val type: BffConstants.Components
    suspend operator fun invoke(
        bffModel: BffModel,
        componentConfig: ComponentConfig,
        commonProperties: CommonProperties,
        nestedMapper: suspend (List<BffModel>?, ...) -> List<ComponentProps>,
    ): ComponentProps?
}
```

**RenderFunction** — Composable renderer:

```kotlin
typealias RenderFunction = @Composable (
    modifierManager: @Composable (ComponentProps?) -> Modifier,
    position: Int,
    props: ComponentProps
) -> Unit
```

**MainRenderMap** — Static registry:

```kotlin
object MainRenderMap {
    val renderMap: HashMap<Class<out ComponentProps>, RenderFunction> = hashMapOf(
        renderEntry<ButtonComponent> { mod, _, props -> ButtonRender(mod(props), props) },
        renderEntry<TextComponent> { mod, _, props -> TextRender(props, mod(props)) },
        // ... 70+ entries
    )
}
```

### 2.3 What Makes This Hard

| Characteristic | ComposeBook | SDUI (browse-android) |
|---|---|---|
| Props type | Known at compile time | Polymorphic, resolved at runtime |
| Prop editing | `getter`/`setter` on `data class` | Payload mutation or model reconstruction |
| Rendering | `story.Content(props, context)` | `renderMap[class]?.invoke(...)` |
| Component identity | `StoryId` (string) | `Class<out ComponentProps>` |
| Composition | Single component per story | Tree of nested `ComponentProps` |
| Controls | `TextControl`, `BooleanControl`, `EnumControl` | Not applicable — props come from server |

---

## 3. Module Scope

### In Scope

- Abstractions for registering SDUI mappers and renderers in ComposeBook
- An `SduiCanvas` that renders component trees using registered renderers
- An `sduiStory()` DSL that creates ComposeBook-compatible stories from SDUI components
- A payload editor control (raw JSON / key-value editing)
- Integration with ComposeBook's existing story selection, tabs, and environment system

### Out of Scope

- Networking / BFF client implementation
- JSON parsing (users bring their own serialization)
- Mapper implementation (users bring their own mappers)
- Renderer implementation (users bring their own renderers)
- Layout engines (sections, grids, carousels — these are user-provided)

---

## 4. Module Structure

```
composebook-sdui/
└── src/main/kotlin/com/ysraelmorenopkg/composebook/sdui/
    ├── api/
    │   ├── SduiComponent.kt           # Base component interface
    │   ├── SduiComponentMapper.kt     # Mapper contract
    │   ├── SduiComponentRenderer.kt   # Renderer contract
    │   └── SduiStoryBuilder.kt        # DSL for creating SDUI stories
    │
    ├── registry/
    │   ├── SduiRegistry.kt            # Central registry interface
    │   ├── InMemorySduiRegistry.kt    # Default implementation
    │   ├── SduiRenderMap.kt           # ComponentType → Renderer resolution
    │   └── SduiMapperMap.kt           # PayloadType → Mapper resolution
    │
    ├── render/
    │   ├── SduiMainRender.kt          # Dispatch composable (like MainRender)
    │   └── SduiFallbackRender.kt      # Fallback for unregistered types
    │
    ├── canvas/
    │   └── SduiCanvas.kt              # Canvas integration for ComposeBook
    │
    ├── model/
    │   ├── SduiPayload.kt             # Raw payload wrapper
    │   └── SduiComponentType.kt       # Component type identifier
    │
    ├── controls/
    │   └── PayloadEditor.kt           # JSON/payload editing control
    │
    └── story/
        └── SduiStory.kt               # ComposeStory adapter for SDUI
```

### Dependency Graph

```
composebook-sdui
├── composebook-core  (Story, StoryRegistry, PropBinding, StoryContext)
├── composebook-ui    (ComposeStory, StoryCanvas, ComposeBookApp integration)
├── kotlinx-serialization-json (payload handling)
└── androidx.compose   (renderer contracts)
```

---

## 5. API Design

### 5.1 SduiComponent — Base Type

This is `composebook-sdui`'s equivalent of browse-android's `ComponentProps`. It's an interface, not a sealed class, because the module doesn't own the component hierarchy — the consuming app does.

```kotlin
interface SduiComponent {
    val type: SduiComponentType
    val id: String
    val children: List<SduiComponent>
        get() = emptyList()
}
```

```kotlin
@JvmInline
value class SduiComponentType(val value: String)
```

**Why not use the app's `ComponentProps` directly?**

Because `composebook-sdui` must be app-agnostic. It doesn't know about `ButtonComponent` or `TextComponent`. Instead, consuming apps provide an adapter:

```kotlin
interface SduiComponentAdapter<T : Any> {
    fun toSduiComponent(appComponent: T): SduiComponent
    fun getType(appComponent: T): SduiComponentType
    fun getChildren(appComponent: T): List<T>
}
```

For browse-android, the adapter would be:

```kotlin
class BrowseComponentAdapter : SduiComponentAdapter<ComponentProps> {
    override fun toSduiComponent(appComponent: ComponentProps) = object : SduiComponent {
        override val type = SduiComponentType(appComponent::class.java.simpleName)
        override val id = appComponent.id
        override val children = appComponent.componentsProps.map { toSduiComponent(it) }
    }

    override fun getType(appComponent: ComponentProps) =
        SduiComponentType(appComponent::class.java.simpleName)

    override fun getChildren(appComponent: ComponentProps) =
        appComponent.componentsProps
}
```

### 5.2 SduiComponentRenderer — Renderer Contract

```kotlin
fun interface SduiComponentRenderer<T : Any> {
    @Composable
    fun Render(component: T, modifier: Modifier, renderChild: @Composable (Any) -> Unit)
}
```

The `renderChild` parameter is critical — it enables recursive rendering for component trees. A `RichTextComponent` that contains children calls `renderChild(child)` for each, and the dispatch system resolves the correct renderer.

### 5.3 SduiComponentMapper — Mapper Contract

```kotlin
fun interface SduiComponentMapper<T : Any> {
    suspend fun map(payload: SduiPayload): T?
}
```

```kotlin
data class SduiPayload(
    val type: String,
    val raw: JsonObject,
    val parameters: Map<String, JsonElement> = emptyMap(),
    val children: List<SduiPayload> = emptyList(),
)
```

### 5.4 SduiRegistry — Central Registry

```kotlin
interface SduiRegistry {
    fun <T : Any> registerRenderer(
        componentClass: KClass<T>,
        renderer: SduiComponentRenderer<T>,
    )

    fun <T : Any> registerMapper(
        type: SduiComponentType,
        componentClass: KClass<T>,
        mapper: SduiComponentMapper<T>,
    )

    fun findRenderer(componentClass: KClass<*>): SduiComponentRenderer<Any>?
    fun findMapper(type: SduiComponentType): SduiComponentMapper<Any>?
}
```

**Registration by consuming app:**

```kotlin
val sduiRegistry = InMemorySduiRegistry()

// Register renderers (mirrors MainRenderMap entries)
sduiRegistry.registerRenderer(ButtonComponent::class) { component, modifier, _ ->
    ButtonRender(modifier, component)
}

sduiRegistry.registerRenderer(TextComponent::class) { component, modifier, _ ->
    TextRender(component, modifier)
}

sduiRegistry.registerRenderer(RichTextComponent::class) { component, modifier, renderChild ->
    Column(modifier) {
        component.componentsProps.forEach { child ->
            renderChild(child)
        }
    }
}

// Register mappers (mirrors ComponentMapper implementations)
sduiRegistry.registerMapper(
    type = SduiComponentType("button"),
    componentClass = ButtonComponent::class,
    mapper = { payload -> payload.toButtonComponent() }
)
```

### 5.5 SduiMainRender — Dispatch Composable

This is the core rendering engine, equivalent to browse-android's `MainRender`:

```kotlin
@Composable
fun SduiMainRender(
    component: Any,
    registry: SduiRegistry,
    modifier: Modifier = Modifier,
) {
    val renderer = registry.findRenderer(component::class)

    if (renderer != null) {
        renderer.Render(
            component = component,
            modifier = modifier,
            renderChild = { child ->
                SduiMainRender(
                    component = child,
                    registry = registry,
                )
            },
        )
    } else {
        SduiFallbackRender(component, modifier)
    }
}
```

### 5.6 SduiCanvas — ComposeBook Integration

Wraps `SduiMainRender` as a ComposeBook canvas:

```kotlin
@Composable
fun <T : Any> SduiCanvas(
    component: T,
    registry: SduiRegistry,
    environment: StoryEnvironment,
    modifier: Modifier = Modifier,
) {
    val theme = environment.theme

    MaterialTheme(
        colorScheme = when (theme) {
            ThemeMode.Light -> lightColorScheme()
            ThemeMode.Dark -> darkColorScheme()
        }
    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                SduiMainRender(
                    component = component,
                    registry = registry,
                )
            }
        }
    }
}
```

### 5.7 SduiStory — Story Adapter

This is the bridge between SDUI components and ComposeBook's `Story` system. There are two modes:

#### Mode A: Static Component Story

For previewing a specific, pre-built component:

```kotlin
fun <T : Any> sduiStory(
    id: String,
    name: String,
    component: T,
    sduiRegistry: SduiRegistry,
): ComposeStory<SduiStoryProps<T>>
```

```kotlin
data class SduiStoryProps<T : Any>(
    val component: T,
    val sduiRegistry: SduiRegistry,
)
```

Usage:

```kotlin
val ButtonSduiStory = sduiStory(
    id = "sdui.button.primary",
    name = "SDUI / Button / Primary",
    component = ButtonComponent(
        text = TextComponent(text = "Click Me"),
        enabled = ButtonState.NORMAL,
    ),
    sduiRegistry = sduiRegistry,
)
```

#### Mode B: Payload-Driven Story

For previewing components from raw BFF payloads, with live JSON editing:

```kotlin
fun sduiPayloadStory(
    id: String,
    name: String,
    defaultPayload: SduiPayload,
    sduiRegistry: SduiRegistry,
): ComposeStory<SduiPayloadProps>
```

```kotlin
data class SduiPayloadProps(
    val payload: SduiPayload,
    val sduiRegistry: SduiRegistry,
)
```

Usage:

```kotlin
val ButtonFromPayloadStory = sduiPayloadStory(
    id = "sdui.button.from-payload",
    name = "SDUI / Button / From Payload",
    defaultPayload = SduiPayload(
        type = "button",
        raw = buildJsonObject {
            put("value", "Click Me")
            put("enabled", true)
        },
    ),
    sduiRegistry = sduiRegistry,
)
```

The payload story automatically provides a JSON editor control, so the user can modify the raw payload and see the re-mapped, re-rendered component in real time.

---

## 6. Payload Editor Control

For payload-driven stories, the controls panel shows a structured payload editor instead of individual `TextControl`/`BooleanControl` widgets:

```kotlin
@Composable
fun PayloadEditor(
    payload: SduiPayload,
    onPayloadChange: (SduiPayload) -> Unit,
    modifier: Modifier = Modifier,
)
```

The editor provides:

1. **Key-value editing** — For simple parameters (`text`, `enabled`, `color`)
2. **Type display** — Shows the component `type` (read-only)
3. **Children tree** — Expandable tree view of nested components
4. **Raw JSON toggle** — Switch between structured view and raw JSON editing

This replaces ComposeBook's standard `ControlsPanel` for SDUI stories. The `SduiStory` adapter handles the conversion:

```
User edits payload in PayloadEditor
    ↓ onPayloadChange(newPayload)
SduiStory.onPropsChange(props.copy(payload = newPayload))
    ↓ SduiComponentMapper.map(newPayload)
New ComponentProps
    ↓ SduiMainRender(component, registry)
Updated Compose UI
```

---

## 7. Full Integration Example

### 7.1 App Setup (bees-browse-android consumer)

```kotlin
// 1. Create SDUI registry and register existing renderers/mappers
val sduiRegistry = InMemorySduiRegistry()

// Renderers — reuse existing RenderFunctions
sduiRegistry.registerRenderer(ButtonComponent::class) { component, modifier, _ ->
    ButtonRender(modifier, component)
}
sduiRegistry.registerRenderer(TextComponent::class) { component, modifier, _ ->
    TextRender(component, modifier)
}
sduiRegistry.registerRenderer(RichTextComponent::class) { component, modifier, renderChild ->
    Column(modifier) {
        component.componentsProps.forEach { child -> renderChild(child) }
    }
}
sduiRegistry.registerRenderer(SpacerComponent::class) { component, modifier, _ ->
    SpacerRender(component)
}

// Mappers — reuse existing ComponentMapper logic
sduiRegistry.registerMapper(
    type = SduiComponentType("button"),
    componentClass = ButtonComponent::class,
    mapper = { payload -> buttonComponentMapper.fromPayload(payload) }
)

// 2. Create SDUI stories
val sduiStories = listOf(
    sduiStory(
        id = "sdui.button.primary",
        name = "SDUI / Button / Primary",
        component = ButtonComponent(
            text = TextComponent(text = "Add to Cart"),
            enabled = ButtonState.NORMAL,
        ),
        sduiRegistry = sduiRegistry,
    ),
    sduiStory(
        id = "sdui.button.disabled",
        name = "SDUI / Button / Disabled",
        component = ButtonComponent(
            text = TextComponent(text = "Out of Stock"),
            enabled = ButtonState.DISABLED,
        ),
        sduiRegistry = sduiRegistry,
    ),
    sduiPayloadStory(
        id = "sdui.richtext.from-bff",
        name = "SDUI / RichText / From BFF",
        defaultPayload = SduiPayload(
            type = "richText",
            raw = sampleRichTextJson,
        ),
        sduiRegistry = sduiRegistry,
    ),
)

// 3. Register with ComposeBook
val storyRegistry = InMemoryStoryRegistry()
registerSampleStories(storyRegistry)
sduiStories.forEach { storyRegistry.register(it) }

// 4. Launch ComposeBook
ComposeBookApp(registry = storyRegistry)
```

### 7.2 What the User Sees

1. **Story sidebar** — Shows both regular stories and SDUI stories, mixed:
   ```
   Badge / Primary
   Badge / Error
   Button / Primary
   Button / Disabled
   SDUI / Button / Primary        ← SDUI stories
   SDUI / Button / Disabled       ← 
   SDUI / RichText / From BFF     ← Payload-driven
   ```

2. **Canvas tab** — Renders the SDUI component using registered renderers

3. **Controls tab** — For static SDUI stories: no controls (component is fixed). For payload stories: PayloadEditor with JSON editing

4. **Environment toggle** — Light/Dark theme applies to SDUI canvas same as regular canvas

---

## 8. Architectural Decisions

### 8.1 Why a Separate Module (Not a Package in composebook-ui)?

| Factor | Package in UI | Separate Module |
|---|---|---|
| Dependency on kotlinx-serialization | Pollutes composebook-ui | Isolated |
| Users without SDUI | Carry unused code | Don't include it |
| API evolution | Tied to composebook-ui releases | Independent versioning |
| Build time | Always compiled | Only when needed |

**Decision**: Separate Gradle module. Users opt in by adding `implementation(composebook-sdui)`.

### 8.2 Why Interface Instead of Sealed Class for SduiComponent?

Browse-android uses `sealed class ComponentProps` because it owns the full hierarchy. `composebook-sdui` does NOT own the hierarchy — it must work with **any** app's component types. Therefore, the abstractions are interfaces.

The trade-off: no exhaustive `when` matching in the module. The `SduiFallbackRender` handles unknown types gracefully.

### 8.3 Why Re-registration Instead of Reusing MainRenderMap Directly?

Browse-android's `MainRenderMap` is a `HashMap<Class<out ComponentProps>, RenderFunction>` — a concrete static object. `composebook-sdui` cannot depend on it without depending on the entire browse-android project.

Instead, apps register their renderers into `SduiRegistry`. This:
- Keeps the module app-agnostic
- Allows selective registration (not all 70+ renderers needed for preview)
- Enables testing with mock renderers

**The cost**: Registration boilerplate. Mitigated by providing bulk registration helpers:

```kotlin
fun SduiRegistry.registerFromRenderMap(
    renderMap: Map<Class<out Any>, @Composable (Any, Modifier) -> Unit>
) {
    renderMap.forEach { (clazz, renderer) ->
        registerRenderer(clazz.kotlin) { component, modifier, _ ->
            renderer(component, modifier)
        }
    }
}
```

### 8.4 Why Two Story Modes (Static vs. Payload)?

**Static mode** answers: *"Does this component render correctly with these specific props?"*
- Useful for visual regression of known component states
- Zero mapping overhead — component is already built
- No controls needed

**Payload mode** answers: *"Does the full pipeline (JSON → mapper → renderer) work?"*
- Useful for testing mapper logic
- PayloadEditor enables exploration
- Tests the entire SDUI chain

Both modes are needed. Static mode is faster for iteration. Payload mode validates the full pipeline.

### 8.5 Why Suspend in SduiComponentMapper?

Browse-android mappers are `suspend` because:
- Some mappers call use cases (e.g., `ConditionalUseCase`)
- Nested mapping is recursive and potentially expensive
- Future mappers may fetch additional data

`composebook-sdui` preserves this contract. The story adapter launches mapping in a coroutine scope and updates the canvas when the result is ready.

---

## 9. What Must Change in Core/UI

### 9.1 No Core Changes Required

The SDUI module works entirely through existing core interfaces:
- `Story<Props>` — SDUI stories implement `ComposeStory<SduiStoryProps>`
- `StoryRegistry` — SDUI stories register normally
- `StoryContext` — Passed to SDUI canvas unchanged

### 9.2 Minimal UI Changes

| Change | Location | Impact |
|---|---|---|
| Allow custom controls panel | `ComposeBookApp` | SDUI stories show `PayloadEditor` instead of standard controls |
| Canvas delegation | `CanvasContent` | SDUI stories use `SduiCanvas` instead of `StoryCanvas` |

These can be achieved via the existing `ComposeStory.Content()` contract — the SDUI story's `Content()` composable renders `SduiCanvas` internally. **No changes to `ComposeBookApp` are strictly necessary** if the SDUI story handles its own rendering.

However, for the PayloadEditor to appear in the controls panel, either:
- **(A)** The SDUI story provides its own controls via `PropBinding` (limited but no UI changes)
- **(B)** `ComposeBookApp` supports a `customControlsPanel` slot (clean but requires UI change)

**Recommendation**: Start with **(A)**. A single `PropBinding` with a custom `PayloadControl` type is simpler than modifying ComposeBookApp. This does require unsealing `PropControl` — which aligns with the plugin architecture document's Phase 0 recommendation.

---

## 10. Phasing

### Phase 1 — Core Rendering (Foundation)

- [ ] Create `composebook-sdui` Gradle module
- [ ] Define `SduiComponentRenderer` interface
- [ ] Implement `SduiRegistry` (renderers only, no mappers)
- [ ] Implement `SduiMainRender` dispatch composable
- [ ] Implement `SduiFallbackRender`
- [ ] Implement `sduiStory()` DSL (static mode only)
- [ ] Register SDUI stories with ComposeBook
- [ ] Proof-of-concept: render 3 browse-android components in ComposeBook canvas

**Validation**: A ButtonComponent, TextComponent, and RichTextComponent render correctly in ComposeBook.

### Phase 2 — Mapper Integration

- [ ] Define `SduiComponentMapper` interface
- [ ] Define `SduiPayload` model
- [ ] Add mapper registration to `SduiRegistry`
- [ ] Implement `sduiPayloadStory()` DSL (payload mode)
- [ ] Wire mapping pipeline: payload → mapper → component → renderer
- [ ] Proof-of-concept: render a component from raw JSON payload

**Validation**: A raw JSON payload maps and renders correctly in the canvas.

### Phase 3 — Payload Editor

- [ ] Implement `PayloadEditor` composable
- [ ] Integrate with ComposeBook controls panel (either via `PropBinding` or custom slot)
- [ ] Support key-value editing of parameters
- [ ] Support raw JSON toggle
- [ ] Live re-mapping on payload change

**Validation**: User edits a JSON value, sees the component update in real time.

### Phase 4 — Polish & Bulk Registration

- [ ] Bulk renderer registration helpers
- [ ] Bulk mapper registration helpers
- [ ] Nested component tree visualization in controls panel
- [ ] Error states for failed mapping/rendering
- [ ] Documentation and sample module

---

## 11. Open Questions

1. **Should the module provide a JSON parser or require the user to bring one?**
   Requiring `kotlinx-serialization-json` as a dependency is reasonable. But if users use Moshi or Gson, we need a format-agnostic payload type. `SduiPayload` using `JsonObject` from kotlinx couples the module to that library.

2. **How to handle component-specific actions (onClick, onScroll)?**
   Browse-android uses `LocalActionHandler.current` to handle actions. Should `composebook-sdui` provide a mock action handler, or should the story author provide one? A no-op action handler as default seems right for preview purposes.

3. **Should SDUI stories appear in the same list as regular stories?**
   Yes — they're `ComposeStory` instances registered in the same `StoryRegistry`. The naming convention (`SDUI / ...`) provides visual separation. But should there be a section/category system? This is a ComposeBook UI concern, not an SDUI concern.

4. **What about component trees vs. single components?**
   An SDUI story wrapping a `RichTextComponent` with 5 children is fundamentally different from a Button story. The canvas needs to handle trees. `SduiMainRender` already supports this via recursive `renderChild`. The question is: should each child be a separate story, or is the tree one story?

   **Recommendation**: One story per component tree. The tree is the unit of preview. Individual children can be separate stories if the user wants to isolate them.

---

## 12. Risk Assessment

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Registration boilerplate is too verbose for 70+ components | High | Medium | Provide bulk registration from existing `renderMap` |
| Mapper failures are hard to debug in preview | Medium | High | Show mapping errors inline in the canvas, not just a blank screen |
| `PropControl` stays sealed, blocking PayloadEditor | Medium | High | Phase 3 depends on unsealing `PropControl` or using a workaround |
| Performance with large component trees | Low | Medium | LazyColumn for tree rendering, limit nesting depth |
| Module adds too many dependencies for non-SDUI users | Low | Low | Separate Gradle module, opt-in only |
