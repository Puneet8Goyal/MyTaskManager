# TaskEase

![Latest Release](https://img.shields.io/github/v/release/Puneet8Goyal/TaskEase)

![TaskEase Logo](app/src/main/res/drawable/taskeaselogo.png)

TaskEase is a simple yet powerful Android task management app that helps users capture, organize, and track their to-do items. Built with modern Android architecture and best practices—Room for local storage, Hilt for dependency injection, Kotlin Coroutines for async operations, and a clean MVVM setup—TaskEase delivers a smooth, responsive experience.

---

## 🚀 Table of Contents

* [Features](#-features)
* [Screenshots](#-screenshots)
* [Architecture & Tech Stack](#-architecture--tech-stack)
* [Getting Started](#-getting-started)

  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#-usage)
* [Download APK](#-download-apk)
* [Contributing](#-contributing)
* [License](#-license)

---

## 🎉 Features

* **Add & Manage Tasks**: Create tasks with title, description, due date, and priority.
* **Pending & Completed Tabs**: Toggle between Pending and Completed tasks with a single tap.
* **Filters & Sorting**: Filter by priority and search tasks by keyword.
* **Local Persistence**: All tasks are stored locally using Room Database.
* **Modern Architecture**: MVVM pattern with ViewModel + LiveData.
* **Dependency Injection**: Hilt for decoupled components.
* **Asynchronous Operations**: Kotlin Coroutines for non-blocking tasks.
* **Material Design UI**: Intuitive UI with AndroidX & Material Components.
* **Navigation**: Jetpack Navigation Component for smooth flows.

---

## 📸 Screenshots

|                     Add Task                     |                     Task List                     |
| :----------------------------------------------: | :-----------------------------------------------: |
| ![Add Task](app/src/main/res/assets/AddTask.png) | ![Task List](app/src/main/res/assets/Pending.png) |

|                    Completed Task                   |                  Filter Tasks                 |
| :-------------------------------------------------: | :-------------------------------------------: |
| ![Completed](app/src/main/res/assets/Completed.png) | ![Filter](app/src/main/res/assets/Filter.png) |

---

## 🏗 Architecture & Tech Stack

* **Language**: Kotlin
* **UI**: AndroidX, Material Components, Fragments
* **Arch**: MVVM (ViewModel + LiveData)
* **DI**: Hilt
* **Database**: Room
* **Async**: Kotlin Coroutines
* **Navigation**: Jetpack Navigation Component
* **Build**: Gradle Kotlin DSL

---

## 🛠 Getting Started

### Prerequisites

* Android Studio Flamingo (or newer)
* Android SDK 33+
* Gradle 7.4+

### Installation

1. **Clone the repo**

   ```bash
   git clone https://github.com/Puneet8Goyal/TaskEase.git
   cd TaskEase
   ```
2. **Open in Android Studio**

   * File → Open… → select the project root
3. **Build & Run**

   * Allow Gradle to sync, then click **Run** on your device/emulator

---

## ⚙️ Usage

1. On launch, you’ll see the splash screen.
2. Tap the **+** FAB to add a new task.
3. Fill in title, due date, and priority, then tap **Save**.
4. Switch between **Pending** and **Completed** tabs to track progress.
5. Use the filter icon to show only High/Medium/Low priority tasks.

---

## 📥 Download APK

Grab the latest TaskEase APK to install directly on your device:

[Download TaskEase v1.0 APK](https://github.com/Puneet8Goyal/TaskEase/releases/download/V1.0/TaskEase-v1.0.apk)

---

## 🤝 Contributing

Contributions are welcome! Please open issues or pull requests for:

* Bug fixes
* Feature requests
* Documentation improvements

1. Fork the repo (`git fork`)
2. Create your feature branch (`git checkout -b feature/MyFeature`)
3. Commit your changes (`git commit -m 'Add MyFeature'`)
4. Push to your branch (`git push origin feature/MyFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
