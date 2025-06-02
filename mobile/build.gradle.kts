plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.jette.myphone"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.jette.myphone"
        minSdk = 28
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    ksp(libs.koin.ksp)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.app)
    implementation(libs.androidx.app.projected)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.legacy.support.v13)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.annotations)
    implementation(platform(libs.androidx.compose.bom))
    implementation(project.dependencies.platform(libs.koin.bom))

    debugImplementation(libs.ui.tooling)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
