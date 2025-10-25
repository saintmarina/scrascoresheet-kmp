import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    id("com.codingfeline.buildkonfig") version "0.17.1"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        iosTarget.compilations.getByName("main") {
            val analyticsBridge by cinterops.creating {
                definitionFile.set(project.file("src/nativeInterop/cinterop/AnalyticsBridge.def"))

                // Framework compiler search path
                compilerOpts(
                    "-framework", "AnalyticsManagerBridge",
                    "-framework", "AmplitudeSwift",
                    "-framework", "AmplitudeCore",
                    "-framework", "AnalyticsConnector",
                    "-F", "${projectDir}/iosFrameworks"
                )
                extraOpts += listOf("-compiler-option", "-fmodules")
            }
        }

        iosTarget.binaries.all {
            linkerOpts(
                "-framework", "AnalyticsManagerBridge",
                "-framework", "AmplitudeSwift",
                "-framework", "AmplitudeCore",
                "-framework", "AnalyticsConnector",
                "-F", "${projectDir}/iosFrameworks"
            )
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.amplitude.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.decompose.core)
            implementation(libs.decompose.compose)
            implementation(libs.essenty.lifecycle)
            implementation(libs.essenty.lifecycle.coroutines)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.lyricist.core)
            implementation(libs.lyricist.full)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

val appPackageName = "com.steelsoftware.scrascoresheet"

android {
    namespace = appPackageName
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appPackageName
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

val localProps = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}

val amplitudeKey: String? = localProps.getProperty("AMPLITUDE_API_KEY")

buildkonfig {
    packageName = appPackageName
    objectName = "AppConfig"

    defaultConfigs {
        buildConfigField(
            Type.STRING,
            "AMPLITUDE_API_KEY",
            amplitudeKey
        )
    }
}
