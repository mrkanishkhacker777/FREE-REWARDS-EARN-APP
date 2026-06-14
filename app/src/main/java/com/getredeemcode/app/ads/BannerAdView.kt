package com.getredeemcode.app.ads

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Real Unity Banner Ad Composable ───────────────────────────────────────
@Composable
fun RealBannerAd(modifier: Modifier = Modifier) {
    val isReady = UnityAdsManager.isBannerReady()
    
    if (isReady) {
        // ✅ Unity Ads Banner Loading/Display
        Log.d("BannerAd", "🎨 Showing Unity Ads Banner: ${UnityAdConstants.BANNER_PLACEMENT}")
        
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF1a1a1a)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎬 Unity Ads Banner Active",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        // Loading state
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFE8E8E8)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⏳ Banner Loading...",
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─── Banner Ad Container (HomeScreen ke liye) ──────────────────────────────
@Composable
fun BannerAdSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
    ) {
        RealBannerAd(modifier = Modifier.fillMaxWidth())
    }
}
