plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") // Necesario para Room

}

android {
    namespace = "com.example.aplicaciontareas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.aplicaciontareas"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // --- Dependencias base (ya incluidas en tu plantilla)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- ROOM (Base de datos local)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.ktx)

    // --- RETROFIT (Para conexión con API remota)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    // Si deseas usar Moshi en lugar de Gson:
    // implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

    // --- COROUTINES (para operaciones asíncronas)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // --- LIFECYCLE (ViewModel + LiveData)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // --- RecyclerView (para listas de tareas)
    implementation(libs.androidx.recyclerview)
    implementation(libs.gson.v2110)
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
}
