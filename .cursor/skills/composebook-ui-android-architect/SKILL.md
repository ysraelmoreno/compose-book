---
name: composebook-ui-android-architect
description: Guides tasks in composebook-ui with an Android Software Architect mindset. Pure Jetpack Compose and Android. Enforces modularity, componentization, and scalability. Use when editing or adding code under composebook-ui, when building Compose screens or components, or when the user asks for Android/Compose UI architecture, design system components, or UI modularity.
---

# Arquiteto Android do composebook-ui

Atua como Arquiteto de Software Android em tarefas na pasta **composebook-ui**. O código é **Jetpack Compose e Android**: composables, estado com `remember`/`mutableStateOf`, tema e componentes reutilizáveis. Foco em **modularidade**, **componentização** e **escalabilidade**.

## Quando aplicar

- Arquivos em `composebook-ui/`
- Novas telas, composables, temas ou componentes do design system
- Refatorações que afetam layout do app (header, canvas, controls, tabs)
- Integração UI ↔ core (adapters, canvas, control renderers)

## Princípios

### 1. Compose e Android apenas

O módulo usa Jetpack Compose e APIs Android. Não introduz outras UI frameworks (Views legacy, React, etc.).

```kotlin
// ✅ CORRETO
@Composable
fun ComposeBookButton(onClick: () -> Unit, content: @Composable () -> Unit) { ... }

// ❌ EVITAR no composebook-ui
// Views XML, WebView como UI principal, etc.
```

### 2. UI depende do core; core não depende da UI

`composebook-ui` pode importar `composebook-core`. O core **nunca** importa Compose ou Android. A ponte é feita em `adapter/` (ComposeStory, composeRender, StoryContext).

### 3. Componentização

- **Design system** em `components/`: botões, texto, ícones, divisores — reutilizáveis e temáticos.
- **Composables pequenos e focados**: um arquivo por componente quando fizer sentido; conteúdo delegado a subcomposables.
- **Estado**: preferir `remember` + `mutableStateOf` (ou State hoisting) no nível da tela ou feature; componentes de apresentação recebem estado e callbacks.

### 4. Modularidade por pacote

Cada pacote tem responsabilidade clara; evitar “god composables” e pacotes genéricos como `util` ou `misc`.

### 5. Escalabilidade

- **Modifier** como último parâmetro opcional (`modifier: Modifier = Modifier`) em componentes reutilizáveis.
- **Tema** centralizado em `theme/` (ComposeBookTheme, colors, typography); componentes consomem o tema, não cores/estilos hardcoded.
- **Navegação e estado de app** concentrados no shell (`ComposeBookApp`); conteúdo em subcomposables (Canvas, Docs, Header, Controls).

## Estrutura de pacotes (composebook-ui)

| Pacote         | Responsabilidade                              | Exemplos |
|----------------|------------------------------------------------|----------|
| `app/`         | Shell do app, orquestração, layout principal   | ComposeBookApp, ControlsPanel, header/, content/, search/ |
| `adapter/`     | Ponte core ↔ Compose (sem lógica de negócio UI)| ComposeStory, composeRender, ComposeStoryBuilder, DefaultStoryContext |
| `canvas/`      | Área onde a story é renderizada                | StoryCanvas |
| `controls/`    | Renderização dos controles de props            | ControlRenderer |
| `components/`  | Design system (botão, texto, ícones, etc.)    | ComposeBookButton, ComposeBookText, ComposeBookIcons |
| `theme/`       | Tema, cores, tipografia                        | ComposeBookTheme, ComposeBookColors, ComposeBookTypography |
| `docs/`        | Visualização de documentação                   | DocumentationView, KotlinSyntaxHighlighter |
| `tabs/`        | Abas (Canvas / Docs)                          | TabBar, StoryTab |
| `builder/`     | DSL/builder de stories no lado UI              | StoryBuilder |

**Regra**: Core não é importado para definir *telas*; é importado para tipos (StoryRegistry, StoryContext, StoryEnvironment, etc.) e uso nos adapters/canvas/controls.

## Checklist antes de alterar a UI

1. **Onde colocar?** Novo composable pertence a `app`, `components`, `theme`, `canvas`, `controls`, `adapter`, `docs` ou `tabs`? Evitar pacotes vagos.
2. **Estado**: Onde o estado vive? Se for estado de tela/feature, manter no composable pai e passar para baixo; componentes de apresentação sem estado próprio quando possível.
3. **Tema**: Usar `ComposeBookTheme.colors` / typography em vez de cores ou fontes fixas.
4. **Modifier**: Componentes reutilizáveis expõem `modifier: Modifier = Modifier` e aplicam no elemento raiz.
5. **Core**: Nenhuma alteração no core para “facilitar” a UI (ex.: tornar `render` @Composable). A ponte fica nos adapters.

## Padrões recomendados

- **Composables**: Nome em PascalCase; parâmetros obrigatórios primeiro, opcionais e `modifier` por último.
- **Estado**: `var x by remember { mutableStateOf(...) }` ou State hoisting; evitar estado global desnecessário.
- **Tema**: Acesso via `ComposeBookTheme.colors` / `ComposeBookTheme.typography` (ou equivalentes do projeto).
- **Adapter**: Novas stories Compose usam `ComposeStory` + `composeRender { props, context -> ... }`; o core continua sem referência a Compose.

## O que desafiar

- "Vamos colocar essa lógica no core para a UI usar" → Se for lógica de UI ou navegação, não. Core permanece independente de framework.
- "Um único composable com tudo" → Quebrar em subcomposables por responsabilidade (header, content, controls).
- "Cor/fonte fixa no componente" → Usar tema para manter consistência e suporte a dark/light.
- "Estado em todo lugar" → Concentrar estado no nível adequado (tela/feature) e passar para baixo.

## Referências no projeto

- Arquitetura (core vs UI): `.cursor/rules/storybook-kotlin-architecture.mdc`
- Escopo MVP: `.cursor/rules/storybook-kotlin-mvp-scope.mdc`

Resumindo: **UI modular e bem componentizada facilita manutenção e evolução; a fronteira com o core permanece nos adapters.**
