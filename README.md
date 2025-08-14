# Memento Mori - Android Reminder App

A productivity app that uses the concept of "memento mori" (remember death) to help users reflect on their time usage and live more intentionally.

## Features

- **Random Reminders**: Sends notifications at unpredictable times for maximum impact
- **Thoughtful Questions**: Prompts users with deep questions about time, mortality, and purpose
- **Smart Scheduling**: Sometimes skips days entirely to maintain effectiveness
- **Customizable Hours**: Set your active hours for receiving reminders
- **Categories**: Questions span time reflection, productivity, mortality awareness, life purpose, and gratitude

## How It Works

The app sends random notifications during your active hours with questions like:
- "Are you proud of how you spent the last hour?"
- "If today were your last day, would you be satisfied with how you've used it?"
- "What's the most important thing you could be doing right now?"

The randomness and unpredictability are key features - the app may send multiple reminders in a day or skip several days to prevent habituation.

## Technical Stack

- **Kotlin** with **Jetpack Compose** for modern Android UI
- **Room Database** for local data storage
- **Hilt** for dependency injection
- **DataStore** for user preferences
- **AlarmManager** for precise notification scheduling
- **Material 3** design system

## Setup

1. Clone the repository
2. Open in Android Studio
3. Build and run on Android device (API 24+)
4. Grant notification permissions when prompted

## Architecture

The app follows MVVM architecture with:
- **Data Layer**: Room database, repositories, and preferences
- **Domain Layer**: Use cases and business logic
- **UI Layer**: Compose screens and ViewModels

## Permissions Required

- `POST_NOTIFICATIONS`: To send reminder notifications
- `SCHEDULE_EXACT_ALARM`: For precise timing of reminders
- `WAKE_LOCK`: To ensure reminders work when device is sleeping

## Philosophy

Based on the Stoic practice of memento mori, this app aims to help users:
- Develop awareness of time's finite nature
- Make more intentional choices about how to spend time
- Reduce time wasted on unimportant activities
- Cultivate gratitude for the present moment
- Align daily actions with deeper values and purpose