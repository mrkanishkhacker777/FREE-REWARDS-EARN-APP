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

@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar("📋 Privacy Policy", "", onBack)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf3f0ff))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PolicyCard(
                    "🔒 Data We Collect",
                    "We collect only essential data: device identifiers for code distribution, usage statistics to improve the app, and referral code information. We do not collect personal information like name, email, or location."
                )
                PolicyCard(
                    "💾 How We Use Data",
                    "Your data is used solely to: assign unique redeem codes, track your earned star balance, manage referral relationships, and prevent fraud. We never sell your data to third parties."
                )
                PolicyCard(
                    "🔐 Data Security",
                    "All data is stored locally on your device using encrypted DataStore. We use industry-standard security practices to protect your information from unauthorized access."
                )
                PolicyCard(
                    "🎁 Redeem Codes",
                    "Codes are randomly generated and assigned per device. Each code is unique and can only be redeemed once. We are not affiliated with Google. Google Play is a trademark of Google LLC."
                )
                PolicyCard(
                    "📊 Analytics",
                    "We may collect anonymous usage analytics to improve app performance. No personally identifiable information is included in analytics data."
                )
                PolicyCard(
                    "👶 Children's Privacy",
                    "This app is not intended for children under 13. We do not knowingly collect data from children under 13 years of age."
                )
                PolicyCard(
                    "📧 Contact Us",
                    "For privacy concerns or data deletion requests, please contact us at: vk.143.official@gmail.com\n\nLast updated: January 2025"
                )
            }
        }
    }
}

@Composable
fun TermsScreen(onBack: () -> Unit) {
    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar("📄 Terms of Service", "", onBack)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf3f0ff))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PolicyCard(
                    "📱 App Purpose",
                    "Daily Rewards & Earn App is an entertainment app where users complete tasks to earn virtual stars which can be exchanged for Google Play redeem codes. This app is for entertainment purposes only."
                )
                PolicyCard(
                    "⭐ Stars & Rewards",
                    "Stars are virtual currency within this app. Stars have no real monetary value outside the app. Redeem codes are provided as-is and availability may vary by region and time."
                )
                PolicyCard(
                    "🚫 Prohibited Activities",
                    "Users must not: use bots or automated tools, create multiple accounts, attempt to hack or manipulate the star balance, share false referral codes, or misuse the redeem code system."
                )
                PolicyCard(
                    "🔒 Account Termination",
                    "We reserve the right to terminate accounts that violate these terms, attempt fraud, or engage in prohibited activities. Earned stars and codes may be forfeited upon termination."
                )
                PolicyCard(
                    "⚠️ Disclaimer",
                    "Redeem codes are provided on a best-effort basis. We do not guarantee availability at all times. We are not responsible for codes that have already been redeemed by other users."
                )
                PolicyCard(
                    "🔄 Changes to Terms",
                    "We may update these terms at any time. Continued use of the app after changes constitutes acceptance of the new terms. We will notify users of significant changes via app notifications."
                )
                PolicyCard(
                    "⚖️ Governing Law",
                    "These terms are governed by applicable laws. Any disputes shall be resolved through binding arbitration. Users agree to waive class action rights.\n\nLast updated: January 2025"
                )
            }
        }
    }
}

@Composable
fun PolicyCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Border, RoundedCornerShape(16.dp))
            .background(White)
            .padding(18.dp)
    ) {
        Column {
            Text(title, color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(content, color = TextGray, fontSize = 14.sp, lineHeight = 22.sp)
        }
    }
}
