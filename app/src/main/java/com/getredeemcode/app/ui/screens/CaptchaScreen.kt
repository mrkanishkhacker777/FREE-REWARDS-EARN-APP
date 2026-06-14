package com.getredeemcode.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel

@Composable
fun CaptchaScreen(vm: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    GradientBackground {
        // Fixed full-screen — NO scroll
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar — matches screenshot style
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
                Text("Recaptcha", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50.dp))
                        .background(White.copy(alpha = 0.2f)).padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⭐", fontSize = 16.sp); Spacer(Modifier.width(4.dp))
                        Text(vm.formatCoinShort(vm.coins), color = White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    }
                }
            }

            // White content — fills screen, no scroll, centered like screenshot
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // "Don't get points if ads failed" — exactly like screenshot
                Text(
                    "Don't get points if ads failed to load or show.",
                    color = TextGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(28.dp))

                if (vm.captchaLimitReached) {
                    // Daily limit reached
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏰", fontSize = 56.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Daily Limit Reached!", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Come back tomorrow for more captchas!", color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                } else {
                    // Captcha image box — light purple like screenshot
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFece9f5)),
                        contentAlignment = Alignment.Center
                    ) {
                        CaptchaTextDisplay(captchaText = vm.captchaText)
                    }

                    Spacer(Modifier.height(28.dp))

                    // Input field — exactly like screenshot
                    OutlinedTextField(
                        value = vm.captchaInput,
                        onValueChange = { vm.captchaInput = it; vm.captchaError = false },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter Captcha", color = TextGray, fontSize = 16.sp) },
                        isError = vm.captchaError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { vm.submitCaptcha(context) }),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Purple,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            errorBorderColor = Color(0xFFef4444)
                        )
                    )
                    if (vm.captchaError) {
                        Spacer(Modifier.height(6.dp))
                        Text("❌ Enter Correct Captcha", color = Color(0xFFef4444), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(24.dp))

                    // Submit button — light purple like screenshot
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFc4b0e8))
                            .clickable { vm.submitCaptcha(context) }
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Submit", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(16.dp))

                    // New captcha link
                    Text(
                        "🔄 Generate new captcha",
                        color = Purple, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                        modifier = Modifier.clickable { vm.generateCaptchaText() }
                    )
                }
            }
        }

        // WIN OVERLAY
        if (vm.showCaptchaWin) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { }, contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp)
                        .clip(RoundedCornerShape(24.dp)).background(White).padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 56.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("You Won!", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(50.dp))
                            .background(Brush.linearGradient(listOf(Purple, Pink)))
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) { Text("⭐ +${vm.formatCoin(vm.pendingCaptchaCoins)} Stars", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp) }
                    Spacer(Modifier.height(6.dp))
                    Text("Great job solving the captcha!", color = TextGray, fontSize = 14.sp)
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .background(Brush.linearGradient(listOf(Purple, Pink)))
                            .clickable { vm.claimCaptchaWin(context) }.padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("🎉 Claim Stars!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) }
                }
            }
        }

        // AdMob Loading overlay
        if (vm.pendingAdSource == "captcha" && vm.showAdLoading) {
            AdLoadingOverlay()
        }

        // TOAST
        ToastDisplay(vm = vm)
    }
}

@Composable
fun CaptchaTextDisplay(captchaText: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colors = listOf(
            Color(0xFF4a1fa8), Color(0xFF1a73e8), Color(0xFFc62828),
            Color(0xFF2e7d32), Color(0xFF6d28d9), Color(0xFF0077b6)
        )
        captchaText.forEachIndexed { idx, char ->
            val strikethrough = idx == 1 || idx == captchaText.length - 2
            Text(
                text = char.toString(),
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = colors[idx % colors.size],
                textDecoration = if (strikethrough) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
        }
    }
}
