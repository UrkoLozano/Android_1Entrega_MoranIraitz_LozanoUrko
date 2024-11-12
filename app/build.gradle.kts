plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.aplikazioa_iraitzmoran_urkolozano"
    compileSdk = 35  // Cambiado de 34 a 35

    defaultConfig {
        applicationId = "com.example.aplikazioa_iraitzmoran_urkolozano"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"  // Puedes cambiar la versión si es necesario
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependencias básicas
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Dependencias de Compose
    implementation("androidx.compose.ui:ui:1.5.1")  // Actualiza la versión si es necesario
    implementation("androidx.compose.material3:material3:1.1.0")  // Puedes cambiar la versión si es necesario
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")  // Herramientas de vista previa para Compose
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")  // Herramientas de vista previa en depuración

    // Navegación en Compose
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Dependencias de Coroutines y Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Para soporte de logs en Android
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0")
    implementation("androidx.compose.runtime:runtime-saveable:1.4.3")

    // Dependencias para las pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}

