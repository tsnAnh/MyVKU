# My VKU
## Caution
My VKU is currently under heavy development. Note that some changes (such as database schema modifications) are not backwards compatible and may cause the app to crash. In this case, please uninstall and re-install the app.
## Getting Started
Clone project using this command

``
git clone https://github.com/tsnanh/VKU.git
``

and open project folder using Android Studio.

## Screenshot
* [Screenshot 1](./screenshots/screenshot_1.jpg)
* [Screenshot 2](./screenshots/screenshot_2.jpg)
* [Screenshot 3](./screenshots/screenshot_3.jpg)
* [Screenshot 4](./screenshots/screenshot_4.jpg)
## Libraries Used
* Google's Android Library
  * [AndroidX Browser](https://developer.chrome.com/multidevice/android/customtabs)
  * [Google Material Design](https://material.io) - Material Design library for Android
  * [Google FlexboxLayout](https://github.com/google/flexbox-layout) - FlexboxLayout is a library project which brings the similar capabilities of CSS Flexible Box Layout Module to Android.
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
* Third party
  * [Glide](https://bumptech.github.io/glide/) - for image loading.
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for managing background threads with simplified code and reducing needs for callbacks
  * [Retrofit](https://square.github.io/retrofit/) for REST API call.
  * [Koin](https://insert-koin.io/) - Dependency Injection for Kotlin
  * [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class. 
* Firebase
  * [Firebase Auth](https://firebase.google.com/docs/auth)
  * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) - (working on it...)
  * [Firebase UI](https://firebase.google.com/docs/auth/android/firebaseui)

## Support
If you've found an error in this sample, please file an issue: https://github.com/tsnanh/VKU/issues

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.