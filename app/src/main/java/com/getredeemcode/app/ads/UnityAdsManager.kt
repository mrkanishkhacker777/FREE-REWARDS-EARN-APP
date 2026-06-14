package com.getredeemcode.app.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ═══════════════════════════════════════════════════════════════════════════
//  UNITY ADS CONSTANTS
// ═══════════════════════════════════════════════════════════════════════════
object UnityAdConstants {
    const val GAME_ID = "800005695"
    const val REWARDED_PLACEMENT = "Rewarded_Android"
    const val INTERSTITIAL_PLACEMENT = "Interstitial_Android"
    const val BANNER_PLACEMENT = "Banner_Android"
}

enum class AdState {
    IDLE, LOADING, LOADED, SHOWING, FAILED
}

// ═══════════════════════════════════════════════════════════════════════════
//  UNITY ADS MANAGER - CLEAN IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════
object UnityAdsManager {
    private const val TAG = "UnityAdsManager"
    private var isInitialized = false
    
    // Rewarded Ad State
    private val _rewardedState = MutableStateFlow(AdState.IDLE)
    val rewardedState: StateFlow<AdState> = _rewardedState
    private var rewardedCallback: ((Boolean) -> Unit)? = null
    
    // Interstitial Ad State
    private val _interstitialState = MutableStateFlow(AdState.IDLE)
    val interstitialState: StateFlow<AdState> = _interstitialState
    private var interstitialCallback: (() -> Unit)? = null
    
    // Banner Ad State
    private val _bannerState = MutableStateFlow(AdState.IDLE)
    val bannerState: StateFlow<AdState> = _bannerState
    
    // ───────────────────────────────────────────────────────────────────────
    //  INITIALIZE
    // ───────────────────────────────────────────────────────────────────────
    fun initialize(context: Context) {
        if (isInitialized) return
        isInitialized = true
        
        Log.d(TAG, "═══════════════════════════════════════════════════════")
        Log.d(TAG, "🎮 UNITY ADS - INITIALIZING")
        Log.d(TAG, "Game ID: ${UnityAdConstants.GAME_ID}")
        Log.d(TAG, "Rewarded: ${UnityAdConstants.REWARDED_PLACEMENT}")
        Log.d(TAG, "Interstitial: ${UnityAdConstants.INTERSTITIAL_PLACEMENT}")
        Log.d(TAG, "Banner: ${UnityAdConstants.BANNER_PLACEMENT}")
        
        UnityAds.initialize(
            context,
            UnityAdConstants.GAME_ID,
            false,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.d(TAG, "✅ Unity Ads initialized!")
                    Log.d(TAG, "═══════════════════════════════════════════════════════")
                    loadRewardedAd()
                    loadInterstitialAd()
                    loadBannerAd()
                }
                
                override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError, message: String) {
                    Log.e(TAG, "❌ Init failed: $error - $message")
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        initialize(context)
                    }, 5000)
                }
            }
        )
    }
    
    // ───────────────────────────────────────────────────────────────────────
    //  REWARDED AD
    // ───────────────────────────────────────────────────────────────────────
    private fun loadRewardedAd() {
        if (_rewardedState.value != AdState.IDLE) return
        
        _rewardedState.value = AdState.LOADING
        Log.d(TAG, "[Rewarded] Loading...")
        
        UnityAds.load(
            UnityAdConstants.REWARDED_PLACEMENT,
            object : IUnityAdsLoadListener {
                override fun onPlayerClicked(placementId: String) {}
                
                override fun onFailure(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
                    _rewardedState.value = AdState.FAILED
                    Log.e(TAG, "[Rewarded] Load failed: $error")
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        loadRewardedAd()
                    }, 5000)
                }
                
                override fun onSuccess(placementId: String) {
                    _rewardedState.value = AdState.LOADED
                    Log.d(TAG, "[Rewarded] ✅ Loaded")
                }
            }
        )
    }
    
    fun showRewardedAd(activity: Activity, onRewarded: (Boolean) -> Unit): Boolean {
        if (_rewardedState.value != AdState.LOADED) {
            Log.w(TAG, "[Rewarded] Not ready")
            onRewarded(false)
            loadRewardedAd()
            return false
        }
        
        _rewardedState.value = AdState.SHOWING
        rewardedCallback = onRewarded
        
        UnityAds.show(
            activity,
            UnityAdConstants.REWARDED_PLACEMENT,
            object : IUnityAdsShowListener {
                override fun onUserLeft() {}
                override fun onStart(placementId: String) {
                    Log.d(TAG, "[Rewarded] Started")
                }
                
                override fun onFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
                    _rewardedState.value = AdState.FAILED
                    Log.e(TAG, "[Rewarded] Show failed: $error")
                    rewardedCallback?.invoke(false)
                    rewardedCallback = null
                    loadRewardedAd()
                }
                
                override fun onFinish(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
                    val rewarded = state == UnityAds.UnityAdsShowCompletionState.COMPLETED
                    Log.d(TAG, "[Rewarded] Finished - Rewarded: $rewarded")
                    
                    _rewardedState.value = AdState.IDLE
                    rewardedCallback?.invoke(rewarded)
                    rewardedCallback = null
                    loadRewardedAd()
                }
            }
        )
        return true
    }
    
    fun isRewardedReady(): Boolean = _rewardedState.value == AdState.LOADED
    
    // ───────────────────────────────────────────────────────────────────────
    //  INTERSTITIAL AD
    // ───────────────────────────────────────────────────────────────────────
    private fun loadInterstitialAd() {
        if (_interstitialState.value != AdState.IDLE) return
        
        _interstitialState.value = AdState.LOADING
        Log.d(TAG, "[Interstitial] Loading...")
        
        UnityAds.load(
            UnityAdConstants.INTERSTITIAL_PLACEMENT,
            object : IUnityAdsLoadListener {
                override fun onPlayerClicked(placementId: String) {}
                
                override fun onFailure(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
                    _interstitialState.value = AdState.FAILED
                    Log.e(TAG, "[Interstitial] Load failed: $error")
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        loadInterstitialAd()
                    }, 5000)
                }
                
                override fun onSuccess(placementId: String) {
                    _interstitialState.value = AdState.LOADED
                    Log.d(TAG, "[Interstitial] ✅ Loaded")
                }
            }
        )
    }
    
    fun showInterstitialAd(activity: Activity, onDismissed: () -> Unit = {}): Boolean {
        if (_interstitialState.value != AdState.LOADED) {
            Log.w(TAG, "[Interstitial] Not ready")
            loadInterstitialAd()
            return false
        }
        
        _interstitialState.value = AdState.SHOWING
        interstitialCallback = onDismissed
        
        UnityAds.show(
            activity,
            UnityAdConstants.INTERSTITIAL_PLACEMENT,
            object : IUnityAdsShowListener {
                override fun onUserLeft() {}
                override fun onStart(placementId: String) {}
                
                override fun onFailure(placementId: String, error: UnityAds.UnityAdsShowError, message: String) {
                    _interstitialState.value = AdState.FAILED
                    Log.e(TAG, "[Interstitial] Show failed: $error")
                    interstitialCallback?.invoke()
                    interstitialCallback = null
                    loadInterstitialAd()
                }
                
                override fun onFinish(placementId: String, state: UnityAds.UnityAdsShowCompletionState) {
                    Log.d(TAG, "[Interstitial] Finished")
                    _interstitialState.value = AdState.IDLE
                    interstitialCallback?.invoke()
                    interstitialCallback = null
                    loadInterstitialAd()
                }
            }
        )
        return true
    }
    
    fun isInterstitialReady(): Boolean = _interstitialState.value == AdState.LOADED
    
    // ───────────────────────────────────────────────────────────────────────
    //  BANNER AD
    // ───────────────────────────────────────────────────────────────────────
    private fun loadBannerAd() {
        if (_bannerState.value != AdState.IDLE) return
        
        _bannerState.value = AdState.LOADING
        Log.d(TAG, "[Banner] Loading...")
        
        UnityAds.load(
            UnityAdConstants.BANNER_PLACEMENT,
            object : IUnityAdsLoadListener {
                override fun onPlayerClicked(placementId: String) {}
                
                override fun onFailure(placementId: String, error: UnityAds.UnityAdsLoadError, message: String) {
                    _bannerState.value = AdState.FAILED
                    Log.e(TAG, "[Banner] Load failed: $error")
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        loadBannerAd()
                    }, 5000)
                }
                
                override fun onSuccess(placementId: String) {
                    _bannerState.value = AdState.LOADED
                    Log.d(TAG, "[Banner] ✅ Loaded")
                }
            }
        )
    }
    
    fun isBannerReady(): Boolean = _bannerState.value == AdState.LOADED
}
