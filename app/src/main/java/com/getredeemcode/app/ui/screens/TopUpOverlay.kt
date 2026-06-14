package com.getredeemcode.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.*

// ═══════════════════════════════════════════════════════════════
// TOP-UP BOTTOM SHEET (from HTML openTopUpScreen)
// ═══════════════════════════════════════════════════════════════
@Composable
fun BoxScope.TopUpOverlay(vm: AppViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
                .clickable { }
                .padding(bottom = 24.dp)
        ) {
            // Handle bar
            Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Border))
            }
            Spacer(Modifier.height(4.dp))

            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("⭐ Star Top-Up", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text("Top up your balance instantly", color = TextGray, fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFf3e8ff)).clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) { Text("✕", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) }
            }

            // Balance + Currency Bar
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFf3e8ff)).padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Aapka Balance", color = TextGray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 22.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(vm.formatCoinShort(vm.coins), color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        }
                    }
                    // Currency selector button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF7c3aed), Color(0xFFa855f7))))
                            .clickable { vm.showCurrencyPicker = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(vm.selectedCurrency.symbol, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            Text(" ${vm.selectedCurrency.code} ▾", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            // Plans label
            Text("📦 Plan Chuniye", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))

            // Plan cards
            Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TOPUP_PLANS.forEach { plan ->
                    val isSelected = vm.selectedPlanId == plan.id
                    val isBestValue = plan.id == 2
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, if (isSelected) Purple else Border, RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFFf3e8ff) else White)
                            .clickable { vm.selectPlan(plan.id) }
                            .padding(16.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐", fontSize = 20.sp)
                                    Text(" ${plan.stars} Stars", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                    if (isBestValue) {
                                        Spacer(Modifier.width(8.dp))
                                        Box(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Pink).padding(horizontal = 8.dp, vertical = 3.dp)) {
                                            Text("BEST VALUE", color = White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                        }
                                    }
                                }
                                Text(plan.bonus, color = TextGray, fontSize = 12.sp)
                            }
                            Text(vm.formatPrice(plan.priceUSD), color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                        if (isSelected) {
                            Box(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)) {
                                Box(modifier = Modifier.size(20.dp).clip(RoundedCornerShape(50.dp)).background(Purple), contentAlignment = Alignment.Center) {
                                    Text("✓", color = White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            // Custom amount section
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFfaf5ff)).border(1.5.dp, Color(0xFFe9d5ff), RoundedCornerShape(16.dp)).padding(16.dp)) {
                Column {
                    Text("💡 \$1 = 40 Stars · Selected currency mein amount bharo", color = Color(0xFF7c3aed), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Amount input
                        Box(modifier = Modifier.weight(1f)) {
                            Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(2.dp, Color(0xFFddd6fe), RoundedCornerShape(12.dp)).background(White).padding(horizontal = 12.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(vm.selectedCurrency.symbol, color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                Spacer(Modifier.width(6.dp))
                                BasicTextField(
                                    value = vm.customAmountLocal,
                                    onValueChange = { vm.onCustomAmountInput(it) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    textStyle = androidx.compose.ui.text.TextStyle(color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp),
                                    modifier = Modifier.weight(1f),
                                    decorationBox = { inner ->
                                        if (vm.customAmountLocal.isEmpty()) Text("0", color = TextGray.copy(alpha = 0.5f), fontSize = 18.sp)
                                        inner()
                                    }
                                )
                            }
                        }
                        // Stars result
                        Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).border(2.dp, Color(0xFFe9d5ff), RoundedCornerShape(12.dp)).background(White).padding(horizontal = 12.dp, vertical = 10.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Milenge", color = Purple, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐", fontSize = 14.sp)
                                    Text(" ${if (vm.customStars > 0) vm.customStars else 0}", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            // Worldwide note
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFf0fdf4)).border(1.5.dp, Color(0xFFbbf7d0), RoundedCornerShape(12.dp)).padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌍", fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Worldwide Payment", color = Color(0xFF15803d), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Text("USA, UK, India, Japan, China, 135+ countries", color = Color(0xFF16a34a), fontSize = 12.sp)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            // Pay button
            val canPay = vm.selectedPlanId != -1 || vm.customStars > 0
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (canPay) Brush.linearGradient(listOf(Purple, Pink)) else Brush.linearGradient(listOf(Color.Gray.copy(0.5f), Color.Gray.copy(0.5f))))
                    .clickable(enabled = canPay) { vm.initiatePayment(context) }
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) { Text(vm.getPayBtnLabel(), color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }

            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("🔒", fontSize = 14.sp); Spacer(Modifier.width(6.dp))
                Text("Secure UPI Payment · Instant Stars", color = TextGray, fontSize = 12.sp)
            }
        }
    }

    // Currency picker overlay
    if (vm.showCurrencyPicker) {
        CurrencyPickerOverlay(vm = vm)
    }
}

// ═══════════════════════════════════════════════════════════════
// CURRENCY PICKER
// ═══════════════════════════════════════════════════════════════
@Composable
fun BoxScope.CurrencyPickerOverlay(vm: AppViewModel) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)).clickable { vm.showCurrencyPicker = false },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(White)
                .clickable { }
                .padding(20.dp)
        ) {
            Text("🌍 Select Currency", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = vm.currencySearchQuery, onValueChange = { vm.currencySearchQuery = it },
                modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search currency...", color = TextGray) },
                singleLine = true, shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Purple, unfocusedBorderColor = Border)
            )
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.heightIn(max = 300.dp).verticalScroll(rememberScrollState())) {
                vm.getFilteredCurrencies().forEach { currency ->
                    val isSelected = vm.selectedCurrency.code == currency.code
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFf3e8ff) else Color.Transparent)
                            .clickable { vm.selectedCurrency = currency; vm.currencySearchQuery = ""; vm.showCurrencyPicker = false }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(currency.symbol, color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, modifier = Modifier.width(30.dp))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(currency.code, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text(currency.name, color = TextGray, fontSize = 12.sp)
                        }
                        if (isSelected) Text("✓", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                    HorizontalDivider(color = Border.copy(alpha = 0.3f), thickness = 0.5.dp)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// PAYMENT CONFIRM SCREEN (QR + UPI App chooser + UTR verify)
// ═══════════════════════════════════════════════════════════════
@Composable
fun BoxScope.PaymentConfirmOverlay(vm: AppViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
                .padding(bottom = 28.dp)
        ) {
            // Handle
            Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Border))
            }
            Spacer(Modifier.height(8.dp))

            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("💳 Payment", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text(vm.paymentConfirmLabel, color = Purple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Box(
                    modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFf3e8ff)).clickable { vm.closePaymentConfirm() },
                    contentAlignment = Alignment.Center
                ) { Text("✕", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) }
            }
            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                if (vm.paymentSuccess) {
                    // SUCCESS STATE
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF22c55e), Color(0xFF16a34a)))).padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅", fontSize = 52.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Payment Verified!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                            Text("+${vm.paymentConfirmStars} ⭐ Stars added to your balance!", color = White.copy(alpha = 0.9f), fontSize = 14.sp, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(16.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(White).clickable { vm.closePaymentConfirm() }.padding(horizontal = 32.dp, vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) { Text("🎉 Done!", color = Color(0xFF16a34a), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                        }
                    }
                } else {
                    // Payment amount info
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFf3e8ff)).padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Amount", color = TextGray, fontSize = 12.sp)
                                Text("₹${String.format("%.2f", vm.paymentConfirmInrAmt)}", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("You Get", color = TextGray, fontSize = 12.sp)
                                Text("⭐ ${vm.paymentConfirmStars} Stars", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            }
                        }
                    }

                    // UPI ID to pay
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).border(1.5.dp, Border, RoundedCornerShape(14.dp)).background(White).padding(16.dp)) {
                        Column {
                            Text("📱 UPI ID pe send karo:", color = TextGray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text("9837514185-2@ybl", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFf3e8ff)).clickable {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", "9837514185-2@ybl"))
                                    vm.showToastMsg("✅ UPI ID copied!")
                                }.padding(8.dp)) { Text("📋", fontSize = 18.sp) }
                            }
                        }
                    }

                    // Pay with apps
                    Text("💳 Pay with App:", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)

                    // App grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PAYMENT_APPS.chunked(3).forEach { rowApps ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowApps.forEach { app ->
                                    Box(
                                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                                            .border(1.5.dp, Border, RoundedCornerShape(14.dp)).background(White)
                                            .clickable { vm.openPaymentApp(context, app.id) }.padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(app.emoji, fontSize = 24.sp)
                                            Spacer(Modifier.height(4.dp))
                                            Text(app.name, color = TextDark, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                                // Fill empty slots
                                if (rowApps.size < 3) repeat(3 - rowApps.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }

                    // Open any UPI app
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .border(1.5.dp, Purple, RoundedCornerShape(50.dp)).clickable { vm.openAnyUpiApp(context) }.padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("📲 Any UPI App se Pay Karo", color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp) }

                    HorizontalDivider(color = Border, thickness = 1.dp)

                    // UTR / Transaction ID verification
                    Text("✅ Payment ke baad UTR/Transaction ID enter karo:", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                    OutlinedTextField(
                        value = vm.paymentUtrInput,
                        onValueChange = { vm.paymentUtrInput = it; vm.paymentUtrError = "" },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("UTR / Transaction ID...", color = TextGray) },
                        isError = vm.paymentUtrError.isNotEmpty(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Purple, unfocusedBorderColor = Border, errorBorderColor = Color(0xFFef4444))
                    )

                    if (vm.paymentUtrError.isNotEmpty()) {
                        Text(vm.paymentUtrError, color = Color(0xFFef4444), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .background(if (!vm.paymentVerifying) Brush.linearGradient(listOf(Purple, Pink)) else Brush.linearGradient(listOf(Color.Gray.copy(0.5f), Color.Gray.copy(0.5f))))
                            .clickable(enabled = !vm.paymentVerifying) { vm.validateAndSubmitUtr(context) }
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (vm.paymentVerifying) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                CircularProgressIndicator(color = White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                Text("Verifying...", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                        } else {
                            Text("✅ Verify & Claim Stars", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }

                    // Note
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFfef3c7)).border(1.dp, Color(0xFFfde68a), RoundedCornerShape(12.dp)).padding(12.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("ℹ️", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Payment ke baad UTR/Reference number bharo. Stars 5-10 minutes mein add ho jayenge. Support ke liye Telegram/WhatsApp pe contact karo.",
                                color = Color(0xFF92400e), fontSize = 12.sp, lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
