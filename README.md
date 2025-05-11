## Overview

**DictionaryApp** is a comprehensive Android application for learning and practicing English. It integrates an English–English dictionary, vocabulary quizzes, favorite word management, and live bilingual (Vietnamese–English) video calls via WebRTC. The dictionary data is stored and queried locally using ObjectBox, while user profiles and learning history are synchronized through Firebase.

## Features

* **English–English Dictionary**
  Look up definitions, pronunciations, and example sentences—all within the app.
* **Vocabulary & Grammar Quizzes**
  Multiple quiz formats (multiple choice, fill-in-the-blank) to reinforce learning.
* **Favorites**
  Mark words you want to review later and manage your personal word list.
* **Real-Time Video Calls**
  Practice speaking with friends or tutors via WebRTC peer-to-peer video calls, with optional text chat and recording.
* **Offline-First Local Search**
  Lightning-fast lookups using ObjectBox, fully functional without network.
* **User Management & Sync**
  Sign up, log in, and sync your favorites and quiz history across devices via Firebase Authentication and Firestore.

## Technology Stack

* **Android** (Kotlin + Jetpack components: ViewModel, LiveData)
* **WebRTC** for peer-to-peer video and data channels
* **ObjectBox** as an embedded, high-performance NoSQL database
* **Firebase** (Authentication, Firestore, Storage) for cloud sync and user data

## Architecture

1. **Presentation Layer**

   * MVVM pattern with Android Architecture Components
2. **Local Data Layer**

   * ObjectBox entities/models for dictionary entries and favorites
3. **Remote Data Layer**

   * Firebase Authentication for user login
   * Firestore for storing and syncing user preferences and quiz results
4. **Real-Time Communication**

   * Signaling via a lightweight server (e.g., Firebase Cloud Functions)
   * `RTCPeerConnection` and `RTCDataChannel` for video, audio, and text streams

## Installation & Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/Naodab/DictionaryApp.git
   ```

2. **Firebase Configuration**

   * Create a new project at [Firebase Console](https://console.firebase.google.com/).
   * Add an Android app and download `google-services.json`.
   * Place `google-services.json` in the `app/` directory.

3. **ObjectBox Setup**
   In your module’s `build.gradle`:

   ```gradle
   implementation "io.objectbox:objectbox-android:3.0.1"
   kapt "io.objectbox:objectbox-processor:3.0.1"
   ```

   See full instructions at [ObjectBox Android Docs](https://docs.objectbox.io/android).

4. **Build & Run**

   * Open in Android Studio, sync Gradle.
   * Connect a device or start an emulator.
   * Click “Run”.

## Contributing

1. Fork this repository.
2. Create a new branch: `git checkout -b feature/YourFeature`.
3. Commit your changes with clear messages.
4. Push to your branch and open a Pull Request.
