const val kotlinVersion = "1.4.31"

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "7.0.0-alpha09"
        const val hiltDagger = "2.33-beta"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationSafeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2"
    const val googleServicesPlugin = "com.google.gms:google-services:4.3.3"
    const val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"
    const val daggerHiltPlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltDagger}"

    const val androidApplication = "com.android.application"
    const val kotlinParcelize = "kotlin-parcelize"
    const val kotlinKapt = "kotlin-kapt"
    const val ktlint = "org.jlleitschuh.gradle.ktlint-idea"
    const val navigationSafeArgsKotlin = "androidx.navigation.safeargs.kotlin"
    const val googleServices = "com.google.gms.google-services"
    const val kotlinSerialization = "plugin.serialization"
}

object AndroidSdk {
    const val min = 21
    const val compile = 30
    const val target = 30
}

object Libraries {
    private object Versions {
        const val kotlinSerialization = "1.1.0"
        const val constraintLayout = "2.0.4"
        const val fragmentVersion = "1.3.0"
        const val lifecycleVersion = "2.3.0"
        const val savedStateVersion = "2.2.0"
        const val navigation = "2.3.2"
        const val roomVersion = "2.2.6"
        const val glideVersion = "4.11.0"
        const val retrofitVersion = "2.9.0"
        const val workManagerVersion = "2.4.0"
        const val materialVersion = "1.2.1"
        const val hiltDagger = "2.33-beta"
        const val hiltJetpack = "1.0.0-alpha03"
        const val paging3Version = "3.0.0-alpha05"
        const val desugarJDKLibsVersion = "1.1.1"
        const val coroutineVersion = "1.4.2"
        const val activityVersion = "1.2.0"
        const val kotlinSerializationConverter = "0.8.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentVersion}"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lifecycleLiveData =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
    const val lifecycleViewModelSaveState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.savedStateVersion}"
    const val lifecycleCommonJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleVersion}"
    const val lifecycleService = "androidx.lifecycle:lifecycle-service:${Versions.lifecycleVersion}"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
    const val preferences = "androidx.preference:preference:1.1.1"
    const val apiNavigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val apiNavigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val kaptRoomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val testRoom = "androidx.room:room-testing:${Versions.roomVersion}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val kaptGlide = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManagerVersion}"
    const val androidTestWorkManager = "androidx.work:work-testing:${Versions.workManagerVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val androidxBrowser = "androidx.browser:browser:1.3.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val playServicesAuth = "com.google.android.gms:play-services-auth:19.0.0"
    const val cloudMessaging = "com.google.firebase:firebase-messaging:21.0.1"
    const val collectionKtx = "androidx.collection:collection-ktx:1.1.0"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha04"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:17.3.0"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx:18.0.1"
    const val hiltDagger = "com.google.dagger:hilt-android:${Versions.hiltDagger}"
    const val hiltDaggerCompilerKapt =
        "com.google.dagger:hilt-android-compiler:${Versions.hiltDagger}"
    const val hiltLifecycleViewModel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltJetpack}"
    const val hiltCompilerAndroidx = "androidx.hilt:hilt-compiler:${Versions.hiltJetpack}"
    const val paging3Common = "androidx.paging:paging-runtime:${Versions.paging3Version}"
    const val paging3Test = "androidx.paging:paging-common:${Versions.paging3Version}"
    const val desugarJDKLibs =
        "com.android.tools:desugar_jdk_libs:${Versions.desugarJDKLibsVersion}"
    const val hiltWorkManager = "androidx.hilt:hilt-work:${Versions.hiltJetpack}"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutineVersion}"
    const val kotlinCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutineVersion}"
    const val kotlinCoroutinesGooglePlayServices =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutineVersion}"
    const val kotlinSerialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}"
    const val kotlinSerializationConverter =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.kotlinSerializationConverter}"
}

object TestLibraries {
    private object Versions {
        const val junitVersion = "4.13.1"
        const val androidJunit = "1.1.2"
        const val espressoVersion = "3.3.0"
    }

    const val junit = "junit:junit:${Versions.junitVersion}"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
    const val androidTestEspresso =
        "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
}

object Version {
    const val name = "0.2.1-dev01"
    const val code = 1
}