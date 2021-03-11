/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.4.31"
    repositories {
        google()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationSafeArgsPlugin)
        classpath(BuildPlugins.googleServicesPlugin)
        classpath(BuildPlugins.ktlintPlugin)
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.1")
        classpath(BuildPlugins.daggerHiltPlugin)
    }
}

allprojects {
    repositories {
        google()
    }
}

tasks.register("clean").configure {
    delete("build")
}
