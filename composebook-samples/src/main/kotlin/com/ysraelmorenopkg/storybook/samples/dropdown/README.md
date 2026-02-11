# Dropdown Component Sample

## Overview

A complete Dropdown component sample has been added to the StorybookCompose project, following the established patterns and architecture rules.

## Created Files

### 1. `DropdownProps.kt`
Location: `composebook-samples/src/main/kotlin/com/ysraelmorenopkg/storybook/samples/dropdown/`

**Contents:**
- `DropdownOption` - Data class for dropdown items (id, label)
- `DropdownSize` - Enum for size variants (Small, Medium, Large)
- `DropdownProps` - Immutable props data class with:
  - `label: String` - Label text above dropdown
  - `placeholder: String` - Placeholder when nothing selected
  - `selectedOptionId: String?` - Currently selected option ID
  - `options: List<DropdownOption>` - Available options
  - `enabled: Boolean` - Enable/disable state
  - `required: Boolean` - Required field indicator
  - `size: DropdownSize` - Size variant
  - `helperText: String?` - Optional helper text

### 2. `DropdownStories.kt`
Location: `composebook-samples/src/main/kotlin/com/ysraelmorenopkg/storybook/samples/dropdown/`

**Three Stories:**

#### a) `DropdownDefaultStory`
- **ID**: `dropdown.default`
- **Name**: `Dropdown / Default`
- **Default State**: Empty selection, medium size, enabled
- **Controls**: Label, Placeholder, Enabled, Required, Size
- **Use Case**: Standard dropdown showcase

#### b) `DropdownWithSelectionStory`
- **ID**: `dropdown.with-selection`
- **Name**: `Dropdown / With Selection`
- **Default State**: Pre-selected "Banana", required field, with helper text
- **Options**: Fruit options (Apple, Banana, Orange, Grape, Mango)
- **Use Case**: Demonstrates pre-selected state and helper text

#### c) `DropdownDisabledStory`
- **ID**: `dropdown.disabled`
- **Name**: `Dropdown / Disabled`
- **Default State**: Disabled, large size, with helper text
- **Use Case**: Shows disabled state styling

**Component Features:**
- Material3 DropdownMenu integration
- Stateful selection (remembers selected option)
- Responsive sizing (Small: 40dp, Medium: 48dp, Large: 56dp)
- Visual states for enabled/disabled
- Required field indicator (red asterisk)
- Helper text support
- Keyboard navigation via Material3 DropdownMenuItem

### 3. `SampleStoriesRegistry.kt` (Updated)
**Changes:**
- Added imports for three new Dropdown stories
- Registered all three stories in `registerSampleStories()` function

## Architecture Compliance

✅ **Follows MVP Scope:**
- Manual story registration (no auto-discovery)
- Explicit PropBindings with getter/setter
- Immutable Props (data class)
- UI-independent core (Compose adapter used)

✅ **Uses Allowed Controls:**
- `TextControl` for label and placeholder
- `BooleanControl` for enabled and required flags
- `EnumControl` for size variants

✅ **Best Practices:**
- No reflection
- Stateless Props
- @Composable component in Stories file
- Clear naming conventions
- Proper documentation

## How to Use

1. **Build the project:**
   ```bash
   ./gradlew :composebook-samples:build
   ```

2. **Run the sample app** (if you have a host app configured)

3. **Navigate to Dropdown stories:**
   - "Dropdown / Default" - Basic dropdown
   - "Dropdown / With Selection" - Pre-selected state
   - "Dropdown / Disabled" - Disabled state

4. **Interact with controls:**
   - Change label text
   - Modify placeholder
   - Toggle enabled/disabled
   - Toggle required indicator
   - Switch size variants

## Component Behavior

- **Click** the dropdown box to expand options
- **Select** an option from the menu
- **Visual feedback** for selected items (primary color)
- **Disabled state** prevents interaction and changes styling
- **Required indicator** shows red asterisk next to label
- **Helper text** appears below the dropdown

## Testing the Component

The component demonstrates:
- State management with `remember` and `mutableStateOf`
- Prop-driven rendering
- Control binding and updates
- Size variants
- Enable/disable states
- Material3 theming integration

## Notes

- Component uses Material3 design system
- Follows Storybook Kotlin architecture (core is UI-independent)
- All state is managed locally within the component
- Props are immutable and updated via copy()
- No business logic in the component (pure presentation)
