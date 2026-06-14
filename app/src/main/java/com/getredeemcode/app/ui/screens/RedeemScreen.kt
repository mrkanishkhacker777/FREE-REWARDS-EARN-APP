package com.getredeemcode.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay

// ─── Data ────────────────────────────────────────────────────────────────────
data class RedeemItem(val tier: Int, val label: String, val sublabel: String, val emoji: String)
data class PhonePeItem(val tier: Int, val label: String, val sublabel: String, val amtLabel: String)

val REDEEM_ITEMS = listOf(
    RedeemItem(1000, "Redeem Code", "₹700 Indian Rupees", "🎮"),
    RedeemItem(1650, "Redeem Code", "₹1500 Indian Rupees", "💎"),
    RedeemItem(2000, "Redeem Code", "₹2000 Indian Rupees", "👑")
)

val PHONEPE_ITEMS = listOf(
    PhonePeItem(1000, "PhonePe", "1000 ⭐ required", "₹700 Indian Rupees"),
    PhonePeItem(1650, "PhonePe", "1650 ⭐ required", "₹1500 Indian Rupees")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(vm: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var showCodeSheet by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(vm.showInsufficientBanner) {
        if (vm.showInsufficientBanner) { delay(3000); vm.showInsufficientBanner = false }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            isRefreshing = false
        }
    }

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR — matches screenshot ──────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Purple, PurpleDark)))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp))
                        .background(White.copy(alpha = 0.2f)).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Text("←", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) }
                Spacer(Modifier.width(12.dp))
                Text("Redeem", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50.dp))
                        .background(White.copy(alpha = 0.2f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⭐", fontSize = 16.sp); Spacer(Modifier.width(4.dp))
                        Text(vm.formatCoinShort(vm.coins), color = White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    }
                }
            }

            // ── PULL TO REFRESH wraps the scrollable content ──────────────
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFf3f0ff))
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 14.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Insufficient banner
                    InsufficientBanner(show = vm.showInsufficientBanner)

                    // ── Google Redeem Code section ─────────────────────────
                    Text(
                        "Google Redeem Code",
                        color = Purple,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    REDEEM_ITEMS.forEach { item ->
                        @Suppress("UNUSED_VARIABLE") val locked = vm.coins < item.tier
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, Color(0xFFd4c5f0), RoundedCornerShape(16.dp))
                                .background(White)
                                .clickable { vm.openCodeSheet(item.tier, item.label); showCodeSheet = true }
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                // Play triangle icon — purple like screenshot
                                Box(
                                    modifier = Modifier.size(44.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Purple play triangle
                                    Canvas(modifier = Modifier.size(28.dp)) {
                                        val path = Path().apply {
                                            moveTo(0f, 0f)
                                            lineTo(size.width, size.height / 2)
                                            lineTo(0f, size.height)
                                            close()
                                        }
                                        drawPath(path, color = Color(0xFF8B5CF6))
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(item.label, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                    Text(item.sublabel, color = TextGray, fontSize = 13.sp)
                                }
                                // Star badge — gold border like screenshot
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .border(2.dp, Color(0xFFE8B84B), RoundedCornerShape(50.dp))
                                        .background(Color(0xFFFFFBEB))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⭐", fontSize = 14.sp)
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "${item.tier}",
                                            color = Color(0xFFB8860B),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── PhonePe section ────────────────────────────────────
                    Text(
                        "PhonePe",
                        color = Purple,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    PHONEPE_ITEMS.forEach { item ->
                        @Suppress("UNUSED_VARIABLE") val locked = vm.coins < item.tier
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, Color(0xFFd4c5f0), RoundedCornerShape(16.dp))
                                .background(White)
                                .clickable { vm.tryRedeemPhonePe(context, item.tier, item.label) }
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                // PhonePe logo — real Devanagari पे symbol
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF5F259F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "पे",
                                        color = White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(item.label, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                    Text(item.amtLabel, color = TextGray, fontSize = 13.sp)
                                }
                                // Star badge — same gold style
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .border(2.dp, Color(0xFFE8B84B), RoundedCornerShape(50.dp))
                                        .background(Color(0xFFFFFBEB))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⭐", fontSize = 14.sp)
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "${item.tier}",
                                            color = Color(0xFFB8860B),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                }
            }
        }

        // ── Code Sheet Overlay ─────────────────────────────────────────────
        if (showCodeSheet) {
            CodeSheetOverlay(vm = vm, onClose = { showCodeSheet = false })
        }

        // ── PhonePe Contact Admin Overlay ──────────────────────────────────
        if (vm.showPhonePeContact) {
            PhonePeContactOverlay(vm = vm, context = context)
        }

        // Toast
        ToastDisplay(vm = vm)
    }
}

// ─── Code Sheet Overlay ───────────────────────────────────────────────────────
@Composable
fun BoxScope.CodeSheetOverlay(vm: AppViewModel, onClose: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { onClose() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
                .clickable { }
                .padding(bottom = 28.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Border))
            }
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("🎁 ${vm.currentCodeLabel}", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    Text("${vm.currentCodeTier} ⭐ required", color = Purple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Box(modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFf3e8ff)).clickable { onClose() }, contentAlignment = Alignment.Center) {
                    Text("✕", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val hasEnough = vm.coins >= vm.currentCodeTier
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(if (hasEnough) Color(0xFFf0fdf4) else Color(0xFFfef2f2))
                    .border(1.dp, if (hasEnough) Color(0xFF22c55e).copy(0.4f) else Color(0xFFef4444).copy(0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (hasEnough) "✅" else "❌", fontSize = 18.sp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                if (hasEnough) "You have enough stars!" else "Not enough stars!",
                                color = if (hasEnough) Color(0xFF166534) else Color(0xFF991b1b),
                                fontWeight = FontWeight.ExtraBold, fontSize = 14.sp
                            )
                            Text(
                                "Your balance: ⭐ ${vm.formatCoinShort(vm.coins)} / ${vm.currentCodeTier} required",
                                color = if (hasEnough) Color(0xFF16a34a) else Color(0xFFdc2626),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .border(2.dp, Border, RoundedCornerShape(14.dp)).background(Color(0xFFf3e8ff)).padding(20.dp),
                    contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Your Code", color = TextGray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (vm.codeRevealed) vm.currentCodeFull else vm.makeBlurredCode(vm.currentCodeFull),
                            color = Purple,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (vm.codeRevealed) 20.sp else 22.sp,
                            letterSpacing = if (vm.codeRevealed) 2.sp else 4.sp,
                            modifier = if (!vm.codeRevealed) Modifier.blur(6.dp) else Modifier
                        )
                        if (!vm.codeRevealed) {
                            Spacer(Modifier.height(6.dp))
                            Text("🔒 Tap below to reveal", color = TextGray, fontSize = 12.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                        .background(
                            if (hasEnough) Brush.linearGradient(listOf(Purple, Pink))
                            else Brush.linearGradient(listOf(Color.Gray.copy(0.5f), Color.Gray.copy(0.5f)))
                        )
                        .clickable(enabled = hasEnough) { vm.copyRedeemCode(context); if (!hasEnough) onClose() }
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (vm.codeRevealed) "📋 Copy Code Again" else if (hasEnough) "🔓 Reveal & Copy Code  (-${vm.currentCodeTier} ⭐)" else "🔒 ${vm.currentCodeTier} ⭐ needed",
                        color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp
                    )
                }

                if (vm.codeRevealed) {
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).border(1.5.dp, Border, RoundedCornerShape(14.dp)).background(White).padding(16.dp)) {
                        Column {
                            Text("📋 How to Redeem:", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Spacer(Modifier.height(8.dp))
                            listOf(
                                "Open Google Play Store on your device",
                                "Tap on Profile → Payments & subscriptions",
                                "Tap 'Redeem gift code' and paste your code",
                                "Enjoy your reward! 🎉"
                            ).forEachIndexed { i, step ->
                                Row(modifier = Modifier.padding(bottom = 6.dp)) {
                                    Text("${i+1}.", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, modifier = Modifier.width(20.dp))
                                    Text(step, color = TextDark, fontSize = 13.sp, lineHeight = 19.sp)
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .border(1.5.dp, Purple, RoundedCornerShape(50.dp))
                            .clickable {
                                try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/redeem"))) }
                                catch (e: Exception) {}
                            }.padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("🛒 Open Google Play", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp) }
                }
            }
        }
    }
}

// ─── PhonePe Contact Admin Overlay ───────────────────────────────────────────
@Composable
fun BoxScope.PhonePeContactOverlay(vm: AppViewModel, context: Context) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp)).background(White).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // PhonePe logo in popup too
            Box(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF5F259F)),
                contentAlignment = Alignment.Center
            ) {
                Text("पे", color = White, fontWeight = FontWeight.Black, fontSize = 28.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text("PhonePe Redeem Request", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(6.dp))
            Text(vm.phonePeContactLabel, color = Purple, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFf3e8ff)).padding(16.dp)) {
                Column {
                    Text("📋 Aage kya karna hai:", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    listOf(
                        "1. Apna PhonePe number/UPI ID prepare karo",
                        "2. Niche diye contact pe message karo",
                        "3. Apna registered mobile number share karo",
                        "4. ₹700/₹1500 24 ghante mein transfer hoga"
                    ).forEach { step ->
                        Text(step, color = TextDark, fontSize = 13.sp, lineHeight = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF229ED9), Color(0xFF1a7dc0))))
                .clickable {
                    try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/getredeemcodeapp"))) }
                    catch (e: Exception) { vm.showToastMsg("Telegram install karo") }
                }.padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) { Text("✈️ Telegram pe Contact Karo", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }

            Spacer(Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF25D366), Color(0xFF128C7E))))
                .clickable {
                    try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919837514185?text=${Uri.encode("Hello, I want to redeem: ${vm.phonePeContactLabel}")}"))) }
                    catch (e: Exception) { vm.showToastMsg("WhatsApp install karo") }
                }.padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) { Text("💬 WhatsApp pe Contact Karo", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }

            Spacer(Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                .border(1.5.dp, Border, RoundedCornerShape(50.dp))
                .clickable { vm.showPhonePeContact = false }.padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) { Text("Close", color = TextGray, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
        }
    }
}
