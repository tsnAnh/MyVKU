plugins {
    id("com.android.library")
    kotlin("android")
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.kotlinKapt)
    kotlin(BuildPlugins.kotlinSerialization) version kotlinVersion
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdkVersion(AndroidSdk.compile)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = Version.code
        versionName = Version.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        allWarningsAsErrors = true
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    implementation(Libraries.kotlinStdLib)
    testImplementation(TestLibraries.junit)
    implementation(Libraries.retrofit)
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation(Libraries.roomRuntime)
    kapt(Libraries.kaptRoomCompiler)
    implementation(Libraries.roomKtx)
    testImplementation(Libraries.testRoom)

    implementation(Libraries.timber)
    implementation("org.jsoup:jsoup:1.14.3")

    implementation(Libraries.hiltDagger)
    kapt(Libraries.hiltDaggerCompilerKapt)

    implementation(Libraries.kotlinCoroutinesCore)
    implementation(Libraries.kotlinSerialization)
    implementation(Libraries.kotlinSerializationConverter)
}
repositories {
    mavenCentral()
}