# GetRedeemCode V5 — Build Guide

## ══════════════════════════════════════════
## AndroidIDE (Mobile) se APK/AAB Build Karna
## ══════════════════════════════════════════

### IMPORTANT — Pehle Yeh Karo (ek hi baar)

**Step 0 — Folder rename karo (ZARURI)**
File manager se project folder rename karo — naam mein SPACE NAHI HONA CHAHIYE:
- GALAT: `GetRedeemCode_VK 143`
- SAHI: `GetRedeemCode_VK143`

Space hone par `cd: too many arguments` error aata hai.

---

### Step 1 — Project Open Karo
1. AndroidIDE open karo
2. **Open Project** → renamed folder ke andar `v5_fixed_final` select karo
3. Gradle sync shuru hogi (internet chahiye ~150MB download)

### Step 2 — Terminal se Build Karo

AndroidIDE ka terminal (`>_`) kholo:

```bash
# Sahi folder mein jao (space nahi hai naam mein ab)
cd "/storage/internal_new/project/GetRedeemCode_VK143/v5_fixed_final"

# Permission do
chmod +x gradlew

# Debug APK banao (keystore ZAROORAT NAHI)
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/app-debug.apk`

---

### Step 3 — Release AAB (Play Store ke liye)

```bash
# Keystore banao (ek hi baar)
keytool -genkey -v \
  -keystore keystore/getredeemcode.jks \
  -alias getredeemcode \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000

# AAB build karo
./gradlew bundleRelease
```

**Output:** `app/build/outputs/bundle/release/app-release.aab`

---

### Common Errors aur Fix

| Error | Fix |
|-------|-----|
| `cd: too many arguments` | Folder ka naam rename karo — space hata do |
| `server starting...wait` (loop) | AndroidIDE force close karo, RAM free karo |
| `Could not create JVM` | `chmod +x gradlew` karo |
| `Gradle sync failed` | Internet check karo, retry karo |

---

## ══════════════════════════════════════════
## Android Studio (PC) se Build Karna
## ══════════════════════════════════════════

**Prerequisites:** Android Studio Hedgehog+, JDK 17, Android SDK 35

1. Project open karo
2. Gradle sync karo
3. **Build → Generate Signed Bundle / APK**

---

## Ad Unit IDs

| Type | ID |
|------|----|
| App ID | ca-app-pub-2366599730777996~6682788999 |
| Rewarded Primary | ca-app-pub-2366599730777996/9102809483 |
| Rewarded Backup | ca-app-pub-2366599730777996/9820005102 |
| Interstitial Primary | ca-app-pub-2366599730777996/1135468367 |
| Interstitial Backup | ca-app-pub-2366599730777996/6754626551 |
