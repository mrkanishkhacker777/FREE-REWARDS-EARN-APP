package com.getredeemcode.app.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ─── AdMob IDs ────────────────────────────────────────────────────────────────
object AdConstants {
    const val APP_ID = "ca-app-pub-2366599730777996~6682788999"

    // Rewarded Ad — video / scratch / captcha ke liye
    const val REWARDED_AD_ID_PRIMARY = "ca-app-pub-2366599730777996/9102809483"
    const val REWARDED_AD_ID_BACKUP  = "ca-app-pub-2366599730777996/9820005102"

    // Interstitial Ad — banner click ke liye
    // TODO: AdMob console mein alag Interstitial ad unit banao aur yahan replace karo
    // Abhi rewarded ID se alag rakha hai — apni real interstitial unit ID daalo
    const val INTERSTITIAL_AD_ID_PRIMARY = "ca-app-pub-2366599730777996/1135468367"
    const val INTERSTITIAL_AD_ID_BACKUP  = "ca-app-pub-2366599730777996/6754626551"
}

// ─── Ad Load State ────────────────────────────────────────────────────────────
enum class AdState {
    IDLE, LOADING, LOADED, SHOWING, FAILED
}

// ─── Ad Manager Singleton ─────────────────────────────────────────────────────
object AdManager {

    private const val TAG = "AdManager"
    private var isInitialized = false

    // ════════════════════════════════════════════════════════
    //  REWARDED AD  (video / scratch / captcha)
    // ════════════════════════════════════════════════════════
    private var rewardedAd: RewardedAd? = null
    private var rewardedUsingBackup = false
    private var rewardedFailCount = 0

    private val _adState = MutableStateFlow(AdState.IDLE)
    val adState: StateFlow<AdState> = _adState

    private var onAdClosed: ((Boolean) -> Unit)? = null

    // ════════════════════════════════════════════════════════
    //  INTERSTITIAL AD  (banner click)
    // ════════════════════════════════════════════════════════
    private var interstitialAd: InterstitialAd? = null
    private var interstitialUsingBackup = false

    private val _interstitialState = MutableStateFlow(AdState.IDLE)
    val interstitialState: StateFlow<AdState> = _interstitialState

    // ── Init (MainActivity se ek baar call karo) ──────────────────────────────
    fun initialize(context: Context) {
        if (isInitialized) return
        isInitialized = true
        MobileAds.initialize(context) {
            Log.d(TAG, "MobileAds initialized")
            loadRewardedAd(context, useBackup = false)
            loadInterstitialAd(context, useBackup = false)
        }
    }

    // ════════════════════════════════════════════════════════
    //  REWARDED AD — Load / Show
    // ════════════════════════════════════════════════════════

    fun loadRewardedAd(context: Context, useBackup: Boolean = false) {
        if (_adState.value == AdState.LOADING || _adState.value == AdState.LOADED) return
        _adState.value = AdState.LOADING

        val adUnitId = if (useBackup) {
            rewardedUsingBackup = true
            Log.w(TAG, "[Rewarded] BACKUP load (primary failed $rewardedFailCount times)")
            AdConstants.REWARDED_AD_ID_BACKUP
        } else {
            rewardedUsingBackup = false
            Log.d(TAG, "[Rewarded] PRIMARY load...")
            AdConstants.REWARDED_AD_ID_PRIMARY
        }

        RewardedAd.load(context, adUnitId, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    _adState.value = AdState.LOADED
                    if (!rewardedUsingBackup) rewardedFailCount = 0
                    Log.d(TAG, "[Rewarded] Loaded ✅ [${if (rewardedUsingBackup) "BACKUP" else "PRIMARY"}]")
                    setupRewardedCallback(context)
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    _adState.value = AdState.FAILED
                    Log.e(TAG, "[Rewarded] Failed [${if (rewardedUsingBackup) "BACKUP" else "PRIMARY"}]: ${error.message}")
                    if (!rewardedUsingBackup) {
                        rewardedFailCount++
                        loadRewardedAd(context, useBackup = true)
                    } else {
                        rewardedUsingBackup = false
                        Log.e(TAG, "[Rewarded] Both PRIMARY + BACKUP failed.")
                    }
                }
            })
    }

    private fun setupRewardedCallback(context: Context) {
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() { _adState.value = AdState.SHOWING }
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null; _adState.value = AdState.IDLE
                loadRewardedAd(context, useBackup = false)
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedAd = null; _adState.value = AdState.FAILED
                onAdClosed?.invoke(false); onAdClosed = null
                loadRewardedAd(context, useBackup = false)
            }
        }
    }

    /** Rewarded ad show karo. Returns true = shown, false = ready nahi tha */
    fun showRewardedAd(activity: Activity, onRewarded: (Boolean) -> Unit): Boolean {
        val ad = rewardedAd
        if (ad == null || _adState.value != AdState.LOADED) {
            Log.w(TAG, "[Rewarded] Not ready, state=${_adState.value}")
            onRewarded(false)
            loadRewardedAd(activity, useBackup = false)
            return false
        }

        onAdClosed = onRewarded
        var rewardGranted = false

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                _adState.value = AdState.SHOWING
                Log.d(TAG, "[Rewarded] Showing [${if (rewardedUsingBackup) "BACKUP" else "PRIMARY"}]")
            }
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "[Rewarded] Dismissed, reward=$rewardGranted")
                rewardedAd = null; _adState.value = AdState.IDLE
                onAdClosed?.invoke(rewardGranted); onAdClosed = null
                loadRewardedAd(activity, useBackup = false)
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[Rewarded] Failed to show: ${error.message}")
                rewardedAd = null; _adState.value = AdState.FAILED
                onAdClosed?.invoke(false); onAdClosed = null
                loadRewardedAd(activity, useBackup = false)
            }
        }

        ad.show(activity) { rewardItem ->
            rewardGranted = true
            Log.d(TAG, "[Rewarded] Reward earned: ${rewardItem.amount} ${rewardItem.type}")
        }
        return true
    }

    fun isAdReady(): Boolean = rewardedAd != null && _adState.value == AdState.LOADED

    // ════════════════════════════════════════════════════════
    //  INTERSTITIAL AD — Load / Show  (banner click ke liye)
    // ════════════════════════════════════════════════════════

    fun loadInterstitialAd(context: Context, useBackup: Boolean = false) {
        if (_interstitialState.value == AdState.LOADING || _interstitialState.value == AdState.LOADED) return
        _interstitialState.value = AdState.LOADING

        val adUnitId = if (useBackup) {
            interstitialUsingBackup = true
            Log.w(TAG, "[Interstitial] BACKUP load")
            AdConstants.INTERSTITIAL_AD_ID_BACKUP
        } else {
            interstitialUsingBackup = false
            Log.d(TAG, "[Interstitial] PRIMARY load...")
            AdConstants.INTERSTITIAL_AD_ID_PRIMARY
        }

        InterstitialAd.load(context, adUnitId, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    _interstitialState.value = AdState.LOADED
                    Log.d(TAG, "[Interstitial] Loaded ✅ [${if (interstitialUsingBackup) "BACKUP" else "PRIMARY"}]")
                    setupInterstitialCallback(context)
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    _interstitialState.value = AdState.FAILED
                    Log.e(TAG, "[Interstitial] Failed [${if (interstitialUsingBackup) "BACKUP" else "PRIMARY"}]: ${error.message}")
                    if (!interstitialUsingBackup) {
                        loadInterstitialAd(context, useBackup = true)
                    } else {
                        interstitialUsingBackup = false
                        Log.e(TAG, "[Interstitial] Both PRIMARY + BACKUP failed.")
                    }
                }
            })
    }

    private fun setupInterstitialCallback(context: Context) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() { _interstitialState.value = AdState.SHOWING }
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "[Interstitial] Dismissed")
                interstitialAd = null; _interstitialState.value = AdState.IDLE
                loadInterstitialAd(context, useBackup = false)
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[Interstitial] Failed to show: ${error.message}")
                interstitialAd = null; _interstitialState.value = AdState.FAILED
                loadInterstitialAd(context, useBackup = false)
            }
        }
    }

    /** Interstitial ad show karo (banner click pe). Returns true = shown */
    fun showInterstitialAd(activity: Activity, onDismissed: () -> Unit = {}): Boolean {
        val ad = interstitialAd
        if (ad == null || _interstitialState.value != AdState.LOADED) {
            Log.w(TAG, "[Interstitial] Not ready, state=${_interstitialState.value}")
            loadInterstitialAd(activity, useBackup = false)
            return false
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                _interstitialState.value = AdState.SHOWING
                Log.d(TAG, "[Interstitial] Showing [${if (interstitialUsingBackup) "BACKUP" else "PRIMARY"}]")
            }
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "[Interstitial] Dismissed")
                interstitialAd = null; _interstitialState.value = AdState.IDLE
                onDismissed()
                loadInterstitialAd(activity, useBackup = false)
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[Interstitial] Failed to show: ${error.message}")
                interstitialAd = null; _interstitialState.value = AdState.FAILED
                onDismissed()
                loadInterstitialAd(activity, useBackup = false)
            }
        }

        ad.show(activity)
        return true
    }

    fun isInterstitialReady(): Boolean =
        interstitialAd != null && _interstitialState.value == AdState.LOADED
}
