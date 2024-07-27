import com.android.build.gradle.internal.dsl.DefaultConfig
import com.android.builder.model.BuildType
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.detekt)
    alias(libs.plugins.junit5)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
}

val getApkName: (BuildType, DefaultConfig) -> String = { buildType, defaultConfig ->
    "${defaultConfig.applicationId}-${buildType.name}" +
            "-v${defaultConfig.versionCode}-[${defaultConfig.versionName}].apk"
}

val localProps = Properties().apply {
    load(project.rootProject.file("local.properties").takeIf { it.exists() }?.inputStream())
}

val signingProps: Properties? =
    project.rootProject.file("signing.properties").takeIf { it.exists() }?.let {
        Properties().apply { load(it.inputStream()) }
    }

val string = "String"
val CURRENCY_BEACON_API_KEY = "CURRENCY_BEACON_API_KEY"

android {
    namespace = "com.ofilip.exchange_rates"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ofilip.exchange_rates"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val currencyBeaconApiKey = localProps.getProperty("currencyBeacon.apiKey")
        if (currencyBeaconApiKey.isNullOrEmpty()) {
            throw GradleException("Currency Beacon API key is not set in local.properties, check readme.md (installation section) for more info.")
        } else {
            buildConfigField(string, CURRENCY_BEACON_API_KEY, currencyBeaconApiKey)
        }
    }

    applicationVariants.all {
        outputs.all {
            // Rename APKs
            val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            outputImpl.outputFileName = getApkName(buildType, defaultConfig)
        }
    }

    signingConfigs {
        signingProps?.let {
            findByName("debug") ?: create("debug") {
                keyAlias = it.getProperty("debug.keyAlias")
                keyPassword = it.getProperty("debug.keyPassword")
                storeFile = file(it.getProperty("debug.storeFile"))
                storePassword = it.getProperty("debug.storePassword")
            }

            findByName("release") ?: create("release") {
                keyAlias = it.getProperty("release.keyAlias")
                keyPassword = it.getProperty("release.keyPassword")
                storeFile = file(it.getProperty("release.storeFile"))
                storePassword = it.getProperty("release.storePassword")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-debug"
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            if (signingConfigs.findByName("release") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        create("qa") {
            initWith(getByName("release"))
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            isJniDebuggable = true
            versionNameSuffix = "-qa"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = true
        javacOptions {
            option("-source", "17")
            option("-target", "17")
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.navigation)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    implementation(libs.coroutines)
    implementation(libs.timber)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    implementation(libs.datastore)
    implementation(libs.protobuf)
    implementation(libs.work.runtime.ktx)
    implementation(libs.swipe.refresh)
    implementation(libs.system.ui.controller)
    implementation(libs.toolbar.compose)
    implementation(libs.joda.time)
    implementation(libs.material3.android)
    implementation(libs.flow.layout)
    implementation(libs.vico.compose)
    implementation(libs.vico.core)
    implementation(libs.vico.views)
    implementation(libs.y.charts)
    implementation(libs.compose.shimmer)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.stdlib)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)

    // Test
    testImplementation(libs.test.unit)
    testImplementation(libs.test.junit.jupiter.api)
    testImplementation(libs.test.junit.jupiter.engine)
    testImplementation(libs.test.junit.jupiter.params)
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.mockito.kotlin)
    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.work)
    testImplementation(libs.test.coroutines.test)
    testImplementation(libs.joda.time)

    // Debug
    debugImplementation(libs.debug.compose.ui.tooling)
    debugImplementation(libs.debug.compose.ui.manifest)

    // Ksp
    ksp(libs.ksp.room.compiler)

    // Kapt
    kapt(libs.kapt.hilt.dagger.compiler)
    kapt(libs.kapt.hilt.compiler)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
