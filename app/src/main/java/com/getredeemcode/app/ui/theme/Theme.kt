package com.getredeemcode.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color Palette (exact from HTML CSS vars) ─────────────────────────────────
val Purple       = Color(0xFF9b59d0)
val PurpleDark   = Color(0xFF7c3aad)
val PurpleLight  = Color(0xFFc084fc)
val Pink         = Color(0xFFf472b6)
val PinkLight    = Color(0xFFfce7f3)
val BgPink       = Color(0xFFf9d5f0)
val White        = Color(0xFFFFFFFF)
val TextDark     = Color(0xFF1a1a2e)
val TextGray     = Color(0xFF888888)
val Border       = Color(0xFFd8b4fe)
val Gold         = Color(0xFFffd700)

@Composable
fun GetRedeemCodeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Purple,
            secondary = Pink,
            background = BgPink,
            surface = White
        ),
        content = content
    )
}

@Composable
fun GradientBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(BgPink),
        content = content
    )
}

@Composable
fun GradientButton(
    text: String,
    emoji: String = "",
    onClick: () -> Unit,
    fontSize: Int = 16
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Brush.linearGradient(listOf(Purple, Pink)))
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        val label = if (emoji.isNotEmpty()) "$emoji $text" else text
        Text(label, color = White, fontWeight = FontWeight.ExtraBold, fontSize = fontSize.sp)
    }
}

@Composable
fun ScreenTopBar(title: String, balance: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Purple, PurpleDark)))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(White.copy(alpha = 0.2f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) { Text("←", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) }
        Spacer(Modifier.width(12.dp))
        Text(title, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(White.copy(alpha = 0.2f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐", fontSize = 16.sp)
                Spacer(Modifier.width(4.dp))
                Text(balance, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
        }
    }
}
