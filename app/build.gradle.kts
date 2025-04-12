plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "dev.randombits.intervaltimer"
    compileSdk = 35

    signingConfigs {
        create("release") {
            if (findProperty("PLAY_STORE_KEY") != null) {
                storeFile = file(findProperty("PLAY_STORE_KEY") as String)
                storePassword = findProperty("PLAY_STORE_PASSWORD") as String?
                keyAlias = findProperty("PLAY_STORE_KEY_ALIAS") as String?
                keyPassword = findProperty("PLAY_STORE_KEY_PASSWORD") as String?
            }
        }
    }

    defaultConfig {
        applicationId = "dev.randombits.intervaltimer"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "1.1.2"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    if (findProperty("PLAY_STORE_KEY") != null) {
        applicationVariants.all {
            outputs.all {
                var apkName = "IntervalTimer-" + defaultConfig.versionName
                if (name == "debug") {
                    apkName += "-${name}"
                }
                this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
                outputFileName = apkName + ".apk"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (findProperty("PLAY_STORE_KEY") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        create("playStore") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (findProperty("PLAY_STORE_KEY") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
}
