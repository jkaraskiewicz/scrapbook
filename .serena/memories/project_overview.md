# Scrapbook Android App - Project Overview

## Purpose
Scrapbook is an Android application designed to save and manage text snippets ("scraps"). The app allows users to:
- Create and store text scraps in a local database
- View a list of saved scraps
- Delete scraps from the list
- Share text to the app via Android's share intent
- Access scraps via Android Auto (car mode)

## Application Package
- **Package name**: `com.karaskiewicz.scrapbook`
- **Application ID**: `com.karaskiewicz.scrapbook`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 33 (Android 13)
- **Compile SDK**: 33

## Main Entry Points
1. **ScrapbookActivity** - Main mobile application entry point
2. **ScrapbookCarService** - Android Auto/Car mode service entry point
3. **ScrapbookApp** - Application class that initializes Koin DI and Timber logging

## Key Features
- Mobile app with Compose UI
- Android Auto support for viewing scraps in car mode
- Share intent handling to receive text from other apps
- Room database for local persistence
- Dependency injection using Koin
- Logging with Timber
