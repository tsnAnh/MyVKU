plugins {
    id("com.android.library")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
}
android {
    compileSdkVersion(AndroidSdk.compile)
    buildToolsVersion = "30.0.2"

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
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appcompat)
    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidTestJunit)
    androidTestImplementation(TestLibraries.androidTestEspresso)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitConverterMoshi)
    implementation(Libraries.moshiKotlin)
    kapt(Libraries.moshiKotlinCodeGen)

    // Lifecycle
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.lifecycleLiveData)
    implementation(Libraries.lifecycleViewModelSaveState)
    implementation(Libraries.lifecycleCommonJava8)
    implementation(Libraries.roomRuntime)
    kapt(Libraries.kaptRoomCompiler)
    implementation(Libraries.roomKtx)
    testImplementation(Libraries.testRoom)

    implementation(Libraries.timber)
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("org.jsoup:jsoup:1.13.1")

    implementation(Libraries.hiltDagger)
    kapt(Libraries.hiltDaggerCompilerKapt)

    implementation(Libraries.kotlinCoroutinesCore)
    implementation(Libraries.kotlinCoroutinesAndroid)
}