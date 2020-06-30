/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
val ktlint by configurations.creating

plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.ktlint)
    id(BuildPlugins.navigationSafeArgsKotlin)
    id(BuildPlugins.googleServices)
    id(BuildPlugins.koin)
}
apply(plugin = "name.remal.check-dependency-updates")

android {
    compileSdkVersion(AndroidSdk.compile)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "dev.tsnanh.vku"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = Version.code
        versionName = Version.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    implementation(project(":domain"))
    ktlint("com.pinterest:ktlint:0.37.2")

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appcompat)
    implementation(Libraries.coreKtx)
    implementation(Libraries.activityKtx)
    implementation(Libraries.collectionKtx)
    implementation(Libraries.fragment)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.legacySupport)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.preferences)
    implementation(Libraries.lifecycleLiveData)
    implementation(Libraries.lifecycleViewModelSaveState)
    implementation(Libraries.lifecycleCommonJava8)
    api(Libraries.apiNavigationFragment)
    api(Libraries.apiNavigationUI)
    api(Libraries.apiNavigationDynamicFeatures)
    implementation(Libraries.roomRuntime)
    kapt(Libraries.kaptRoomCompiler)
    implementation(Libraries.roomKtx)
    testImplementation(Libraries.testRoom)
    implementation(Libraries.glide)
    kapt(Libraries.kaptGlide)
    implementation(Libraries.paging)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitConverterMoshi)
    implementation(Libraries.moshiKotlin)
    kapt(Libraries.moshiKotlinCodeGen)

    // Koin for Android
    implementation(Libraries.koin)
    implementation(Libraries.koinAndroidXScope)
    // Koin AndroidX ViewModel features
    implementation(Libraries.koinAndroidXViewModel)
    // Koin AndroidX Fragment features
    implementation(Libraries.koinAndroidXFragment)
    // Koin AndroidX Experimental features
    implementation(Libraries.koinAndroidExt)

    implementation(Libraries.workManager)
    androidTestImplementation(Libraries.androidTestWorkManager)
    // Testing
    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidTestJunit)
    androidTestImplementation(TestLibraries.androidTestEspresso)
    // Firebase Authentication
    implementation(Libraries.firebaseAuthKtx)
    // Material Design
    implementation(Libraries.material)
    // Androidx Browser
    implementation(Libraries.androidxBrowser)
    // Firebase UI Auth
//    implementation(Libraries.firebaseUI)
    // Flexbox Layout
    implementation(Libraries.flexbox)
    // Timber Log
    implementation(Libraries.timber)
    // ViewPager2
    implementation(Libraries.viewPager2)
    // Google Sign In
    implementation(Libraries.playServicesAuth)

    implementation(Libraries.cloudMessaging)
    implementation(Libraries.recyclerView)
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}
