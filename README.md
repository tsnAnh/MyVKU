[![Android CI](https://github.com/tsnanh/MyVKU/actions/workflows/android.yml/badge.svg?branch=master)](https://github.com/tsnanh/MyVKU/actions/workflows/android.yml)
# My VKU
## Caution
My VKU is currently under heavy development. Note that some changes (such as database schema modifications) are not backwards compatible and may cause the app to crash. In this case, please uninstall and re-install the app.
## Getting Started
Clone project using this command

``
git clone https://github.com/tsnanh/MyVKU.git
``

and open project folder using Android Studio.
## Clean Architecture + MVVM
A strong base architecture is extremely important for an app to scale and meet the expectation of the user base.

## Gradle Kotlin DSL
Gradle’s Kotlin DSL provides an alternative syntax to the traditional Groovy DSL with an enhanced editing experience in supported IDEs, with superior content assist, refactoring, documentation, and more.

## Screenshot
* [Screenshot 1](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_1.jpg)
* [Screenshot 2](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_2.jpg)
* [Screenshot 3](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_3.jpg)
* [Screenshot 4](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_4.jpg)
* [Screenshot 5](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_5.jpg)
* [Screenshot 6](https://github.com/tsnanh/MyVKU/tree/master/screenshots/screenshot_6.jpg)

## Libraries Used
* Kotlin
  * Ktlint
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for managing background threads with simplified code and reducing needs for callbacks
* Google's Android Library
  * [AndroidX Browser](https://developer.chrome.com/multidevice/android/customtabs)
  * [Google Material Design](https://material.io) - Material Design library for Android
* [Android Jetpack](https://developer.android.com/jetpack) - Jetpack is a suite of libraries, tools, and guidance to help developers write high-quality apps more easily.
  * [AppCompat](https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat) - Degrade gracefully on older versions of Android.
  * [Android KTX](https://developer.android.com/kotlin/ktx) - Write more concise, idiomatic Kotlin code.
  * [Data Binding](https://developer.android.com/topic/libraries/data-binding/) - Declaratively bind observable data to UI elements.
  * [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) - Create a UI that automatically responds to lifecycle events.
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Build data objects that notify views when the underlying database changes.
  * [Navigation](https://developer.android.com/topic/libraries/architecture/livedata) - Handle everything needed for in-app navigation.
  * [Room](https://developer.android.com/topic/libraries/architecture/room) - Access your app's SQLite database with in-app objects and compile-time checks.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
  * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Manage your Android background jobs.
  * [Animations & Transitions](https://developer.android.com/training/animation/) - Move widgets and transition between screens.
  * [Fragment](https://developer.android.com/guide/components/fragments)
  * [Layout](https://developer.android.com/guide/topics/ui/declaring-layout) - Lay out widgets using different algorithms.
  * [ViewPager2](https://developer.android.com/training/animation/vp2-migration) - an improved version of the ViewPager.
  * [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
  * [Java 8+ API](https://developer.android.com/studio/write/java8-support) - Java 8+ API desugaring support
* Third party
  * [Glide](https://bumptech.github.io/glide/) - for image loading.
  * [Retrofit](https://square.github.io/retrofit/) for REST API call.
  * [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class. 
* Firebase
  * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
  * [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics)
* Machine Learning (pending)

## Support
If you've found an error, please file an issue: https://github.com/tsnanh/MyVKU/issues

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
