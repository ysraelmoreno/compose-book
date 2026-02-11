# Getting Started with Modern ComposeBook UI

Quick checklist to start using the new modern UI.

## ✅ Pre-flight Checklist

### 1. Gradle Sync
- [ ] Open the project in Android Studio
- [ ] Wait for Gradle sync to complete
- [ ] Verify `composebook-ui` module is recognized
- [ ] Check for any dependency errors

### 2. Module Structure
Verify these modules exist:
- [ ] `composebook-core` - Core functionality
- [ ] `composebook-ui` - Modern UI with complete story management
- [ ] `composebook-samples` - Sample stories
- [ ] `app` - Demo application

### 3. Build Configuration
Check these files are updated:
- [ ] `settings.gradle.kts` includes `:composebook-ui`
- [ ] `app/build.gradle.kts` depends on `composebook-ui`
- [ ] `composebook-ui/build.gradle.kts` has correct dependencies

### 4. Code Changes
Verify these updates:
- [ ] `MainActivity.kt` imports `ComposeBookApp`
- [ ] `MainActivity.kt` uses `ComposeBookApp`
- [ ] No compilation errors

## 🚀 Building the Project

### Option 1: Android Studio
1. Open project in Android Studio
2. Select **Build > Make Project** (⌘F9 / Ctrl+F9)
3. Wait for build to complete
4. Check **Build** tab for any errors

### Option 2: Command Line
```bash
# Navigate to project root
cd /path/to/ComposeBookCompose

# Clean build
./gradlew clean

# Build all modules
./gradlew build

# Build specific module
./gradlew :composebook-ui:build

# Build and install app
./gradlew :app:installDebug
```

## 📱 Running the App

### Android Studio
1. Connect device or start emulator
2. Select **Run > Run 'app'** (⌃R / Ctrl+R)
3. Wait for app to launch

### Command Line
```bash
# Install on connected device
./gradlew :app:installDebug

# Launch manually
adb shell am start -n com.ysraelmorenopkg.storybookcompose/.MainActivity
```

## 🎨 Expected Result

When the app launches, you should see:

### Layout
```
┌────────────┬──────────────────────┬────────────┐
│  Stories   │      Canvas          │  Controls  │
│  Sidebar   │    (Component)       │   Panel    │
│            │                      │            │
│ 📚 Stories │  Your component      │ ⚙️ Settings│
│ ├─ Button  │  renders here        │            │
│ │  ├─ Prim │                      │ Text       │
│ │  └─ Sec  │                      │ ✓ Enabled  │
│ └─ Card    │                      │            │
└────────────┴──────────────────────┴────────────┘
```

### Visual Features
- Dark theme background (#1A1A1A)
- Blue accent color (#029CFD) on selected items
- Custom icons (Book, Settings, Chevrons)
- Retractable panels (click icons to toggle)
- Smooth animations

### Interactive Features
- Click **Stories** header icons to collapse/expand sidebar
- Click **Categories** (Button, Card) to expand/collapse
- Click **Story items** to select and render
- Click **Controls** header icons to collapse/expand panel
- Edit controls to see component update in real-time

## 🔍 Troubleshooting

### Build Errors

#### "Compose Compiler Gradle plugin is required"
This has been fixed! The `composebook-ui/build.gradle.kts` now includes:
```kotlin
plugins {
    alias(libs.plugins.kotlin.compose) // ✅ Added
}
```

#### "Module composebook-ui not found"
```bash
# Solution: Sync Gradle
./gradlew --stop
./gradlew build
```

#### "Unresolved reference: ComposeBookApp"
```kotlin
// Check import
import com.ysraelmorenopkg.composebook.ui.app.ComposeBookApp
```

#### Dependency resolution errors
```bash
# Clean and rebuild
./gradlew clean build --refresh-dependencies
```

### Runtime Errors

#### "No stories found"
- Check `registerSampleStories(registry)` is called
- Verify stories are registered before `setContent`

#### Black screen / blank canvas
- Check if stories are registered correctly
- Verify `ComposeBookApp` receives the registry
- Check logcat for errors

#### Controls not working
- Ensure `data class` Props have `copy()` method
- Verify binding setters use `.copy()`
- Check control getters return correct values

## 📊 Verifying the UI

### Dark Theme Check
- [ ] Background is dark (#1A1A1A)
- [ ] Elevated surfaces are lighter (#262626)
- [ ] Text is readable (high contrast)
- [ ] Accent color is blue (#029CFD)

### Layout Check
- [ ] Sidebar is on the left (280dp)
- [ ] Canvas is in the center (flexible)
- [ ] Controls panel is on the right (320dp)
- [ ] All panels are visible initially

### Functionality Check
- [ ] Clicking sidebar toggle button collapses sidebar
- [ ] Clicking controls toggle button collapses panel
- [ ] Selecting a story updates the canvas
- [ ] Editing controls updates the component
- [ ] Categories can expand/collapse

### Icons Check
- [ ] Book icon appears in sidebar header
- [ ] Settings icon appears in controls header
- [ ] Chevron icons appear for categories
- [ ] Chevron icons appear for panel toggles

## 🎯 Next Steps

### Learn the System
1. Read [Modern UI Implementation](modern_ui_implementation.md)
2. Review [Visual Comparison](visual_comparison.md)
3. Study [Customization Guide](customization_guide.md)

### Customize
1. Change colors in `ComposeBookColors.kt`
2. Adjust typography in `ComposeBookTypography.kt`
3. Create custom components
4. Add your own stories

### Explore Code
```
composebook-ui/
├── theme/
│   ├── ComposeBookColors.kt      ← Start here
│   ├── ComposeBookTypography.kt  ← Then here
│   └── ComposeBookTheme.kt
├── components/
│   ├── ComposeBookButton.kt
│   ├── ComposeBookText.kt
│   ├── ComposeBookIcons.kt
│   └── ComposeBookDivider.kt
└── app/
    ├── ComposeBookApp.kt   ← Main app
    └── ControlsPanel.kt
```

## 📝 Quick Reference

### Change to Light Theme
```kotlin
ComposeBookApp(
    registry = registry,
    darkTheme = false  // Change to light theme
)
```

### Use Custom Colors
```kotlin
ComposeBookTheme(darkTheme = true) {
    CompositionLocalProvider(
        LocalComposeBookColors provides YourCustomColors
    ) {
        ComposeBookApp(registry = registry)
    }
}
```

## 🎉 Success!

If you see:
- ✅ Dark theme interface
- ✅ Three-panel layout
- ✅ Blue accent colors
- ✅ Custom icons
- ✅ Retractable panels
- ✅ Smooth animations

**Congratulations!** The modern UI is working correctly.

## 🆘 Need Help?

### Documentation
- `MODERNIZATION_SUMMARY.md` - What was changed
- `docs/modern_ui_implementation.md` - Technical details
- `docs/visual_comparison.md` - Before/After comparison
- `docs/customization_guide.md` - How to customize

### Code Examples
- Look at `composebook-samples/` for story examples
- Check `app/MainActivity.kt` for usage
- Review `composebook-ui/` for implementation details

### Common Issues
1. **Gradle sync fails**: `./gradlew --stop && ./gradlew build`
2. **Module not found**: Check `settings.gradle.kts`
3. **Import errors**: Sync Gradle in Android Studio
4. **Black screen**: Check story registration

## 📦 Project Status

All files created and ready to use:
- ✅ Theme system
- ✅ Custom components
- ✅ Modern app shell
- ✅ Controls panel
- ✅ Icons
- ✅ Documentation
- ✅ Examples

**The project is ready to build and run!**
