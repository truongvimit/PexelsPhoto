# 📱 Pexels Photo Explorer - Tinder-Style Photo Discovery

<div align="center">
  <img src="https://img.shields.io/badge/Android-34A853?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design 3"/>
</div>
 
<p align="center">
  <img src="https://github.com/truongvimit/PexelsPhoto/blob/main/screenshots/app_icon.png" alt="App Icon" width="120" height="120"/>
</p>

<p align="center">
  <strong>A modern, beautifully crafted Android app that transforms photo browsing into an engaging Tinder-like experience</strong>
</p>

<div align="center">

[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/truongvimit/PexelsPhoto)](https://github.com/truongvimit/PexelsPhoto/releases)
[![Downloads](https://img.shields.io/github/downloads/truongvimit/PexelsPhoto/total)](https://github.com/truongvimit/PexelsPhoto/releases)

</div>

---

## 🌟 Features

### 🎯 Core Features
- **🔥 Tinder-Style Swiping**: Intuitive swipe gestures to like or pass photos
- **📸 High-Quality Photos**: Curated photos from Pexels API with stunning visual quality
- **💾 Smart Caching**: Intelligent photo filtering that remembers your preferences
- **🔍 Advanced Search**: Powerful search functionality with query history
- **❤️ Favorites Management**: Save and manage your liked photos
- **🎨 Modern UI**: Beautiful Material Design 3 interface with smooth animations

### 🚀 Technical Highlights
- **📱 100% Jetpack Compose**: Modern declarative UI toolkit
- **🏗️ Clean Architecture**: MVVM + Clean Architecture pattern
- **💉 Dependency Injection**: Koin for clean and testable code
- **🗄️ Room Database**: Local storage with efficient data management
- **🌐 Retrofit + OkHttp**: Robust networking with interceptors
- **📄 Paging 3**: Efficient data loading with infinite scroll
- **🎭 Coil**: Advanced image loading and caching
- **🔄 Coroutines + Flow**: Reactive programming with lifecycle awareness

---

## 📱 Screenshots

<div align="center">
  <table>
    <tr>
      <td><img src="https://github.com/truongvimit/PexelsPhoto/blob/main/screenshots/home_screen.png" alt="Home Screen" width="250"/></td>
      <td><img src="https://github.com/truongvimit/PexelsPhoto/blob/main/screenshots/search_screen.png" alt="Search Screen" width="250"/></td>
      <td><img src="https://github.com/truongvimit/PexelsPhoto/blob/main/screenshots/favorites_screen.png" alt="Favorites Screen" width="250"/></td>
    </tr>
    <tr>
      <td align="center"><b>🏠 Home - Swipe to Discover</b></td>
      <td align="center"><b>🔍 Search - Find Specific Photos</b></td>
      <td align="center"><b>❤️ Favorites - Your Liked Photos</b></td>
    </tr>
  </table>
</div>

---

## 🎥 Demo

<div align="center">

### 📱 App in Action

[![Watch Demo](https://img.shields.io/badge/▶️%20Watch%20Demo-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://www.youtube.com/shorts/4fWxj6M5tTU)

*Experience the Tinder-style photo discovery in action*

---

### 🖼️ Video Preview

[![App Demo Video](https://img.youtube.com/vi/4fWxj6M5tTU/maxresdefault.jpg)](https://www.youtube.com/shorts/4fWxj6M5tTU)

*Click to watch full demo on YouTube*

</div>

---

## 🏗️ Architecture

This app follows **Clean Architecture** principles with **MVVM** pattern for optimal maintainability
and testability.

```
📦 Architecture Overview
├── 🎨 Presentation Layer (UI)
│   ├── Jetpack Compose UI
│   ├── ViewModels
│   └── Navigation
├── 💼 Domain Layer (Business Logic)
│   ├── Use Cases
│   ├── Repositories (Interfaces)
│   └── Models
└── 🗄️ Data Layer
    ├── Remote Data Source (Pexels API)
    ├── Local Data Source (Room DB)
    └── Repository Implementations
```

### 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **🎨 UI Framework** | Jetpack Compose, Material Design 3 |
| **🏗️ Architecture** | MVVM, Clean Architecture, Repository Pattern |
| **💉 DI** | Koin |
| **🌐 Networking** | Retrofit2, OkHttp3, Kotlinx Serialization |
| **🗄️ Database** | Room, SQLite |
| **📄 Pagination** | Paging 3 |
| **🖼️ Image Loading** | Coil |
| **🔄 Async** | Coroutines, Flow |
| **🧪 Testing** | JUnit, Espresso |

---

## 📥 Download & Installation

### 📱 Direct Download
<div align="center">
  <a href="https://github.com/truongvimit/PexelsPhoto/releases/latest">
    <img src="https://img.shields.io/badge/📱%20Download%20APK-34A853?style=for-the-badge&logo=android&logoColor=white" alt="Download APK"/>
  </a>
</div>

### 🛠️ Build from Source

#### Prerequisites
- Android Studio Hedgehog+ (2023.1.1)
- JDK 17+
- Android SDK API 34+
- Git

#### Steps
```bash
# 1. Clone the repository
git clone https://github.com/truongvimit/PexelsPhoto.git
cd PexelsPhoto

# 2. Open in Android Studio
# File -> Open -> Select the project folder

# 3. Sync project with Gradle files
# Android Studio will automatically sync

# 4. Run the app
# Click the "Run" button or use Shift+F10
```

---

## 🔧 Configuration

### 🔑 API Setup
1. Get your free API key from [Pexels API](https://www.pexels.com/api/)
2. Add your API key in `Constants.kt`:
```kotlin
object Constants {
    object MainApi {
        const val API_KEY: String = "YOUR_PEXELS_API_KEY_HERE"
    }
}
```

---

## 📂 Project Structure

```
app/
├── 📱 presentation/          # UI Layer
│   ├── ui/
│   │   ├── home/            # Tinder-style photo swiping
│   │   ├── search/          # Photo search functionality
│   │   ├── favorite/        # Liked photos management
│   │   └── components/      # Reusable UI components
│   ├── navigation/          # App navigation
│   └── theme/               # Material Design 3 theming
├── 💼 domain/               # Business Logic Layer
│   ├── interactors/         # Use cases
│   └── interfaces/          # Repository contracts
├── 🗄️ data/                # Data Layer
│   ├── remote/              # API integration
│   ├── local/               # Room database
│   ├── repositories/        # Data source implementations
│   └── model/               # Data models
├── 🔧 di/                   # Dependency Injection
└── 🛡️ security/            # Security utilities
```

---

## ✨ Key Features Breakdown

### 🏠 Home Screen - Tinder-Style Discovery
- **Swipe Gestures**: Natural left/right swipe to like/pass
- **Stack Animation**: Smooth card stack with depth perception
- **Smart Filtering**: Never shows the same photo twice
- **Visual Feedback**: Real-time swipe indicators
- **Gesture Controls**: Tap buttons or swipe gestures

### 🔍 Search Screen
- **Instant Search**: Real-time photo search as you type
- **Search History**: Remembers your previous searches
- **Category Filtering**: Browse by popular categories
- **Grid Layout**: Optimized photo grid with lazy loading

### ❤️ Favorites Screen
- **Liked Photos**: All your favorite photos in one place
- **Organized View**: Clean grid layout with smooth scrolling
- **Photo Details**: Photographer credits and photo information
- **Share Feature**: Share your favorite discoveries

---

## 🎯 Performance Optimizations

- **📄 Paging 3**: Efficient data loading with automatic pagination
- **🖼️ Image Caching**: Smart image caching with Coil
- **💾 Database Filtering**: Room-based filtering prevents duplicate photos
- **🔄 Background Processing**: Network calls on background threads
- **⚡ Lazy Loading**: UI components load only when needed

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### 🐛 Bug Reports
- Use GitHub Issues
- Include device info, Android version, and steps to reproduce
- Attach screenshots if possible

### 💡 Feature Requests

- Open a GitHub Issue with the "enhancement" label
- Describe the feature and its benefits
- Include mockups if applicable

---

## 📄 License

```
MIT License

Copyright (c) 2025 Truong Vim IT

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **[Pexels](https://www.pexels.com/)** - For providing beautiful, free photos
- **[Material Design](https://m3.material.io/)** - For design system guidelines
- **[JetBrains](https://www.jetbrains.com/)** - For Kotlin programming language
- **[Google](https://developer.android.com/)** - For Android development tools

---

## 📞 Contact & Support

<div align="center">

### 👨‍💻 Developer

**Truong Vim IT**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/truongvim)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/truongvimit)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:truongvimit@gmail.com)

### 💬 Get Support

[![Issues](https://img.shields.io/badge/Report%20Bug-FF6B6B?style=for-the-badge&logo=github&logoColor=white)](https://github.com/truongvimit/PexelsPhoto/issues)
[![Discussions](https://img.shields.io/badge/Community-4ECDC4?style=for-the-badge&logo=github&logoColor=white)](https://github.com/truongvimit/PexelsPhoto/discussions)
[![Email](https://img.shields.io/badge/Direct%20Contact-0077B5?style=for-the-badge&logo=gmail&logoColor=white)](mailto:truongvimit@gmail.com)

</div>

---

<div align="center">
  <h3>⭐ If this project helped you, please give it a star! ⭐</h3>
  <p>Made with ❤️ and ☕ for the Android community</p>
</div>
