# Storybook Kotlin — Arquitetura, Contratos e Diretrizes

> **Objetivo**
>
> Criar uma biblioteca open-source em Kotlin inspirada no Storybook, voltada para:
>
> - Demonstração de componentes de Design Systems
> - Exploração de variações de UI
> - Uso interno em times de produto
> - Possível adoção externa (open-source)
>
> O foco **não é preview visual**, mas **modelagem explícita de componentes, estados, variações e ambientes**.

---

## 1. Princípios Fundamentais

Estas regras **não são negociáveis**. Violá-las compromete a escalabilidade, adoção open-source e suporte multiplatform.

1. **Core independente de UI**  
   O módulo central não conhece Compose, Android ou qualquer framework visual.

2. **Stories são dados, não telas**  
   Um story descreve *o que* renderizar, não *como navegar até ele*.

3. **Nada de reflection mágica**  
   Props, controles e estados são descritos explicitamente.

4. **Imutabilidade como padrão**  
   Toda alteração de estado gera uma nova instância de Props.

5. **API pública mínima e estável**  
   Open-source exige previsibilidade e baixo acoplamento.

---

## 2. Visão Geral da Arquitetura

```
composebook-kotlin/
├── composebook-core/
│   ├── api/
│   ├── model/
│   ├── control/
│   ├── environment/
│   ├── registry/
│   └── runtime/
│
├── composebook-compose/
│   ├── canvas/
│   ├── controls-ui/
│   ├── navigation/
│   └── app/
│
├── composebook-compose-mpp/   (opcional)
├── composebook-samples/
└── storybook-gradle-plugin/ (futuro)
```

---

## 3. composebook-core

O módulo **mais importante** do projeto. Tudo que estiver aqui deve funcionar sem Compose.

### 3.1 Identidade

```kotlin
@JvmInline
value class StoryId(val value: String)
```

- Evita strings soltas
- Facilita navegação, persistência e deep links

---

### 3.2 Story

```kotlin
interface Story<Props : Any> {
    val id: StoryId
    val name: String
    val description: String?
    val defaultProps: Props
    val controls: List<PropBinding<Props, *>>

    fun render(props: Props, context: StoryContext)
}
```

**Observações importantes:**
- `render` não é Composable
- O core não sabe o que é UI

---

### 3.3 StoryContext

```kotlin
interface StoryContext {
    val environment: StoryEnvironment
    val inspectionMode: Boolean
}
```

Permite que componentes saibam quando estão rodando em modo Storybook.

---

## 4. Sistema de Props e Controles

### 4.1 PropControl

```kotlin
sealed interface PropControl<T : Any> {
    val label: String
    val description: String?
}
```

Implementações básicas:

```kotlin
class TextControl(
    override val label: String,
    override val description: String? = null
) : PropControl<String>

class BooleanControl(
    override val label: String
) : PropControl<Boolean>

class EnumControl<T : Enum<T>>(
    override val label: String,
    val values: List<T>
) : PropControl<T>
```

---

### 4.2 PropBinding

Responsável por conectar um campo de `Props` a um `PropControl`.

```kotlin
class PropBinding<Props : Any, T : Any>(
    val key: String,
    val getter: (Props) -> T,
    val setter: (Props, T) -> Props,
    val control: PropControl<T>
)
```

**Por que isso existe?**
- Elimina reflection
- Mantém imutabilidade
- Permite edição dinâmica

---

## 5. DSL de Criação de Stories

### 5.1 StoryBuilder

```kotlin
class StoryBuilder<Props : Any>(
    private val id: StoryId,
    private val name: String,
    private val defaultProps: Props
) {
    private val bindings = mutableListOf<PropBinding<Props, *>>()
    private lateinit var renderer: (Props, StoryContext) -> Unit

    fun <T : Any> control(
        key: String,
        control: PropControl<T>,
        getter: (Props) -> T,
        setter: (Props, T) -> Props
    ) {
        bindings += PropBinding(key, getter, setter, control)
    }

    fun render(block: (Props, StoryContext) -> Unit) {
        renderer = block
    }

    fun build(): Story<Props> { /* implementação */ }
}
```

### 5.2 Função helper

```kotlin
fun <Props : Any> story(
    id: String,
    name: String,
    defaultProps: Props,
    block: StoryBuilder<Props>.() -> Unit
): Story<Props>
```

---

## 6. Story Registry

Descoberta e gerenciamento de stories.

```kotlin
interface StoryRegistry {
    fun register(story: Story<*>)
    fun getAll(): List<Story<*>>
    fun findById(id: StoryId): Story<*>?
}
```

Implementação inicial:

```kotlin
class InMemoryStoryRegistry : StoryRegistry
```

Registro explícito é obrigatório para evitar problemas multiplatform.

---

## 7. Environment

### 7.1 StoryEnvironment

```kotlin
data class StoryEnvironment(
    val theme: ThemeMode,
    val locale: Locale,
    val device: DeviceProfile
)
```

```kotlin
enum class ThemeMode { Light, Dark }
```

### 7.2 DeviceProfile

```kotlin
data class DeviceProfile(
    val name: String,
    val widthDp: Int,
    val heightDp: Int,
    val density: Float
)
```

---

## 8. Runtime State

```kotlin
data class StoryRuntimeState<Props : Any>(
    val props: Props,
    val environment: StoryEnvironment
)
```

Permite:
- Reset de estado
- Undo/redo
- Snapshots futuros

---

## 9. composebook-compose

Primeira implementação oficial de UI baseada em Jetpack Compose.

### 9.1 ComposeStory

```kotlin
interface ComposeStory<Props : Any> : Story<Props> {
    @Composable
    fun Content(props: Props)
}
```

Adapter:

```kotlin
class ComposeStoryAdapter<Props : Any>(
    private val content: @Composable (Props) -> Unit
) : (Props, StoryContext) -> Unit {
    override fun invoke(props: Props, context: StoryContext) {
        CompositionLocalProvider(
            LocalInspectionMode provides context.inspectionMode
        ) {
            content(props)
        }
    }
}
```

---

### 9.2 StoryCanvas

```kotlin
@Composable
fun StoryCanvas(
    story: Story<*>,
    state: StoryRuntimeState<*>
)
```

Responsabilidades:
- Aplicar environment
- Renderizar story
- Controlar recomposição

---

### 9.3 Controls UI

```kotlin
interface ControlRenderer<T : Any> {
    @Composable
    fun Render(
        control: PropControl<T>,
        value: T,
        onChange: (T) -> Unit
    )
}
```

Registry:

```kotlin
class ControlRendererRegistry {
    fun <T : Any> register(
        controlClass: KClass<out PropControl<T>>,
        renderer: ControlRenderer<T>
    )
}
```

---

## 10. Samples (Obrigatório)

```
composebook-samples/
├── button/
│   ├── ButtonProps.kt
│   ├── ButtonStory.kt
├── card/
│   └── CardStory.kt
```

Exemplo:

```kotlin
val ButtonStory = story(
    id = "button.primary",
    name = "Button / Primary",
    defaultProps = ButtonProps("Click", true)
) {
    control(
        key = "text",
        control = TextControl("Text"),
        getter = { it.text },
        setter = { p, v -> p.copy(text = v) }
    )

    render { props, _ ->
        Button(text = props.text, enabled = props.enabled)
    }
}
```

---

## 11. Roadmap Sugerido

### Fase 1
- Core estável
- Compose Android
- Registro manual

### Fase 2
- Persistência de estado
- Snapshots visuais
- Export de stories

### Fase 3
- Gradle Plugin
- Codegen opcional
- Compose Multiplatform

---

## 12. Considerações Finais

Este projeto **não compete** com Storybook JS.

Ele existe para:
- Design Systems em Kotlin
- Componentização real
- Exploração explícita de estados

Se o core for bem feito, o resto evolui.
Se o core for fraco, nada se sustenta.

