package com.getredeemcode.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*

@Composable
fun LoginScreen(onEnter: () -> Unit) {
    var termsAccepted by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }

    // Floating animation for money bag
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -14f,
        animationSpec = infiniteRepeatable(tween(1500, easing = EaseInOut), RepeatMode.Reverse),
        label = "floatY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFFf7c7ec), BgPink)))
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            // Illustration top section
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Money bag SVG-like emoji illustration
                    Box(
                        modifier = Modifier.offset(y = floatOffset.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💰", fontSize = 100.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("⭐", fontSize = 24.sp)
                                Text("🎁", fontSize = 24.sp)
                                Text("💎", fontSize = 24.sp)
                            }
                        }
                    }
                }
            }

            // Bottom content
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 48.dp)) {
                Text(
                    "Get Redeem code",
                    color = TextDark, fontWeight = FontWeight.Black, fontSize = 32.sp,
                    textAlign = TextAlign.Center, lineHeight = 38.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Complete Tasks, Unlock Rewards",
                    color = TextDark, fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))

                // Terms box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(White.copy(alpha = 0.55f))
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        "This app provides redeem codes for entertainment purposes only. Codes are randomly generated and assigned to users. By using this app, you agree to our Terms & Conditions and Privacy Policy.",
                        color = TextDark, fontSize = 13.sp, lineHeight = 21.sp
                    )
                }
                Spacer(Modifier.height(16.dp))

                // Checkbox row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { termsAccepted = !termsAccepted },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(checkedColor = Purple, uncheckedColor = Purple)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "I agree to the ",
                        color = TextDark, fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                    )
                    Text(
                        "Terms & Privacy",
                        color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp,
                        modifier = Modifier.clickable { showTerms = true }
                    )
                }
                Spacer(Modifier.height(18.dp))

                // Get Started button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (termsAccepted) Brush.linearGradient(listOf(Purple, Pink))
                            else Brush.linearGradient(listOf(Color.Gray.copy(alpha = 0.5f), Color.Gray.copy(alpha = 0.5f)))
                        )
                        .clickable(enabled = termsAccepted) { onEnter() }
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "🚀 Get Started",
                        color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }

    // Privacy overlay
    if (showPrivacy) {
        PrivacyScreen(onBack = { showPrivacy = false })
    }
    // Terms overlay
    if (showTerms) {
        TermsScreen(onBack = { showTerms = false })
    }
}
