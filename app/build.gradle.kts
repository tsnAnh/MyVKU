/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
val ktlint by configurations.creating

plugins {
    id(BuildPlugins.androidApplication)
    kotlin("android")
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.ktlint)
    id(BuildPlugins.navigationSafeArgsKotlin)
    id(BuildPlugins.googleServices)
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    kotlin(BuildPlugins.kotlinSerialization) version kotlinVersion
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdkVersion(AndroidSdk.compile)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "dev.tsnanh.myvku"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        allWarningsAsErrors = true
    }
    lint {
        isAbortOnError = false
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    implementation(project(":domain"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")

    coreLibraryDesugaring(Libraries.desugarJDKLibs)

    implementation("androidx.gridlayout:gridlayout:1.0.0")
    ktlint("com.pinterest:ktlint:0.44.0")
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appcompat)
    implementation(Libraries.coreKtx)
    implementation(Libraries.activityKtx)
    implementation(Libraries.collectionKtx)
    implementation(Libraries.fragment)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.legacySupport)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.lifecycleService)
    implementation(Libraries.preferences)
    implementation(Libraries.lifecycleLiveData)
    implementation(Libraries.lifecycleViewModelSaveState)
    implementation(Libraries.lifecycleCommonJava8)
    implementation(Libraries.apiNavigationFragment)
    implementation(Libraries.apiNavigationUI)
    implementation(Libraries.roomRuntime)
    implementation(Libraries.glide)
    kapt(Libraries.kaptGlide)

    // Work Manager
    implementation(Libraries.workManager)
    androidTestImplementation(Libraries.androidTestWorkManager)
    // Testing
    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidTestJunit)
    androidTestImplementation(TestLibraries.androidTestEspresso)
    // Material Design
    implementation(Libraries.material)
    // Androidx Browser
    implementation(Libraries.androidxBrowser)
    // Timber Log
    implementation(Libraries.timber)
    // ViewPager2
    implementation(Libraries.viewPager2)
    // Google Sign In
    implementation(Libraries.playServicesAuth)

    implementation(Libraries.cloudMessaging)
    implementation(Libraries.recyclerView)
    implementation(Libraries.firebaseCrashlytics)
    implementation(Libraries.firebaseAnalytics)

    // Dagger Hilt
    implementation(Libraries.hiltDagger)
    kapt(Libraries.hiltDaggerCompilerKapt)
    implementation(Libraries.hiltLifecycleViewModel)
    kapt(Libraries.hiltCompilerAndroidx)
    implementation(Libraries.hiltWorkManager)

    // Paging 3
    implementation(Libraries.paging3Common)
    testImplementation(Libraries.paging3Test)

    implementation(Libraries.kotlinCoroutinesCore)
    implementation(Libraries.kotlinCoroutinesAndroid)
    implementation(Libraries.kotlinCoroutinesGooglePlayServices)

    implementation("org.apache.commons:commons-text:1.9")
    implementation(Libraries.kotlinSerialization)
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

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
    kotlinOptions.freeCompilerArgs.toMutableList().add("-Xuse-experimental=kotlin.Experimental")
}
repositories {
    mavenCentral()
}