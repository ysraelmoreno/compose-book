# Storybook Kotlin — Especificação do MVP

> **Objetivo do MVP**
>
> Validar o **modelo conceitual** da biblioteca (stories, props, controles e ambiente), **não** construir uma UI bonita ou completa.
>
> O MVP deve provar que:
> - O core é sólido e independente
> - A API de stories é utilizável no dia a dia
> - É possível renderizar e editar componentes de forma previsível

---

## 1. Escopo do MVP

### 1.1 O que o MVP DEVE ter

- `storybook-core` funcional
- `storybook-compose` mínimo (Android)
- Registro manual de stories
- Painel simples de navegação
- Canvas de renderização
- Controles básicos editáveis
- Suporte a Theme (Light/Dark)

### 1.2 O que o MVP NÃO deve ter

- Persistência de estado
- Snapshots
- Codegen
- Multiplatform
- Gradle Plugin
- UI refinada

> **Regra de ouro:** se não valida o modelo, está fora do MVP.

---

## 2. Módulos do MVP

```
composebook-kotlin/
├── storybook-core/
├── storybook-compose/
└── storybook-samples/
```

---

## 3. storybook-core — Requisitos do MVP

### 3.1 API mínima obrigatória

O core deve expor apenas:

- `Story`
- `StoryId`
- `StoryBuilder`
- `PropControl`
- `PropBinding`
- `StoryRegistry`
- `StoryEnvironment`
- `StoryContext`

Nada além disso.

---

### 3.2 Contratos obrigatórios

#### Story

```kotlin
interface Story<Props : Any> {
    val id: StoryId
    val name: String
    val defaultProps: Props
    val controls: List<PropBinding<Props, *>>

    fun render(props: Props, context: StoryContext)
}
```

> `description` é opcional no MVP.

---

#### StoryBuilder

```kotlin
fun <Props : Any> story(
    id: String,
    name: String,
    defaultProps: Props,
    block: StoryBuilder<Props>.() -> Unit
): Story<Props>
```

Obrigatório suportar:
- Pelo menos 1 control
- Render simples

---

### 3.3 Controles suportados no MVP

| Controle | Tipo |
|--------|------|
| Texto | `String` |
| Boolean | `Boolean` |
| Enum | `Enum<T>` |

Nada além disso.

---

### 3.4 StoryRegistry

```kotlin
class InMemoryStoryRegistry : StoryRegistry
```

- Registro manual
- Ordem previsível
- Sem auto-discovery

---

## 4. storybook-compose — Requisitos do MVP

### 4.1 Responsabilidade do módulo

- Adaptar `Story.render` para Compose
- Renderizar o Canvas
- Renderizar controles
- Controlar estado atual do story

---

### 4.2 StorybookApp (Shell)

```kotlin
@Composable
fun ComposeBookApp(registry: StoryRegistry)
```

Estrutura mínima:

```
┌──────────────────────────────┐
│ Sidebar (Stories)            │
├──────────────┬───────────────┤
│ Controls     │ Canvas        │
│              │               │
└──────────────┴───────────────┘
```

---

### 4.3 Navegação

- Lista simples de stories
- Seleção por clique
- Estado local (remember)

Nada de deep links no MVP.

---

### 4.4 Canvas

```kotlin
@Composable
fun StoryCanvas(
    story: Story<*>,
    state: StoryRuntimeState<*>
)
```

Responsabilidades:
- Aplicar `StoryEnvironment`
- Executar `story.render`
- Isolar recomposição

---

### 4.5 Estado do Story

```kotlin
data class StoryRuntimeState<Props : Any>(
    val props: Props,
    val environment: StoryEnvironment
)
```

- Criado a partir de `defaultProps`
- Atualizado via controls

---

### 4.6 Controles UI

Implementar renderização direta:

- `TextControl` → `TextField`
- `BooleanControl` → `Switch`
- `EnumControl` → `Dropdown`

Sem abstração extra no MVP.

---

## 5. storybook-samples — Requisitos do MVP

### 5.1 Objetivo

Provar que a API é utilizável por outros times.

---

### 5.2 Quantidade mínima

- 2 componentes
- 2 stories cada

Exemplo:

- Button
  - Primary
  - Disabled

- Card
  - Default
  - With Image

---

### 5.3 Exemplo mínimo obrigatório

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

## 6. Critérios de Sucesso do MVP

O MVP está pronto quando:

1. Um dev externo consegue criar um story sem olhar código interno
2. Props podem ser alteradas em runtime
3. O mesmo componente suporta múltiplas variações
4. O core não depende de Compose
5. O código não usa reflection

---

## 7. Anti-Objetivos (Erros Comuns)

- "Vamos melhorar só um pouco a UI"
- "Vamos já preparar para multiplatform"
- "Depois a gente limpa o core"
- "Vamos automatizar discovery"

Se apareceu no MVP, está errado.

---

## 8. Entregáveis do MVP

- Repositório compilável
- README simples (setup + exemplo)
- Documentação de arquitetura (este doc)
- 1 APK / App rodando

---

## 9. Próximo Passo Pós-MVP

Após validação interna:

1. Congelar API do core
2. Marcar APIs experimentais
3. Abrir repositório publicamente
4. Coletar feedback real

> **Sem validação real, não existe roadmap.**
