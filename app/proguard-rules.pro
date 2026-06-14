# ─── App classes ──────────────────────────────────────────────────────────────
-keep class com.getredeemcode.app.** { *; }

# ─── Google AdMob / Play Services Ads ────────────────────────────────────────
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.measurement.** { *; }
-dontwarn com.google.android.gms.**

# ─── Kotlin Coroutines ────────────────────────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ─── Jetpack Compose ──────────────────────────────────────────────────────────
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ─── DataStore ────────────────────────────────────────────────────────────────
-keep class androidx.datastore.** { *; }
-keep class androidx.datastore.preferences.** { *; }

# ─── AndroidX / Lifecycle / Navigation ───────────────────────────────────────
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }
-keep class androidx.activity.** { *; }

# ─── Multi-dex ────────────────────────────────────────────────────────────────
-keep class androidx.multidex.** { *; }

# ─── Kotlin ───────────────────────────────────────────────────────────────────
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Lazy { *; }

# ─── Enum classes ─────────────────────────────────────────────────────────────
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ─── Serialization safety ─────────────────────────────────────────────────────
-keepclassmembers class * {
    @kotlin.jvm.JvmStatic <methods>;
}

# ─── Compose State (mutableStateOf, remember, etc.) ──────────────────────────
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep @androidx.compose.runtime.Stable class * { *; }
-keep @androidx.compose.runtime.Immutable class * { *; }

# ─── R8 Full Mode — interface default methods ─────────────────────────────────
-keepclassmembers interface * {
    default <methods>;
}

# ─── Remove debug logs in release ────────────────────────────────────────────
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
