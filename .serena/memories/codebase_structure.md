# Codebase Structure

## Project Root
```
scrapbook/
├── app/                    # Main application module
├── gradle/                 # Gradle wrapper files
├── .gradle/                # Gradle build cache
├── .git/                   # Git repository
├── .idea/                  # IntelliJ/Android Studio project files
├── .serena/                # Serena MCP configuration
├── .claude/                # Claude configuration
├── build.gradle            # Root build configuration
├── settings.gradle         # Gradle settings
├── gradle.properties       # Gradle properties
├── gradlew                 # Gradle wrapper script (Unix)
└── gradlew.bat            # Gradle wrapper script (Windows)
```

## App Module Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/karaskiewicz/scrapbook/
│   │   ├── res/           # Android resources
│   │   └── AndroidManifest.xml
│   ├── test/              # Unit tests
│   └── androidTest/       # Instrumented tests
├── build.gradle           # App module build configuration
└── proguard-rules.pro     # ProGuard configuration
```

## Source Code Organization (by Feature)

### Main Application Files
- `ScrapbookApp.kt` - Application class (Koin initialization, Timber setup)
- `ScrapbookActivity.kt` - Main activity (Compose entry point)
- `ScrapbookScreen.kt` - Navigation setup

### Feature: List Scraps
```
list/
├── ui/
│   └── ScrapListScreen.kt      # List UI with LazyColumn
├── viewmodel/
│   └── ScrapListViewModel.kt   # List state management
└── data/
    └── ScrapListState.kt       # UI state data class
```

### Feature: Add Scrap
```
add/
├── ui/
│   └── ScrapAddScreen.kt       # Add scrap UI
├── viewmodel/
│   └── ScrapAddViewModel.kt    # Add logic
└── data/
    └── ScrapAddState.kt        # UI state data class
```

### Feature: Android Auto (Car Mode)
```
car/
├── ui/
│   ├── ScrapbookCarScreen.kt        # Car list screen
│   └── ScrapbookCarDetailsScreen.kt # Car detail screen
├── viewmodel/
│   ├── ScrapbookCarViewModel.kt           # Car list logic
│   └── ScrapbookCarDetailsViewModel.kt    # Car detail logic
├── service/
│   └── ScrapbookCarService.kt      # Car app service entry point
├── session/
│   └── ScrapbookCarSession.kt      # Car session management
└── data/
    ├── ScrapbookCarState.kt        # Car list state
    └── ScrapbookCarDetailsState.kt # Car detail state
```

### Database Layer
```
database/
├── entity/
│   └── ScrapEntity.kt         # Room entity
├── dao/
│   └── ScrapDao.kt            # Room DAO interface
├── repository/
│   └── ScrapRepository.kt     # Repository pattern
├── ScrapDatabase.kt           # Room database definition
└── MockDatabaseHelper.kt      # Development mock data
```

### Dependency Injection
```
di/
└── KoinModules.kt             # Koin module definitions
    - databaseModule
    - viewModelsModule
    - utilsModule
```

### Common/Shared Code
```
common/
├── data/
│   └── ScrapData.kt           # Shared data model
└── Extensions.kt              # Kotlin extensions
```

### UI Theme
```
ui/theme/
├── Color.kt                   # Material3 color definitions
├── Theme.kt                   # Material3 theme setup
└── Type.kt                    # Typography definitions
```

## Data Flow

### Mobile App Flow
1. `ScrapbookActivity` → Compose UI entry
2. `ScrapbookScreen` → Navigation setup
3. Feature screens → Use ViewModels via Koin
4. ViewModels → Access ScrapRepository
5. ScrapRepository → Room DAO operations
6. ScrapDao → Database operations

### Android Auto Flow
1. `ScrapbookCarService` → Car app entry
2. `ScrapbookCarSession` → Session management
3. Car screens → Use ViewModels
4. ViewModels → Same repository as mobile app
5. Shared data layer

## Key Design Patterns

- **MVVM**: ViewModels manage UI state, Views observe
- **Repository Pattern**: Abstraction over data sources
- **Dependency Injection**: Koin for loose coupling
- **Unidirectional Data Flow**: StateFlow from ViewModel to UI
- **Single Source of Truth**: Repository as the single source
