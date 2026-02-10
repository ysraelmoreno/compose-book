# Customization Guide - Modern ComposeBook UI

This guide explains how to customize and extend the modern ComposeBook UI to match your design system.

## Quick Customization

### 1. Change Color Palette

Edit `storybook-ui/src/main/kotlin/.../theme/ComposeBookColors.kt`:

```kotlin
val CustomDark = ComposeBookColors(
    // Your custom colors
    background = Color(0xFF0D1117),        // GitHub dark
    backgroundElevated = Color(0xFF161B22),
    accent = Color(0xFF58A6FF),            // GitHub blue
    textPrimary = Color(0xFFC9D1D9),
    textSecondary = Color(0xFF8B949E),
    // ... other colors
)
```

Then use in your app:

```kotlin
ComposeBookTheme(darkTheme = true) {
    // Use CustomDark colors
    CompositionLocalProvider(
        LocalComposeBookColors provides CustomDark
    ) {
        ModernComposeBookApp(registry = registry)
    }
}
```

### 2. Customize Typography

Edit `storybook-ui/src/main/kotlin/.../theme/ComposeBookTypography.kt`:

```kotlin
val CustomTypography = ComposeBookTypography(
    titleLarge = TextStyle(
        fontFamily = YourCustomFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    // ... other text styles
)
```

### 3. Change Theme Mode

```kotlin
// Force light theme
ModernComposeBookApp(
    registry = registry,
    darkTheme = false
)

// Dynamic based on system
ModernComposeBookApp(
    registry = registry,
    darkTheme = isSystemInDarkTheme()
)
```

## Advanced Customization

### Custom Sidebar Width

Edit `ModernComposeBookApp.kt`:

```kotlin
StoriesSidebar(
    // ... other params
    modifier = Modifier
        .width(320.dp)  // Change from 280dp
        .fillMaxHeight()
)
```

### Custom Controls Panel Width

```kotlin
ControlsPanel(
    // ... other params
    modifier = Modifier
        .width(360.dp)  // Change from 320dp
        .fillMaxHeight()
)
```

### Custom Icons

Create your own icons in `components/ComposeBookIcons.kt`:

```kotlin
@Composable
fun CustomBookIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = ComposeBookTheme.colors.accent
) {
    Canvas(modifier = modifier.size(size)) {
        // Your custom icon drawing code
        drawPath(
            path = yourCustomPath,
            color = color
        )
    }
}
```

Then replace in `ModernComposeBookApp.kt`:

```kotlin
Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    CustomBookIcon()  // Instead of BookIcon()
    ComposeBookTitle("Stories")
}
```

### Custom Button Styles

Create variants in `components/ComposeBookButton.kt`:

```kotlin
@Composable
fun ComposeBookPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ComposeBookButton(
        onClick = onClick,
        backgroundColor = ComposeBookTheme.colors.accent,
        contentColor = Color.White,
        modifier = modifier,
        content = content
    )
}
```

### Custom Control Renderers

Add new control types in `app/ControlsPanel.kt`:

```kotlin
@Composable
private fun ColorControlRenderer(
    control: ColorControl,
    binding: PropBinding<Any, Color>,
    currentProps: Any,
    onPropsChange: (Any) -> Unit
) {
    var color by remember(currentProps) { 
        mutableStateOf(binding.getter(currentProps)) 
    }
    
    // Your custom color picker UI
    ColorPicker(
        color = color,
        onColorChange = { newColor ->
            color = newColor
            onPropsChange(binding.setter(currentProps, newColor))
        }
    )
}
```

## Creating Custom Theme Presets

### GitHub Dark Theme

```kotlin
object GitHubTheme {
    val colors = ComposeBookColors(
        background = Color(0xFF0D1117),
        backgroundElevated = Color(0xFF161B22),
        backgroundHighlight = Color(0xFF21262D),
        surface = Color(0xFF161B22),
        surfaceSelected = Color(0xFF1C2128),
        surfaceHover = Color(0xFF21262D),
        border = Color(0xFF30363D),
        borderSubtle = Color(0xFF21262D),
        textPrimary = Color(0xFFC9D1D9),
        textSecondary = Color(0xFF8B949E),
        textTertiary = Color(0xFF6E7681),
        accent = Color(0xFF58A6FF),
        accentHover = Color(0xFF79C0FF),
        accentPressed = Color(0xFF388BFD),
        success = Color(0xFF3FB950),
        warning = Color(0xFFD29922),
        error = Color(0xFFDA3633),
        info = Color(0xFF58A6FF)
    )
}
```

### VS Code Theme

```kotlin
object VSCodeTheme {
    val colors = ComposeBookColors(
        background = Color(0xFF1E1E1E),
        backgroundElevated = Color(0xFF252526),
        backgroundHighlight = Color(0xFF2D2D30),
        surface = Color(0xFF252526),
        surfaceSelected = Color(0xFF094771),
        surfaceHover = Color(0xFF2A2D2E),
        border = Color(0xFF3E3E42),
        borderSubtle = Color(0xFF2D2D30),
        textPrimary = Color(0xFFCCCCCC),
        textSecondary = Color(0xFF9D9D9D),
        textTertiary = Color(0xFF6E6E6E),
        accent = Color(0xFF007ACC),
        accentHover = Color(0xFF1C97EA),
        accentPressed = Color(0xFF005A9E),
        success = Color(0xFF89D185),
        warning = Color(0xFFCCA700),
        error = Color(0xFFF48771),
        info = Color(0xFF007ACC)
    )
}
```

### Using Custom Theme

```kotlin
ComposeBookTheme(darkTheme = true) {
    CompositionLocalProvider(
        LocalComposeBookColors provides GitHubTheme.colors
    ) {
        ModernComposeBookApp(registry = registry)
    }
}
```

## Adding New Features

### Add Search Bar

In `ModernComposeBookApp.kt`, add to `StoriesSidebar`:

```kotlin
Column(
    modifier = modifier.background(ComposeBookTheme.colors.backgroundElevated)
) {
    // Header
    SidebarHeader(...)
    
    ComposeBookDivider()
    
    // NEW: Search bar
    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    
    // Stories List
    LazyColumn(...) {
        // Filtered stories based on searchQuery
    }
}
```

### Add Canvas Background Options

Add toolbar controls:

```kotlin
CanvasToolbar(
    // ... other params
    onBackgroundChange = { background ->
        when (background) {
            Background.White -> Color.White
            Background.Dark -> Color(0xFF1A1A1A)
            Background.Grid -> /* Custom grid background */
        }
    }
)
```

### Add Viewport Controls

```kotlin
data class Viewport(
    val width: Dp,
    val height: Dp,
    val name: String
)

val viewports = listOf(
    Viewport(360.dp, 640.dp, "Mobile"),
    Viewport(768.dp, 1024.dp, "Tablet"),
    Viewport(1920.dp, 1080.dp, "Desktop")
)

// In CanvasContent
Box(
    modifier = Modifier
        .size(
            width = selectedViewport.width,
            height = selectedViewport.height
        )
) {
    StoryCanvas(...)
}
```

## Best Practices

### 1. Keep Colors Consistent
Always use `ComposeBookTheme.colors` instead of hardcoded colors:

```kotlin
// ✅ Good
color = ComposeBookTheme.colors.accent

// ❌ Bad
color = Color(0xFF029CFD)
```

### 2. Use Typography System
Always use `ComposeBookTheme.typography`:

```kotlin
// ✅ Good
style = ComposeBookTheme.typography.bodyMedium

// ❌ Bad
fontSize = 13.sp
```

### 3. Maintain Spacing Consistency
Use multiples of 4dp or 8dp:

```kotlin
// ✅ Good
padding = 16.dp
spacing = 8.dp

// ❌ Bad
padding = 13.dp
spacing = 7.dp
```

### 4. Custom Components
Create reusable components in `components/`:

```kotlin
@Composable
fun ComposeBookCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ComposeBookTheme.colors.surface)
            .padding(16.dp)
    ) {
        content()
    }
}
```

### 5. Animation Consistency
Use consistent animation specs:

```kotlin
val animationSpec = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

AnimatedVisibility(
    visible = expanded,
    enter = slideInVertically(animationSpec),
    exit = slideOutVertically(animationSpec)
) {
    // Content
}
```

## Testing Custom Themes

### Preview with Custom Theme

```kotlin
@Preview(showBackground = true)
@Composable
fun CustomThemePreview() {
    ComposeBookTheme(darkTheme = true) {
        CompositionLocalProvider(
            LocalComposeBookColors provides GitHubTheme.colors
        ) {
            // Your component
        }
    }
}
```

### Testing Both Themes

```kotlin
@Preview(name = "Dark Theme", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Theme", uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ThemePreview() {
    ComposeBookTheme(darkTheme = isSystemInDarkTheme()) {
        ModernComposeBookApp(registry = testRegistry)
    }
}
```

## Examples

### Complete Custom Theme Example

```kotlin
// 1. Define your colors
object MyCompanyTheme {
    val colors = ComposeBookColors(
        background = Color(0xFF1A1F2E),
        backgroundElevated = Color(0xFF252B3B),
        accent = Color(0xFF00D9FF),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFB0B8C1),
        // ... other colors
    )
    
    val typography = ComposeBookTypography(
        titleLarge = TextStyle(
            fontFamily = FontFamily(Font(R.font.my_custom_font)),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        // ... other text styles
    )
}

// 2. Use in your app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val registry = InMemoryStoryRegistry()
        registerStories(registry)
        
        setContent {
            ComposeBookTheme(darkTheme = true) {
                CompositionLocalProvider(
                    LocalComposeBookColors provides MyCompanyTheme.colors,
                    LocalComposeBookTypography provides MyCompanyTheme.typography
                ) {
                    ModernComposeBookApp(registry = registry)
                }
            }
        }
    }
}
```

## Resources

- [Modern UI Implementation Docs](modern_ui_implementation.md)
- [Visual Comparison](visual_comparison.md)
- [ComposeBook JS Design System](https://storybook.js.org/)
- [Jetpack Compose Theming](https://developer.android.com/jetpack/compose/themes)

## Questions?

Check the following files for implementation details:
- Theme: `storybook-ui/src/main/kotlin/.../theme/`
- Components: `storybook-ui/src/main/kotlin/.../components/`
- App: `storybook-ui/src/main/kotlin/.../app/`
