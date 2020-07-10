const val kotlinVersion = "1.3.72"

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "4.0.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationSafeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2"
    const val googleServicesPlugin = "com.google.gms:google-services:4.3.3"
    const val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"
    const val koinGradlePlugin = "org.koin:koin-gradle-plugin:2.1.5"

    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val ktlint = "org.jlleitschuh.gradle.ktlint-idea"
    const val navigationSafeArgsKotlin = "androidx.navigation.safeargs.kotlin"
    const val googleServices = "com.google.gms.google-services"
    const val koin = "koin"
}

object AndroidSdk {
    const val min = 21
    const val compile = 30
    const val target = compile
}

object Libraries {
    private object Versions {
        const val constraintLayout = "1.1.3"
        const val fragmentVersion = "1.2.5"
        const val lifecycleVersion = "2.2.0"
        const val savedStateVersion = "2.2.0"
        const val navigation = "2.3.0"
        const val roomVersion = "2.2.5"
        const val glideVersion = "4.11.0"
        const val pagingVersion = "2.1.2"
        const val retrofitVersion = "2.9.0"
        const val koinVersion = "2.1.6"
        const val workManagerVersion = "2.3.4"
        const val materialVersion = "1.2.0-beta01"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentVersion}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}"
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
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val coreKtx = "androidx.core:core-ktx:1.3.0"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
    const val preferences = "androidx.preference:preference:1.1.1"
    const val apiNavigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val apiNavigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val apiNavigationDynamicFeatures =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val kaptRoomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val testRoom = "androidx.room:room-testing:${Versions.roomVersion}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val kaptGlide = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofitConverterMoshi =
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.9.3"
    const val moshiKotlinCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:1.9.3"
    const val koin = "org.koin:koin-android:${Versions.koinVersion}"
    const val koinAndroidXScope = "org.koin:koin-androidx-scope:${Versions.koinVersion}"
    const val koinAndroidXViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koinVersion}"
    const val koinAndroidXFragment = "org.koin:koin-androidx-fragment:${Versions.koinVersion}"
    const val koinAndroidExt = "org.koin:koin-androidx-ext:${Versions.koinVersion}"
    const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManagerVersion}"
    const val androidTestWorkManager = "androidx.work:work-testing:${Versions.workManagerVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val firebaseAuthKtx = "com.google.firebase:firebase-auth-ktx:19.3.1"
    const val androidxBrowser = "androidx.browser:browser:1.2.0"
    const val flexbox = "com.google.android:flexbox:2.0.1"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val playServicesAuth = "com.google.android.gms:play-services-auth:18.0.0"
    const val cloudMessaging = "com.google.firebase:firebase-messaging:20.2.1"
    const val collectionKtx = "androidx.collection:collection-ktx:1.1.0"
    const val activityKtx = "androidx.activity:activity-ktx:1.1.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha04"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:17.1.0"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx:17.4.3"
}

object TestLibraries {
    private object Versions {
        const val junitVersion = "4.13"
        const val androidJunit = "1.1.1"
        const val espressoVersion = "3.2.0"
    }

    const val junit = "junit:junit:${Versions.junitVersion}"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
    const val androidTestEspresso =
        "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
}

object Version {
    const val name = "0.1.2-dev05"
    const val code = 1
}