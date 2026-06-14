package com.getredeemcode.app.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getredeemcode.app.data.AppDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.app.Activity

// ─── Exact Redeem Code Pools from HTML ───────────────────────────────────────
val CODES_1000 = listOf(
    "GKGXPJM8SXMBFR35","DRBGMSZA1RTA0VKZ","F2CZWR3TB5Z2F3V2","ANZ5CGF5SDV5942K",
    "9EU9GW9T1MBW5HWL","3MWRD8FA392V7VGF","E8PSM04NEPB8A1P6","JMYZG0FN55NUVZXH",
    "4YMXKPZZ4EL5ERWU","G3VWDXUJUTX5HF95","6B6L5LGZYLAPKPTY","221J9N46M1ZVMVD2",
    "70589VDCPJMXH7EE","97Y1YV8BVNY9V2YG","7W3KZTF8US8NCRYH","1R1WEYA3XSZPFZAX",
    "8ZNXZFGUG9UV1C2X","CAR4MGHEMCLBF3PT","6BP8Z0ELK8DP41F1","30XWDT2VLZ40DE8B",
    "JF0PRP8EG1UK7L2R","K5P5GKVA70J1B882","6D7U1XHJTX2DJT3R","FLUT347D7ELN04S8"
)
val CODES_1650 = listOf(
    "2PBB8C0A0PB3HLMP","09RWLEVXYF0PRPJ8","9J9V03LW48HMVH1V","8GP68G4NP1TZWCTW",
    "118MZPEV8XBGF13S","HWXE7URKR745SBRA","BLFDP18FG3C2JLN2","KLV6NBXZTBZ0PWDC",
    "HPCCW9EUU2U5NAG1","G11JD31TGWPP3RV2","AYTCCREVAPPAL4UY","2X41T50173R8ZAZ8",
    "HC9P9WMW4P8N8VEA","8FD98V2YPK3NZ0SU","DWS4DURC53T4XVL1","689EP8K09BVFXHCL",
    "7C2F2D11KPFP1N5Z","1W6P2P3ZSKW3S5JY","71GJU6KMYF1P1RTN","5RRRFJVLPN08BF2C",
    "C57W47J6W9HJ52CY","FVFAB0YC66JKVW6L","7PMMXWKSLAGCG8VS","C4AX6W534APFKA6Z"
)
val CODES_2000 = listOf(
    "H1LSYY6FLZVCK1VJ","94W969TW11ENBUD7","HPAZXFXFV5FEEC1S","F3VRMMA9U720T504",
    "6YAA4P4DMHKTS3G9","FR2CCB44WE130PZZ","EDTCY3MZ59E09SNK","9BM24RVNCZRRVC64",
    "F5XE3VYE38860Y9U","6ANUD6P9NJETT822","505SUN7D4KC6SE9M","6N8E5WZUZKS9K6W5",
    "6SEHGLX1C9U1DP0Y","59A6F9PUYLZSUUMU","KCE5WLMM4J9N2HVB","151862EWN9DWYHK7",
    "8EV82EY4DG8XT205","AZFKDL863YGPALRB","25NSWR374TTN3181","GVMNAGSGS4DJSAPU",
    "HGY7X71CXYEXFU0F","6LW1B5ESFEYFP5C5","J0X905N0TCJZXAFZ"
)

// ─── Coin Pools (exact from HTML) ─────────────────────────────────────────────
val SUB_ONE_COIN_POOL = listOf(
    0.03,0.05,0.07,0.08,0.09,0.10,0.11,0.12,0.13,0.14,
    0.15,0.16,0.17,0.18,0.19,0.20,0.21,0.22,0.23,0.24,
    0.25,0.26,0.27,0.28,0.29,0.30,0.31,0.32,0.33,0.34,
    0.35,0.36,0.37,0.38,0.39,0.40,0.41,0.42,0.43,0.44,
    0.45,0.46,0.47,0.48,0.49,
    0.50,0.51,0.53,0.55,0.57,0.60,0.62,0.64,0.66,0.68,
    0.70,0.72,0.75,0.77,0.80,0.83,0.87
)
val SUB_ONE_COIN_POOL_DEEP = listOf(
    0.005,0.006,0.007,0.008,0.009,
    0.010,0.011,0.012,0.013,0.014,0.015,0.016,0.017,0.018,0.019,
    0.020,0.021,0.022,0.023,0.024,0.025,0.026,0.027,0.028,0.029,
    0.030,0.031,0.032,0.033,0.034,0.035,0.036,0.037,0.038,0.039,
    0.040,0.041,0.042,0.043,0.044,0.045,0.046,0.047,0.048,0.049,
    0.050,0.055,0.060,0.065,0.070,0.075,0.080,0.085,0.090,0.095,0.099
)
val SUB_ONE_COIN_POOL_ULTRA = listOf(
    0.00001,0.00002,0.00003,0.00004,0.00005,0.00006,0.00007,0.00008,0.00009,
    0.000011,0.000022,0.000033,0.000044,0.000055,0.000066,0.000077,0.000088,0.000099,
    0.000015,0.000025,0.000035,0.000045,0.000055,0.000065,0.000075,0.000085,0.000095
)
val NORMAL_COIN_POOL_A = listOf(
    1,1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,5,5,5,5,5,5,
    6,6,6,6,6,6,6,6,6,6,6,6,6,6,
    7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
    8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
    9,9,9,9,9,9,9,9,9,9,9,9,
    10,10,10,10,10,10,10,10,10,10
).map { it.toDouble() }
val NORMAL_COIN_POOL_B = listOf(
    1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,1.0,1.2,1.5,1.7,1.9,
    2.0,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,2.0,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,2.0,2.2,2.5,2.7,2.9,
    3.0,3.1,3.2,3.3,3.4,3.5,3.6,3.7,3.8,3.9,3.0,3.2,3.5,3.7,3.9,3.1,3.3,3.6,3.8,
    4.0,4.1,4.2,4.3,4.5,4.6,4.7,4.8,4.9,4.0,4.3,4.6,4.9,4.2,4.5,
    5.0,5.1,5.2,5.3,5.5,5.6,5.7,5.8,5.9,5.0,5.3,5.5,5.7,5.9,5.2
)
val NORMAL_COIN_POOL_C = listOf(
    1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,
    1.0,1.2,1.4,1.6,1.8,1.1,1.3,1.5,1.7,1.9,
    2.0,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,2.0,2.2,2.4,2.6,2.8,2.1,2.3,2.5,2.7,2.9
)
val CAPTCHA_COIN_POOL = listOf(
    0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,0.50,
    0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,0.75,
    1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,
    1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,
    1.20,1.20,1.20,1.20,1.20,1.20,1.20,1.20,1.20,1.20,1.20,1.20,
    1.33,1.33,1.33,1.33,1.33,1.33,1.33,1.33,1.33,1.33,
    1.50,1.50,1.50,1.50,1.50,1.50,1.50,
    2.00,2.00,2.00
)

// ─── Quiz Data ────────────────────────────────────────────────────────────────
data class QuizQuestion(val question: String, val options: List<String>, val answer: Int)

val QUIZ_DATA = listOf(
    QuizQuestion("What is the capital of India?", listOf("Mumbai","New Delhi","Kolkata","Chennai"), 1),
    QuizQuestion("How many states are in India?", listOf("25","27","28","29"), 2),
    QuizQuestion("Which planet is called the Red Planet?", listOf("Venus","Jupiter","Mars","Saturn"), 2),
    QuizQuestion("Who invented the telephone?", listOf("Edison","Tesla","Bell","Newton"), 2),
    QuizQuestion("What is 12 × 12?", listOf("124","144","148","132"), 1),
    QuizQuestion("Which country is the largest by area?", listOf("China","USA","Canada","Russia"), 3),
    QuizQuestion("How many continents are there?", listOf("5","6","7","8"), 2),
    QuizQuestion("What is the chemical symbol for Gold?", listOf("Go","Gd","Au","Ag"), 2),
    QuizQuestion("Who painted the Mona Lisa?", listOf("Picasso","Da Vinci","Raphael","Michelangelo"), 1),
    QuizQuestion("What is the boiling point of water?", listOf("90°C","100°C","110°C","120°C"), 1)
)

val QUIZ_WEBVIEW_QUESTIONS = listOf(
    QuizQuestion("1/2 - Human body has how many ribs?", listOf("12","18","24"), 0),
    QuizQuestion("2/2 - Which planet is closest to the Sun?", listOf("Venus","Mercury","Mars"), 1),
    QuizQuestion("1/2 - How many bones are in the adult human body?", listOf("186","206","226"), 1),
    QuizQuestion("2/2 - Which is the largest ocean on Earth?", listOf("Atlantic","Indian","Pacific"), 2),
    QuizQuestion("1/2 - Who invented the telephone?", listOf("Edison","Bell","Tesla"), 1),
    QuizQuestion("2/2 - What is the capital of Japan?", listOf("Beijing","Seoul","Tokyo"), 2),
    QuizQuestion("1/2 - How many players are in a cricket team?", listOf("9","11","13"), 1),
    QuizQuestion("2/2 - Which metal is liquid at room temperature?", listOf("Mercury","Silver","Gold"), 0)
)

val QUIZ_SITE_URLS = listOf("qureka.com","quiz.cashkaro.com","qureka.com","quiz.meesho.com")

// ─── Data Models ──────────────────────────────────────────────────────────────
data class ReferEntry(val name: String, val date: String, val coins: Double)

data class Currency(val code: String, val symbol: String, val name: String, val rate: Double)
val CURRENCIES = listOf(
    Currency("USD","$","US Dollar",1.0), Currency("INR","₹","Indian Rupee",83.5),
    Currency("EUR","€","Euro",0.92), Currency("GBP","£","British Pound",0.79),
    Currency("JPY","¥","Japanese Yen",149.0), Currency("CNY","¥","Chinese Yuan",7.24),
    Currency("AUD","A$","Australian Dollar",1.53), Currency("CAD","C$","Canadian Dollar",1.36),
    Currency("SGD","S$","Singapore Dollar",1.34), Currency("AED","د.إ","UAE Dirham",3.67),
    Currency("SAR","﷼","Saudi Riyal",3.75), Currency("MYR","RM","Malaysian Ringgit",4.72),
    Currency("BDT","৳","Bangladeshi Taka",110.0), Currency("PKR","Rs","Pakistani Rupee",278.0),
    Currency("NGN","₦","Nigerian Naira",1590.0), Currency("BRL","R$","Brazilian Real",4.97),
    Currency("KRW","₩","South Korean Won",1330.0), Currency("TRY","₺","Turkish Lira",32.0)
)

data class TopUpPlan(val id: Int, val stars: Int, val priceUSD: Double, val label: String, val bonus: String = "")
val TOPUP_PLANS = listOf(
    TopUpPlan(1, 40, 0.99, "40 Stars · Starter Pack", "135+ countries"),
    TopUpPlan(2, 100, 1.99, "100 Stars · Best Value", "2x value! Most popular 🔥"),
    TopUpPlan(3, 220, 3.99, "220 Stars · Power Pack", "+20 Bonus Stars 🎉 Best deal")
)

data class PaymentApp(val id: String, val name: String, val emoji: String, val packageName: String)
val PAYMENT_APPS = listOf(
    PaymentApp("phonepe","PhonePe","💜","com.phonepe.app"),
    PaymentApp("gpay","Google Pay","🔵","com.google.android.apps.nbu.paisa.user"),
    PaymentApp("paytm","Paytm","🔷","net.one97.paytm"),
    PaymentApp("bhim","BHIM","🇮🇳","in.org.npci.upiapp"),
    PaymentApp("amazonpay","Amazon Pay","🟠","in.amazon.mShop.android.shopping")
)

class AppViewModel : ViewModel() {

    // ── Core ───────────────────────────────────────────────────────────────────
    var coins by mutableStateOf(0.0)
    var bonusClaimed by mutableStateOf(false)
    var isLoaded by mutableStateOf(false)
    var toastMsg by mutableStateOf("")
    var showToast by mutableStateOf(false)

    // ── Video ──────────────────────────────────────────────────────────────────
    var videoTime by mutableStateOf(30)
    var videoRunning by mutableStateOf(false)
    var videoCompleted by mutableStateOf(false)
    private var videoJob: kotlinx.coroutines.Job? = null

    // ── Scratch ────────────────────────────────────────────────────────────────
    var scratchProgress by mutableStateOf(0f)
    var scratchDone by mutableStateOf(false)
    var pendingScratchCoins by mutableStateOf(0.0)
    var showScratchResultPopup by mutableStateOf(false)
    // Scratch ad: handled by HomeScreen via UnityAdsManager

    // ── Captcha ────────────────────────────────────────────────────────────────
    var captchaText by mutableStateOf("")
    var captchaInput by mutableStateOf("")
    var captchaError by mutableStateOf(false)
    var captchaLimitReached by mutableStateOf(false)
    var captchaUsedToday by mutableStateOf(0)
    var pendingCaptchaCoins by mutableStateOf(0.0)
    var showCaptchaWin by mutableStateOf(false)
    val CAPTCHA_DAILY_LIMIT = 20
    private var captchaDateStr by mutableStateOf("")
    private var captchaCountToday by mutableStateOf(0)

    // ── Quiz ───────────────────────────────────────────────────────────────────
    var quizQuestion by mutableStateOf<QuizQuestion?>(null)
    var quizAnswered by mutableStateOf(false)
    var quizSelectedOption by mutableStateOf(-1)
    var quizResult by mutableStateOf("")

    // ── Quiz Webview ───────────────────────────────────────────────────────────
    var showQuizWebview by mutableStateOf(false)
    var qwvGameNum by mutableStateOf(1)
    var qwvQ1 by mutableStateOf<QuizQuestion?>(null)
    var qwvQ2 by mutableStateOf<QuizQuestion?>(null)
    var qwvQ1Answered by mutableStateOf(false)
    var qwvQ2Answered by mutableStateOf(false)
    var qwvBothCorrect by mutableStateOf(0)
    var qwvEarned by mutableStateOf(0.0)
    var qwvQ1SelectedOpt by mutableStateOf(-1)
    var qwvQ2SelectedOpt by mutableStateOf(-1)
    var qwvDone by mutableStateOf(false)
    var qwvRedirectSec by mutableStateOf(3)
    var qwvShowRedirect by mutableStateOf(true)
    var qwvSiteUrl by mutableStateOf("")
    private var qwvRedirectJob: kotlinx.coroutines.Job? = null

    // ── Real AdMob loading indicator ─────────────────────────────────────────
    // "video" | "scratch" | "captcha" | ""
    var pendingAdSource by mutableStateOf("")
    var showAdLoading by mutableStateOf(false)

    // ── Redeem ─────────────────────────────────────────────────────────────────
    var currentCodeTier by mutableStateOf(1000)
    var currentCodeLabel by mutableStateOf("")
    var currentCodeFull by mutableStateOf("")
    var codeRevealed by mutableStateOf(false)
    var showInsufficientBanner by mutableStateOf(false)
    var showPhonePeContact by mutableStateOf(false)
    var phonePeContactLabel by mutableStateOf("")
    var phonePeContactTier by mutableStateOf(0)

    // ── Refer ──────────────────────────────────────────────────────────────────
    var myReferCode by mutableStateOf("")
    var referInput by mutableStateOf("")
    var referMsg by mutableStateOf("")
    var referFriends by mutableStateOf(listOf<ReferEntry>())
    var referEarned by mutableStateOf(0)
    var referUsedCode by mutableStateOf(false)
    var referUsedCodeValue by mutableStateOf("")
    var referCommissionEarned by mutableStateOf(0)
    var referJoinBonus by mutableStateOf(0)

    // ── Top-Up ─────────────────────────────────────────────────────────────────
    var selectedPlanId by mutableStateOf(-1)
    var selectedCurrency by mutableStateOf(CURRENCIES[0])
    var customAmountLocal by mutableStateOf("")
    var customStars by mutableStateOf(0)
    var showCurrencyPicker by mutableStateOf(false)
    var currencySearchQuery by mutableStateOf("")
    // Payment flow
    var showPaymentConfirm by mutableStateOf(false)
    var paymentConfirmStars by mutableStateOf(0)
    var paymentConfirmLabel by mutableStateOf("")
    var paymentConfirmUrl by mutableStateOf("")
    var paymentConfirmInrAmt by mutableStateOf(0.0)
    var paymentUtrInput by mutableStateOf("")
    var paymentUtrError by mutableStateOf("")
    var paymentVerifying by mutableStateOf(false)
    var paymentSuccess by mutableStateOf(false)

    // ── Code Indices ───────────────────────────────────────────────────────────
    var codeIdx1000 by mutableStateOf(0)
    var codeIdx1650 by mutableStateOf(0)
    var codeIdx2000 by mutableStateOf(0)
    var deviceOffset by mutableStateOf(-1)

    // ─────────────────────────────────────────────────────────────────────────
    fun load(context: Context) {
        viewModelScope.launch {
            coins = AppDataStore.getCoins(context).first()
            bonusClaimed = AppDataStore.getBonusClaimed(context).first()
            myReferCode = AppDataStore.getMyReferCode(context).first()
            codeIdx1000 = AppDataStore.getCodeIdx(context, 1000).first()
            codeIdx1650 = AppDataStore.getCodeIdx(context, 1650).first()
            codeIdx2000 = AppDataStore.getCodeIdx(context, 2000).first()
            deviceOffset = AppDataStore.getDeviceOffset(context).first()
            parseReferStatsFromJson(AppDataStore.getReferStats(context).first())
            parseCaptchaData(AppDataStore.getCaptchaData(context).first())
            if (myReferCode.isEmpty()) {
                myReferCode = generateReferCode()
                AppDataStore.setMyReferCode(context, myReferCode)
            }
            if (deviceOffset == -1) {
                deviceOffset = Random.nextInt(CODES_1000.size)
                AppDataStore.setDeviceOffset(context, deviceOffset)
            }
            generateCaptchaText()
            isLoaded = true
        }
    }

    // ── Coin Logic ─────────────────────────────────────────────────────────────
    private fun isHighBalance() = coins >= 800
    private fun isDeepBalance() = coins >= 850 && coins < 900
    private fun isUltraBalance() = coins >= 900

    fun getSmartCoin(): Double = if (isHighBalance()) getSubOneCoin() else getBalancedNormalCoin()

    private fun getSubOneCoin(): Double {
        val pool = when { isUltraBalance() -> SUB_ONE_COIN_POOL_ULTRA; isDeepBalance() -> SUB_ONE_COIN_POOL_DEEP; else -> SUB_ONE_COIN_POOL }
        return pool[Random.nextInt(pool.size)]
    }

    private fun getBalancedNormalCoin(): Double {
        val pool = when { coins >= 700 -> NORMAL_COIN_POOL_C; coins >= 300 -> NORMAL_COIN_POOL_B; else -> NORMAL_COIN_POOL_A }
        return pool[Random.nextInt(pool.size)]
    }

    fun formatCoin(v: Double): String {
        return if (isHighBalance()) formatSubCoin(v) else if (v % 1.0 != 0.0) String.format("%.1f", v) else v.toInt().toString()
    }

    private fun formatSubCoin(v: Double): String = when {
        isUltraBalance() -> String.format("%.7f", v); isDeepBalance() -> String.format("%.3f", v); else -> String.format("%.2f", v)
    }

    fun formatCoinShort(v: Double): String {
        val s = v.toString(); val d = s.indexOf('.')
        return if (d == -1) s else s.substring(0, d) + "." + s.substring(d + 1).take(3)
    }

    fun addCoins(context: Context, amount: Double) {
        viewModelScope.launch { coins += amount; AppDataStore.setCoins(context, coins) }
    }

    fun claimBonus(context: Context) {
        if (!bonusClaimed) viewModelScope.launch {
            bonusClaimed = true; AppDataStore.setBonusClaimed(context, true)
            addCoins(context, 50.0); showToastMsg("🎉 +50 stars claimed!")
        }
    }

    fun showToastMsg(msg: String) { toastMsg = msg; showToast = true }

    // ── PhonePe Redeem ─────────────────────────────────────────────────────────
    fun tryRedeemPhonePe(context: Context, required: Int, label: String) {
        if (coins < required) {
            showInsufficientBanner = true
            viewModelScope.launch { kotlinx.coroutines.delay(3000); showInsufficientBanner = false }
        } else {
            phonePeContactLabel = label
            phonePeContactTier = required
            showPhonePeContact = true
        }
    }

    // ── Video ──────────────────────────────────────────────────────────────────
    fun startVideo() {
        if (videoRunning) return
        videoRunning = true
        videoJob = viewModelScope.launch {
            while (videoTime > 0) { kotlinx.coroutines.delay(1000); videoTime-- }
            videoCompleted = true; videoRunning = false
        }
    }

    fun claimVideo(context: Context) {
        if (!videoCompleted) return
        val amt = getSmartCoin()
        // Ad handling is now done on HomeScreen via UnityAdsManager
        addCoins(context, amt)
        showToastMsg("🎬 +${formatCoin(amt)} Stars Earned!")
        triggerReferralCommission(context)
        resetVideo()
    }

    fun resetVideo() { videoJob?.cancel(); videoTime = 30; videoRunning = false; videoCompleted = false }

    // ── Watch Video: Handled by HomeScreen via UnityAdsManager ──────────────────
    // This function is deprecated - use UnityAdsManager.showRewardedAd() directly
    @Deprecated("Use UnityAdsManager.showRewardedAd() from HomeScreen instead")
    fun watchVideoAd(context: Context) {
        showToastMsg("⏳ Loading ad...")
    }

    // ── Scratch ────────────────────────────────────────────────────────────────
    fun prepareScratch() {
        scratchDone = false; scratchProgress = 0f
        pendingScratchCoins = getSmartCoin()
        showScratchResultPopup = false
    }

    fun onScratchProgressUpdate(progress: Float, context: Context? = null) {
        scratchProgress = progress
        if (progress > 0.4f && !scratchDone) {
            scratchDone = true
            viewModelScope.launch { kotlinx.coroutines.delay(300); openScratchAd(context) }
        }
    }

    fun openScratchAd(context: Context? = null) {
        // Ad handling is now done on HomeScreen via UnityAdsManager
        showScratchResultPopup = true
    }

    // closeScratchAd removed — AdMob handles dismissal via callback

    fun claimScratchResult(context: Context) {
        showScratchResultPopup = false
        addCoins(context, pendingScratchCoins)
        showToastMsg("🎉 +${formatCoin(pendingScratchCoins)} stars added!")
        triggerReferralCommission(context)
    }

    // ── Captcha ────────────────────────────────────────────────────────────────
    private val CAPTCHA_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789"

    fun generateCaptchaText() {
        captchaText = (1..(5 + Random.nextInt(2))).map { CAPTCHA_CHARS[Random.nextInt(CAPTCHA_CHARS.length)] }.joinToString("")
        captchaInput = ""; captchaError = false; showCaptchaWin = false
    }

    private fun getTodayStr(): String {
        val cal = java.util.Calendar.getInstance()
        return "${cal.get(java.util.Calendar.YEAR)}-${cal.get(java.util.Calendar.MONTH)+1}-${cal.get(java.util.Calendar.DAY_OF_MONTH)}"
    }

    private fun parseCaptchaData(json: String) {
        if (json.isBlank()) { captchaCountToday = 0; return }
        try {
            val today = getTodayStr()
            val date = Regex("\"date\":\"([^\"]+)\"").find(json)?.groupValues?.get(1) ?: ""
            val count = Regex("\"count\":(\\d+)").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            captchaDateStr = date; captchaCountToday = if (date == today) count else 0; captchaUsedToday = captchaCountToday
        } catch (e: Exception) { captchaCountToday = 0 }
    }

    private suspend fun incrementCaptchaCount(context: Context) {
        val today = getTodayStr(); captchaDateStr = today; captchaCountToday++; captchaUsedToday = captchaCountToday
        AppDataStore.setCaptchaData(context, "{\"date\":\"$today\",\"count\":$captchaCountToday}")
    }

    fun submitCaptcha(context: Context) {
        if (captchaUsedToday >= CAPTCHA_DAILY_LIMIT) { captchaLimitReached = true; return }
        val entered = captchaInput.trim()
        if (entered.isEmpty() || entered != captchaText) { captchaError = true; return }
        captchaError = false
        pendingCaptchaCoins = CAPTCHA_COIN_POOL[Random.nextInt(CAPTCHA_COIN_POOL.size)]
        viewModelScope.launch { incrementCaptchaCount(context); showCaptchaWin = true }
    }

    fun claimCaptchaWin(context: Context) {
        showCaptchaWin = false
        val coinsToAdd = pendingCaptchaCoins
        // Ad handling is now done on HomeScreen via UnityAdsManager
        addCoins(context, coinsToAdd)
        triggerReferralCommission(context)
        showToastMsg("✅ +${formatCoin(coinsToAdd)} Stars Earned!")
        if (captchaUsedToday >= CAPTCHA_DAILY_LIMIT) captchaLimitReached = true else generateCaptchaText()
    }

    // ── Quiz ───────────────────────────────────────────────────────────────────
    fun prepareQuiz() { quizQuestion = QUIZ_DATA[Random.nextInt(QUIZ_DATA.size)]; quizAnswered = false; quizSelectedOption = -1; quizResult = "" }

    fun answerQuiz(context: Context, optIdx: Int) {
        val q = quizQuestion ?: return
        quizSelectedOption = optIdx; quizAnswered = true
        if (optIdx == q.answer) {
            quizResult = "correct"; val amt = getSmartCoin()
            addCoins(context, amt); showToastMsg("✅ Correct! +${formatCoin(amt)} stars!")
            triggerReferralCommission(context)
        } else { quizResult = "wrong"; showToastMsg("❌ Wrong! Try next question.") }
    }

    fun nextQuizQuestion() = prepareQuiz()

    // ── Quiz Webview ───────────────────────────────────────────────────────────
    fun openQuizWebview(gameNum: Int, context: Context) {
        qwvGameNum = gameNum; qwvSiteUrl = QUIZ_SITE_URLS[(gameNum - 1) % QUIZ_SITE_URLS.size]
        val pairIdx = (gameNum - 1) % 4
        qwvQ1 = QUIZ_WEBVIEW_QUESTIONS[pairIdx * 2]; qwvQ2 = QUIZ_WEBVIEW_QUESTIONS[pairIdx * 2 + 1]
        qwvQ1Answered = false; qwvQ2Answered = false; qwvBothCorrect = 0; qwvEarned = 0.0
        qwvQ1SelectedOpt = -1; qwvQ2SelectedOpt = -1; qwvDone = false
        qwvRedirectSec = 3; qwvShowRedirect = true; showQuizWebview = true
        qwvRedirectJob?.cancel()
        qwvRedirectJob = viewModelScope.launch {
            while (qwvRedirectSec > 0) { kotlinx.coroutines.delay(1000); qwvRedirectSec-- }
            qwvShowRedirect = false
        }
    }

    fun skipQwvRedirect() { qwvRedirectJob?.cancel(); qwvShowRedirect = false }

    fun answerQwvQ1(optIdx: Int, context: Context) {
        if (qwvQ1Answered) return
        qwvQ1SelectedOpt = optIdx; qwvQ1Answered = true
        if (optIdx == qwvQ1!!.answer) qwvBothCorrect++
    }

    fun answerQwvQ2(optIdx: Int, context: Context) {
        if (qwvQ2Answered) return
        qwvQ2SelectedOpt = optIdx; qwvQ2Answered = true
        if (optIdx == qwvQ2!!.answer) qwvBothCorrect++
        val earned = getSmartCoin(); qwvEarned = earned
        addCoins(context, earned); triggerReferralCommission(context)
        viewModelScope.launch { kotlinx.coroutines.delay(900); qwvDone = true }
        showToastMsg("⭐ You Have Won ${formatCoin(earned)} Stars")
    }

    fun closeQuizWebview() { qwvRedirectJob?.cancel(); showQuizWebview = false }

    // ── Redeem Code Sheet ──────────────────────────────────────────────────────
    fun openCodeSheet(tier: Int, label: String) {
        currentCodeTier = tier; currentCodeLabel = label
        val pool = when(tier) { 1650 -> CODES_1650; 2000 -> CODES_2000; else -> CODES_1000 }
        val idx = (deviceOffset + when(tier) { 1650 -> codeIdx1650; 2000 -> codeIdx2000; else -> codeIdx1000 }) % pool.size
        currentCodeFull = pool[idx]; codeRevealed = false
    }

    fun copyRedeemCode(context: Context) {
        if (coins < currentCodeTier) {
            showInsufficientBanner = true
            viewModelScope.launch { kotlinx.coroutines.delay(2800); showInsufficientBanner = false }
            return
        }
        coins -= currentCodeTier.toDouble()
        viewModelScope.launch { AppDataStore.setCoins(context, coins) }
        codeRevealed = true; advanceCodeIndex(context, currentCodeTier)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Redeem Code", currentCodeFull))
        showToastMsg("✅ Code copied: $currentCodeFull")
    }

    private fun advanceCodeIndex(context: Context, tier: Int) {
        viewModelScope.launch {
            when(tier) {
                1650 -> { codeIdx1650 = (codeIdx1650 + 1) % CODES_1650.size; AppDataStore.setCodeIdx(context, 1650, codeIdx1650) }
                2000 -> { codeIdx2000 = (codeIdx2000 + 1) % CODES_2000.size; AppDataStore.setCodeIdx(context, 2000, codeIdx2000) }
                else -> { codeIdx1000 = (codeIdx1000 + 1) % CODES_1000.size; AppDataStore.setCodeIdx(context, 1000, codeIdx1000) }
            }
        }
    }

    fun makeBlurredCode(code: String): String {
        if (code.length <= 3) return code
        return code.take(2) + "*".repeat(code.length - 3) + code.last()
    }

    // ── Refer ──────────────────────────────────────────────────────────────────
    private fun generateReferCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..6).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    fun submitReferCode(context: Context) {
        val code = referInput.trim().uppercase(); referMsg = ""
        if (code.length < 4) { referMsg = "❌ Valid code enter karo (4-8 characters)"; return }
        if (referUsedCode) { referMsg = "❌ Aapne already ek referral code use kar liya hai!"; return }
        if (code == myReferCode) { referMsg = "❌ Apna khud ka code use nahi kar sakte!"; return }
        referUsedCode = true; referUsedCodeValue = code; referJoinBonus += 20; referEarned += 20
        addCoins(context, 20.0); referInput = ""
        referMsg = "🎉 +20 ⭐ Stars mile! Referral code successfully applied!"
        viewModelScope.launch { saveReferStats(context) }
        showToastMsg("⭐ You Have Won 20 Stars")
    }

    fun copyReferCode(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Refer Code", myReferCode))
        showToastMsg("✅ Referral code copied: $myReferCode")
    }

    fun shareReferLink(context: Context) {
        val text = "🎁 Join Daily Rewards & Earn App aur FREE stars kamao!\n\nMera referral code use karo: $myReferCode\n\nDownload karo: https://freemoneyfreeredeemcode.blogspot.com"
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }, "Share via"))
    }

    fun triggerReferralCommission(context: Context) {
        if (!referUsedCode || referUsedCodeValue.isEmpty()) return
        if (Random.nextFloat() < 0.2f) { referCommissionEarned++; viewModelScope.launch { saveReferStats(context) } }
    }

    private suspend fun saveReferStats(context: Context) {
        val json = "{\"friends\":${serializeFriends()},\"earned\":$referEarned,\"usedCode\":$referUsedCode,\"usedCodeValue\":\"$referUsedCodeValue\",\"commissionEarned\":$referCommissionEarned,\"joinBonus\":$referJoinBonus}"
        AppDataStore.setReferStats(context, json)
    }

    private fun serializeFriends() = "[" + referFriends.joinToString(",") { "{\"name\":\"${it.name}\",\"date\":\"${it.date}\",\"coins\":${it.coins}}" } + "]"

    private fun parseReferStatsFromJson(json: String) {
        if (json.isBlank()) return
        try {
            referEarned = Regex("\"earned\":(\\d+)").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            referUsedCode = Regex("\"usedCode\":(true|false)").find(json)?.groupValues?.get(1) == "true"
            referUsedCodeValue = Regex("\"usedCodeValue\":\"([^\"]+)\"").find(json)?.groupValues?.get(1) ?: ""
            referCommissionEarned = Regex("\"commissionEarned\":(\\d+)").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            referJoinBonus = Regex("\"joinBonus\":(\\d+)").find(json)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        } catch (e: Exception) {}
    }

    fun simulateReferralJoin(context: Context, friendName: String = "Friend") {
        viewModelScope.launch {
            val cal = java.util.Calendar.getInstance()
            val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
            val dateStr = "${cal.get(java.util.Calendar.DAY_OF_MONTH)} ${months[cal.get(java.util.Calendar.MONTH)]} ${cal.get(java.util.Calendar.YEAR)}"
            referFriends = referFriends + ReferEntry(friendName, dateStr, 20.0)
            referEarned += 20; referJoinBonus += 20; addCoins(context, 20.0)
            showToastMsg("🎉 $friendName joined! +20 ⭐ Stars mile!"); saveReferStats(context)
        }
    }

    // ── Top-Up / Payment ───────────────────────────────────────────────────────
    fun getCurrencyPrice(priceUSD: Double): Double {
        val raw = priceUSD * selectedCurrency.rate
        return when { selectedCurrency.rate >= 100 -> Math.round(raw).toDouble(); selectedCurrency.rate >= 10 -> String.format("%.1f", raw).toDouble(); else -> String.format("%.2f", raw).toDouble() }
    }

    fun formatPrice(priceUSD: Double) = selectedCurrency.symbol + getCurrencyPrice(priceUSD)

    fun openTopUp() { selectedPlanId = -1; customAmountLocal = ""; customStars = 0 }

    fun selectPlan(id: Int) { selectedPlanId = id; customAmountLocal = ""; customStars = 0 }

    fun onCustomAmountInput(value: String) {
        customAmountLocal = value; selectedPlanId = -1
        val amt = value.toDoubleOrNull() ?: 0.0
        customStars = ((amt / selectedCurrency.rate) * 40).toInt()
    }

    fun onCustomStarsInput(value: String) {
        val stars = value.toIntOrNull() ?: 0; customStars = stars; selectedPlanId = -1
        val localAmt = (stars / 40.0) * selectedCurrency.rate
        customAmountLocal = when { selectedCurrency.rate >= 100 -> Math.round(localAmt).toString(); else -> String.format("%.2f", localAmt) }
    }

    fun getPayBtnLabel(): String {
        if (selectedPlanId != -1) {
            val plan = TOPUP_PLANS.find { it.id == selectedPlanId } ?: return "⭐ Plan Chuniye"
            return "💳 ${formatPrice(plan.priceUSD)} Pay Karo · +${plan.stars} ⭐"
        }
        val amt = customAmountLocal.toDoubleOrNull() ?: 0.0
        if (amt > 0 && customStars > 0) return "💳 ${selectedCurrency.symbol}${customAmountLocal} Pay Karo · +$customStars ⭐"
        return "⭐ Plan Chuniye"
    }

    fun initiatePayment(context: Context) {
        val upiVpa = "9837514185-2@ybl"
        val stars: Int; val inrAmt: Double
        if (selectedPlanId != -1) {
            val plan = TOPUP_PLANS.find { it.id == selectedPlanId } ?: return
            stars = plan.stars; inrAmt = plan.priceUSD * 83.5
        } else {
            val usdVal = (customAmountLocal.toDoubleOrNull() ?: 0.0) / selectedCurrency.rate
            stars = (usdVal * 40).toInt(); inrAmt = usdVal * 83.5
        }
        if (stars == 0) return
        val upiUrl = "upi://pay?pa=$upiVpa&pn=${Uri.encode("Daily Rewards & Earn")}&am=${String.format("%.2f", inrAmt)}&cu=INR&tn=${Uri.encode("Stars $stars - DailyRewardsEarn")}"
        paymentConfirmStars = stars
        paymentConfirmLabel = if (selectedPlanId != -1) TOPUP_PLANS.find { it.id == selectedPlanId }?.label ?: "+$stars Stars" else "+$stars Stars"
        paymentConfirmUrl = upiUrl; paymentConfirmInrAmt = inrAmt
        paymentUtrInput = ""; paymentUtrError = ""; paymentVerifying = false; paymentSuccess = false
        showPaymentConfirm = true
    }

    fun openPaymentApp(context: Context, appId: String) {
        val app = PAYMENT_APPS.find { it.id == appId }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentConfirmUrl)).apply { app?.packageName?.let { setPackage(it) } }
            context.startActivity(intent)
        } catch (e: Exception) {
            try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(paymentConfirmUrl))) }
            catch (e2: Exception) { showToastMsg("UPI app not found. Install PhonePe/GPay/Paytm") }
        }
    }

    fun openAnyUpiApp(context: Context) {
        try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(paymentConfirmUrl))) }
        catch (e: Exception) { showToastMsg("No UPI app found on device") }
    }

    fun validateAndSubmitUtr(context: Context) {
        val utr = paymentUtrInput.trim(); paymentUtrError = ""
        if (utr.length < 6) { paymentUtrError = "❌ Valid UTR/Transaction ID enter karo (min 6 characters)"; return }
        paymentVerifying = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            paymentVerifying = false; paymentSuccess = true
            addCoins(context, paymentConfirmStars.toDouble())
            showToastMsg("🎉 +$paymentConfirmStars ⭐ Stars added! Thank you!")
        }
    }

    fun closePaymentConfirm() { showPaymentConfirm = false; paymentUtrInput = ""; paymentUtrError = ""; paymentVerifying = false; paymentSuccess = false }

    fun getFilteredCurrencies(): List<Currency> {
        if (currencySearchQuery.isBlank()) return CURRENCIES
        val q = currencySearchQuery.lowercase()
        return CURRENCIES.filter { it.name.lowercase().contains(q) || it.code.lowercase().contains(q) || it.symbol.contains(q) }
    }
}
