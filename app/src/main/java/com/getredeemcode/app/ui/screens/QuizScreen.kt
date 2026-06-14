package com.getredeemcode.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import android.content.Intent
import android.net.Uri
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.*

val QUIZ_GAME_LIST = listOf(
    Triple("1", "Play Game 1", "Coming Soon"),
    Triple("2", "Play Game 2", "Coming Soon"),
    Triple("3", "Play Quiz 3", "Coming Soon"),
    Triple("4", "Play Quiz 4", "Coming Soon")
)

@Composable
fun QuizScreen(vm: AppViewModel, onBack: () -> Unit) {
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
                Text("Quizzes & Games", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.weight(1f))
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

            // Content area — fixed, no scroll, like screenshot
            Column(
                modifier = Modifier.fillMaxSize().background(Color(0xFFf3f0ff))
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // "Play for 60 seconds to unlock the reward" — red text like screenshot
                Text(
                    "Play for 60 seconds to unlock the reward",
                    color = Color(0xFFe53935),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Game list — exactly like screenshot, 4 items with lock icon
                QUIZ_GAME_LIST.forEach { (num, title, _) ->
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(White)
                            .clickable { vm.openQuizWebview(num.toInt(), context) }
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            // Brain icon circle — light purple like screenshot
                            Box(
                                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFece9f5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧠", fontSize = 24.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Text(
                                title,
                                color = TextDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            // Lock + "Coming Soon" — exactly like screenshot
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔒", fontSize = 20.sp)
                                Text("Coming Soon", color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }

        // ── QUIZ WEBVIEW OVERLAY ─────────────────────────────────────────────
        if (vm.showQuizWebview) {
            QuizWebviewOverlay(vm = vm)
        }

        // Toast
        ToastDisplay(vm = vm)
    }
}

// ─── Quiz Webview Overlay ─────────────────────────────────────────────────────
@Composable
fun BoxScope.QuizWebviewOverlay(vm: AppViewModel) {
    val context = LocalContext.current

    LaunchedEffect(vm.qwvShowRedirect) {
        if (!vm.qwvShowRedirect && vm.qwvSiteUrl.isNotEmpty()) {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://${vm.qwvSiteUrl}")))
            } catch (e: Exception) {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Purple, PurpleDark))).padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(White.copy(alpha = 0.2f)).clickable { vm.closeQuizWebview() }, contentAlignment = Alignment.Center) {
                    Text("✕", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Game ${vm.qwvGameNum}", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text(vm.qwvSiteUrl, color = White.copy(alpha = 0.75f), fontSize = 12.sp)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(White.copy(alpha = 0.2f)).padding(horizontal = 14.dp, vertical = 6.dp)) {
                    Text("⭐ ${vm.formatCoinShort(vm.coins)}", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }

            Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1a0533)).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (vm.qwvShowRedirect) {
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(White.copy(alpha = 0.1f)).border(1.dp, White.copy(alpha = 0.2f), RoundedCornerShape(14.dp)).padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Redirecting to ${vm.qwvSiteUrl} in ${vm.qwvRedirectSec}s...", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Purple).clickable { vm.skipQwvRedirect() }.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text("Skip →", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Text("Let's get started,", color = White.copy(alpha = 0.8f), fontSize = 15.sp)
                Text("answer 2 simple questions to earn Stars now:", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)

                val q1 = vm.qwvQ1
                if (q1 != null) {
                    WebviewQuestion(question = q1, answered = vm.qwvQ1Answered, selectedOpt = vm.qwvQ1SelectedOpt, onAnswer = { vm.answerQwvQ1(it, context) })
                }

                val q2 = vm.qwvQ2
                if (q2 != null && vm.qwvQ1Answered) {
                    WebviewQuestion(question = q2, answered = vm.qwvQ2Answered, selectedOpt = vm.qwvQ2SelectedOpt, onAnswer = { vm.answerQwvQ2(it, context) })
                }

                if (vm.qwvDone) {
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF6d28d9), Color(0xFF9b59d0)))).padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎉", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Congratulations!", color = Color(0xFFffd600), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Text("⭐ +${vm.formatCoin(vm.qwvEarned)} Stars Earned!", color = White.copy(alpha = 0.9f), fontSize = 16.sp)
                            Spacer(Modifier.height(16.dp))
                            Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(White).clickable { vm.closeQuizWebview() }.padding(horizontal = 32.dp, vertical = 14.dp)) {
                                Text("✅ Back to App", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("By proceeding, you agree to our Terms of Use & Privacy Policy", color = White.copy(alpha = 0.5f), fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

// ─── Webview Question Card ────────────────────────────────────────────────────
@Composable
fun WebviewQuestion(question: QuizQuestion, answered: Boolean, selectedOpt: Int, onAnswer: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).border(1.5.dp, White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)).background(White.copy(alpha = 0.1f)).padding(18.dp)) {
        Text(question.question, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, lineHeight = 22.sp)
        Spacer(Modifier.height(12.dp))
        question.options.forEachIndexed { idx, opt ->
            val isSelected = selectedOpt == idx
            val isCorrect = answered && idx == question.answer
            val isWrong = answered && isSelected && idx != question.answer
            val bg = when { isCorrect -> Color(0xFF22c55e); isWrong -> Color(0xFFef4444); isSelected -> Purple; else -> White.copy(alpha = 0.15f) }
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(10.dp)).background(bg)
                    .clickable(enabled = !answered) { onAnswer(idx) }.padding(horizontal = 16.dp, vertical = 13.dp)
            ) {
                Text(opt, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
