package com.getredeemcode.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel

@Composable
fun ReferScreen(vm: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar("🤝 Refer & Earn", vm.formatCoinShort(vm.coins), onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf3f0ff))
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 28.dp)
            ) {

                // HERO BANNER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF4a1fa8), Color(0xFF9b59d0), Color(0xFFf472b6))
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🤝", fontSize = 52.sp)
                        Spacer(Modifier.height(10.dp))
                        Text("Refer & Earn Together!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(6.dp))
                        Text("Invite friends and both of you earn ⭐ Stars!", color = White.copy(alpha = 0.9f), fontSize = 14.sp, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(White.copy(alpha = 0.2f)).padding(horizontal = 14.dp, vertical = 7.dp)) {
                                Text("You get +20 ⭐", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                            Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(White.copy(alpha = 0.2f)).padding(horizontal = 14.dp, vertical = 7.dp)) {
                                Text("Friend gets +20 ⭐", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // STATS ROW
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp, 16.dp, 14.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(modifier = Modifier.weight(1f), label = "Friends Referred", value = "${vm.referFriends.size}")
                    StatCard(modifier = Modifier.weight(1f), label = "Stars Earned", value = "⭐ ${vm.referEarned}")
                    StatCard(modifier = Modifier.weight(1f), label = "Commission", value = "⭐ ${vm.referCommissionEarned}")
                }

                Spacer(Modifier.height(16.dp))

                // MY REFER CODE CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.5.dp, Border, RoundedCornerShape(18.dp))
                        .background(White)
                        .padding(20.dp)
                ) {
                    Column {
                        Text("🔑 Your Referral Code", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFf3e8ff))
                                .border(2.dp, Border, RoundedCornerShape(12.dp))
                                .padding(vertical = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                vm.myReferCode,
                                color = Purple,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp,
                                letterSpacing = 4.sp
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(50.dp))
                                    .border(1.5.dp, Border, RoundedCornerShape(50.dp))
                                    .clickable { vm.copyReferCode(context) }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) { Text("📋 Copy Code", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp) }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Brush.linearGradient(listOf(Purple, Pink)))
                                    .clickable { vm.shareReferLink(context) }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) { Text("📤 Share Link", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp) }
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // HOW IT WORKS
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.5.dp, Border, RoundedCornerShape(18.dp))
                        .background(White)
                        .padding(20.dp)
                ) {
                    Column {
                        Text("📋 How it Works", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Spacer(Modifier.height(14.dp))
                        listOf(
                            Pair("1", "Share your unique referral code with friends"),
                            Pair("2", "Friend downloads the app & enters your code"),
                            Pair("3", "Both of you instantly earn +20 ⭐ Stars!"),
                            Pair("4", "Earn commission stars every time your friends complete tasks")
                        ).forEach { (num, text) ->
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier.size(28.dp).clip(CircleShape).background(Purple),
                                    contentAlignment = Alignment.Center
                                ) { Text(num, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp) }
                                Spacer(Modifier.width(12.dp))
                                Text(text, color = TextDark, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ENTER REFERRAL CODE
                if (!vm.referUsedCode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.5.dp, Border, RoundedCornerShape(18.dp))
                            .background(White)
                            .padding(20.dp)
                    ) {
                        Column {
                            Text("🎁 Enter a Referral Code", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Spacer(Modifier.height(6.dp))
                            Text("Enter a friend's code to earn +20 ⭐ Stars!", color = TextGray, fontSize = 13.sp)
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(
                                value = vm.referInput,
                                onValueChange = { vm.referInput = it.uppercase(); vm.referMsg = "" },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Enter code e.g. ABC123", color = TextGray) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { vm.submitReferCode(context) }),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Purple,
                                    unfocusedBorderColor = Border
                                )
                            )
                            if (vm.referMsg.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                Text(vm.referMsg, color = if (vm.referMsg.startsWith("🎉")) Color(0xFF22c55e) else Color(0xFFef4444), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Brush.linearGradient(listOf(Purple, Pink)))
                                    .clickable { vm.submitReferCode(context) }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) { Text("✅ Apply Code", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                        }
                    }
                } else {
                    // Already used a code
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFf0fdf4))
                            .border(1.5.dp, Color(0xFF22c55e).copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✅", fontSize = 24.sp)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Referral code applied!", color = Color(0xFF166534), fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                Text("Code: ${vm.referUsedCodeValue}  |  Bonus: +${vm.referJoinBonus} ⭐", color = Color(0xFF166534), fontSize = 13.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // REFERRED FRIENDS LIST
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.5.dp, Border, RoundedCornerShape(18.dp))
                        .background(White)
                        .padding(20.dp)
                ) {
                    Column {
                        Text("👥 Your Referred Friends", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                        if (vm.referFriends.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🤷", fontSize = 36.sp)
                                    Spacer(Modifier.height(8.dp))
                                    Text("No friends referred yet.", color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center)
                                    Text("Share your code to start earning!", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                }
                            }
                        } else {
                            vm.referFriends.forEach { friend ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFf3e8ff)),
                                        contentAlignment = Alignment.Center
                                    ) { Text(friend.name.take(1).uppercase(), color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(friend.name, color = TextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(friend.date, color = TextGray, fontSize = 12.sp)
                                    }
                                    Text("+${friend.coins} ⭐", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                }
                                HorizontalDivider(color = Border.copy(alpha = 0.4f), thickness = 0.7.dp)
                            }
                        }
                    }
                }
            }
        }

        // TOAST
        ToastDisplay(vm = vm)
    }
}

@Composable
fun StatCard(modifier: Modifier, label: String, value: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.5.dp, Border, RoundedCornerShape(14.dp))
            .background(White)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(label, color = TextGray, fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 15.sp)
        }
    }
}
