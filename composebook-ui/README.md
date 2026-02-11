# ComposeBook UI Module

Custom design system and modern UI components for ComposeBook Kotlin.

## Overview

This module provides a modern, professional interface inspired by ComposeBook JS 7+. It replaces the default Material Design look with a custom design system optimized for developer tools and component showcases.

## Features

- **Custom Design System**: Professional color palette, typography, and components
- **Dark Theme by Default**: Optimized for developer workflows
- **Light Theme Support**: Alternative theme for daylight usage
- **Modern Layout**: Retractable sidebar and controls panel
- **Custom Icons**: SVG-style minimal icons matching ComposeBook JS
- **Professional Components**: Custom buttons, text, dividers, and controls

## Design System Components

### Theme
- `ComposeBookTheme`: Main theme provider
- `ComposeBookColors`: Color system (Dark/Light variants)
- `ComposeBookTypography`: Typography system

### UI Components
- `ComposeBookButton`: Custom button components
- `ComposeBookIconButton`: Icon-only buttons for toolbars
- `ComposeBookText`: Text components with theme integration
- `ComposeBookDivider`: Subtle borders and separators
- `ComposeBookIcons`: Custom SVG-style icons

### Application
- `ComposeBookApp`: Main application shell with modern UI
- `ControlsPanel`: Custom controls panel matching ComposeBook JS design

## Usage

```kotlin
import com.ysraelmorenopkg.storybook.ui.app.ComposeBookApp

ComposeBookApp(
    registry = yourRegistry,
    darkTheme = true
)
```

## Color Palette

### Dark Theme
- Background: `#1A1A1A`
- Surface: `#262626`
- Accent: `#029CFD` (ComposeBook blue)
- Text Primary: `#E8E8E8`
- Text Secondary: `#B3B3B3`

### Light Theme
- Background: `#F6F9FC`
- Surface: `#FFFFFF`
- Accent: `#029CFD`
- Text Primary: `#2E3438`
- Text Secondary: `#5C6870`

## Dependencies

- `composebook-core`: Core story functionality
- `composebook-compose`: Compose integration
- Jetpack Compose UI
- Material 3 (minimal usage for switches)
