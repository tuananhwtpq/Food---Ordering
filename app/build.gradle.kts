plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt) // Thêm plugin kapt
    alias(libs.plugins.hilt.android)  // Thêm plugin Hilt
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.food_order"
    // Sử dụng SDK 34 là phiên bản ổn định mới nhất.
    // Nếu bạn đã cài đặt bản Preview của SDK 35, bạn có thể đổi lại thành 35.
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.food_order"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    // 1. Core & UI (Sử dụng từ version catalog)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity) // Cần cho một số component

    // 2. Lifecycle Components (MVVM)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // 3. Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // 4. Networking (Retrofit & Gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation("com.google.android.gms:play-services-location:21.0.1")


    // 5. Image Loading (Glide)
    implementation(libs.glide)

    // 6. Dependency Injection (Hilt)
    implementation(libs.hilt.android)
//    implementation("androidx.compose.ui:ui-graphics-android:1.6.7")
//    //implementation(libs.activity)
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    kapt(libs.hilt.compiler)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
