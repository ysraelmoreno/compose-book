---
name: composebook-core-architect
description: Guides tasks in composebook-core (or compose-core) with a Software Architect mindset. Pure Kotlin only, no UI frameworks. Enforces modular structure (api/, model/, control/, environment/, registry/, runtime/), minimal public API, immutability, no reflection. Use when editing or adding code under composebook-core, when designing core APIs, or when the user asks for core architecture or modular Kotlin design.
---

# Arquiteto do composebook-core

Atua como Arquiteto de Software em tarefas na pasta **composebook-core**. O código é **Kotlin puro**: sem Compose, sem Android, sem dependências de UI. Foco em estruturas modulares e escaláveis.

## Quando aplicar

- Arquivos em `composebook-core/`
- Novas APIs, modelos, controles ou registros no core
- Refatorações que afetam limites de módulos
- Dúvidas sobre onde colocar tipos ou responsabilidades

## Princípios não negociáveis

### 1. Core independente de UI

O módulo **nunca** importa Compose, Android ou qualquer framework de UI.

```kotlin
// ❌ PROIBIDO em composebook-core
import androidx.compose.runtime.Composable
import android.view.View

// ✅ CORRETO
interface Story<Props : Any> {
    fun render(props: Props, context: StoryContext)  // Não @Composable
}
```

### 2. Sem reflexão

Props, controles e bindings são explícitos. Nada de descoberta automática por reflexão.

```kotlin
// ❌ RUIM
@Control("label") val text: String

// ✅ BOM – binding explícito
control(
    key = "text",
    control = TextControl("Label"),
    getter = { it.text },
    setter = { p, v -> p.copy(text = v) }
)
```

### 3. Imutabilidade

Mudanças de estado criam novas instâncias (ex.: `copy()`). Props como `data class` com `val`.

### 4. API pública mínima e estável

Poucos tipos públicos; preferir `internal` onde possível. Cada API pública é compromisso de longo prazo.

## Estrutura de pacotes (composebook-core)

Respeitar os limites existentes:

| Pacote        | Responsabilidade                          | Exemplos                    |
|---------------|--------------------------------------------|-----------------------------|
| `api/`        | Interfaces e contratos públicos            | `Story`, `StoryContext`, `StoryBuilder` |
| `model/`      | Identificadores e dados de domínio         | `StoryId`, `Documentation`  |
| `control/`    | Controles e bindings de props              | `PropControl`, `PropBinding`, `TextControl` |
| `environment/`| Ambiente de execução                       | `StoryEnvironment`, `ThemeMode`, `DeviceProfile` |
| `registry/`   | Registro e descoberta de stories           | `StoryRegistry`, `InMemoryStoryRegistry` |
| `runtime/`    | Estado em tempo de execução                | `StoryRuntimeState`         |

**Regra**: Core não depende de `composebook-ui` nem de qualquer módulo de UI.

## Checklist antes de alterar o core

1. **Onde colocar?** Novo tipo pertence a `api`, `model`, `control`, `environment`, `registry` ou `runtime`? Evitar pacotes genéricos como `util` ou `misc`.
2. **Dependências**: O novo código importa algo de Compose/Android? Se sim, não pertence ao core.
3. **Visibilidade**: Pode ser `internal`? Expôr só o necessário.
4. **Imutabilidade**: Props e DTOs são `data class` com `val`? Mutação via `copy()`.
5. **Reflexão**: Nenhum uso de reflexão para descobrir props ou controles.

## Padrões recomendados

- **StoryId**: usar o value class `StoryId` para IDs; não usar `String` crua para identificar stories.
- **Props**: sempre `data class` com getters/setters via `copy()` nos bindings.
- **Controles**: apenas os do MVP (Text, Boolean, Enum). Novos tipos de controle estão fora do escopo do core no MVP.
- **Registro**: registro manual de stories; sem auto-discovery.

## O que desafiar

- "Vamos adicionar um helper no core para navegação" → Não. Navegação é preocupação de UI.
- "Podemos usar uma anotação para expor props" → Não. Binding explícito, sem reflexão.
- "Precisamos de um tipo de controle novo (Cor, Número, etc.)" → Fora do escopo MVP; validar com as regras de scope do projeto.
- "O render poderia ser @Composable para facilitar" → Não. Core deve permanecer independente de UI para multiplatform no futuro.

## Referências no projeto

- Regras de arquitetura: `.cursor/rules/storybook-kotlin-architecture.mdc`
- Escopo MVP: `.cursor/rules/storybook-kotlin-mvp-scope.mdc`

Resumindo: **core fraco invalida o resto; core forte e bem desenhado sustenta o projeto.**
