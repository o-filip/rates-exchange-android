import java.util.Properties
import com.android.builder.model.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.google.protobuf") version "0.9.1"
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

val getApkName: (BuildType, DefaultConfig) -> String = { buildType, defaultConfig ->
    "${defaultConfig.applicationId}-${buildType.name}" +
            "-v${defaultConfig.versionCode}-[${defaultConfig.versionName}].apk"
}

val localProps = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

val signingProps: Properties? = project.rootProject.file("signing.properties").takeIf { it.exists() }?.let {
    Properties().apply { load(it.inputStream()) }
}

val STRING = "String"
val CURRENCY_BEACON_API_KEY = "CURRENCY_BEACON_API_KEY"

android {
    namespace = "com.ofilip.exchange_rates"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ofilip.exchange_rates"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val currencyBeaconApiKey = localProps.getProperty("currencyBeacon.apiKey")
        if (currencyBeaconApiKey.isNullOrEmpty()) {
            throw GradleException("Currency Beacon API key is not set in local.properties, check readme.md (installation section) for more info.")
        } else {
            buildConfigField(STRING, CURRENCY_BEACON_API_KEY, "$currencyBeaconApiKey")
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
    implementation(libs.coreKtx)
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.lifecycleViewModelCompose)
    implementation(libs.activityCompose)
    implementation(libs.composeUi)
    implementation(libs.composeMaterial)
    implementation(libs.composeUiPreview)
    implementation(libs.composeNavigation)
    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    implementation(libs.gson)
    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigation)
    implementation(libs.hiltWork)
    implementation(libs.coroutines)
    implementation(libs.timber)
    implementation(libs.retrofitCore)
    implementation(libs.retrofitConverterJackson)
    implementation(libs.retrofitLogging)
    implementation(libs.datastorePreferences)
    implementation(libs.datastoreCore)
    implementation(libs.datastore)
    implementation(libs.protobuf)
    implementation(libs.workRuntimeKtx)
    implementation(libs.swipeRefresh)
    implementation(libs.systemUiController)
    implementation(libs.toolbarCompose)

    testImplementation(libs.testUnit)
    testImplementation(libs.testJunitJupiterApi)
    testImplementation(libs.testJunitJupiterEngine)
    testImplementation(libs.testJunitJupiterParams)
    testImplementation(libs.testMockitoCore)
    testImplementation(libs.testMockitoKotlin)
    testImplementation(libs.testAndroidxCore)
    testImplementation(libs.testWork)
    testImplementation(libs.textCoroutinesTest)

    debugImplementation(libs.debugComposeUiTooling)
    debugImplementation(libs.debugComposeUiManifest)

    kapt(libs.kaptRoomCompiler)
    kapt(libs.kaptHiltDaggerCompiler)
    kapt(libs.kaptHiltCompiler)
}

protobuf {
    protoc {
        artifact = libs.protobufProtoc.get().toString()
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
