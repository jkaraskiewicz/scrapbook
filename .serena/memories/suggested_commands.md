# Suggested Commands

## Build Commands

### Build the project
```bash
./gradlew build
```

### Clean build
```bash
./gradlew clean build
```

### Assemble debug APK
```bash
./gradlew assembleDebug
```

### Assemble release APK
```bash
./gradlew assembleRelease
```

## Testing Commands

### Run unit tests
```bash
./gradlew test
```

### Run instrumented tests (requires device/emulator)
```bash
./gradlew connectedAndroidTest
```

### Run all tests
```bash
./gradlew testDebugUnitTest connectedAndroidTest
```

## Running the App

### Install debug build on connected device/emulator
```bash
./gradlew installDebug
```

### Build and install debug
```bash
./gradlew assembleDebug installDebug
```

## Linting and Code Quality

### Run Android lint
```bash
./gradlew lint
```

### Generate lint report
```bash
./gradlew lintDebug
```

Note: No ktlint or detekt configuration found in the project.

## Gradle Tasks

### List all available tasks
```bash
./gradlew tasks
```

### List project dependencies
```bash
./gradlew dependencies
```

## macOS Specific Notes

On macOS (Darwin), ensure gradlew has execute permissions:
```bash
chmod +x gradlew
```

## Git Commands

### Common git operations
```bash
git status
git add .
git commit -m "message"
git push
git pull
git log --oneline
```

## Project-Specific Notes

- The project uses Gradle wrapper, so always use `./gradlew` instead of `gradle`
- Android Studio can sync and build the project automatically
- The app includes Android Auto support, test in car mode emulator or actual car display
