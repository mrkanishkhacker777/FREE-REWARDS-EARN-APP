# GetRedeemCode V5 — Play Store Final Checklist

## ✅ Is ZIP mein kya change hua (Points logic UNTOUCHED)

| File | Kya badla |
|------|-----------|
| `gradle.properties` | R8 full mode ON, build cache ON, JVM memory 4GB, parallel builds |
| `app/build.gradle.kts` | multidex added, ABI filters added, buildConfig=false |
| `app/proguard-rules.pro` | R8 full mode rules, compose state rules, multidex rules, `Log.i` bhi remove |
| `app/src/main/AndroidManifest.xml` | `AD_ID` permission, `MultiDexApplication`, `OPTIMIZE_AD_LOADING` meta-data, `extractNativeLibs=false` |
| `app/src/main/res/xml/network_security_config.xml` | AdMob ke extra domains add, sab HTTPS only |

## ⚠️ Aapko khud karna hai (machine pe)

### 1. Keystore banao (ek baar)
```bash
keytool -genkey -v \
  -keystore keystore/getredeemcode.jks \
  -alias getredeemcode \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

### 2. `app/build.gradle.kts` mein password fill karo
```kotlin
storePassword = "APNA_PASSWORD_YAHAN"
keyPassword   = "APNA_PASSWORD_YAHAN"
```

### 3. `local.properties` mein SDK path set karo
```
sdk.dir=C:\Users\APNA_NAME\AppData\Local\Android\Sdk   # Windows
# ya
sdk.dir=/Users/apna_name/Library/Android/sdk           # Mac
```

### 4. AAB build karo (Play Store ke liye)
```bash
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

### 5. APK build karo (direct install ke liye)
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

---

## 🔒 Points Logic — BILKUL NAHI CHEDA

Ye sab files 100% original hain, ek character bhi nahi badla:
- `AppViewModel.kt` — coin pools, smart coin, captcha coins, quiz coins, refer logic
- `AdManager.kt` — ad flow
- `HomeScreen.kt`, `ScratchScreen.kt`, `CaptchaScreen.kt`, `QuizScreen.kt`, `RedeemScreen.kt`, `ReferScreen.kt`
- `AppDataStore.kt`, `MainActivity.kt`, `Routes.kt`, `Theme.kt`, `CommonComponents.kt`
- `TopUpOverlay.kt`, `LoginScreen.kt`, `PolicyScreens.kt`

---

## 📋 Play Console Upload Steps

1. [play.google.com/console](https://play.google.com/console) → App select karo
2. Release → Production (ya Internal Testing pehle)
3. Create new release → `.aab` upload karo
4. Release notes likho (Hindi + English dono)
5. Review and rollout → 20% rollout se shuru karo

---

## 📱 Play Store Listing ke liye zaroori cheezein

- **App icon**: 512×512 PNG (Google Play Console pe upload)
- **Feature graphic**: 1024×500 PNG
- **Screenshots**: Min 2, max 8 (phone screenshots)
- **Short description**: Max 80 characters
- **Full description**: Max 4000 characters
- **Privacy Policy URL**: Required (kyunki app me AdMob hai)
- **Content rating**: IARC questionnaire fill karo (likely "Everyone")
- **Category**: Tools ya Entertainment

---

## 🎯 AdMob — Play Console mein link karo

1. AdMob console → Apps → Link to Play Store
2. App review ke baad live ads start honge
3. Test ke liye test device add karo (AdMob console → Test devices)
