# Storybook UI Module

Custom design system and modern UI components for Storybook Kotlin.

## Overview

This module provides a modern, professional interface inspired by Storybook JS 7+. It replaces the default Material Design look with a custom design system optimized for developer tools and component showcases.

## Features

- **Custom Design System**: Professional color palette, typography, and components
- **Dark Theme by Default**: Optimized for developer workflows
- **Light Theme Support**: Alternative theme for daylight usage
- **Modern Layout**: Retractable sidebar and controls panel
- **Custom Icons**: SVG-style minimal icons matching Storybook JS
- **Professional Components**: Custom buttons, text, dividers, and controls

## Design System Components

### Theme
- `StorybookTheme`: Main theme provider
- `StorybookColors`: Color system (Dark/Light variants)
- `StorybookTypography`: Typography system

### UI Components
- `StorybookButton`: Custom button components
- `StorybookIconButton`: Icon-only buttons for toolbars
- `StorybookText`: Text components with theme integration
- `StorybookDivider`: Subtle borders and separators
- `StorybookIcons`: Custom SVG-style icons

### Application
- `ModernStorybookApp`: Main application shell with modern UI
- `ControlsPanel`: Custom controls panel matching Storybook JS design

## Usage

```kotlin
import com.ysraelmorenopkg.storybook.ui.app.ModernStorybookApp

ModernStorybookApp(
    registry = yourRegistry,
    darkTheme = true
)
```

## Color Palette

### Dark Theme
- Background: `#1A1A1A`
- Surface: `#262626`
- Accent: `#029CFD` (Storybook blue)
- Text Primary: `#E8E8E8`
- Text Secondary: `#B3B3B3`

### Light Theme
- Background: `#F6F9FC`
- Surface: `#FFFFFF`
- Accent: `#029CFD`
- Text Primary: `#2E3438`
- Text Secondary: `#5C6870`

## Dependencies

- `storybook-core`: Core story functionality
- `storybook-compose`: Compose integration
- Jetpack Compose UI
- Material 3 (minimal usage for switches)
