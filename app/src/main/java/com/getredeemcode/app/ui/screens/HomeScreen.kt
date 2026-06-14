package com.getredeemcode.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.getredeemcode.app.ads.UnityAdsManager
import com.getredeemcode.app.ads.RealBannerAd

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: AppViewModel,
    onScratch: () -> Unit,
    onQuiz: () -> Unit,
    onCaptcha: () -> Unit,
    onRedeem: () -> Unit,
    onRefer: () -> Unit,
    onPrivacy: () -> Unit,
    onTerms: () -> Unit
) {
    val context = LocalContext.current
    var showDropdown by remember { mutableStateOf(false) }
    var showBonusModal by remember { mutableStateOf(false) }
    var showVideoNotLoading by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val adBanners = listOf(
        AdBannerData("Watch Ads & Earn", "FREE POINTS", "📺", listOf(Color(0xFF4a1fa8), Color(0xFF7c3aad), Color(0xFFa855f7))),
        AdBannerData("Complete Tasks Now", "BONUS STARS", "⭐", listOf(Color(0xFF1565c0), Color(0xFF1976d2), Color(0xFF42a5f5))),
        AdBannerData("Get Gift Cards", "REDEEM NOW", "🎁", listOf(Color(0xFFc62828), Color(0xFFe53935), Color(0xFFef9a9a)))
    )
    val pagerState = rememberPagerState(pageCount = { adBanners.size })
    // Auto-scroll ads
    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            val next = (pagerState.currentPage + 1) % adBanners.size
            pagerState.animateScrollToPage(next)
        }
    }

    GradientBackground {
        // Fixed full-screen layout — NO vertical scroll
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Purple, PurpleDark)))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(PinkLight),
                    contentAlignment = Alignment.Center
                ) { Text("💰", fontSize = 24.sp) }
                Spacer(Modifier.width(12.dp))
                Text("Daily Rewards & Earn App", color = White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                Text("🎁", fontSize = 26.sp, modifier = Modifier.clickable { onRefer() }.padding(4.dp))
                Spacer(Modifier.width(16.dp))
                Box(modifier = Modifier.size(36.dp).clickable { showDropdown = !showDropdown }, contentAlignment = Alignment.Center) {
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        repeat(3) { Box(modifier = Modifier.size(22.dp, 2.5.dp).background(White, RoundedCornerShape(2.dp))) }
                    }
                }
            }

            // ── PULL TO REFRESH + SCROLLABLE CONTENT ──────────────────────
            val pullRefreshState = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    vm.load(context)
                },
                state = pullRefreshState,
                modifier = Modifier.fillMaxSize()
            ) {
                LaunchedEffect(isRefreshing) {
                    if (isRefreshing) { delay(800); isRefreshing = false }
                }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf3f0ff))
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // BALANCE CARD
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)).border(1.5.dp, Border, RoundedCornerShape(18.dp))
                        .background(White).padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Your Balance", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("⭐", fontSize = 24.sp); Spacer(Modifier.width(6.dp))
                                Text(vm.formatCoinShort(vm.coins), color = Purple, fontWeight = FontWeight.Black, fontSize = 24.sp)
                            }
                        }
                        GradientButton("Redeem", emoji = "🎁", onClick = onRedeem, fontSize = 17)
                    }
                }

                // BONUS CARD (conditional — only if not claimed)
                if (!vm.bonusClaimed) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp)).border(1.5.dp, Border, RoundedCornerShape(18.dp))
                            .background(White).clickable { showBonusModal = true }.padding(14.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFf3e8ff)), contentAlignment = Alignment.Center) { Text("🎁", fontSize = 26.sp) }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("New User Bonus!", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                Text("Claim FREE 50 ⭐ Stars now →", color = Purple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // REAL BANNER AD — Unity Ads (Real banner, not fake carousel)
                RealBannerAd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                // EARN STARS SECTION
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 16.sp); Spacer(Modifier.width(6.dp))
                        Text("Earn Stars", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFFf3e8ff)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("⭐ ${vm.formatCoinShort(vm.coins)}", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    // TASK GRID — 2x2
                    val tasks = listOf(
                        // Watch Video - Unity Rewarded Ad
                        TaskItem("📺", "Watch Video", "Earn per video") {
                            val activity = context as? android.app.Activity
                            if (activity != null) {
                                val shown = UnityAdsManager.showRewardedAd(activity) { rewarded ->
                                    if (rewarded) {
                                        vm.addCoins(10) // 10 stars for watching ad
                                        vm.showToast("✅ +10 ⭐ Stars earned!")
                                    } else {
                                        vm.showToast("❌ Ad not completed")
                                    }
                                }
                                if (!shown) vm.showToast("⏳ Ad loading...")
                            }
                        },
                        TaskItem("🎰", "Scratch & Win", "Up to 100 ⭐") { vm.prepareScratch(); onScratch() },
                        // Recaptcha - Unity Rewarded Ad
                        TaskItem("🤖", "Recaptcha", "Earn per captcha") {
                            val activity = context as? android.app.Activity
                            if (activity != null) {
                                val shown = UnityAdsManager.showRewardedAd(activity) { rewarded ->
                                    if (rewarded) {
                                        vm.generateCaptchaText()
                                        onCaptcha()
                                    } else {
                                        vm.showToast("❌ Ad not completed")
                                    }
                                }
                                if (!shown) vm.showToast("⏳ Ad loading...")
                            }
                        },
                        // Play Quiz - Unity Rewarded Ad
                        TaskItem("🧠", "Play Quiz", "Answer & earn") {
                            val activity = context as? android.app.Activity
                            if (activity != null) {
                                val shown = UnityAdsManager.showRewardedAd(activity) { rewarded ->
                                    if (rewarded) {
                                        vm.prepareQuiz()
                                        onQuiz()
                                    } else {
                                        vm.showToast("❌ Ad not completed")
                                    }
                                }
                                if (!shown) vm.showToast("⏳ Ad loading...")
                            }
                        }
                    )
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        tasks.chunked(2).forEach { rowTasks ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                rowTasks.forEach { task -> TaskCard(task = task, modifier = Modifier.weight(1f)) }
                                if (rowTasks.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            } // end PullToRefreshBox
        }

        // ── DROPDOWN MENU ───────────────────────────────────────────────────
        if (showDropdown) {
            Box(modifier = Modifier.fillMaxSize().clickable { showDropdown = false }.background(Color.Black.copy(alpha = 0.25f)))
            Box(
                modifier = Modifier.padding(top = 70.dp, end = 12.dp).align(Alignment.TopEnd)
                    .width(230.dp).clip(RoundedCornerShape(14.dp)).background(White).border(1.dp, Border, RoundedCornerShape(14.dp))
            ) {
                Column {
                    listOf(
                        Triple("🤝", "Refer & Earn") { showDropdown = false; onRefer() },
                        Triple("📺", "Why video not loading?") { showDropdown = false; showVideoNotLoading = true },
                        Triple("📋", "Privacy Policy") { showDropdown = false; onPrivacy() },
                        Triple("📄", "Terms of Service") { showDropdown = false; onTerms() }
                    ).forEachIndexed { idx, (emoji, label, action) ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { action() }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(emoji, fontSize = 18.sp); Spacer(Modifier.width(12.dp))
                            Text(label, color = TextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        if (idx < 3) HorizontalDivider(color = Border.copy(alpha = 0.4f), thickness = 0.7.dp)
                    }
                }
            }
        }

        // ── BONUS MODAL ─────────────────────────────────────────────────────
        if (showBonusModal) {
            ModalOverlay(onDismiss = { showBonusModal = false }) {
                Text("🎁", fontSize = 52.sp)
                Spacer(Modifier.height(8.dp))
                Text("New User Bonus!", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Complete any task and earn stars!\nCollect 1000 stars to get ₹700 Google Play Code.",
                    color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center, lineHeight = 22.sp
                )
                Spacer(Modifier.height(20.dp))
                if (!vm.bonusClaimed) {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .background(Brush.linearGradient(listOf(Purple, Pink)))
                            .clickable { vm.claimBonus(context); showBonusModal = false }.padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("🎉 Claim FREE 50 Stars!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) }
                } else {
                    Text("✅ Bonus already claimed!", color = Purple, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .border(1.5.dp, Border, RoundedCornerShape(50.dp)).clickable { showBonusModal = false }.padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("Close", color = TextGray, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                }
            }
        }

        // VIDEO: Direct AdMob rewarded ad — no fake modal

        // ── VIDEO NOT LOADING SHEET ─────────────────────────────────────────
        if (showVideoNotLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { showVideoNotLoading = false },
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF0EEF6))
                        .clickable { }
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 22.dp, vertical = 20.dp)
                ) {
                    // Close button top-right
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        Text(
                            "✕",
                            fontSize = 22.sp,
                            color = Color(0xFF555555),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { showVideoNotLoading = false }.padding(4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    // Reasons why video not loading
                    listOf(
                        "1. Because You have Watched Maximum number of Videos.",
                        "2. Because in Your country maximum number of video reach.",
                        "3. In your country video service not available."
                    ).forEach { reason ->
                        Text(
                            reason,
                            color = Color(0xFF7B2FBE),
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // How To Fix This Problem heading
                    Text(
                        "How To Fix This Problem",
                        color = Color(0xFF7B2FBE),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    // Fix steps
                    listOf(
                        "1. Download Free VPN App.",
                        "2. Connect to USA Server.",
                        "3. Then Restart Daily Rewards & Earn App.",
                        "4. Wait for a minute, video will load successfully."
                    ).forEach { step ->
                        Text(
                            step,
                            color = Color(0xFF7B2FBE),
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Note heading
                    Text(
                        "Note",
                        color = Color(0xFF7B2FBE),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    // Note content
                    Text(
                        "If video loading without VPN, Then use Daily Rewards & Earn App without VPN, If video is not loading then use VPN App.",
                        color = Color(0xFF7B2FBE),
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // ── PAYMENT CONFIRM OVERLAY ─────────────────────────────────────────
        if (vm.showPaymentConfirm) {
            PaymentConfirmOverlay(vm = vm)
        }

        // ── TOAST ────────────────────────────────────────────────────────────
        if (vm.showToast) {
            LaunchedEffect(vm.showToast, vm.toastMsg) { delay(2500); vm.showToast = false }
            Box(modifier = Modifier.fillMaxSize().padding(bottom = 64.dp), contentAlignment = Alignment.BottomCenter) {
                Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Color(0xFF1a1a2e)).padding(horizontal = 24.dp, vertical = 14.dp)) {
                    Text(vm.toastMsg, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// ─── Data classes ─────────────────────────────────────────────────────────────
data class AdBannerData(val title: String, val sub: String, val emoji: String, val colors: List<Color>)
data class TaskItem(val emoji: String, val title: String, val sub: String, val onClick: () -> Unit)

// ─── Task Card ─────────────────────────────────────────────────────────────────
@Composable
fun TaskCard(task: TaskItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, Border, RoundedCornerShape(18.dp)).background(White)
            .clickable { task.onClick() }.padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFf3e8ff)), contentAlignment = Alignment.Center) {
                Text(task.emoji, fontSize = 28.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(task.title, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(3.dp))
            Text(task.sub, color = TextGray, fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}


// ─── Modal Overlay ─────────────────────────────────────────────────────────────
@Composable
fun ModalOverlay(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)).clickable { onDismiss() }, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp)).background(White).clickable { }.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { content() }
    }
}
