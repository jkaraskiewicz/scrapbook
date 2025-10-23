# Code Style and Conventions

## Kotlin Code Style
- **Official Kotlin code style** is enforced (defined in gradle.properties: `kotlin.code.style=official`)

## Naming Conventions
- **Classes**: PascalCase (e.g., `ScrapbookActivity`, `ScrapEntity`, `ScrapListViewModel`)
- **Functions/Methods**: camelCase (e.g., `deleteScrap`, `collectAsState`)
- **Properties**: camelCase (e.g., `scrapRepository`, `viewModelScope`)
- **Private backing properties**: underscore prefix (e.g., `_state` for MutableStateFlow)

## Architecture Patterns

### MVVM (Model-View-ViewModel)
The app follows MVVM architecture with clear separation:
- **View**: Composable functions in `ui` packages
- **ViewModel**: ViewModels in `viewmodel` packages
- **Model**: Data classes in `data` packages, entities in `entity` packages

### Package Structure by Feature
The codebase is organized by feature modules:
```
com.karaskiewicz.scrapbook/
├── add/           (Add scrap feature)
│   ├── ui/
│   ├── viewmodel/
│   └── data/
├── list/          (List scraps feature)
│   ├── ui/
│   ├── viewmodel/
│   └── data/
├── car/           (Android Auto feature)
│   ├── ui/
│   ├── viewmodel/
│   ├── service/
│   ├── session/
│   └── data/
├── database/      (Data layer)
│   ├── entity/
│   ├── dao/
│   └── repository/
├── di/            (Dependency injection)
└── common/        (Shared utilities)
```

### State Management
- **StateFlow** for UI state in ViewModels
- Private `MutableStateFlow` with public `StateFlow` exposure pattern
- Example:
  ```kotlin
  private val _state: MutableStateFlow<State> = MutableStateFlow(State())
  val state: StateFlow<State> = _state.asStateFlow()
  ```

### Dependency Injection
- **Koin** for dependency injection
- Modules organized by concern (database, viewModels, utils)
- ViewModels use `viewModel { }` delegate
- Car-specific components use `single { (param) -> }` with parameters

## Compose UI Conventions
- **@Composable** functions use PascalCase
- Screen-level composables named with "Screen" suffix (e.g., `ScrapListScreen`)
- Extract reusable components (e.g., `ScrapRow`, `ScrapList`)
- Use `@OptIn` for experimental APIs (e.g., `ExperimentalMaterial3Api`)
- Modifier chaining follows logical order (padding, fill, etc.)

## File Naming
- Kotlin files match the primary class name
- One primary class per file
- Related small classes/data classes can share a file

## Comments
- Minimal comments in the codebase
- Code is self-documenting through clear naming
- Comments used sparingly for clarification (e.g., `// perhaps collectAsStateWithLifecycle?`)

## Type Hints
- Kotlin's type inference is used where possible
- Explicit types used for public API boundaries and clarity
- Function return types explicitly declared

## Imports
- Fully qualified imports (no wildcards)
- Organized by package
- Android/AndroidX imports first, then third-party, then project imports
