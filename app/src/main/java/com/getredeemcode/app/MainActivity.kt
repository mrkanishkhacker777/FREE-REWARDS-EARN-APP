package com.getredeemcode.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getredeemcode.app.ads.UnityAdsManager
import com.getredeemcode.app.data.AppDataStore
import com.getredeemcode.app.ui.screens.*
import com.getredeemcode.app.ui.theme.GetRedeemCodeTheme
import com.getredeemcode.app.viewmodel.AppViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Status bar transparent rakho, icons white (light)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false // white icons battery/signal ke liye

        // ── Initialize Unity Ads (Primary) + AdMob (Fallback) ──────────────────
        UnityAdsManager.initialize(this)

        setContent {
            GetRedeemCodeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val vm: AppViewModel = viewModel()

    LaunchedEffect(Unit) { vm.load(context) }

    val hasVisited = remember {
        runBlocking { AppDataStore.getVisited(context).first() }
    }
    val startDestination = if (hasVisited) Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(onEnter = {
                runBlocking { AppDataStore.setVisited(context, true) }
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME) {
            HomeScreen(
                vm = vm,
                onScratch = { navController.navigate(Routes.SCRATCH) },
                onQuiz = { navController.navigate(Routes.QUIZ) },
                onCaptcha = { navController.navigate(Routes.CAPTCHA) },
                onRedeem = { navController.navigate(Routes.REDEEM) },
                onRefer = { navController.navigate(Routes.REFER) },
                onPrivacy = { navController.navigate(Routes.PRIVACY) },
                onTerms = { navController.navigate(Routes.TERMS) }
            )
        }

        composable(Routes.SCRATCH) {
            ScratchScreen(vm = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.QUIZ) {
            QuizScreen(vm = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.CAPTCHA) {
            CaptchaScreen(vm = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.REDEEM) {
            RedeemScreen(vm = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.REFER) {
            ReferScreen(vm = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.PRIVACY) {
            PrivacyScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.TERMS) {
            TermsScreen(onBack = { navController.popBackStack() })
        }
    }
}
