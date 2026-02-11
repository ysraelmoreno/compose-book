# Changelog

All notable changes to ComposeBook will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2024-02-10

### Added

#### Core Modules
- **composebook-core**: Pure Kotlin, UI-independent core library
  - Story API with type-safe prop controls
  - StoryRegistry for story management and organization
  - PropControl system (TextControl, BooleanControl, EnumControl)
  - PropBinding for connecting controls to props
  - StoryEnvironment for theme and locale context
  - StoryRuntimeState for managing story state at runtime
  - Immutable-by-default architecture

- **composebook-compose**: Jetpack Compose adapter
  - ComposeStory wrapper for Compose integration
  - StoryCanvas for rendering stories
  - Classic Material Theme UI implementation
  - Control renderers for Material Design components
  - ComposeBookApp with retractable panels

- **composebook-ui**: Modern UI with custom design system
  - Custom design system inspired by Storybook JS 7+
  - ComposeBookTheme with custom colors and typography
  - Dark/Light theme support
  - ModernComposeBookApp with vertical 3-panel layout
  - Custom components (ComposeBookButton, ComposeBookText, ComposeBookDivider)
  - Custom SVG-style icons (ChevronDown, ChevronUp, ChevronRight, Book, Settings)
  - Professional color palette (#1A1A1A background, #029CFD accent)
  - Optimized typography for technical content
  - Collapsible story categories with smooth animations
  - Retractable StoriesHeader and ControlsPanel with height limits
  - Scrollable story list and controls with LazyColumn

- **composebook-samples**: Example stories demonstrating usage
  - Button stories (Primary, Secondary, Disabled variants)
  - Card stories (Default, With Image)
  - Badge stories (Status badges)

#### Story Definition Features
- Declarative Story DSL with builder pattern
- Type-safe prop controls with getter/setter bindings
- Story ID system for unique identification
- Hierarchical story organization with categories
- Runtime prop editing with instant preview
- Story context with environment information

#### UI Features
- **Modern UI**:
  - Vertical 3-panel layout (Stories at top, Canvas in middle, Controls at bottom)
  - Collapsible panels with AnimatedVisibility
  - Height-limited panels (450dp for Stories, 400dp for Controls)
  - Scrollable content with LazyColumn
  - Custom theme independent of Material Design
  - Professional developer tool aesthetics

- **Classic UI**:
  - Material Design 3 theme support
  - Customizable theme integration
  - Familiar Android patterns

#### Control System
- **TextControl**: Edit string properties with TextField
- **BooleanControl**: Toggle boolean properties with Switch
- **EnumControl**: Select from predefined values with scrollable dropdown
  - Scrollable options list with LazyColumn
  - Visual feedback for selected option
  - Clickable interaction model

#### Architecture
- UI-independent core (no Compose dependencies in composebook-core)
- Explicit API design (no reflection magic)
- Immutable props pattern with data classes
- Manual story registration for full control
- Modular architecture (core, compose, ui, samples)

### Core Features

#### Story Management
- InMemoryStoryRegistry for story storage
- Story grouping by category (extracted from story names)
- Story retrieval by ID or list all
- Hierarchical organization with expandable categories

#### Prop Controls
- Type-safe prop editing at runtime
- Explicit getter/setter bindings
- Support for primitive types (String, Boolean)
- Support for enum types with visual picker
- Real-time prop updates with state management

#### Environment Context
- Theme mode support (Light/Dark)
- Locale configuration
- Device profile information
- Inspection mode for Compose preview

#### Developer Experience
- Clean, readable DSL for story definition
- Minimal boilerplate code
- Type safety throughout the API
- Clear error messages
- Comprehensive documentation

### Technical Details

#### Dependencies
- Kotlin 2.0+
- Jetpack Compose 1.5+
- Android minSdk 25 (Android 8.0+)
- Material3 for classic UI

#### Build Configuration
- Gradle 8.0+
- Maven publishing configured
- Group ID: `com.ysraelmorenopkg.composebook`
- Version: 0.1.0

#### Modules Structure
```
ComposeBookCompose/
├── composebook-core/       # Pure Kotlin core
├── composebook-compose/    # Compose adapter
├── composebook-ui/         # Modern custom UI
└── composebook-samples/    # Example stories
```

### Documentation
- Comprehensive README with quick start guide
- Publication guide for Maven Central
- Architecture documentation
- Version update tracking
- Rename completion documentation
- Modern UI implementation details
- Visual comparison guide
- Customization guide

### Project Branding
- Full rebranding from "Storybook" to "ComposeBook"
- All user-facing APIs updated
- Module names updated (storybook-* → composebook-*)
- Maven coordinates updated
- Documentation fully updated
- Package names kept as `storybook` for stability (internal only)

### Quality Assurance
- Type-safe APIs
- Explicit over implicit design
- No reflection usage
- Minimal public API surface
- Clean separation of concerns

### License
- MIT License (open-source friendly)
- Permissive usage and modification
- Commercial use allowed

### Known Limitations (Post-MVP)
- State persistence not implemented
- Visual snapshot testing not available
- Auto-discovery of stories not supported
- Multiplatform support pending (Android only)
- Advanced controls pending (Color, Number, Date)
- Deep linking not implemented
- No Gradle plugin for code generation

### Breaking Changes
- N/A (initial release)

### Migration
- N/A (initial release)

### Contributors
- Initial implementation and design
- Modern UI design system
- Complete rebranding to ComposeBook
- Publication preparation

---

[Unreleased]: https://github.com/yourusername/composebook/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/yourusername/composebook/releases/tag/v0.1.0
