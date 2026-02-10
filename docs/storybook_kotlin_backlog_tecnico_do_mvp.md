# Storybook Kotlin — Backlog Técnico do MVP

> **Objetivo deste documento**
>
> Converter a especificação do MVP em um **backlog técnico executável**, organizado em **issues reais**, na **ordem correta**, com critérios de aceite claros.
>
> Este backlog deve permitir que:
> - O trabalho seja feito incrementalmente
> - Cada etapa valide uma hipótese do MVP
> - O projeto possa parar a qualquer momento sem virar lixo

---

## Visão Geral do Backlog

O backlog está organizado em **épicos sequenciais**. Cada épico só deve começar quando o anterior estiver concluído e validado.

```
Épico 0 — Setup e governança
Épico 1 — storybook-core (modelo)
Épico 2 — Registry e runtime
Épico 3 — storybook-compose (adaptação)
Épico 4 — UI Shell mínima
Épico 5 — Controls UI
Épico 6 — Samples e validação
```

> ❗ **Regra dura:** não avance de épico sem fechar o anterior.

---

## Épico 0 — Setup e Governança

### Issue 0.1 — Criar repositório e estrutura base

**Descrição**  
Criar o repositório `composebook-kotlin` com estrutura inicial de módulos.

**Tarefas**
- Criar repositório Git
- Configurar Gradle Kotlin DSL
- Criar módulos vazios:
  - `storybook-core`
  - `storybook-compose`
  - `storybook-samples`

**Critérios de aceite**
- Build executa sem erros
- Nenhum módulo depende de Compose ainda

---

### Issue 0.2 — Adicionar documentação inicial

**Descrição**  
Adicionar documentação mínima para orientar contribuidores.

**Tarefas**
- README.md com:
  - O que é o projeto
  - O que não é
  - Status: MVP em construção
- Pasta `docs/` com:
  - Arquitetura
  - Especificação do MVP

**Critérios de aceite**
- Um dev externo entende o propósito do projeto em 5 minutos

---

## Épico 1 — storybook-core: Modelo Fundamental

### Issue 1.1 — Implementar StoryId

**Descrição**  
Criar identidade forte para stories.

**Tarefas**
- Implementar `StoryId`
- Validar unicidade via valor

**Critérios de aceite**
- Nenhum uso de `String` cru para identificar story

---

### Issue 1.2 — Definir contrato Story

**Descrição**  
Criar interface central de Story.

**Tarefas**
- Criar interface `Story<Props>`
- Definir propriedades mínimas

**Critérios de aceite**
- Core não conhece Compose
- Render é função pura

---

### Issue 1.3 — Implementar StoryContext e StoryEnvironment

**Descrição**  
Criar contexto de execução e ambiente.

**Tarefas**
- Criar `StoryContext`
- Criar `StoryEnvironment`
- Criar `ThemeMode`

**Critérios de aceite**
- Story pode acessar ambiente sem depender de UI

---

## Épico 2 — Props e Controles

### Issue 2.1 — Implementar PropControl

**Descrição**  
Criar sistema base de controles.

**Tarefas**
- Criar `PropControl<T>`
- Implementar:
  - `TextControl`
  - `BooleanControl`
  - `EnumControl`

**Critérios de aceite**
- Nenhum uso de reflection

---

### Issue 2.2 — Implementar PropBinding

**Descrição**  
Conectar props imutáveis a controles editáveis.

**Tarefas**
- Criar `PropBinding<Props, T>`
- Garantir imutabilidade

**Critérios de aceite**
- Setter sempre retorna nova instância

---

### Issue 2.3 — Implementar StoryBuilder

**Descrição**  
Criar DSL pública para definição de stories.

**Tarefas**
- Criar `StoryBuilder`
- Criar função helper `story {}`

**Critérios de aceite**
- Story funcional sem acessar código interno

---

## Épico 3 — Registry e Runtime

### Issue 3.1 — Implementar StoryRegistry

**Descrição**  
Registrar e recuperar stories.

**Tarefas**
- Criar interface `StoryRegistry`
- Criar `InMemoryStoryRegistry`

**Critérios de aceite**
- Ordem previsível
- Registro manual explícito

---

### Issue 3.2 — Implementar StoryRuntimeState

**Descrição**  
Representar estado atual do story.

**Tarefas**
- Criar `StoryRuntimeState<Props>`
- Inicializar com `defaultProps`

**Critérios de aceite**
- Reset simples possível

---

## Épico 4 — storybook-compose: Adaptação

### Issue 4.1 — Criar adaptador Compose

**Descrição**  
Adaptar `Story.render` para Compose.

**Tarefas**
- Criar `ComposeStoryAdapter`
- Integrar `LocalInspectionMode`

**Critérios de aceite**
- Core permanece livre de Compose

---

### Issue 4.2 — Implementar StoryCanvas

**Descrição**  
Renderizar story em Compose.

**Tarefas**
- Criar `StoryCanvas`
- Aplicar environment básico

**Critérios de aceite**
- Story renderiza corretamente

---

## Épico 5 — UI Shell e Controls

### Issue 5.1 — Criar StorybookApp

**Descrição**  
Criar shell mínimo da aplicação.

**Tarefas**
- Criar layout sidebar + canvas + controls
- Estado local com `remember`

**Critérios de aceite**
- Troca de story funcional

---

### Issue 5.2 — Implementar Controls UI

**Descrição**  
Permitir edição de props.

**Tarefas**
- Mapear controles para Compose UI:
  - Text → TextField
  - Boolean → Switch
  - Enum → Dropdown

**Critérios de aceite**
- Alterações refletem no canvas em tempo real

---

## Épico 6 — Samples e Validação

### Issue 6.1 — Criar stories de exemplo

**Descrição**  
Validar API com uso real.

**Tarefas**
- Criar Button stories
- Criar Card stories

**Critérios de aceite**
- Stories criados sem hacks

---

### Issue 6.2 — Validação com dev externo

**Descrição**  
Validar usabilidade da API.

**Tarefas**
- Pedir para outro dev criar um story
- Coletar feedback

**Critérios de aceite**
- Dev cria story sem ajuda

---

## Critério Final de Conclusão do MVP

O MVP está concluído quando:

- Todos os épicos acima estão fechados
- Nenhuma dependência circular existe
- Nenhum uso de reflection foi introduzido
- O projeto pode ser aberto sem explicação verbal

> **Se não pode ser usado sem você explicar, falhou.**
