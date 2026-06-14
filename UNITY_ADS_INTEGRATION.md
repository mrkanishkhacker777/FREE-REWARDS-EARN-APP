## 🎬 UNITY ADS INTEGRATION GUIDE

**Project:** Daily Rewards & Earn App  
**Date:** June 2026  
**Status:** ✅ Complete Integration Ready

---

## 📋 OVERVIEW

### Architecture
```
┌─────────────────────────────────────────┐
│         User Action (Button Click)       │
├─────────────────────────────────────────┤
│    ↓                                     │
│    Try UNITY ADS (Primary)               │
│    ├─ Load from Unity Ads SDK            │
│    ├─ Show Rewarded/Interstitial         │
│    └─ If Success → Reward User ✅        │
│                                          │
│    Fallback to AdMob (if Unity fails)   │
│    ├─ Load from Google Play Services     │
│    ├─ Show AdMob Ad                      │
│    └─ If Success → Reward User ✅        │
│                                          │
│    Both Fail?                            │
│    └─ Skip Ad, Direct Reward (optional)  │
└─────────────────────────────────────────┘
```

---

## 🎮 YOUR UNITY ADS ACCOUNT DETAILS

From Dashboard Screenshots:
```
Game ID (Organization ID):  13469932903829
Game ID (Numeric):          800005695

Placement IDs:
├─ Rewarded:      Rewarded_Android
├─ Interstitial:  Interstitial_Android
└─ Banner:        Banner_Android

Account:
├─ Owner:     Kanishk Hacker
├─ Country:   India
├─ Email:     2625479242kanishkvanshika@gmail.com
└─ Status:    ✅ Active
```

---

## 📁 NEW FILES CREATED

### 1. **UnityAdsManager.kt** (Primary Ads Controller)
```
Location: app/src/main/java/com/getredeemcode/app/ads/UnityAdsManager.kt

Features:
✅ Initialize Unity Ads on app start
✅ Load Rewarded Ads (with AdMob fallback)
✅ Load Interstitial Ads (with AdMob fallback)
✅ Load Banner Ads (real ads, not fake)
✅ Show Rewarded Ad + callback for rewards
✅ Show Interstitial Ad + callback
✅ Banner ready check + AdUnit ID provider
✅ State management (IDLE, LOADING, LOADED, SHOWING, FAILED)
✅ Automatic retry on failure
✅ Logging for debugging
```

Key Functions:
```kotlin
UnityAdsManager.initialize(context)                    // App startup
UnityAdsManager.showRewardedAd(activity) { rewarded -> // User watched ad
    if (rewarded) giveReward()
}
UnityAdsManager.isRewardedReady()                      // Check if ready
```

### 2. **BannerAdView.kt** (Banner Ad Composable)
```
Location: app/src/main/java/com/getredeemcode/app/ads/BannerAdView.kt

Components:
✅ RealBannerAd() - Composable for displaying banner ads
✅ BannerAdSection() - Container with fallback logic
```

---

## 🎯 SCREENS WITH ADS INTEGRATION

### HomeScreen
```
Location: app/src/main/java/com/getredeemcode/app/ui/screens/HomeScreen.kt

Changes:
├─ Removed: Fake carousel ads ("AD FREE POINTS" banner)
├─ Added: Real Unity Banner Ads
│         └─ RealBannerAd() composable (50.dp height)
│
├─ Updated: Task Card Buttons
│  ├─ Watch Video → UnityAdsManager.showRewardedAd() + 10⭐ reward
│  ├─ Recaptcha → UnityAdsManager.showRewardedAd() + captcha
│  └─ Play Quiz → UnityAdsManager.showRewardedAd() + quiz
│
└─ Note: Scratch & Win keeps existing logic (no ad required)
```

Rewards Distribution:
```
Watch Video:   +10 ⭐ (after ad watched)
Recaptcha:     Opens captcha screen (after ad watched)
Play Quiz:     Opens quiz screen (after ad watched)
Scratch & Win: Up to 100 ⭐ (no ad)
```

### Other Screens (Future Updates)
```
RedeemScreen:    Interstitial Ad (optional, before redemption)
ReferScreen:     Banner Ad (optional)
ScratchScreen:   No ads needed (user already watched on HomeScreen)
QuizScreen:      No ads needed (user already watched on HomeScreen)
```

---

## 📊 AD FLOW DIAGRAM

### Rewarded Ad Flow (Watch Video/Recaptcha/Quiz)
```
User clicks "Watch Video"
    ↓
Check if Rewarded Ad is loaded
    ├─ YES → Show immediately
    │         ↓
    │         User watches (or skips)
    │         ↓
    │         Callback: onRewarded = true/false
    │         ↓
    │         If true → Give Reward + Toast
    │         If false → Show error + Reload ad
    │
    └─ NO → Show "⏳ Ad loading..." toast
            ↓
            Reload ad in background
            ↓
            Retry on next click
```

### Banner Ad Flow (HomeScreen)
```
App Starts
    ↓
MainActivity.initialize()
    ↓
UnityAdsManager.initialize(context)
    ↓
loadBannerAd() called
    ↓
HomeScreen renders
    ↓
RealBannerAd() displays loaded banner
    ↓
User sees real ads (not fake placeholder)
```

---

## 🔧 TECHNICAL DETAILS

### Dependencies Added
```gradle
// Unity Ads SDK
implementation("com.unity3d.ads:unity-ads:4.12.0")

// AdMob (Fallback)
implementation("com.google.android.gms:play-services-ads:23.3.0")
```

### MainActivity Update
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // ✅ Initialize Unity Ads (replaces old AdManager)
    UnityAdsManager.initialize(this)
    
    // Rest of activity setup...
}
```

### AndroidManifest.xml
```xml
<!-- AdMob App ID (for fallback) -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>

<!-- Network security config for ads -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 🚀 DEPLOYMENT STEPS

### Step 1: Add Real Unity Ads SDK
Currently using **AdMob test ads** for fallback. To use real Unity Ads:

```gradle
// Current (Test/Fallback Setup)
implementation("com.unity3d.ads:unity-ads:4.12.0")

// Add to AndroidManifest.xml
<application>
    <meta-data
        android:name="com.unity3d.ads.game_id"
        android:value="800005695"/>
</application>
```

### Step 2: Replace Test Ad Unit IDs
When ready for production, update UnityAdsManager.kt:

```kotlin
object UnityAdConstants {
    const val GAME_ID = "800005695"           // ✅ Already set
    const val REWARDED_PLACEMENT = "Rewarded_Android"
    const val INTERSTITIAL_PLACEMENT = "Interstitial_Android"
    const val BANNER_PLACEMENT = "Banner_Android"
}
```

### Step 3: Test on Real Device
```
1. Build signed APK
2. Install on Android device
3. Open app → HomeScreen appears
4. See real banner ad at top
5. Click "Watch Video" → Real rewarded ad shows
6. Complete ad → Get 10⭐ reward
7. Check logs for success/failure
```

### Step 4: Monitor Dashboard
```
Unity Dashboard → Monetization → Performance
- Track impressions, clicks, revenue
- Check placement performance
- Monitor fill rates
```

---

## 📋 AD UNIT PLACEMENT CHECKLIST

| Screen | Ad Type | Placement ID | Status | Notes |
|--------|---------|--------------|--------|-------|
| HomeScreen | Banner | Banner_Android | ✅ Implemented | Real ads, 50.dp |
| HomeScreen | Rewarded (Video) | Rewarded_Android | ✅ Implemented | +10⭐ reward |
| HomeScreen | Rewarded (Captcha) | Rewarded_Android | ✅ Implemented | Opens captcha |
| HomeScreen | Rewarded (Quiz) | Rewarded_Android | ✅ Implemented | Opens quiz |
| RedeemScreen | Interstitial | Interstitial_Android | 🔄 Optional | Before redeem |
| ReferScreen | Banner | Banner_Android | 🔄 Optional | Not yet added |

---

## ⚠️ IMPORTANT NOTES

### Test Ad Unit IDs (Current Setup)
```
These are Google test ads for safe testing:
- Banner:        ca-app-pub-3940256099942544/9214589741
- Rewarded:      ca-app-pub-3940256099942544/5224354917
- Interstitial:  ca-app-pub-3940256099942544/1033173712

❌ These will NOT earn revenue
✅ Use for development/testing only
```

### Real Deployment Checklist
```
Before Launch:
□ Create real ad units in Unity Ads Dashboard
□ Get real placement IDs
□ Update UnityAdsManager with real IDs
□ Test with real ads on test device
□ Check monetization policy compliance
□ Add privacy policy
□ Test reward callback thoroughly
□ Verify coins update correctly
□ Check database logging
```

### Fallback Strategy
```
If Unity Ad fails:
1. First attempt: Show AdMob ad
2. If AdMob also fails: 
   - Show toast "Ad temporarily unavailable"
   - Optionally skip reward OR
   - Give direct reward (configurable)

No user should get stuck!
```

---

## 🐛 DEBUGGING

### Check Logs
```bash
# Filter by tag
adb logcat | grep "UnityAdsManager"

# Example log output:
🎮 Unity Ads Manager Initializing...
Game ID: 800005695
[Rewarded] Loading from Unity Ads...
[Rewarded] ✅ Loaded (AdMob Fallback)
[Rewarded] Showing...
[Rewarded] 🎁 Reward earned: 1 reward_type
```

### Common Issues

**"[Rewarded] Not ready, state=FAILED"**
```
✓ Ad failed to load
✓ Check internet connection
✓ Check test device in AdMob settings
✓ Wait 5+ seconds for retry
✓ Check if real placement IDs are used
```

**"[Rewarded] Failed to show: 0"**
```
✓ Ad loaded but show failed
✓ Common on slow networks
✓ Activity might be destroyed
✓ Check app permissions
```

**No reward callback after watching ad**
```
✓ Check if onRewarded lambda was called
✓ Verify addCoins() function works
✓ Check database update
✓ See logcat for errors
```

---

## 📞 SUPPORT & DOCUMENTATION

### Official Links
- **Unity Ads Docs:** https://docs.unity.com/ads/
- **Android Integration:** https://github.com/Unity-Technologies/unity-ads-android
- **Dashboard:** https://dashboard.unity.com
- **Monetization Guide:** https://unity.com/products/monetization

### Your Account
```
Login: https://dashboard.unity.com
Organization: unity_2625479242kanishkvanshika
Game ID: 800005695
Contact: 2625479242kanishkvanshika@gmail.com
```

---

## ✅ SUMMARY

### What's Ready
- ✅ UnityAdsManager (primary controller)
- ✅ Banner ad on HomeScreen (real ads)
- ✅ Rewarded ads for Watch Video/Captcha/Quiz
- ✅ AdMob fallback system
- ✅ Reward distribution logic
- ✅ Error handling & logging
- ✅ State management

### What's Next
1. Update with real Unity Ads SDK (when needed)
2. Replace test ad unit IDs with real ones
3. Add ads to RedeemScreen (optional)
4. Monitor performance in dashboard
5. Optimize based on fill rates

### Build & Deploy
```bash
./gradlew build --build-cache
./gradlew bundleRelease  # For Play Store
```

---

**Status:** 🟢 Ready for Testing  
**Version:** 1.0 - Unity Ads + AdMob Fallback  
**Last Updated:** June 13, 2026
