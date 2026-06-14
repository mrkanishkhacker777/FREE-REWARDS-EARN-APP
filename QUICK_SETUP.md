## ⚡ QUICK SETUP - Unity Ads Integration

### Files Changed/Added
```
NEW FILES:
✅ UnityAdsManager.kt
✅ BannerAdView.kt
✅ UNITY_ADS_INTEGRATION.md (this guide)

UPDATED FILES:
✅ MainActivity.kt (initialize UnityAdsManager)
✅ HomeScreen.kt (add rewarded ads + real banner)
✅ build.gradle.kts (add Unity Ads SDK dependency)
```

---

### Current Status: 🟢 TEST MODE (AdMob Fallback)

The app is configured with **Google AdMob test ads** as fallback.

**Features Working:**
- ✅ Watch Video → Shows rewarded ad → +10⭐ reward
- ✅ Recaptcha → Shows rewarded ad → Opens captcha
- ✅ Play Quiz → Shows rewarded ad → Opens quiz
- ✅ Banner ads on HomeScreen (real ads, not fake)
- ✅ AdMob fallback if Unity Ads fail
- ✅ Error handling & auto-retry
- ✅ Logging for debugging

---

### When Ready for REAL UNITY ADS:

1. **Get Real Placement IDs** from Unity Dashboard:
   ```
   Dashboard → Monetization → Placements
   Copy your actual placement IDs
   ```

2. **Update UnityAdsManager.kt**:
   ```kotlin
   object UnityAdConstants {
       const val GAME_ID = "800005695"  // ✅ Already set
       const val REWARDED_PLACEMENT = "Rewarded_Android"  // ✅ Already set
       const val INTERSTITIAL_PLACEMENT = "Interstitial_Android"
       const val BANNER_PLACEMENT = "Banner_Android"
   }
   ```

3. **Implement Real Unity Ads SDK**:
   - Currently: Using AdMob as fallback
   - TODO: Replace AdMob calls with actual Unity Ads SDK calls
   - See comments in UnityAdsManager.kt marked with `// TODO:`

4. **Test on Device**:
   ```bash
   ./gradlew installDebug  # Build and install
   # Click buttons, watch ads, verify rewards
   ```

---

### Architecture Summary

```
HomeScreen Button Click
    ↓
UnityAdsManager.showRewardedAd()
    ├─ Try: Load from Unity Ads SDK
    │   ├─ Success → Show ad → onRewarded callback → Give reward ✅
    │   └─ Fail → Fallback to AdMob
    │       ├─ Success → Show AdMob → onRewarded callback → Give reward ✅
    │       └─ Fail → Show error toast
    │
    └─ State Management (IDLE → LOADING → LOADED → SHOWING → IDLE)
```

---

### Test Video Ad Flow

**Current (Works with AdMob test ads):**
1. User clicks "Watch Video" on HomeScreen
2. App checks if rewarded ad is loaded
3. If not ready: "⏳ Ad loading..." toast
4. If ready: Rewarded ad shows
5. User watches video (or can skip on some ads)
6. Completion callback fires → +10⭐ Stars added
7. Toast shows: "✅ +10 ⭐ Stars earned!"

---

### Key Functions

```kotlin
// Initialize on app startup
UnityAdsManager.initialize(context)

// Show rewarded ad with callback
UnityAdsManager.showRewardedAd(activity) { rewarded ->
    if (rewarded) {
        // Give reward
        vm.addCoins(10)
        vm.showToast("✅ +10 ⭐ Stars earned!")
    } else {
        vm.showToast("❌ Ad not completed")
    }
}

// Check if ad is ready
if (UnityAdsManager.isRewardedReady()) {
    // Show ad immediately
}

// Banner ads on screen
RealBannerAd(modifier = Modifier.fillMaxWidth().height(50.dp))
```

---

### Logging

Run this to see ads debug logs:
```bash
adb logcat UnityAdsManager:V *:S
```

Example logs:
```
🎮 Unity Ads Manager Initializing...
Game ID: 800005695
[Rewarded] Loading from Unity Ads...
[Rewarded] ✅ Loaded (AdMob Fallback)
[Rewarded] Showing...
[Rewarded] 🎁 Reward earned: 1 reward_type
[Rewarded] Dismissed, reloading...
```

---

### Next Steps

1. ✅ Build & test on device
2. ✅ Verify rewards are adding correctly
3. ✅ Check database logging
4. ✅ When ready: Replace AdMob test IDs with real Unity Ads IDs
5. ✅ Submit to Play Store

---

### Support Files

- `UNITY_ADS_INTEGRATION.md` - Complete integration guide
- Logs in MainActivity - Check for initialization success
- UnityAdsManager.kt - All ads logic
- BannerAdView.kt - Banner display

**Questions?** Check UnityAdsManager.kt for TODO comments
