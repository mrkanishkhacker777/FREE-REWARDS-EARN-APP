package com.getredeemcode.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay

// ─── Insufficient Banner ──────────────────────────────────────────────────────
@Composable
fun InsufficientBanner(show: Boolean) {
    if (!show) return
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp)).background(Color(0xFFfef3c7)).padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚠️", fontSize = 20.sp); Spacer(Modifier.width(10.dp))
            Text("Not enough ⭐ stars! Earn more by completing tasks.", color = Color(0xFF92400e), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

// ─── Toast Display ─────────────────────────────────────────────────────────────
@Composable
fun BoxScope.ToastDisplay(vm: AppViewModel) {
    if (vm.showToast) {
        LaunchedEffect(vm.showToast, vm.toastMsg) { delay(2500); vm.showToast = false }
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 64.dp), contentAlignment = Alignment.BottomCenter) {
            Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Color(0xFF1a1a2e)).padding(horizontal = 24.dp, vertical = 14.dp)) {
                Text(vm.toastMsg, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

// ─── Coin Won Toast ────────────────────────────────────────────────────────────
@Composable
fun BoxScope.CoinWonToast(show: Boolean, amount: String, onDone: () -> Unit) {
    if (!show) return
    LaunchedEffect(show) { delay(2000); onDone() }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF1a1a2e), Color(0xFF2d2d4e))))
                .padding(horizontal = 32.dp, vertical = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⭐", fontSize = 40.sp)
                Text("+$amount Stars Won!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }
        }
    }
}

// ─── AdMob Loading Overlay ─────────────────────────────────────────────────────
// Shows briefly while real AdMob Rewarded Ad is loading/displaying
@Composable
fun AdLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Spinner
            CircularProgressIndicator(
                color = Purple,
                modifier = Modifier.size(52.dp),
                strokeWidth = 4.dp
            )
            // Ad icon
            Text("📺", fontSize = 40.sp)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Ad Loading...",
                    color = White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Watch ad to earn ⭐ Stars",
                    color = White.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
            }
        }
    }
}
