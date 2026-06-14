package com.getredeemcode.app.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.getredeemcode.app.ui.theme.*
import com.getredeemcode.app.viewmodel.AppViewModel

@Composable
fun ScratchScreen(vm: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var scratchKey by remember { mutableStateOf(0) }

    GradientBackground {
        // Fixed full-screen — NO scroll at all
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar — same style as screenshot
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
                Text("Scratch & Win", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.weight(1f))
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

            // White content area — fills rest of screen, no scroll
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Scratch card — medium square size (jaise screenshot mein gift box wala)
                // Size: 300dp x 300dp approx — 4 strokes mein cover ho jaaye
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    // Prize layer underneath (white background with gift + win info)
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎁", fontSize = 72.sp)
                            Spacer(Modifier.height(12.dp))
                            Text("You Won!", color = Purple, fontWeight = FontWeight.Black, fontSize = 22.sp)
                            Spacer(Modifier.height(6.dp))
                            Text("+${vm.formatCoin(vm.pendingScratchCoins)} Stars", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                    }
                    // Scratch overlay on top
                    if (!vm.scratchDone) {
                        key(scratchKey) {
                            ScratchCanvasView(
                                onProgress = { progress -> vm.onScratchProgressUpdate(progress, context) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // "Scratch with your finger to reveal!" — like screenshot
                Text(
                    "👆 Scratch with your finger to reveal!",
                    color = TextGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )

                Spacer(Modifier.height(12.dp))

                // New card option (only if not done)
                if (!vm.scratchDone) {
                    Text(
                        "🔄 New scratch card? Start over.",
                        color = Purple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { scratchKey++; vm.prepareScratch() }
                    )
                }
            }
        }

        // Real AdMob rewarded ad handled by AdManager — no fake overlay needed

        // ── SCRATCH RESULT POPUP ─────────────────────────────────────────────
        if (vm.showScratchResultPopup) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { }, contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp)
                        .clip(RoundedCornerShape(24.dp)).background(White).padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 56.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Congratulations!", color = TextDark, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(50.dp))
                            .background(Brush.linearGradient(listOf(Purple, Pink)))
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) { Text("⭐ +${vm.formatCoin(vm.pendingScratchCoins)} Stars", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp) }
                    Spacer(Modifier.height(6.dp))
                    Text("You scratched the card successfully!", color = TextGray, fontSize = 14.sp)
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp))
                            .background(Brush.linearGradient(listOf(Purple, Pink)))
                            .clickable { vm.claimScratchResult(context); scratchKey++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎉 Claim Stars!", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 18.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("🔄 Scratch Again", color = Purple, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                        modifier = Modifier.clickable { vm.prepareScratch(); scratchKey++ })
                }
            }
        }

        // ── AdMob Loading overlay ──────────────────────────────────────────────
        if (vm.pendingAdSource == "scratch" && vm.showAdLoading) {
            AdLoadingOverlay()
        }

        // Toast
        ToastDisplay(vm = vm)
    }
}

// ─── Native Android Canvas for scratch ───────────────────────────────────────
// strokeWidth 160f — 4 broad strokes mein 300dp card pura cover ho jaaye
@Composable
fun ScratchCanvasView(onProgress: (Float) -> Unit) {
    AndroidView(
        factory = { ctx ->
            object : View(ctx) {
                private var scratchBitmap: Bitmap? = null
                private var scratchCanvas: Canvas? = null
                private var lastX = 0f
                private var lastY = 0f

                private val erasePaint = Paint().apply {
                    alpha = 0
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                    strokeWidth = 160f  // Wide stroke — 4 swipes mein pura cover
                }
                private val bgPaint = Paint().apply {
                    color = android.graphics.Color.rgb(140, 82, 208)  // Purple
                }
                // Gift box circle background
                private val circlePaint = Paint().apply {
                    color = android.graphics.Color.argb(60, 255, 255, 255)  // translucent white
                    style = Paint.Style.FILL
                }
                private val textPaint = Paint().apply {
                    color = android.graphics.Color.WHITE
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = 48f
                }
                private val emojiPaint = Paint().apply {
                    textAlign = Paint.Align.CENTER
                    textSize = 120f
                }

                override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                    super.onSizeChanged(w, h, oldw, oldh)
                    if (w > 0 && h > 0) {
                        scratchBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                        scratchCanvas = Canvas(scratchBitmap!!)
                        // Purple background
                        scratchCanvas!!.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bgPaint)
                        // Translucent circle in center
                        val cx = w / 2f
                        val cy = h / 2f
                        val radius = minOf(w, h) * 0.38f
                        scratchCanvas!!.drawCircle(cx, cy, radius, circlePaint)
                        // Gift box emoji in circle
                        val emojiY = cy - ((emojiPaint.descent() + emojiPaint.ascent()) / 2f)
                        scratchCanvas!!.drawText("🎁", cx, emojiY, emojiPaint)
                        // "Scratch Here!" text below circle
                        val textY = cy + radius + 60f
                        scratchCanvas!!.drawText("👆 Scratch Here!", cx, textY, textPaint)
                    }
                }

                override fun onDraw(canvas: Canvas) {
                    super.onDraw(canvas)
                    scratchBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
                }

                override fun onTouchEvent(event: MotionEvent): Boolean {
                    // Always block parent from stealing this touch — no scroll interference
                    parent?.requestDisallowInterceptTouchEvent(true)

                    val bmp = scratchBitmap ?: return true
                    val sc = scratchCanvas ?: return true

                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            lastX = event.x
                            lastY = event.y
                            sc.drawPoint(event.x, event.y, erasePaint)
                            invalidate()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            // Draw smooth continuous line through all historical points
                            if (event.historySize > 0) {
                                for (i in 0 until event.historySize) {
                                    sc.drawLine(lastX, lastY, event.getHistoricalX(i), event.getHistoricalY(i), erasePaint)
                                    lastX = event.getHistoricalX(i)
                                    lastY = event.getHistoricalY(i)
                                }
                            }
                            sc.drawLine(lastX, lastY, event.x, event.y, erasePaint)
                            lastX = event.x
                            lastY = event.y
                            invalidate()

                            // Sample progress (every 12px for accuracy)
                            var cleared = 0
                            val step = 12
                            for (x in 0 until bmp.width step step) {
                                for (y in 0 until bmp.height step step) {
                                    if (android.graphics.Color.alpha(bmp.getPixel(x, y)) == 0) cleared++
                                }
                            }
                            val total = (bmp.width / step) * (bmp.height / step)
                            onProgress(if (total > 0) cleared.toFloat() / total else 0f)
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            parent?.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    return true
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
