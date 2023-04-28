// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val applicationId by extra("com.faa.watcher")
    val compileSdk by extra(33)
    val minSdkVersion by extra(26)
    val targetSdkVersion by extra(33)
    val versionCode by extra(1)
    val versionName by extra("1.0.0")
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.safeargs.kotlin) apply false
}