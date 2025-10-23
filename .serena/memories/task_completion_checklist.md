# Task Completion Checklist

When completing a development task in this project, follow these steps:

## 1. Code Quality Checks

### Build Verification
```bash
./gradlew build
```
Ensure the project builds successfully without errors.

### Lint Check
```bash
./gradlew lint
```
Check for Android lint warnings and errors.

## 2. Testing

### Run Unit Tests
```bash
./gradlew test
```
Verify that all unit tests pass.

### Run Instrumented Tests (if applicable)
If you modified Android-specific code or UI:
```bash
./gradlew connectedAndroidTest
```

## 3. Code Review Points

- **Kotlin Style**: Verify code follows official Kotlin code style
- **Architecture**: Ensure MVVM pattern is maintained
- **State Management**: Check StateFlow usage is correct
- **Dependency Injection**: Verify Koin modules are properly configured
- **Compose Best Practices**: Review composable functions for proper structure
- **Package Organization**: Ensure files are in correct feature-based packages

## 4. Android-Specific Checks

- **Manifest**: Verify any new activities/services are registered
- **Resources**: Check that new strings are in strings.xml
- **Permissions**: Add necessary permissions if required
- **ProGuard**: Update ProGuard rules if needed for new dependencies

## 5. Feature-Specific Validation

### If modifying database layer:
- Verify Room schema is correct
- Test database migrations if applicable
- Update repository methods if needed

### If modifying UI:
- Test on different screen sizes
- Verify Material3 theming is consistent
- Check accessibility (content descriptions)

### If modifying Android Auto:
- Test in car mode emulator
- Verify car screen templates are correct
- Check navigation flow in car context

## 6. Git Workflow

### Before committing:
1. Review changes: `git status` and `git diff`
2. Ensure no unintended files are included
3. Verify build passes

### Commit message format:
Use clear, descriptive commit messages following existing patterns:
- "Fix [issue]"
- "Add [feature]"
- "Update [component]"
- "Refactor [code area]"

## 7. Documentation

- Update code comments if complex logic is added
- Consider if README or documentation needs updates
- Document any new Koin modules or dependencies

## Notes

- **No formatting tool configured**: Manual code style adherence is required
- **Timber logging**: Use Timber for logging, not System.out or Log
- **Mock data**: Be aware of MockDatabaseHelper injecting fake scraps on app start
