package com.droidbubble.app.island

import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.ArrowUpward
import com.droidbubble.app.model.FpsInfo
import com.droidbubble.app.model.HourlyWeather
import com.droidbubble.app.model.WeatherInfo
import com.droidbubble.app.model.NetworkSpeedInfo
import com.droidbubble.app.island.IslandStateManager.RingerMode
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidbubble.app.model.IslandPriority
import com.droidbubble.app.model.IslandSettings
import com.droidbubble.app.model.MediaInfo
import com.droidbubble.app.model.NotificationInfo
import com.droidbubble.app.model.ScreenRecordingState
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt


private val blurredBitmapCache = java.util.WeakHashMap<Bitmap, Bitmap>()

fun createBlurredBitmap(src: Bitmap, blurRadius: Int = 14, scaleFraction: Float = 0.20f): Bitmap? {
    if (src.isRecycled) return null
    synchronized(blurredBitmapCache) {
        blurredBitmapCache[src]?.let { if (!it.isRecycled) return it }
    }
    return try {
        val w = (src.width * scaleFraction).toInt().coerceAtLeast(16)
        val h = (src.height * scaleFraction).toInt().coerceAtLeast(16)
        val scaled = Bitmap.createScaledBitmap(src, w, h, true)
        val mutable = scaled.copy(Bitmap.Config.ARGB_8888, true)
        if (scaled != src && scaled != mutable && !scaled.isRecycled) {
            scaled.recycle()
        }
        fastBoxBlur(mutable, blurRadius.coerceIn(3, 25))
        synchronized(blurredBitmapCache) {
            blurredBitmapCache[src] = mutable
        }
        mutable
    } catch (_: Throwable) {
        null
    }
}

private fun fastBoxBlur(bitmap: Bitmap, radius: Int) {
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    val wm = width - 1
    val hm = height - 1
    val wh = width * height
    val div = radius + radius + 1

    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    val vmin = IntArray(maxOf(width, height))

    var yw = 0
    var yi = 0

    for (y in 0 until height) {
        var rsum = 0; var gsum = 0; var bsum = 0
        for (i in -radius..radius) {
            val p = pixels[yi + minOf(wm, maxOf(i, 0))]
            rsum += (p shr 16) and 0xff
            gsum += (p shr 8) and 0xff
            bsum += p and 0xff
        }
        for (x in 0 until width) {
            r[yi] = rsum / div
            g[yi] = gsum / div
            b[yi] = bsum / div

            if (y == 0) {
                vmin[x] = minOf(x + radius + 1, wm)
            }
            val p1 = pixels[yw + vmin[x]]
            val p2 = pixels[yw + maxOf(x - radius, 0)]

            rsum += ((p1 shr 16) and 0xff) - ((p2 shr 16) and 0xff)
            gsum += ((p1 shr 8) and 0xff) - ((p2 shr 8) and 0xff)
            bsum += (p1 and 0xff) - (p2 and 0xff)
            yi++
        }
        yw += width
    }

    for (x in 0 until width) {
        var rsum = 0; var gsum = 0; var bsum = 0
        for (i in -radius..radius) {
            yi = maxOf(0, minOf(hm, i)) * width + x
            rsum += r[yi]
            gsum += g[yi]
            bsum += b[yi]
        }
        yi = x
        for (y in 0 until height) {
            pixels[yi] = (0xff shl 24) or ((rsum / div) shl 16) or ((gsum / div) shl 8) or (bsum / div)
            if (x == 0) {
                vmin[y] = minOf(y + radius + 1, hm) * width
            }
            val p1 = x + vmin[y]
            val p2 = x + maxOf(y - radius, 0) * width

            rsum += r[p1] - r[p2]
            gsum += g[p1] - g[p2]
            bsum += b[p1] - b[p2]

            yi += width
        }
    }

    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
}

private val accentColorCache = java.util.WeakHashMap<Bitmap, Color>()

fun extractMediaAccentColor(bitmap: Bitmap?): Color {
    if (bitmap == null) return Color.Transparent
    synchronized(accentColorCache) {
        accentColorCache[bitmap]?.let { return it }
    }
    return try {
        // Sample with a small 16x16 scaled bitmap for ultra-fast, smooth calculation without JNI pixel bottlenecks
        val scaled = if (bitmap.width > 16 || bitmap.height > 16) {
            Bitmap.createScaledBitmap(bitmap, 16, 16, false)
        } else {
            bitmap
        }
        val pixels = IntArray(scaled.width * scaled.height)
        scaled.getPixels(pixels, 0, scaled.width, 0, 0, scaled.width, scaled.height)
        if (scaled != bitmap && !scaled.isRecycled) {
            scaled.recycle()
        }

        var redSum = 0L
        var greenSum = 0L
        var blueSum = 0L
        var count = 0L

        for (pixel in pixels) {
            val a = (pixel shr 24) and 0xff
            if (a > 60) {
                redSum += (pixel shr 16) and 0xff
                greenSum += (pixel shr 8) and 0xff
                blueSum += pixel and 0xff
                count++
            }
        }

        val color = if (count > 0L) {
            val avgR = (redSum / count).toInt().coerceIn(0, 255)
            val avgG = (greenSum / count).toInt().coerceIn(0, 255)
            val avgB = (blueSum / count).toInt().coerceIn(0, 255)
            Color(avgR, avgG, avgB)
        } else {
            Color.Transparent
        }

        synchronized(accentColorCache) {
            accentColorCache[bitmap] = color
        }
        color
    } catch (_: Exception) {
        Color.Transparent
    }
}

/** Remembers the last non-zero expanded card size (plain holder, not snapshot state on purpose). */
private class LastExpandedSize {
    var card1Width: Dp = 0.dp
    var card1Height: Dp = 0.dp
    var card2Height: Dp = 0.dp
}

/** Fade curve of the expanded card content (starts later when collapsing so it vanishes first). */
private fun expandedContentAlpha(isExpanded: Boolean, progress: Float): Float =
    if (isExpanded) {
        ((progress - 0.30f) / 0.70f).coerceIn(0f, 1f)
    } else {
        ((progress - 0.45f) / 0.55f).coerceIn(0f, 1f)
    }

/** Fade curve of the compact pill content while the card expands / collapses. */
private fun compactContentAlpha(isExpanded: Boolean, progress: Float): Float =
    if (isExpanded) {
        (1f - (progress * 3.0f)).coerceIn(0f, 1f)
    } else {
        (1f - (progress * 1.8f)).coerceIn(0f, 1f)
    }

/**
 * Draws the island outline in the DRAW phase so the (animated) corner radius / alpha never has to be
 * read during composition (Modifier.border with an animated shape forced a recomposition per frame).
 */
private fun Modifier.islandOutline(
    enabled: Boolean,
    thicknessDp: Float,
    color: Color,
    minWidth: Dp = 8.dp,
    alpha: () -> Float = { 1f },
    cornerRadius: () -> Dp
): Modifier = if (!enabled) this else this.drawWithContent {
    drawContent()
    if (size.width > minWidth.toPx()) {
        val stroke = thicknessDp.dp.toPx()
        val inset = stroke / 2f
        val a = alpha()
        if (a > 0.001f && size.width > stroke && size.height > stroke) {
            val radius = (cornerRadius().toPx() - inset)
                .coerceIn(0f, (minOf(size.width, size.height) - stroke) / 2f)
            drawRoundRect(
                color = color.copy(alpha = color.alpha * a),
                topLeft = Offset(inset, inset),
                size = Size(size.width - stroke, size.height - stroke),
                cornerRadius = CornerRadius(radius, radius),
                style = Stroke(width = stroke)
            )
        }
    }
}

/**
 * Measures its child at a FIXED [width] x [height] (top-center aligned) regardless of the size of the
 * container. Used so the heavy expanded content is laid out once at its final size and is only
 * revealed by the growing/shrinking, clipped container instead of being re-measured every frame.
 */
private fun Modifier.fixedSizeTopCenter(width: Dp, height: Dp): Modifier =
    this.layout { measurable, constraints ->
        val w = width.roundToPx().coerceAtLeast(0)
        val h = height.roundToPx().coerceAtLeast(0)
        val placeable = measurable.measure(Constraints.fixed(w, h))
        val containerW = if (constraints.hasBoundedWidth) constraints.maxWidth else w
        val containerH = if (constraints.hasBoundedHeight) constraints.maxHeight else h
        layout(containerW, containerH) {
            placeable.place((containerW - w) / 2, 0)
        }
    }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IslandOverlayRoot(
    settings: IslandSettings,
    onBoundsChange: (widthPx: Int, heightPx: Int) -> Unit
) {
    val isExpandedRaw by IslandStateManager.isExpanded.collectAsState()
    val isPopIn by IslandStateManager.isPopInActive.collectAsState()
    val media by IslandStateManager.activeMedia.collectAsState()
    val notification by IslandStateManager.activeNotification.collectAsState()
    val progressNotification by IslandStateManager.activeProgress.collectAsState()
    val recording by IslandStateManager.screenRecordingState.collectAsState()
    val charging by IslandStateManager.activeCharging.collectAsState()
    val isUnlocked by IslandStateManager.isUnlocked.collectAsState()
    val isBluetoothConnected by IslandStateManager.isBluetoothConnected.collectAsState()
    val activeRingerMode by IslandStateManager.activeRingerMode.collectAsState()
    val isFlashlightOn by IslandStateManager.isFlashlightOn.collectAsState()
    val flashlightLevel by IslandStateManager.flashlightLevel.collectAsState()
    val maxFlashlightLevel by IslandStateManager.maxFlashlightLevel.collectAsState()
    val isKeyboardFocused by IslandStateManager.isKeyboardFocused.collectAsState()
    val fpsInfo by IslandStateManager.fpsInfo.collectAsState()
    val weatherInfo by IslandStateManager.weatherInfo.collectAsState()
    val networkSpeedInfo by IslandStateManager.networkSpeedInfo.collectAsState()

    val view = LocalView.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var lastRingerMode by remember { mutableStateOf(RingerMode.RING) }
    LaunchedEffect(activeRingerMode) {
        if (activeRingerMode != null) {
            lastRingerMode = activeRingerMode!!
        }
    }
    val displayRingerMode = activeRingerMode ?: lastRingerMode

    val idleBounceY = remember { Animatable(0f) }
    val idleBounceScale = remember { Animatable(1f) }

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val allowInOrientation = settings.isEnabled && (settings.showInLandscape || !isLandscape)

    if (!allowInOrientation || !settings.isEnabled) {
        return
    }

    // ---- Window-size handshake (keeps the real window resize, just sequences it) ----
    // The service grows the overlay window (1x1 -> pill -> fullscreen) when the island appears / expands.
    // That relayout + new surface buffer makes one heavy frame on mid-range GPUs; if the spring animation
    // starts in the same frame, that hitch lands INSIDE the animation. So the animations are held back
    // until the window has actually reached its new size (with a timeout fallback so we never get stuck).
    var rootSize by remember { mutableStateOf(IntSize.Zero) }
    Box(modifier = Modifier.fillMaxSize().onSizeChanged { rootSize = it })

    val windowDensity = LocalDensity.current
    val expandNeedPx = with(windowDensity) { (settings.topOffsetDp + settings.heightDp + 28).dp.roundToPx() }
    var forceExpandReady by remember { mutableStateOf(false) }
    LaunchedEffect(isExpandedRaw) {
        forceExpandReady = false
        if (isExpandedRaw) {
            delay(250)
            forceExpandReady = true
        }
    }
    // Everything below treats the island as "expanded" only once the window is big enough.
    val isExpanded = isExpandedRaw && (rootSize.height >= expandNeedPx || forceExpandReady)

    val currentMedia = media
    val currentNotif = notification
    val currentProgress = progressNotification
    val mediaArt = currentMedia?.albumArt ?: currentMedia?.appIcon
    val mediaAccentColor = remember(mediaArt) { extractMediaAccentColor(mediaArt) }
    // Perf: the CPU box-blur used to run on the main thread right when a track changed (a visible
    // hitch on mid-range SoCs). It now runs on Dispatchers.Default; until it finishes we simply
    // don't draw the blurred backdrop instead of drawing the sharp art.
    val blurredMediaArt by produceState<Bitmap?>(null, mediaArt, settings.blurredAlbumArtBackground) {
        val art = mediaArt
        value = if (settings.blurredAlbumArtBackground && art != null) {
            withContext(Dispatchers.Default) { createBlurredBitmap(art) }
        } else {
            null
        }
    }
    val blurredMediaArtImage = remember(blurredMediaArt) { blurredMediaArt?.asImageBitmap() }
    val isRecording = allowInOrientation && recording.isRecording && settings.showScreenRecording
    val isProgress = allowInOrientation && currentProgress != null && settings.showLiveProgress
    val isNotification = allowInOrientation && currentNotif != null && settings.showNotifications
    val isMedia = allowInOrientation && currentMedia != null && (!settings.dismissMediaOnPause || currentMedia.isPlaying) && (currentMedia.isPlaying || currentMedia.title.isNotBlank()) && settings.showMedia
    val isFlashlight = allowInOrientation && settings.showFlashlight && isFlashlightOn
    val isCharging = allowInOrientation && settings.showCharging && charging != null
    val isUnlock = allowInOrientation && isUnlocked && !isCharging
    val isBluetooth = allowInOrientation && settings.showBluetooth && isBluetoothConnected && !isCharging && !isUnlock
    val isRingerMode = allowInOrientation && settings.showRingerMode && activeRingerMode != null && !isCharging && !isUnlock && !isBluetooth

    val isFps = allowInOrientation && settings.showFps
    val isWeather = allowInOrientation && settings.showWeather
    val isNetworkSpeed = allowInOrientation && settings.showNetworkSpeed
    val hasPersistent = isFps || isWeather || isNetworkSpeed

    val isOccupied = allowInOrientation && (isRecording || isNotification || isMedia || isProgress || isCharging || isUnlock || isBluetooth || isFlashlight || isRingerMode || hasPersistent)

    // Same handshake for the pill appearing: wait until the window left its 1x1 idle size.
    val compactNeedH = with(windowDensity) { (settings.topOffsetDp + settings.heightDp).dp.roundToPx() }
    val compactNeedW = with(windowDensity) { settings.widthDp.dp.roundToPx() }
    var forceCompactReady by remember { mutableStateOf(false) }
    val wantsCompact = isOccupied || settings.alwaysShowPill
    LaunchedEffect(wantsCompact) {
        forceCompactReady = false
        if (wantsCompact) {
            delay(200)
            forceCompactReady = true
        }
    }
    val compactWindowReady = (rootSize.height >= compactNeedH && rootSize.width >= compactNeedW) || forceCompactReady

    val persistentList = buildList {
        if (isFps) add(IslandPriority.FPS)
        if (isWeather) add(IslandPriority.WEATHER)
        if (isNetworkSpeed) add(IslandPriority.NETWORK_SPEED)
    }
    val persistentPrimary = persistentList.firstOrNull()
    val persistentSecondary = if (persistentList.size > 1) persistentList[1] else null

    // Determine dual-island state (when 2+ activities occupy the island, temporarily overshadowed by charging, unlock, bluetooth, or ringer)
    val activeCount = (if (isRecording) 1 else 0) +
            (if (isNotification) 1 else 0) +
            (if (isMedia) 1 else 0) +
            (if (isProgress) 1 else 0) +
            (if (isFlashlight) 1 else 0)
    val ephemeralCount = activeCount
    val isDual = !isCharging && !isUnlock && !isBluetooth && !isRingerMode && (
        ephemeralCount >= 2 || (ephemeralCount == 0 && persistentList.size >= 2)
    )

    // Primary & Secondary activity assignment ordered dynamically by settings.featurePriorities:
    val orderedEphemeralPriorities = buildList {
        for (featureKey in settings.featurePriorities) {
            when (featureKey) {
                "MEDIA" -> if (isMedia) add(IslandPriority.MEDIA)
                "PROGRESS" -> if (isProgress) add(IslandPriority.PROGRESS)
                "RECORDING" -> if (isRecording) add(IslandPriority.SCREEN_RECORDING)
                "NOTIFICATION" -> if (isNotification) add(IslandPriority.POP_NOTIFICATION)
                "FLASHLIGHT" -> if (isFlashlight) add(IslandPriority.FLASHLIGHT)
            }
        }
        if (isFlashlight && "FLASHLIGHT" !in settings.featurePriorities) {
            add(IslandPriority.FLASHLIGHT)
        }
    }

    val primaryPriority: IslandPriority = when {
        orderedEphemeralPriorities.isNotEmpty() -> orderedEphemeralPriorities.first()
        persistentPrimary != null -> persistentPrimary
        else -> IslandPriority.IDLE
    }

    val secondaryPriority: IslandPriority? = when {
        !isDual -> null
        orderedEphemeralPriorities.size >= 2 -> {
            if (isPopIn && isNotification && primaryPriority != IslandPriority.POP_NOTIFICATION) {
                IslandPriority.POP_NOTIFICATION
            } else {
                orderedEphemeralPriorities[1]
            }
        }
        ephemeralCount == 0 -> persistentSecondary
        else -> null
    }

    // Dynamic non-customizable outline colors (around 15% opacity):
    val redRecordingOutline = Color(0xFFFF3B30).copy(alpha = 0.15f)
    val defaultTaskOutline = Color.White.copy(alpha = 0.15f)
    val mediaOutline = if (mediaAccentColor != Color.Transparent) {
        mediaAccentColor.copy(alpha = 0.15f)
    } else {
        defaultTaskOutline
    }
    val flashlightOutline = Color.White.copy(alpha = 0.25f)

    // Card 1 (Main Island):
    val primaryOutlineColor = when {
        isCharging -> Color(0xFF30D158).copy(alpha = 0.30f)
        primaryPriority == IslandPriority.SCREEN_RECORDING -> redRecordingOutline
        primaryPriority == IslandPriority.MEDIA -> mediaOutline
        primaryPriority == IslandPriority.FLASHLIGHT -> flashlightOutline
        else -> defaultTaskOutline
    }

    // Card 2 / Auxiliary Bubble (Secondary Island):
    // Secondary islands have the color of their respective task (15% opacity white usually, or red for recording) instead of the media color
    val secondaryOutlineColor = when {
        secondaryPriority == IslandPriority.SCREEN_RECORDING -> redRecordingOutline
        secondaryPriority == IslandPriority.MEDIA -> mediaOutline
        secondaryPriority == IslandPriority.FLASHLIGHT -> flashlightOutline
        else -> defaultTaskOutline
    }

    val speed = settings.animationSpeed.coerceIn(0.5f, 2.0f)
    val speedSq = speed * speed

    // Animation Specs (dynamically scaled by settings.animationSpeed):
    val sizeAnimSpecDp = when {
        isCharging || isUnlock || isBluetooth || isRingerMode -> spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = settings.appearDamping,
            stiffness = settings.appearStiffness * speedSq
        )
        isExpanded -> spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = settings.expandDamping,
            stiffness = settings.expandStiffness * speedSq
        )
        else -> tween<androidx.compose.ui.unit.Dp>(
            durationMillis = (190 / speed).roundToInt(),
            easing = CubicBezierEasing(0.28f, 0.0f, 0.20f, 1.0f)
        )
    }

    val sizeAnimSpecFloat = when {
        isCharging || isUnlock || isBluetooth || isRingerMode -> spring<Float>(
            dampingRatio = settings.appearDamping,
            stiffness = settings.appearStiffness * speedSq
        )
        isExpanded -> spring<Float>(
            dampingRatio = settings.expandDamping,
            stiffness = settings.expandStiffness * speedSq
        )
        else -> tween<Float>(
            durationMillis = (190 / speed).roundToInt(),
            easing = CubicBezierEasing(0.28f, 0.0f, 0.20f, 1.0f)
        )
    }

    val positionAnimSpecDp = if (isExpanded) {
        spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = settings.expandDamping,
            stiffness = settings.expandStiffness * speedSq
        )
    } else {
        tween<androidx.compose.ui.unit.Dp>(
            durationMillis = (200 / speed).roundToInt(),
            easing = CubicBezierEasing(0.28f, 0.0f, 0.20f, 1.0f)
        )
    }

    // Dedicated spec for compact pill width:
    // When expanding: crisp spring shrinking into 0 width without overshoot
    // When emerging back: subtle horizontal bounce of a few pixels (dampingRatio = 0.63f)!
    val compactPillWidthAnimSpec = if (isExpanded) {
        spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = 0.82f,
            stiffness = 380f * speedSq
        )
    } else {
        spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = settings.splitDamping,
            stiffness = settings.splitStiffness * speedSq
        )
    }

    val expandProgressState = animateFloatAsState(
        targetValue = if (isExpanded && !isCharging && !isUnlock && !isBluetooth && !isRingerMode) 1f else 0f,
        animationSpec = sizeAnimSpecFloat,
        label = "expandProgress"
    )

    val chargingContentAlphaState = animateFloatAsState(
        targetValue = if (isCharging) 1f else 0f,
        animationSpec = tween(durationMillis = (130 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "chargingContentAlpha"
    )

    val unlockContentAlphaState = animateFloatAsState(
        targetValue = if (isUnlock) 1f else 0f,
        animationSpec = tween(durationMillis = (130 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "unlockContentAlpha"
    )

    var unlockCheckmarkVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isUnlock) {
        if (isUnlock) {
            unlockCheckmarkVisible = false
            kotlinx.coroutines.delay((110L / speed).toLong())
            unlockCheckmarkVisible = true
        } else {
            unlockCheckmarkVisible = false
        }
    }

    val unlockCheckmarkAlphaState = animateFloatAsState(
        targetValue = if (unlockCheckmarkVisible && isUnlock) 1f else 0f,
        animationSpec = tween(durationMillis = (150 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "unlockCheckmarkAlpha"
    )

    val unlockCheckmarkScaleState = animateFloatAsState(
        targetValue = if (unlockCheckmarkVisible && isUnlock) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = 0.52f, // Gentle bounce: overshoots slightly by ~3-4px, then settles
            stiffness = 320f * speedSq
        ),
        label = "unlockCheckmarkScale"
    )

    val bluetoothContentAlphaState = animateFloatAsState(
        targetValue = if (isBluetooth) 1f else 0f,
        animationSpec = tween(durationMillis = (130 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "bluetoothContentAlpha"
    )

    var bluetoothCheckmarkVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isBluetooth) {
        if (isBluetooth) {
            bluetoothCheckmarkVisible = false
            kotlinx.coroutines.delay((110L / speed).toLong())
            bluetoothCheckmarkVisible = true
        } else {
            bluetoothCheckmarkVisible = false
        }
    }

    val bluetoothCheckmarkAlphaState = animateFloatAsState(
        targetValue = if (bluetoothCheckmarkVisible && isBluetooth) 1f else 0f,
        animationSpec = tween(durationMillis = (150 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "bluetoothCheckmarkAlpha"
    )

    val bluetoothCheckmarkScaleState = animateFloatAsState(
        targetValue = if (bluetoothCheckmarkVisible && isBluetooth) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = 0.52f, // Gentle bounce: overshoots slightly by ~3-4px, then settles
            stiffness = 320f * speedSq
        ),
        label = "bluetoothCheckmarkScale"
    )

    // Ringer Mode (Silent, Vibrate, Ring) animations:
    val ringerContentAlphaState = animateFloatAsState(
        targetValue = if (isRingerMode) 1f else 0f,
        animationSpec = tween(durationMillis = (130 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "ringerContentAlpha"
    )

    var ringerSecondaryVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isRingerMode) {
        if (isRingerMode) {
            ringerSecondaryVisible = false
            kotlinx.coroutines.delay((110L / speed).toLong())
            ringerSecondaryVisible = true
        } else {
            ringerSecondaryVisible = false
        }
    }

    val ringerSecondaryAlphaState = animateFloatAsState(
        targetValue = if (ringerSecondaryVisible && isRingerMode) 1f else 0f,
        animationSpec = tween(durationMillis = (150 / speed).roundToInt(), easing = FastOutSlowInEasing),
        label = "ringerSecondaryAlpha"
    )

    val ringerSecondaryScaleState = animateFloatAsState(
        targetValue = if (ringerSecondaryVisible && isRingerMode) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = 0.52f,
            stiffness = 320f * speedSq
        ),
        label = "ringerSecondaryScale"
    )

    // Perf: gating flags for the small state-change visuals. Reading the raw animated alpha in
    // composition forced the whole (huge) root composable to recompose on EVERY frame of the tween.
    val chargingVisible by remember(isCharging) { derivedStateOf { isCharging || chargingContentAlphaState.value > 0.01f } }
    val unlockVisible by remember(isUnlock) { derivedStateOf { isUnlock || unlockContentAlphaState.value > 0.01f } }
    val bluetoothVisible by remember(isBluetooth) { derivedStateOf { isBluetooth || bluetoothContentAlphaState.value > 0.01f } }
    val ringerVisible by remember(isRingerMode) { derivedStateOf { isRingerMode || ringerContentAlphaState.value > 0.01f } }
    val unlockCheckShown by remember { derivedStateOf { unlockCheckmarkAlphaState.value > 0.01f } }
    val bluetoothCheckShown by remember { derivedStateOf { bluetoothCheckmarkAlphaState.value > 0.01f } }
    val ringerLabelShown by remember { derivedStateOf { ringerSecondaryAlphaState.value > 0.01f } }

    val bellShakeRotation = remember { Animatable(0f) }

    LaunchedEffect(isRingerMode, activeRingerMode) {
        if (isRingerMode && activeRingerMode == RingerMode.RING) {
            launch {
                if (settings.hapticsEnabled) {
                    try {
                        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                            vm?.defaultVibrator
                        } else {
                            @Suppress("DEPRECATION")
                            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                        }
                        if (vibrator != null && vibrator.hasVibrator()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val timings = longArrayOf(0, 25, 100, 25, 100, 25)
                                val amplitudes = intArrayOf(0, 180, 0, 200, 0, 220)
                                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(longArrayOf(0, 25, 100, 25, 100, 25), -1)
                            }
                        } else {
                            for (i in 0 until 3) {
                                if (i > 0) delay(100)
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            }
                        }
                    } catch (_: Throwable) {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    }
                }
            }
            bellShakeRotation.snapTo(0f)
            val animDuration = (1000 / speed).roundToInt()
            val rotSpec = keyframes<Float> {
                durationMillis = animDuration
                0f at 0 using FastOutSlowInEasing
                -24f at (animDuration * 0.06f).roundToInt() using FastOutSlowInEasing
                24f at (animDuration * 0.13f).roundToInt() using FastOutSlowInEasing
                -18f at (animDuration * 0.22f).roundToInt() using FastOutSlowInEasing
                14f at (animDuration * 0.33f).roundToInt() using FastOutSlowInEasing
                -10f at (animDuration * 0.46f).roundToInt() using FastOutSlowInEasing
                6f at (animDuration * 0.60f).roundToInt() using FastOutSlowInEasing
                -3f at (animDuration * 0.75f).roundToInt() using FastOutSlowInEasing
                1f at (animDuration * 0.88f).roundToInt() using FastOutSlowInEasing
                0f at animDuration using FastOutSlowInEasing
            }
            bellShakeRotation.animateTo(0f, animationSpec = rotSpec)
        } else {
            bellShakeRotation.snapTo(0f)
        }
    }

    val vibrateShakeOffset = remember { Animatable(0f) }
    LaunchedEffect(isRingerMode, activeRingerMode) {
        if (isRingerMode && activeRingerMode == RingerMode.VIBRATE) {
            vibrateShakeOffset.snapTo(0f)
            val vibDuration = (600 / speed).roundToInt()
            val vibSpec = keyframes<Float> {
                durationMillis = vibDuration
                0f at 0 using FastOutSlowInEasing
                -3f at (vibDuration * 0.08f).roundToInt() using FastOutSlowInEasing
                3f at (vibDuration * 0.16f).roundToInt() using FastOutSlowInEasing
                -3f at (vibDuration * 0.25f).roundToInt() using FastOutSlowInEasing
                3f at (vibDuration * 0.35f).roundToInt() using FastOutSlowInEasing
                -2f at (vibDuration * 0.48f).roundToInt() using FastOutSlowInEasing
                2f at (vibDuration * 0.62f).roundToInt() using FastOutSlowInEasing
                -1f at (vibDuration * 0.78f).roundToInt() using FastOutSlowInEasing
                0f at vibDuration using FastOutSlowInEasing
            }
            vibrateShakeOffset.animateTo(0f, animationSpec = vibSpec)
        } else {
            vibrateShakeOffset.snapTo(0f)
        }
    }

    val chargingPct = charging?.percentage ?: 0
    val targetChargingFraction = if (isCharging) (chargingPct.toFloat() / 100f).coerceIn(0f, 1f) else 0f
    val animatedChargingProgressState = animateFloatAsState(
        targetValue = targetChargingFraction,
        animationSpec = if (isCharging) {
            tween(
                durationMillis = (750 / speed).roundToInt(),
                delayMillis = (60 / speed).roundToInt(),
                easing = FastOutSlowInEasing
            )
        } else {
            tween(
                durationMillis = (150 / speed).roundToInt(),
                easing = FastOutLinearInEasing
            )
        },
        label = "chargingProgress"
    )

    val chargingIslandWidth = (settings.widthDp + 20).dp.coerceAtLeast(140.dp)
    val unlockIslandWidth = (settings.widthDp * 0.82f).dp.coerceAtLeast(108.dp)
    val ringerIslandWidth = (settings.widthDp * 0.95f).dp.coerceAtLeast(130.dp)

    // Minimised Island Pill: sits docked at settings.topOffsetDp.dp.
    // When expanding, it animates from its compact width down to 0.dp.
    // When minimising, it animates from 0.dp back to its compact width.
    val targetCompactWidth = when {
        !isOccupied && !settings.alwaysShowPill && !hasPersistent -> 0.dp
        !compactWindowReady -> 0.dp // window is still growing from its idle size; don't animate into it yet
        isExpanded -> 0.dp // Shrinks horizontally into 0 width while card expands!
        isCharging -> chargingIslandWidth
        isRingerMode -> ringerIslandWidth
        isUnlock || isBluetooth -> unlockIslandWidth
        isDual -> (settings.widthDp * 0.74f).dp
        else -> settings.widthDp.dp
    }

    // Expanded Cards: Card 1 (and Card 2 if dual mode).
    // When expanded, Card 1 slides down to (settings.topOffsetDp + 52).dp, widening to 340.dp * scale.
    // When minimising, Card 1 shrinks back to 0.dp width (transitioning into the small circle at the punch hole) as it slides back up.
    val screenWidth = configuration.screenWidthDp.dp
    val targetCard1Width = if (isExpanded && !isCharging && !isUnlock && !isBluetooth && !isRingerMode) {
        (340.dp * settings.expandedScale).coerceAtMost(screenWidth - 16.dp)
    } else {
        0.dp
    }

    val notifText = currentNotif?.text ?: ""
    val isLongNotif = notifText.length > 55 || notifText.contains('\n')
    val notifBaseHeight = when {
        isKeyboardFocused -> 188.dp
        isLongNotif -> 168.dp
        else -> 144.dp
    }

    val targetCard1Height = when {
        isExpanded && primaryPriority == IslandPriority.SCREEN_RECORDING -> (115.dp * settings.expandedScale).coerceAtLeast(100.dp)
        isExpanded && primaryPriority == IslandPriority.POP_NOTIFICATION -> (notifBaseHeight * settings.expandedScale).coerceAtLeast(136.dp)
        isExpanded && primaryPriority == IslandPriority.PROGRESS -> (182.dp * settings.expandedScale).coerceAtLeast(155.dp)
        isExpanded && primaryPriority == IslandPriority.FLASHLIGHT -> (205.dp * settings.expandedScale).coerceAtLeast(165.dp)
        isExpanded && primaryPriority == IslandPriority.FPS -> (185.dp * settings.expandedScale).coerceAtLeast(150.dp)
        isExpanded && primaryPriority == IslandPriority.WEATHER -> (195.dp * settings.expandedScale).coerceAtLeast(155.dp)
        isExpanded && primaryPriority == IslandPriority.NETWORK_SPEED -> (185.dp * settings.expandedScale).coerceAtLeast(150.dp)
        isExpanded -> (195.dp * settings.expandedScale).coerceAtLeast(155.dp)
        else -> settings.heightDp.dp
    }

    val targetCornerRadius = when {
        isExpanded && !isCharging && !isUnlock && !isBluetooth && !isRingerMode -> 28.dp
        else -> settings.cornerRadiusDp.dp
    }

    // Card 2 delayed appearance: starts a split frame after Card 1 when expanding
    var card2Expanded by remember { mutableStateOf(false) }
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            kotlinx.coroutines.delay((45L / speed).toLong())
            card2Expanded = true
        } else {
            card2Expanded = false
        }
    }

    val showSecondaryCard = !isCharging && !isUnlock && !isBluetooth && !isRingerMode && activeCount >= 2 && secondaryPriority != null
    val targetCard2Height = if (card2Expanded && showSecondaryCard) {
        val baseH = when (secondaryPriority) {
            IslandPriority.SCREEN_RECORDING -> 115.dp
            IslandPriority.POP_NOTIFICATION -> notifBaseHeight
            IslandPriority.PROGRESS -> 182.dp
            IslandPriority.FLASHLIGHT -> 205.dp
            IslandPriority.FPS -> 185.dp
            IslandPriority.WEATHER -> 195.dp
            IslandPriority.NETWORK_SPEED -> 185.dp
            else -> 180.dp
        }
        val minH = when (secondaryPriority) {
            IslandPriority.SCREEN_RECORDING -> 100.dp
            IslandPriority.POP_NOTIFICATION -> 136.dp
            IslandPriority.PROGRESS -> 155.dp
            IslandPriority.FLASHLIGHT -> 165.dp
            else -> 150.dp
        }
        (baseH * settings.expandedScale).coerceAtLeast(minH)
    } else 0.dp
    val targetCard2Alpha = if (card2Expanded && showSecondaryCard) 1f else 0f
    val targetCard2OffsetY = if (card2Expanded && showSecondaryCard) 0.dp else (-14).dp

    val bubbleSize = settings.heightDp.dp
    val compactMainHalf = (settings.widthDp * 0.74f).dp / 2f
    val bubbleGap = 6.dp
    val restingBubbleCenterX = compactMainHalf + bubbleGap + (bubbleSize / 2f)

    val shouldEmergeSecondary = !isCharging && !isUnlock && !isBluetooth && !isRingerMode && isDual && !isExpanded && (isOccupied || hasPersistent)
    var secondaryBubbleEmerged by remember { mutableStateOf(false) }
    LaunchedEffect(shouldEmergeSecondary) {
        if (shouldEmergeSecondary) {
            // Subtle delay for a split fraction when the main island appears, so the main island establishes first
            kotlinx.coroutines.delay((60L / speed).toLong().coerceAtLeast(10L))
            secondaryBubbleEmerged = true
        } else {
            secondaryBubbleEmerged = false
        }
    }

    val targetBubbleOffsetX = when {
        secondaryBubbleEmerged && shouldEmergeSecondary -> restingBubbleCenterX
        else -> 0.dp
    }

    val bubbleOffsetAnimSpec = if (secondaryBubbleEmerged && shouldEmergeSecondary) {
        spring<Dp>(
            dampingRatio = settings.splitDamping, // Slight bounce! Gently overshoots by ~3-4dp and bounces back
            stiffness = settings.splitStiffness * speedSq
        )
    } else {
        spring<Dp>(
            dampingRatio = 0.85f, // Smooth retraction back inside the punch hole
            stiffness = 380f * speedSq
        )
    }

    val targetHorizontalOffset = if (isExpanded && !isCharging && !isUnlock && !isBluetooth && !isRingerMode) {
        0.dp
    } else if (!isCharging && !isUnlock && !isBluetooth && !isRingerMode) {
        settings.horizontalOffsetDp.dp
    } else {
        0.dp
    }

    val targetTopOffset = if (isExpanded && !isCharging && !isUnlock && !isBluetooth && !isRingerMode) {
        (settings.topOffsetDp + 52).dp // Sits way below initial position (comfortably below status bar) when expanded!
    } else {
        settings.topOffsetDp.dp // Docks right over the punch hole when minimised or charging/unlock/bluetooth/ringer
    }

    val targetCardSpacing = if (isExpanded && showSecondaryCard) 10.dp else 0.dp

    // PERF NOTE: every animated value below is kept as a `State` and is ONLY read inside layout /
    // draw lambdas (offset {}, layout {}, graphicsLayer {}, drawWithContent {}). Reading them with
    // `by` (i.e. in composition) made this whole ~1600 line composable recompose on every frame of
    // every animation, which is what made the island stutter on mid-range phones (Redmi 12).
    // Anything that must gate composition goes through derivedStateOf so it only recomposes when the
    // boolean actually flips.
    val compactWidthState = animateDpAsState(targetValue = targetCompactWidth, animationSpec = compactPillWidthAnimSpec, label = "compactWidth")
    val card1WidthState = animateDpAsState(targetValue = targetCard1Width, animationSpec = sizeAnimSpecDp, label = "card1Width")
    val card1HeightState = animateDpAsState(targetValue = targetCard1Height, animationSpec = sizeAnimSpecDp, label = "card1Height")
    val cornerRadiusState = animateDpAsState(targetValue = targetCornerRadius, animationSpec = sizeAnimSpecDp, label = "cornerRadius")
    val card2HeightState = animateDpAsState(targetValue = targetCard2Height, animationSpec = sizeAnimSpecDp, label = "card2Height")
    val card2AlphaState = animateFloatAsState(targetValue = targetCard2Alpha, animationSpec = sizeAnimSpecFloat, label = "card2Alpha")
    val card2OffsetYState = animateDpAsState(targetValue = targetCard2OffsetY, animationSpec = sizeAnimSpecDp, label = "card2OffsetY")
    val bubbleOffsetXState = animateDpAsState(targetValue = targetBubbleOffsetX, animationSpec = bubbleOffsetAnimSpec, label = "bubbleOffsetX")
    val horizontalOffsetState = animateDpAsState(targetValue = targetHorizontalOffset, animationSpec = positionAnimSpecDp, label = "horizontalOffset")
    val topOffsetState = animateDpAsState(targetValue = targetTopOffset, animationSpec = positionAnimSpecDp, label = "topOffset")
    val cardSpacingState = animateDpAsState(targetValue = targetCardSpacing, animationSpec = sizeAnimSpecDp, label = "cardSpacing")

    // Last non-zero expanded size. The card CONTENT is laid out once at this final size and merely
    // revealed (clipped) by the growing container, so the heavy content tree is not re-measured on
    // every animation frame.
    val lastExpandedSize = remember { LastExpandedSize() }
    if (targetCard1Width > 0.dp) {
        lastExpandedSize.card1Width = targetCard1Width
        lastExpandedSize.card1Height = targetCard1Height
    }
    if (targetCard2Height > 0.dp) {
        lastExpandedSize.card2Height = targetCard2Height
    }

    // Composition-level gates (flip rarely, unlike the raw animated values).
    val animationsSettled by remember(isExpanded) {
        derivedStateOf { compactWidthState.value <= 0.1.dp && expandProgressState.value <= 0.001f }
    }
    val backdropVisible by remember { derivedStateOf { expandProgressState.value > 0.05f } }
    val expandActive by remember { derivedStateOf { expandProgressState.value > 0.001f } }
    val card1WidthVisible by remember { derivedStateOf { card1WidthState.value > 1.dp } }
    val card2HeightVisible by remember { derivedStateOf { card2HeightState.value > 1.dp } }
    val card2ContentVisible by remember { derivedStateOf { card2AlphaState.value > 0.15f } }
    val expandedContentVisible by remember(isExpanded) {
        derivedStateOf { isExpanded || expandedContentAlpha(false, expandProgressState.value) > 0.01f }
    }
    val compactPillVisible by remember { derivedStateOf { compactWidthState.value > 0.5.dp } }
    val compactContentVisible by remember(isExpanded) {
        derivedStateOf {
            compactContentAlpha(isExpanded, expandProgressState.value) > 0.01f && compactWidthState.value > 24.dp
        }
    }
    // emergence of the pill out of the punch hole (smoothstep over the first ~circle of width)
    val circleThresholdDp = (settings.heightDp.dp * 0.95f).coerceAtLeast(10.dp)
    val compactPillAlphaState = remember(circleThresholdDp) {
        derivedStateOf {
            val e = (compactWidthState.value / circleThresholdDp).coerceIn(0f, 1f)
            e * e * (3f - 2f * e)
        }
    }

    var card1OffsetX by remember { mutableFloatStateOf(0f) }
    var card2OffsetX by remember { mutableFloatStateOf(0f) }

    val animatedCard1OffsetXState = animateFloatAsState(
        targetValue = card1OffsetX,
        animationSpec = spring(dampingRatio = 0.76f, stiffness = 420f * speedSq),
        label = "card1OffsetX"
    )
    val animatedCard2OffsetXState = animateFloatAsState(
        targetValue = card2OffsetX,
        animationSpec = spring(dampingRatio = 0.76f, stiffness = 420f * speedSq),
        label = "card2OffsetX"
    )

    LaunchedEffect(isExpanded) {
        if (!isExpanded) {
            card1OffsetX = 0f
            card2OffsetX = 0f
        }
    }

    // Parentheses overshoot animation: curve shoots out with dynamic spring physics and settles
    val parenthesisExtraShoot = remember { Animatable(0f) }
    var wasExpanded by remember { mutableStateOf(isExpanded) }
    var wasOccupied by remember { mutableStateOf(isOccupied) }

    LaunchedEffect(isExpanded, isOccupied, settings.bubbleEffectEnabled) {
        val justMinimised = wasExpanded && !isExpanded
        val justAppeared = !wasOccupied && isOccupied && !isExpanded
        wasExpanded = isExpanded
        wasOccupied = isOccupied

        if (settings.bubbleEffectEnabled && (justMinimised || justAppeared)) {
            parenthesisExtraShoot.snapTo(0f)
            // Wait for minimised island pill to expand close to punch hole exit (~70ms at 1x speed)
            kotlinx.coroutines.delay((70L / speed).toLong().coerceAtLeast(10L))
            // Shoot outward from the main island with spring bounce and settle back to 0
            parenthesisExtraShoot.animateTo(
                targetValue = 0f,
                initialVelocity = 240f * speed,
                animationSpec = spring(
                    dampingRatio = 0.65f,
                    stiffness = 260f * speedSq
                )
            )
        } else {
            parenthesisExtraShoot.snapTo(0f)
        }
    }

    LaunchedEffect(charging?.timestamp) {
        if (charging != null && settings.hapticsEnabled) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    LaunchedEffect(isExpanded, isOccupied, primaryPriority) {
        if (isExpanded) {
            if (!isOccupied || primaryPriority == IslandPriority.IDLE) {
                IslandStateManager.collapse()
            }
        }
    }

    fun triggerHaptic() {
        if (settings.hapticsEnabled) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    // Completely idle and collapsed: nothing is visible on screen. Skip rendering the view hierarchy to preserve CPU & battery.
    if (!isOccupied && !settings.alwaysShowPill && !isExpanded && animationsSettled) {
        return
    }

    // Outer container: full touch bounds when expanded to capture outside tap, or exact bounds when minimised
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Tap outside backdrop when expanded (active during whole expand and collapse animation)
        if (backdropVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        triggerHaptic()
                        IslandStateManager.collapse()
                    }
            )
        }



        // Expanded Cards Container: Positioned at animated top and horizontal offset
        if (expandActive || isExpanded) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = horizontalOffsetState.value.roundToPx(),
                            y = topOffsetState.value.roundToPx()
                        )
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                // Cards Stack (Card 1 + Card 2). The gap between the cards is applied as a layout-phase
                // offset on card 2 (see below) so it can animate without recomposition.
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = if (isExpanded && primaryPriority != IslandPriority.FLASHLIGHT) {
                        Modifier.pointerInput(Unit) {
                            detectVerticalDragGestures { _, dragAmount ->
                                if (dragAmount < -15f) {
                                    triggerHaptic()
                                    IslandStateManager.collapse()
                                }
                            }
                        }
                    } else Modifier
                ) {
                    // Card 1: Primary Expanded Card
                    if (card1WidthVisible && (expandActive || isExpanded)) {
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(animatedCard1OffsetXState.value.roundToInt(), 0) }
                                // size is read in the layout phase only (no recomposition per frame)
                                .layout { measurable, constraints ->
                                    val w = constraints.constrainWidth(card1WidthState.value.roundToPx().coerceAtLeast(0))
                                    val h = constraints.constrainHeight(card1HeightState.value.roundToPx().coerceAtLeast(0))
                                    val placeable = measurable.measure(Constraints.fixed(w, h))
                                    layout(w, h) { placeable.place(0, 0) }
                                }
                                // alpha + animated rounded clip in ONE layer, evaluated in the draw phase
                                .graphicsLayer {
                                    alpha = if (isExpanded) 1f else ((expandProgressState.value - 0.05f) / 0.95f).coerceIn(0f, 1f)
                                    shape = RoundedCornerShape(cornerRadiusState.value)
                                    clip = true
                                }
                                .background(Color.Black)
                                .islandOutline(
                                    enabled = settings.showOutline,
                                    thicknessDp = settings.outlineThicknessDp,
                                    color = primaryOutlineColor,
                                    cornerRadius = { cornerRadiusState.value }
                                )
                                .then(
                                    if (isExpanded) {
                                        Modifier.pointerInput(primaryPriority) {
                                            detectHorizontalDragGestures(
                                                onDragEnd = {
                                                    val threshold = 160f
                                                    when (primaryPriority) {
                                                        IslandPriority.MEDIA -> {
                                                            if (abs(card1OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                IslandStateManager.dismissMedia()
                                                                if (activeCount <= 1) {
                                                                    IslandStateManager.collapse()
                                                                }
                                                            }
                                                        }
                                                        IslandPriority.POP_NOTIFICATION -> {
                                                            if (abs(card1OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                val dismissed = IslandStateManager.dismissCurrentNotification()
                                                                if (dismissed && activeCount <= 1) {
                                                                    IslandStateManager.collapse()
                                                                }
                                                            }
                                                        }
                                                        IslandPriority.PROGRESS -> {
                                                            if (abs(card1OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                val dismissed = IslandStateManager.dismissProgress()
                                                                if (dismissed && activeCount <= 1) {
                                                                    IslandStateManager.collapse()
                                                                }
                                                            }
                                                        }
                                                        else -> Unit
                                                    }
                                                    card1OffsetX = 0f
                                                },
                                                onDragCancel = {
                                                    card1OffsetX = 0f
                                                },
                                                onHorizontalDrag = { _, dragAmount ->
                                                    when (primaryPriority) {
                                                        IslandPriority.SCREEN_RECORDING -> {
                                                            card1OffsetX += dragAmount * 0.18f
                                                        }
                                                        else -> {
                                                            card1OffsetX += dragAmount
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Subtle ambient gradient background or blurred album art for media player or flashlight
                            if (isExpanded) {
                                if (primaryPriority == IslandPriority.MEDIA && settings.blurredAlbumArtBackground && mediaArt != null) {
                                    // The card itself already clips to the animated rounded rect. The art is pre-blurred
                                    // on the CPU (see createBlurredBitmap), so the extra full-size GPU Modifier.blur(32.dp)
                                    // that used to run here on every frame was pure overdraw cost and has been removed.
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        if (blurredMediaArtImage != null) {
                                            Image(
                                                bitmap = blurredMediaArtImage,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .graphicsLayer {
                                                        scaleX = 1.35f
                                                        scaleY = 1.35f
                                                        alpha = expandProgressState.value * 0.75f
                                                    }
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.38f))
                                        )
                                        // Black gradient coming from the bottom
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Transparent,
                                                            Color.Black.copy(alpha = 0.40f),
                                                            Color.Black.copy(alpha = 0.82f),
                                                            Color.Black
                                                        )
                                                    )
                                                )
                                        )
                                    }
                                } else {
                                    val ambientBrush = when {
                                        primaryPriority == IslandPriority.MEDIA && mediaAccentColor != Color.Transparent -> {
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    mediaAccentColor.copy(alpha = 0.35f),
                                                    mediaAccentColor.copy(alpha = 0.12f),
                                                    Color.Transparent
                                                )
                                            )
                                        }
                                        primaryPriority == IslandPriority.FLASHLIGHT -> {
                                            val blurpleTint = Color(0xFFD2D6FF)
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    blurpleTint.copy(alpha = 0.06f),
                                                    blurpleTint.copy(alpha = 0.20f)
                                                )
                                            )
                                        }
                                        primaryPriority == IslandPriority.SCREEN_RECORDING -> {
                                            val redTint = Color(0xFFFF453A)
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    redTint.copy(alpha = 0.16f),
                                                    redTint.copy(alpha = 0.05f),
                                                    Color.Transparent
                                                )
                                            )
                                        }
                                        else -> null
                                    }
                                    if (ambientBrush != null) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer { alpha = expandProgressState.value }
                                                .background(ambientBrush)
                                        )
                                    }
                                }
                            }

                            // Expanded Card 1 Content (e.g. Media Player or Primary Notification).
                            // Composed as soon as the card starts expanding (alpha 0) so the expensive first
                            // composition happens BEFORE the animation clock starts instead of mid-animation, and laid
                            // out once at its final size (the container just reveals it).
                            if (!isCharging && expandedContentVisible) {
                                Box(
                                    modifier = Modifier
                                        .fixedSizeTopCenter(
                                            width = lastExpandedSize.card1Width,
                                            height = lastExpandedSize.card1Height
                                        )
                                        .padding(
                                            horizontal = ((if (primaryPriority == IslandPriority.FLASHLIGHT) 16.dp else 20.dp) * settings.expandedScale).coerceIn(14.dp, 24.dp),
                                            vertical = ((if (primaryPriority == IslandPriority.FLASHLIGHT) 12.dp else if (primaryPriority == IslandPriority.POP_NOTIFICATION || primaryPriority == IslandPriority.PROGRESS) 16.dp else 20.dp) * settings.expandedScale).coerceIn(10.dp, 22.dp)
                                        )
                                        .graphicsLayer {
                                            val a = expandedContentAlpha(isExpanded, expandProgressState.value)
                                            alpha = a
                                            scaleX = 0.94f + (0.06f * a)
                                            scaleY = 0.94f + (0.06f * a)
                                        }
                                ) {
                                    ExpandedCardContent(
                                        priority = primaryPriority,
                                        media = currentMedia,
                                        notification = if (primaryPriority == IslandPriority.PROGRESS) currentProgress else currentNotif,
                                        recording = recording,
                                        flashlightLevel = flashlightLevel,
                                        maxFlashlightLevel = maxFlashlightLevel,
                                        fpsInfo = fpsInfo,
                                        weatherInfo = weatherInfo,
                                        networkSpeedInfo = networkSpeedInfo,
                                        scale = settings.expandedScale,
                                        onCollapse = {
                                            triggerHaptic()
                                            IslandStateManager.collapse()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Card 2: Secondary Action Card (smoothly expands and collapses in dual mode!)
                    if (card2HeightVisible) {
                        Box(
                            modifier = Modifier
                                // y includes the gap between the two cards (was Arrangement.spacedBy(...))
                                .offset {
                                    IntOffset(
                                        animatedCard2OffsetXState.value.roundToInt(),
                                        card2OffsetYState.value.roundToPx() + cardSpacingState.value.roundToPx()
                                    )
                                }
                                .layout { measurable, constraints ->
                                    val w = constraints.constrainWidth(card1WidthState.value.roundToPx().coerceAtLeast(0))
                                    val h = constraints.constrainHeight(card2HeightState.value.roundToPx().coerceAtLeast(0))
                                    val placeable = measurable.measure(Constraints.fixed(w, h))
                                    layout(w, h) { placeable.place(0, 0) }
                                }
                                .graphicsLayer {
                                    val a = card2AlphaState.value.coerceIn(0f, 1f)
                                    alpha = card2AlphaState.value
                                    scaleX = 0.92f + (0.08f * a)
                                    scaleY = 0.92f + (0.08f * a)
                                    shape = RoundedCornerShape(cornerRadiusState.value)
                                    clip = true
                                }
                                .background(Color.Black)
                                .islandOutline(
                                    enabled = settings.showOutline,
                                    thicknessDp = settings.outlineThicknessDp,
                                    color = secondaryOutlineColor,
                                    minWidth = 0.dp,
                                    cornerRadius = { cornerRadiusState.value }
                                )
                                .then(
                                    if (isExpanded && secondaryPriority != null) {
                                        Modifier.pointerInput(secondaryPriority) {
                                            detectHorizontalDragGestures(
                                                onDragEnd = {
                                                    val threshold = 160f
                                                    when (secondaryPriority) {
                                                        IslandPriority.MEDIA -> {
                                                            if (abs(card2OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                IslandStateManager.dismissMedia()
                                                            }
                                                        }
                                                        IslandPriority.POP_NOTIFICATION -> {
                                                            if (abs(card2OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                IslandStateManager.dismissCurrentNotification()
                                                            }
                                                        }
                                                        IslandPriority.PROGRESS -> {
                                                            if (abs(card2OffsetX) > threshold) {
                                                                triggerHaptic()
                                                                IslandStateManager.dismissProgress()
                                                            }
                                                        }
                                                        else -> Unit
                                                    }
                                                    card2OffsetX = 0f
                                                },
                                                onDragCancel = {
                                                    card2OffsetX = 0f
                                                },
                                                onHorizontalDrag = { _, dragAmount ->
                                                    when (secondaryPriority) {
                                                        IslandPriority.PROGRESS,
                                                        IslandPriority.SCREEN_RECORDING -> {
                                                            card2OffsetX += dragAmount * 0.18f
                                                        }
                                                        else -> {
                                                            card2OffsetX += dragAmount
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    } else Modifier
                                )
                        ) {
                            // Subtle ambient gradient background or blurred album art for secondary card if it's the media player or flashlight
                            if (isExpanded) {
                                if (secondaryPriority == IslandPriority.MEDIA && settings.blurredAlbumArtBackground && mediaArt != null) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        if (blurredMediaArtImage != null) {
                                            Image(
                                                bitmap = blurredMediaArtImage,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .graphicsLayer {
                                                        scaleX = 1.35f
                                                        scaleY = 1.35f
                                                        alpha = card2AlphaState.value * 0.75f
                                                    }
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.38f))
                                        )
                                        // Black gradient coming from the bottom
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Transparent,
                                                            Color.Black.copy(alpha = 0.40f),
                                                            Color.Black.copy(alpha = 0.82f),
                                                            Color.Black
                                                        )
                                                    )
                                                )
                                        )
                                    }
                                } else {
                                    val ambientBrush = when {
                                        secondaryPriority == IslandPriority.MEDIA && mediaAccentColor != Color.Transparent -> {
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    mediaAccentColor.copy(alpha = 0.35f),
                                                    mediaAccentColor.copy(alpha = 0.12f),
                                                    Color.Transparent
                                                )
                                            )
                                        }
                                        secondaryPriority == IslandPriority.FLASHLIGHT -> {
                                            val blurpleTint = Color(0xFFD2D6FF)
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    blurpleTint.copy(alpha = 0.06f),
                                                    blurpleTint.copy(alpha = 0.20f)
                                                )
                                            )
                                        }
                                        secondaryPriority == IslandPriority.SCREEN_RECORDING -> {
                                            val redTint = Color(0xFFFF453A)
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    redTint.copy(alpha = 0.16f),
                                                    redTint.copy(alpha = 0.05f),
                                                    Color.Transparent
                                                )
                                            )
                                        }
                                        else -> null
                                    }
                                    if (ambientBrush != null) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer { alpha = card2AlphaState.value }
                                                .background(ambientBrush)
                                        )
                                    }
                                }
                            }

                            if (card2ContentVisible && secondaryPriority != null) {
                                Box(
                                    modifier = Modifier
                                        .fixedSizeTopCenter(
                                            width = lastExpandedSize.card1Width,
                                            height = lastExpandedSize.card2Height
                                        )
                                        .padding(
                                            horizontal = ((if (secondaryPriority == IslandPriority.FLASHLIGHT) 16.dp else 20.dp) * settings.expandedScale).coerceIn(14.dp, 24.dp),
                                            vertical = ((if (secondaryPriority == IslandPriority.FLASHLIGHT) 12.dp else if (secondaryPriority == IslandPriority.POP_NOTIFICATION || secondaryPriority == IslandPriority.PROGRESS) 16.dp else 20.dp) * settings.expandedScale).coerceIn(10.dp, 22.dp)
                                        )
                                ) {
                                    ExpandedCardContent(
                                        priority = secondaryPriority,
                                        media = currentMedia,
                                        notification = if (secondaryPriority == IslandPriority.PROGRESS) currentProgress else currentNotif,
                                        recording = recording,
                                        flashlightLevel = flashlightLevel,
                                        maxFlashlightLevel = maxFlashlightLevel,
                                        fpsInfo = fpsInfo,
                                        weatherInfo = weatherInfo,
                                        networkSpeedInfo = networkSpeedInfo,
                                        scale = settings.expandedScale,
                                        onCollapse = {
                                            triggerHaptic()
                                            IslandStateManager.collapse()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Minimised Island Container: Positioned strictly at camera cutout level (settings.topOffsetDp.dp)
        // Shrinks horizontally to 0 width while the card expands downwards, and blossoms from 0 width when minimising!
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = horizontalOffsetState.value.roundToPx(),
                        y = settings.topOffsetDp.dp.roundToPx()
                    )
                },
            contentAlignment = Alignment.TopCenter
        ) {
            // Smoothly fade in over the first frames where it is barely a circle emerging from the punch hole,
            // and smoothly fade out when shrinking back into the punch hole (compactPillAlphaState).
            // Applies to both single and dual activities!

            // Alpha of the auxiliary bubble. Evaluated only inside draw lambdas / derived states.
            // When emerging from the punch hole, the secondary bubble also fades in with the main island
            // so no opaque circle sits exposed at the punch hole during the initial frames!
            fun bubbleAlphaNow(): Float {
                val base = if (isExpanded) {
                    ((1f - expandProgressState.value * 4f).coerceIn(0f, 1f))
                } else if (!isOccupied && !hasPersistent) {
                    0f
                } else {
                    1f
                }
                return base * compactPillAlphaState.value
            }
            fun bubbleContentAlphaNow(): Float {
                val emergedDistance = bubbleOffsetXState.value - (compactMainHalf - bubbleSize / 2f)
                return ((emergedDistance - bubbleSize * 0.35f) / (bubbleSize * 0.55f)).coerceIn(0f, 1f) * bubbleAlphaNow()
            }
            val bubbleKeys = arrayOf<Any?>(isExpanded, isOccupied, hasPersistent, settings.widthDp, settings.heightDp)
            val bubbleSlotShown by remember(isDual) { derivedStateOf { isDual || bubbleOffsetXState.value > 1.dp } }
            val bubbleAlphaVisible by remember(*bubbleKeys) { derivedStateOf { bubbleAlphaNow() > 0.01f } }
            val bubbleContentVisible by remember(*bubbleKeys) { derivedStateOf { bubbleContentAlphaNow() > 0.05f } }

            // 1. Auxiliary Circular Island Bubble: Placed on the LAYER BELOW (rendered first so main pill covers it!)
            // Fixed circular shape & size; emerges and retracts purely along the X axis from the middle of the punch hole (x = 0)
            if (bubbleSlotShown) {
                if (bubbleAlphaVisible) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(x = bubbleOffsetXState.value.roundToPx(), y = 0) }
                            .size(bubbleSize)
                            .graphicsLayer { alpha = bubbleAlphaNow() }
                            .clip(CircleShape)
                            .background(Color.Black)
                            .islandOutline(
                                enabled = settings.showOutline,
                                thicknessDp = settings.outlineThicknessDp,
                                color = secondaryOutlineColor,
                                minWidth = 0.dp,
                                alpha = { if (bubbleOffsetXState.value > compactMainHalf * 0.7f) 1f else 0f },
                                cornerRadius = { 1000.dp }
                            )
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    triggerHaptic()
                                    IslandStateManager.expand(secondaryPriority)
                                },
                                onLongClick = {
                                    triggerHaptic()
                                    openAppForPriority(
                                        context = context,
                                        priority = secondaryPriority,
                                        media = currentMedia,
                                        notification = currentNotif,
                                        progress = currentProgress,
                                        onDismissNotification = {
                                            IslandStateManager.dismissCurrentNotification()
                                        }
                                    )
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bubbleContentVisible) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { alpha = bubbleContentAlphaNow() },
                                contentAlignment = Alignment.Center
                            ) {
                                AuxiliaryBubbleContent(
                                    priority = secondaryPriority,
                                    media = currentMedia,
                                    notification = if (secondaryPriority == IslandPriority.PROGRESS) currentProgress else currentNotif,
                                    recording = recording,
                                    fpsInfo = fpsInfo,
                                    weatherInfo = weatherInfo,
                                    networkSpeedInfo = networkSpeedInfo
                                )
                            }
                        }
                    }
                }
            }

            // 2. Main Compact Pill: Rendered on top of the bubble (covers it when sliding near x = 0)
            if (compactPillVisible) {
                Box(
                    modifier = Modifier
                        // width is read in the layout phase only (no recomposition per frame)
                        .layout { measurable, constraints ->
                            val w = constraints.constrainWidth(compactWidthState.value.roundToPx().coerceAtLeast(0))
                            val h = constraints.constrainHeight(settings.heightDp.dp.roundToPx())
                            val placeable = measurable.measure(Constraints.fixed(w, h))
                            layout(w, h) { placeable.place(0, 0) }
                        }
                        .graphicsLayer {
                            alpha = compactPillAlphaState.value
                            translationY = idleBounceY.value.dp.toPx()
                            scaleX = idleBounceScale.value
                            scaleY = idleBounceScale.value
                        }
                        .clip(RoundedCornerShape(settings.cornerRadiusDp.dp))
                        .background(Color.Black)
                        .islandOutline(
                            enabled = settings.showOutline,
                            thicknessDp = settings.outlineThicknessDp,
                            color = primaryOutlineColor,
                            alpha = { compactPillAlphaState.value },
                            cornerRadius = { settings.cornerRadiusDp.dp }
                        )
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!isExpanded && (isOccupied || hasPersistent) && !isUnlock && !isBluetooth && !isRingerMode) {
                                    triggerHaptic()
                                    IslandStateManager.expand(primaryPriority)
                                } else if (!isExpanded && !isOccupied && !hasPersistent && settings.alwaysShowPill) {
                                    if (settings.hapticsEnabled) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                                        } else {
                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        }
                                    }
                                    coroutineScope.launch {
                                        launch {
                                            idleBounceY.snapTo(0f)
                                            idleBounceY.animateTo(
                                                targetValue = 3.5f,
                                                animationSpec = tween(70, easing = FastOutSlowInEasing)
                                            )
                                            idleBounceY.animateTo(
                                                targetValue = -1.5f,
                                                animationSpec = tween(90, easing = FastOutSlowInEasing)
                                            )
                                            idleBounceY.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        }
                                        launch {
                                            idleBounceScale.snapTo(1f)
                                            idleBounceScale.animateTo(
                                                targetValue = 0.95f,
                                                animationSpec = tween(70, easing = FastOutSlowInEasing)
                                            )
                                            idleBounceScale.animateTo(
                                                targetValue = 1.04f,
                                                animationSpec = tween(90, easing = FastOutSlowInEasing)
                                            )
                                            idleBounceScale.animateTo(
                                                targetValue = 1.0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            onLongClick = {
                                if (!isExpanded && !isUnlock && !isBluetooth && !isRingerMode) {
                                    triggerHaptic()
                                    openAppForPriority(
                                        context = context,
                                        priority = primaryPriority,
                                        media = currentMedia,
                                        notification = currentNotif,
                                        progress = currentProgress,
                                        onDismissNotification = {
                                            IslandStateManager.dismissCurrentNotification()
                                        }
                                    )
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Background darker green progress bar for charging status:
                    if (chargingVisible) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = chargingContentAlphaState.value }
                                .background(Color(0xFF04190B))
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .layout { measurable, constraints ->
                                    val w = (constraints.maxWidth * animatedChargingProgressState.value.coerceIn(0.001f, 1f)).roundToInt()
                                    val placeable = measurable.measure(Constraints.fixed(w, constraints.maxHeight))
                                    layout(w, constraints.maxHeight) { placeable.place(0, 0) }
                                }
                                .align(Alignment.CenterStart)
                                .graphicsLayer { alpha = chargingContentAlphaState.value }
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF062B12),
                                            Color(0xFF0D3D1B)
                                        )
                                    )
                                )
                        )
                    }

                    // 1. Compact Content or Charging Content: strictly clear 36dp center over punch-hole
                    if (chargingVisible) {
                        val wattage = charging?.wattage
                        val chargingLabel = if (wattage != null && wattage >= 1.0) {
                            if (wattage >= 10.0 || (wattage % 1.0) < 0.05 || (wattage % 1.0) > 0.95) {
                                "${wattage.roundToInt()}W"
                            } else {
                                String.format(Locale.US, "%.1fW", wattage)
                            }
                        } else {
                            "Charging"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp)
                                .graphicsLayer { alpha = chargingContentAlphaState.value },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Wattage (e.g. "33W") or "Charging" to save extreme space
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = chargingLabel,
                                    color = Color(0xFF30D158),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Center: 36dp deadzone strictly over camera cutout
                            Spacer(modifier = Modifier.width(36.dp))

                            // Right: Current battery percentage in green
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = "${charging?.percentage ?: 100}%",
                                    color = Color(0xFF30D158),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                            }
                        }
                    } else if (unlockVisible) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .graphicsLayer { alpha = unlockContentAlphaState.value },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Unlocked lock icon at the slight edge of the island
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = "Unlocked",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }

                            // Center: 36dp deadzone strictly over camera cutout
                            Spacer(modifier = Modifier.width(36.dp))

                            // Right: Checkmark at the slight edge of the island with smooth bounce growth animation
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (unlockCheckShown) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .graphicsLayer {
                                                alpha = unlockCheckmarkAlphaState.value
                                                scaleX = unlockCheckmarkScaleState.value
                                                scaleY = unlockCheckmarkScaleState.value
                                            }
                                            .size(19.dp)
                                    )
                                }
                            }
                        }
                    } else if (bluetoothVisible) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .graphicsLayer { alpha = bluetoothContentAlphaState.value },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Bluetooth icon at the slight edge of the island
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bluetooth,
                                    contentDescription = "Bluetooth",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }

                            // Center: 36dp deadzone strictly over camera cutout
                            Spacer(modifier = Modifier.width(36.dp))

                            // Right: Checkmark at the slight edge of the island with smooth bounce growth animation
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (bluetoothCheckShown) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Connected",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .graphicsLayer {
                                                alpha = bluetoothCheckmarkAlphaState.value
                                                scaleX = bluetoothCheckmarkScaleState.value
                                                scaleY = bluetoothCheckmarkScaleState.value
                                            }
                                            .size(19.dp)
                                    )
                                }
                            }
                        }
                    } else if (ringerVisible) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .graphicsLayer { alpha = ringerContentAlphaState.value },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Ringer icon (Bell, Slashed Bell, or Vibration)
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                val icon = when (displayRingerMode) {
                                    RingerMode.SILENT -> Icons.Default.NotificationsOff
                                    RingerMode.VIBRATE -> Icons.Default.Vibration
                                    RingerMode.RING -> Icons.Default.Notifications
                                }
                                val iconTint = if (displayRingerMode == RingerMode.SILENT) {
                                    Color(0xFFFF3B30) // Red icon for silent
                                } else {
                                    Color.White
                                }

                                Icon(
                                    imageVector = icon,
                                    contentDescription = displayRingerMode.name,
                                    tint = iconTint,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            if (displayRingerMode == RingerMode.RING) {
                                                transformOrigin = TransformOrigin(0.5f, 0.0f)
                                                rotationZ = bellShakeRotation.value
                                            } else if (displayRingerMode == RingerMode.VIBRATE) {
                                                transformOrigin = TransformOrigin.Center
                                                translationX = vibrateShakeOffset.value.dp.toPx()
                                            } else {
                                                transformOrigin = TransformOrigin.Center
                                            }
                                        }
                                        .size(19.dp)
                                )
                            }

                            // Center: 36dp deadzone strictly over camera cutout
                            Spacer(modifier = Modifier.width(36.dp))

                            // Right: Text label (Silent, Vibrate, Ring) with smooth bounce scaling & alpha
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (ringerLabelShown) {
                                    val labelText = when (displayRingerMode) {
                                        RingerMode.SILENT -> "Silent"
                                        RingerMode.VIBRATE -> "Vibrate"
                                        RingerMode.RING -> "Ring"
                                    }
                                    val labelColor = if (displayRingerMode == RingerMode.SILENT) {
                                        Color(0xFFFF3B30) // Red text for silent
                                    } else {
                                        Color.White
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.graphicsLayer {
                                            alpha = ringerSecondaryAlphaState.value
                                            scaleX = ringerSecondaryScaleState.value
                                            scaleY = ringerSecondaryScaleState.value
                                        }
                                    ) {
                                        Text(
                                            text = labelText,
                                            color = labelColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    } else if (compactContentVisible) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = compactContentAlpha(isExpanded, expandProgressState.value) },
                            contentAlignment = Alignment.Center
                        ) {
                            CompactContent(
                                priority = primaryPriority,
                                media = currentMedia,
                                notification = if (primaryPriority == IslandPriority.PROGRESS) currentProgress else currentNotif,
                                recording = recording,
                                isCompactDual = isDual,
                                flashlightLevel = flashlightLevel,
                                maxFlashlightLevel = maxFlashlightLevel,
                                fpsInfo = fpsInfo,
                                weatherInfo = weatherInfo,
                                networkSpeedInfo = networkSpeedInfo,
                                secondaryPriority = secondaryPriority
                            )
                        }
                    }
                }
            }

            // 3. Parentheses Accents: Neat parentheses-shaped brackets on each side of the island
            // Shorter than the height of the island, smoothly positioning symmetrically around 1 or 2 activities, and neatly close to the edges.
            // Perf: parentheses geometry/alpha are evaluated in offset{} / graphicsLayer{} lambdas, so the spring
            // "shoot" + width/split animations never trigger recomposition of this composable.
            val parenthesisOccupiedAlpha = animateFloatAsState(
                targetValue = if (!isOccupied && !hasPersistent) 0f else 1f,
                animationSpec = tween(durationMillis = (130 / speed).roundToInt(), easing = FastOutSlowInEasing),
                label = "parenthesisAlpha"
            )
            fun parenthesisAlphaNow(): Float {
                val expandFade = (1f - expandProgressState.value * 3.5f).coerceIn(0f, 1f)
                // Fades in a split second earlier as the island begins emerging from the punch hole
                val emergence = (compactWidthState.value / 14.dp).coerceIn(0f, 1f)
                return parenthesisOccupiedAlpha.value * expandFade * emergence
            }
            val parenthesesVisible by remember(settings.bubbleEffectEnabled) {
                derivedStateOf {
                    settings.bubbleEffectEnabled && parenthesisAlphaNow() > 0.01f && compactWidthState.value > 3.dp
                }
            }

            if (parenthesesVisible) {
                val parenthesisHeight = (settings.heightDp.dp * 0.52f).coerceIn(16.dp, 21.dp)
                val parenthesisGap = 3.5.dp
                val parenthesisStroke = 1.8.dp
                val density = LocalDensity.current

                val pYPx = with(density) { ((settings.heightDp.dp - parenthesisHeight) / 2f).roundToPx() }

                fun extraShootDp(): Dp = parenthesisExtraShoot.value.coerceAtLeast(0f).dp
                fun parenthesisWidthNow(): Dp = 5.0.dp + (extraShootDp() * 0.20f)

                val bracketColor = Color.Black

                // Left Parenthesis `(`: tips facing right are at centerX + tipOffsetFromCenter.
                // Sits at islandLeft - parenthesisGap - extraShootDp.
                ParenthesisBracket(
                    isLeft = true,
                    widthProvider = { parenthesisWidthNow() },
                    height = parenthesisHeight,
                    strokeWidth = parenthesisStroke,
                    color = bracketColor,
                    modifier = Modifier
                        .offset {
                            val tipOffsetFromCenter = (parenthesisWidthNow() - parenthesisStroke) / 2f
                            val islandLeftDp = -compactWidthState.value / 2f
                            IntOffset(
                                x = (islandLeftDp - parenthesisGap - extraShootDp() - tipOffsetFromCenter).roundToPx(),
                                y = pYPx
                            )
                        }
                        .graphicsLayer { alpha = parenthesisAlphaNow() }
                )

                // Right Parenthesis `)`: tips facing left are at centerX - tipOffsetFromCenter.
                // Sits at rightContentEdgeDp + parenthesisGap + extraShootDp.
                // When emerging, curve shoots out slightly further from the island and settles back.
                val bubbleCanExtendRight = !isCharging && !isUnlock && !isBluetooth && !isRingerMode
                ParenthesisBracket(
                    isLeft = false,
                    widthProvider = { parenthesisWidthNow() },
                    height = parenthesisHeight,
                    strokeWidth = parenthesisStroke,
                    color = bracketColor,
                    modifier = Modifier
                        .offset {
                            val tipOffsetFromCenter = (parenthesisWidthNow() - parenthesisStroke) / 2f
                            val bubbleRightEdge = bubbleOffsetXState.value + (bubbleSize / 2f)
                            val rightContentEdgeDp = if (bubbleCanExtendRight && bubbleOffsetXState.value > 1.dp) {
                                maxOf(compactWidthState.value / 2f, bubbleRightEdge)
                            } else {
                                compactWidthState.value / 2f
                            }
                            IntOffset(
                                x = (rightContentEdgeDp + parenthesisGap + extraShootDp() + tipOffsetFromCenter).roundToPx(),
                                y = pYPx
                            )
                        }
                        .graphicsLayer { alpha = parenthesisAlphaNow() }
                )
            }
        }
    }
}

fun launchAppOrIntent(
    context: android.content.Context,
    packageName: String?,
    fallbackPendingIntent: PendingIntent? = null
): Boolean {
    // 1. If it is an app, open it directly via its launcher intent with NEW_TASK
    if (!packageName.isNullOrBlank()) {
        try {
            val pm = context.packageManager
            val launchIntent = pm.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                )
                context.startActivity(launchIntent)
                return true
            }
        } catch (e: Throwable) {
            Log.e("IslandView", "Failed to launch app by package name: $packageName", e)
        }
    }

    // 2. If it is not a launchable app or launchIntent failed, try the PendingIntent with background activity start allowed
    if (fallbackPendingIntent != null) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val options = ActivityOptions.makeBasic().apply {
                    pendingIntentBackgroundActivityStartMode = ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                }.toBundle()
                fallbackPendingIntent.send(context, 0, null, null, null, null, options)
            } else {
                fallbackPendingIntent.send()
            }
            return true
        } catch (e: Throwable) {
            Log.e("IslandView", "Failed to send pendingIntent", e)
        }
    }

    return false
}

fun openAppForPriority(
    context: android.content.Context,
    priority: IslandPriority?,
    media: MediaInfo?,
    notification: NotificationInfo?,
    progress: NotificationInfo?,
    onDismissNotification: (() -> Unit)? = null
) {
    when (priority) {
        IslandPriority.POP_NOTIFICATION -> {
            if (notification != null) {
                launchAppOrIntent(context, notification.packageName, notification.contentIntent)
                onDismissNotification?.invoke()
            }
        }
        IslandPriority.PROGRESS -> {
            if (progress != null) {
                launchAppOrIntent(context, progress.packageName, progress.contentIntent)
            }
        }
        IslandPriority.MEDIA -> {
            if (media != null) {
                launchAppOrIntent(context, media.packageName, media.sessionActivity)
            }
        }
        else -> {}
    }
}

@Composable
fun ParenthesisBracket(
    isLeft: Boolean,
    widthProvider: () -> Dp,
    height: Dp,
    strokeWidth: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.layout { measurable, _ ->
            val w = widthProvider().roundToPx().coerceAtLeast(0)
            val h = height.roundToPx().coerceAtLeast(0)
            val placeable = measurable.measure(Constraints.fixed(w, h))
            layout(w, h) { placeable.place(0, 0) }
        }
    ) {
        val w = size.width
        val h = size.height
        val strokePx = strokeWidth.toPx()
        val path = Path()

        if (isLeft) {
            // Left parenthesis `(`: curves outward to the left
            path.moveTo(w - strokePx / 2f, strokePx / 2f)
            path.cubicTo(
                x1 = strokePx / 2f, y1 = h * 0.22f,
                x2 = strokePx / 2f, y2 = h * 0.78f,
                x3 = w - strokePx / 2f, y3 = h - strokePx / 2f
            )
        } else {
            // Right parenthesis `)`: curves outward to the right
            path.moveTo(strokePx / 2f, strokePx / 2f)
            path.cubicTo(
                x1 = w - strokePx / 2f, y1 = h * 0.22f,
                x2 = w - strokePx / 2f, y2 = h * 0.78f,
                x3 = strokePx / 2f, y3 = h - strokePx / 2f
            )
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokePx,
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
fun AuxiliaryBubbleContent(
    priority: IslandPriority?,
    media: MediaInfo?,
    notification: NotificationInfo?,
    recording: ScreenRecordingState,
    fpsInfo: FpsInfo = FpsInfo(),
    weatherInfo: WeatherInfo = WeatherInfo(),
    networkSpeedInfo: NetworkSpeedInfo = NetworkSpeedInfo()
) {
    when (priority) {
        IslandPriority.SCREEN_RECORDING -> {
            PulsingRecordingDot()
        }
        IslandPriority.POP_NOTIFICATION -> {
            if (notification?.iconBitmap != null) {
                Image(
                    bitmap = notification.iconBitmap.asImageBitmap(),
                    contentDescription = "Notification Icon",
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        IslandPriority.PROGRESS -> {
            val pct = notification?.progressFraction ?: 0f
            val isIndet = notification?.isIndeterminate == true
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isIndet) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFE5E5EA),
                        trackColor = Color(0xFF2C2C2E)
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { pct },
                        modifier = Modifier.size(17.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFE5E5EA),
                        trackColor = Color(0xFF2C2C2E)
                    )
                    Text(
                        text = "${(pct * 100).roundToInt()}",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        IslandPriority.MEDIA -> {
            val displayArt = media?.albumArt ?: media?.appIcon
            if (displayArt != null) {
                Image(
                    bitmap = displayArt.asImageBitmap(),
                    contentDescription = "Media Art",
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                )
            } else {
                EqualizerVisualizer(isPlaying = media?.isPlaying == true)
            }
        }
        IslandPriority.FLASHLIGHT -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.45f),
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                Icon(
                    imageVector = Icons.Default.FlashlightOn,
                    contentDescription = "Flashlight",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        IslandPriority.FPS -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${fpsInfo.fps}",
                    color = Color(0xFF30D158),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
        IslandPriority.WEATHER -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "${weatherInfo.temperatureC}°",
                    color = Color(0xFFFF9F0A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        IslandPriority.NETWORK_SPEED -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = Color(0xFF64D2FF),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        else -> Unit
    }
}

@Composable
fun CompactContent(
    priority: IslandPriority,
    media: MediaInfo?,
    notification: NotificationInfo?,
    recording: ScreenRecordingState,
    isCompactDual: Boolean = false,
    flashlightLevel: Int = 1,
    maxFlashlightLevel: Int = 1,
    fpsInfo: FpsInfo = FpsInfo(),
    weatherInfo: WeatherInfo = WeatherInfo(),
    networkSpeedInfo: NetworkSpeedInfo = NetworkSpeedInfo(),
    secondaryPriority: IslandPriority? = null
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Leading content: Pinned strictly to the left of the camera punch hole
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            when (priority) {
                IslandPriority.SCREEN_RECORDING -> {
                    PulsingRecordingDot()
                }

                IslandPriority.FLASHLIGHT -> {
                    Box(
                        modifier = Modifier.offset(x = (-4).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.50f),
                                            Color.White.copy(alpha = 0.16f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = "Flashlight",
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                IslandPriority.POP_NOTIFICATION -> {
                    if (notification?.iconBitmap != null) {
                        Image(
                            bitmap = notification.iconBitmap.asImageBitmap(),
                            contentDescription = "Notification Icon",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFFE5E5EA),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                IslandPriority.PROGRESS -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (notification?.iconBitmap != null) {
                            Image(
                                bitmap = notification.iconBitmap.asImageBitmap(),
                                contentDescription = "Download Icon",
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color(0xFFE5E5EA),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (!isCompactDual) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = notification?.appName ?: "Download",
                                color = Color(0xFFE5E5EA),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                IslandPriority.MEDIA -> {
                    val displayArt = media?.albumArt ?: media?.appIcon
                    if (displayArt != null) {
                        Image(
                            bitmap = displayArt.asImageBitmap(),
                            contentDescription = "Media Art",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color(0xFFA1A1AA),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IslandPriority.FPS -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "FPS",
                            tint = if (fpsInfo.isSmooth) Color(0xFF30D158) else Color(0xFFFFD60A),
                            modifier = Modifier.size(15.dp)
                        )
                        if (!isCompactDual) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "FPS",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IslandPriority.WEATHER -> {
                    val weatherIcon = when {
                        weatherInfo.condition.contains("Sun", ignoreCase = true) || weatherInfo.condition.contains("Clear", ignoreCase = true) -> Icons.Default.WbSunny
                        weatherInfo.condition.contains("Rain", ignoreCase = true) -> Icons.Default.WaterDrop
                        else -> Icons.Default.Cloud
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = weatherIcon,
                            contentDescription = "Weather",
                            tint = Color(0xFFFF9F0A),
                            modifier = Modifier.size(15.dp)
                        )
                        if (!isCompactDual) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = weatherInfo.condition,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                IslandPriority.NETWORK_SPEED -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (networkSpeedInfo.isWifi) Icons.Default.Wifi else Icons.Default.ArrowDownward,
                            contentDescription = "Speed",
                            tint = Color(0xFF64D2FF),
                            modifier = Modifier.size(14.dp)
                        )
                        if (!isCompactDual) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (networkSpeedInfo.isWifi) "Wi-Fi" else "5G",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                IslandPriority.IDLE -> Unit
            }
        }

        // Camera punch-hole deadzone: STRICTLY 36dp EMPTY SPACE IN THE MIDDLE!
        // No UI elements will EVER enter or overlap the camera cutout area
        Spacer(modifier = Modifier.width(36.dp))

        // Trailing content: Pinned strictly to the right of the camera punch hole
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            when (priority) {
                IslandPriority.SCREEN_RECORDING -> {
                    val minutes = recording.durationSeconds / 60
                    val seconds = recording.durationSeconds % 60
                    Text(
                        text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                        color = Color(0xFFE5E5EA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IslandPriority.FLASHLIGHT -> Unit

                IslandPriority.POP_NOTIFICATION -> {
                    Text(
                        text = notification?.title ?: "Notification",
                        color = Color(0xFFE5E5EA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IslandPriority.PROGRESS -> {
                    val pct = notification?.progressFraction ?: 0f
                    val isIndet = notification?.isIndeterminate == true
                    if (isIndet) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFFE5E5EA),
                            trackColor = Color(0xFF2C2C2E)
                        )
                    } else {
                        Text(
                            text = "${(pct * 100).roundToInt()}%",
                            color = Color(0xFFE5E5EA),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                IslandPriority.MEDIA -> {
                    val displayArt = media?.albumArt ?: media?.appIcon
                    val compactAccent = remember(displayArt) { extractMediaAccentColor(displayArt) }
                    val compactEqTint = if (compactAccent != Color.Transparent) {
                        lerp(Color(0xFFE5E5EA), compactAccent, 0.50f)
                    } else {
                        Color(0xFFE5E5EA)
                    }
                    EqualizerVisualizer(isPlaying = media?.isPlaying == true, barColor = compactEqTint)
                }

                IslandPriority.FPS -> {
                    Text(
                        text = "${fpsInfo.fps} FPS",
                        color = if (fpsInfo.isSmooth) Color(0xFF30D158) else Color(0xFFFFD60A),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IslandPriority.WEATHER -> {
                    Text(
                        text = "${weatherInfo.temperatureC}°",
                        color = Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IslandPriority.NETWORK_SPEED -> {
                    Text(
                        text = "↓ ${networkSpeedInfo.downloadSpeedFormatted}",
                        color = Color(0xFF64D2FF),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IslandPriority.IDLE -> Unit
            }
        }
    }
}

@Composable
fun ExpandedCardContent(
    priority: IslandPriority,
    media: MediaInfo?,
    notification: NotificationInfo?,
    recording: ScreenRecordingState,
    flashlightLevel: Int = 1,
    maxFlashlightLevel: Int = 1,
    fpsInfo: FpsInfo = FpsInfo(),
    weatherInfo: WeatherInfo = WeatherInfo(),
    networkSpeedInfo: NetworkSpeedInfo = NetworkSpeedInfo(),
    scale: Float = 1.0f,
    onCollapse: () -> Unit
) {
    when (priority) {
        IslandPriority.SCREEN_RECORDING -> {
            ExpandedRecordingView(recording, onCollapse, scale)
        }
        IslandPriority.POP_NOTIFICATION -> {
            ExpandedNotificationView(notification, onCollapse, scale)
        }
        IslandPriority.PROGRESS -> {
            ExpandedProgressView(notification, onCollapse, scale)
        }
        IslandPriority.MEDIA -> {
            ExpandedMediaView(media, onCollapse, scale)
        }
        IslandPriority.FLASHLIGHT -> {
            ExpandedFlashlightView(
                level = flashlightLevel,
                onSetLevel = { newLevel ->
                    IslandStateManager.setFlashlightBrightness(newLevel)
                },
                onTurnOff = {
                    IslandStateManager.turnOffFlashlight()
                },
                onCollapse = onCollapse
            )
        }
        IslandPriority.FPS -> {
            ExpandedFpsView(fpsInfo = fpsInfo, onCollapse = onCollapse)
        }
        IslandPriority.WEATHER -> {
            ExpandedWeatherView(weatherInfo = weatherInfo, onCollapse = onCollapse)
        }
        IslandPriority.NETWORK_SPEED -> {
            ExpandedNetworkSpeedView(networkSpeedInfo = networkSpeedInfo, onCollapse = onCollapse)
        }
        IslandPriority.IDLE -> {
            LaunchedEffect(Unit) {
                onCollapse()
            }
            ExpandedIdleView(onCollapse)
        }
    }
}

@Composable
fun ExpandedFlashlightView(
    level: Int,
    onSetLevel: (Int) -> Unit,
    onTurnOff: () -> Unit,
    onCollapse: () -> Unit
) {
    val view = LocalView.current

    // Current brightness level 1..4
    var currentLevel by remember(level) { mutableIntStateOf(level.coerceIn(1, 4)) }
    LaunchedEffect(level) {
        currentLevel = level.coerceIn(1, 4)
    }

    val currentLevelState = rememberUpdatedState(currentLevel)
    val onSetLevelState = rememberUpdatedState(onSetLevel)

    // Animated fraction for the light beam height (1 -> 0.25f, 4 -> 1.0f)
    val targetFraction = (currentLevel / 4f)
    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 450f
        ),
        label = "flashlightBeamFraction"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper section: The light-beam triangular blur vertical slider
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val h = size.height.toFloat()
                        if (h > 0) {
                            val relY = (down.position.y / h).coerceIn(0f, 1f)
                            val newLevel = (((1f - relY) * 3.999f).toInt() + 1).coerceIn(1, 4)
                            if (newLevel != currentLevelState.value) {
                                currentLevel = newLevel
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                onSetLevelState.value(newLevel)
                            }
                        }
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break
                            if (!change.pressed) break
                            change.consume()
                            val currentH = size.height.toFloat()
                            if (currentH > 0) {
                                val relY = (change.position.y / currentH).coerceIn(0f, 1f)
                                val newLevel = (((1f - relY) * 3.999f).toInt() + 1).coerceIn(1, 4)
                                if (newLevel != currentLevelState.value) {
                                    currentLevel = newLevel
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    onSetLevelState.value(newLevel)
                                }
                            }
                        }
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp, bottom = 0.dp)
                    .blur(radius = 8.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            ) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f

                val blurpleTint = Color(0xFFD2D6FF)

                // Geometry:
                // Maximum widths at full brightness (level 4):
                val maxSecTopHalfW = (w * 0.18f).coerceAtMost(52.dp.toPx())
                val secBaseHalfW = 7.dp.toPx()

                val maxPrimTopHalfW = (w * 0.09f).coerceAtMost(26.dp.toPx())
                val primBaseHalfW = 3.5.dp.toPx()

                // Width expands dynamically with brightness, maxing out at current width for level 4!
                val widthFactor = 0.35f + 0.65f * animatedFraction
                val currentSecTopHalfW = maxSecTopHalfW * widthFactor
                val currentPrimTopHalfW = maxPrimTopHalfW * widthFactor

                // 1. Background guide cone: subtle blurple tint, fades away at the top!
                val guideOuterPath = Path().apply {
                    moveTo(centerX - secBaseHalfW - 3.dp.toPx(), h)
                    lineTo(centerX - maxSecTopHalfW - 6.dp.toPx(), 0f)
                    quadraticTo(centerX, -4.dp.toPx(), centerX + maxSecTopHalfW + 6.dp.toPx(), 0f)
                    lineTo(centerX + secBaseHalfW + 3.dp.toPx(), h)
                    close()
                }
                drawPath(
                    path = guideOuterPath,
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent, // Fades away at the top!
                            0.20f to blurpleTint.copy(alpha = 0.03f),
                            0.60f to blurpleTint.copy(alpha = 0.05f),
                            1.0f to Color.Transparent
                        ),
                        startY = 0f,
                        endY = h
                    )
                )

                val guidePath = Path().apply {
                    moveTo(centerX - secBaseHalfW, h)
                    lineTo(centerX - maxSecTopHalfW, 0f)
                    quadraticTo(centerX, -4.dp.toPx(), centerX + maxSecTopHalfW, 0f)
                    lineTo(centerX + secBaseHalfW, h)
                    close()
                }
                drawPath(
                    path = guidePath,
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent, // Fades away at the top!
                            0.18f to blurpleTint.copy(alpha = 0.08f),
                            0.55f to blurpleTint.copy(alpha = 0.13f),
                            1.0f to blurpleTint.copy(alpha = 0.04f)
                        ),
                        startY = 0f,
                        endY = h
                    )
                )

                // 2. Active illuminated beams filling up to animatedFraction (1..4)
                val activeH = h * animatedFraction
                val activeTopY = h - activeH
                val activeSecTopHalfW = secBaseHalfW + (currentSecTopHalfW - secBaseHalfW) * animatedFraction
                val activePrimTopHalfW = primBaseHalfW + (currentPrimTopHalfW - primBaseHalfW) * animatedFraction

                // Dynamic fade distance at top: fades away at top, but fade reduces when less bright
                val fadeLength = (28.dp.toPx() * animatedFraction).coerceAtMost(activeH * 0.40f).coerceAtLeast(4.dp.toPx())
                val fadeStop = (fadeLength / activeH).coerceIn(0.04f, 0.45f)

                // Diffused outer atmospheric halo
                val haloSpread = 5.dp.toPx() * animatedFraction
                val haloPath = Path().apply {
                    moveTo(centerX - secBaseHalfW - 2.dp.toPx(), h)
                    lineTo(centerX - activeSecTopHalfW - haloSpread, activeTopY)
                    quadraticTo(centerX, activeTopY - 4.dp.toPx(), centerX + activeSecTopHalfW + haloSpread, activeTopY)
                    lineTo(centerX + secBaseHalfW + 2.dp.toPx(), h)
                    close()
                }
                drawPath(
                    path = haloPath,
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            fadeStop to Color.White.copy(alpha = 0.12f * animatedFraction),
                            1.0f to Color.Transparent
                        ),
                        startY = activeTopY,
                        endY = h
                    )
                )

                // 3. Secondary beam: slightly less bright, same width as now (realistic spill beam)
                val secBeamPath = Path().apply {
                    moveTo(centerX - secBaseHalfW, h)
                    lineTo(centerX - activeSecTopHalfW, activeTopY)
                    quadraticTo(centerX, activeTopY - 3.dp.toPx(), centerX + activeSecTopHalfW, activeTopY)
                    lineTo(centerX + secBaseHalfW, h)
                    close()
                }
                val secIntensity = 0.32f + 0.22f * animatedFraction
                drawPath(
                    path = secBeamPath,
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent, // Fades away at the top!
                            fadeStop to Color.White.copy(alpha = 0.35f * secIntensity),
                            0.60f to Color.White.copy(alpha = 0.24f * secIntensity),
                            1.0f to Color.White.copy(alpha = 0.08f * secIntensity)
                        ),
                        startY = activeTopY,
                        endY = h
                    )
                )

                // 4. Primary core beam: half as thin again, brilliant hotspot, fades away at the top
                val primBeamPath = Path().apply {
                    moveTo(centerX - primBaseHalfW, h)
                    lineTo(centerX - activePrimTopHalfW, activeTopY)
                    quadraticTo(centerX, activeTopY - 2.dp.toPx(), centerX + activePrimTopHalfW, activeTopY)
                    lineTo(centerX + primBaseHalfW, h)
                    close()
                }
                val primIntensity = 0.70f + 0.30f * animatedFraction
                drawPath(
                    path = primBeamPath,
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent, // Fades away at the top!
                            fadeStop to Color.White.copy(alpha = 0.96f * primIntensity),
                            0.65f to Color.White.copy(alpha = 0.78f * primIntensity),
                            1.0f to Color.White.copy(alpha = 0.45f * primIntensity)
                        ),
                        startY = activeTopY,
                        endY = h
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Bottom section: Large centered flashlight icon (NO dark circle background, NO radial glow)
        // Tapping on this icon turns the flashlight off!
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                    onTurnOff()
                    onCollapse()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FlashlightOn,
                contentDescription = "Turn off flashlight",
                tint = Color.White,
                modifier = Modifier.size(46.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedMediaView(media: MediaInfo?, onCollapse: () -> Unit, scale: Float = 1.0f) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isSeeking by remember { mutableStateOf(false) }
    var scrubFraction by remember { mutableFloatStateOf(0f) }
    var isSeekPending by remember { mutableStateOf(false) }

    val rawDuration = media?.durationMs ?: 0L
    val hasValidDuration = rawDuration > 0L
    val duration = if (hasValidDuration) rawDuration else 0L

    var livePosition by remember(media?.packageName, media?.title) {
        mutableLongStateOf(media?.computeCurrentPosition() ?: 0L)
    }

    LaunchedEffect(media?.isPlaying, media?.positionMs, media?.lastPositionUpdateTimeMs, isSeeking, isSeekPending) {
        if (!isSeeking && !isSeekPending) {
            livePosition = media?.computeCurrentPosition() ?: 0L
            if (media?.isPlaying == true) {
                while (isActive) {
                    kotlinx.coroutines.delay(250)
                    if (!isSeeking && !isSeekPending) {
                        livePosition = media?.computeCurrentPosition() ?: 0L
                    }
                }
            }
        }
    }

    val currentPosition = when {
        isSeeking -> if (hasValidDuration) (scrubFraction * duration).toLong().coerceIn(0L, duration) else livePosition
        else -> livePosition
    }

    val sliderProgress = when {
        isSeeking -> scrubFraction
        hasValidDuration -> (livePosition.toFloat() / duration).coerceIn(0f, 1f)
        else -> 0f
    }

    val displayArt = media?.albumArt ?: media?.appIcon
    val mediaAccentColor = remember(displayArt) { extractMediaAccentColor(displayArt) }

    val controlsTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color(0xFFE5E5EA), mediaAccentColor, 0.40f)
    } else {
        Color(0xFFE5E5EA)
    }

    val playButtonBg = if (mediaAccentColor != Color.Transparent) {
        lerp(Color.White, mediaAccentColor, 0.28f)
    } else {
        Color.White
    }

    val playButtonIconTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color.Black, mediaAccentColor, 0.15f)
    } else {
        Color.Black
    }

    val eqTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color(0xFFE5E5EA), mediaAccentColor, 0.50f)
    } else {
        Color(0xFFE5E5EA)
    }

    val sliderActiveTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color(0xFFE5E5EA), mediaAccentColor, 0.55f)
    } else {
        Color(0xFFE5E5EA)
    }

    val sliderInactiveTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color(0xFF2C2C2E), mediaAccentColor, 0.22f)
    } else {
        Color(0xFF2C2C2E)
    }

    val durationTextTint = if (mediaAccentColor != Color.Transparent) {
        lerp(Color(0xFF8E8E93), mediaAccentColor, 0.35f)
    } else {
        Color(0xFF8E8E93)
    }

    val artSize = (54.dp * scale).coerceIn(40.dp, 64.dp)
    val headerGap = (14.dp * scale).coerceIn(8.dp, 16.dp)
    val titleSize = (17 * scale).coerceIn(14f, 19f).sp
    val artistSize = (13 * scale).coerceIn(11.5f, 15f).sp
    val sliderGapTop = (8.dp * scale).coerceIn(4.dp, 10.dp)
    val sliderGapBottom = (4.dp * scale).coerceIn(2.dp, 8.dp)
    val prevNextSize = (42.dp * scale).coerceIn(34.dp, 48.dp)
    val prevNextIcon = (24.dp * scale).coerceIn(18.dp, 28.dp)
    val playBtnSize = (50.dp * scale).coerceIn(40.dp, 58.dp)
    val playBtnIcon = (30.dp * scale).coerceIn(22.dp, 36.dp)
    val controlsGap = (20.dp * scale).coerceIn(12.dp, 26.dp)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header: Album cover / app icon + Title & Artist + Equalizer
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album cover (larger, tap to open app and collapse island)
            Box(
                modifier = Modifier
                    .size(artSize)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF212121))
                    .clickable {
                        launchAppOrIntent(context, media?.packageName, media?.sessionActivity)
                        onCollapse()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (displayArt != null) {
                    Image(
                        bitmap = displayArt.asImageBitmap(),
                        contentDescription = "Cover Art",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Radio,
                        contentDescription = null,
                        tint = Color(0xFFA1A1AA),
                        modifier = Modifier.size((28.dp * scale).coerceIn(20.dp, 32.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.width(headerGap))

            Column(modifier = Modifier.weight(1f)) {
                // Title with infinite marquee slide
                Text(
                    text = media?.title?.ifBlank { "No Media Playing" } ?: "No Media Playing",
                    color = Color.White,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        initialDelayMillis = 1000,
                        velocity = 30.dp
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                // Artist with marquee slide
                Text(
                    text = media?.artist?.ifBlank { "Unknown Artist" } ?: "Unknown Artist",
                    color = Color(0xFFA1A1AA),
                    fontSize = artistSize,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        initialDelayMillis = 1500,
                        velocity = 26.dp
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            EqualizerVisualizer(isPlaying = media?.isPlaying == true, barColor = eqTint)
        }

        Spacer(modifier = Modifier.height(sliderGapTop))

        // Progress Bar & Duration on Left & Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDuration(currentPosition),
                color = durationTextTint,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Slider(
                value = sliderProgress,
                enabled = hasValidDuration,
                onValueChange = { frac ->
                    isSeeking = true
                    scrubFraction = frac
                },
                onValueChangeFinished = {
                    if (hasValidDuration) {
                        val seekTargetMs = (scrubFraction * duration).toLong().coerceIn(0L, duration)
                        livePosition = seekTargetMs
                        isSeekPending = true
                        isSeeking = false
                        IslandStateManager.onSeekTo?.invoke(seekTargetMs)
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(650)
                            isSeekPending = false
                        }
                    } else {
                        isSeeking = false
                    }
                },
                thumb = {
                    if (hasValidDuration) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(sliderActiveTint)
                        )
                    }
                },
                track = { sliderState ->
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        drawStopIndicator = null,
                        drawTick = { _, _ -> },
                        colors = SliderDefaults.colors(
                            activeTrackColor = sliderActiveTint,
                            inactiveTrackColor = sliderInactiveTint,
                            disabledActiveTrackColor = sliderInactiveTint.copy(alpha = 0.5f),
                            disabledInactiveTrackColor = sliderInactiveTint.copy(alpha = 0.3f)
                        )
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = if (hasValidDuration) formatDuration(duration) else "--:--",
                color = durationTextTint,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(sliderGapBottom))

        // Playback Controls (moved upward, no big gap)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Centered Controls: Previous, Play/Pause, Next
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { IslandStateManager.onSkipPrevious?.invoke() },
                    modifier = Modifier.size(prevNextSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Previous",
                        tint = controlsTint,
                        modifier = Modifier.size(prevNextIcon)
                    )
                }

                Spacer(modifier = Modifier.width(controlsGap))

                IconButton(
                    onClick = { IslandStateManager.onPlayPause?.invoke() },
                    modifier = Modifier
                        .size(playBtnSize)
                        .clip(CircleShape)
                        .background(playButtonBg)
                ) {
                    Icon(
                        imageVector = if (media?.isPlaying == true) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = playButtonIconTint,
                        modifier = Modifier.size(playBtnIcon)
                    )
                }

                Spacer(modifier = Modifier.width(controlsGap))

                IconButton(
                    onClick = { IslandStateManager.onSkipNext?.invoke() },
                    modifier = Modifier.size(prevNextSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Next",
                        tint = controlsTint,
                        modifier = Modifier.size(prevNextIcon)
                    )
                }
            }


        }
    }
}

@Composable
fun ExpandedNotificationView(notification: NotificationInfo?, onCollapse: () -> Unit, scale: Float = 1.0f) {
    val context = LocalContext.current
    var isReplying by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }

    val replyAction = remember(notification) {
        notification?.actions?.firstOrNull { it.isReply || it.title.contains("Reply", ignoreCase = true) }
    }

    fun openNotification() {
        IslandStateManager.setKeyboardFocused(false)
        val pendingIntent = notification?.contentIntent
        var launched = false
        if (pendingIntent != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val options = ActivityOptions.makeBasic().apply {
                        pendingIntentBackgroundActivityStartMode = ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                    }.toBundle()
                    pendingIntent.send(context, 0, null, null, null, null, options)
                } else {
                    pendingIntent.send()
                }
                launched = true
            } catch (e: Exception) {
                Log.e("ExpandedNotification", "Failed to send contentIntent", e)
            }
        }
        if (!launched && !notification?.packageName.isNullOrBlank()) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(notification.packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                context.startActivity(launchIntent)
            }
        }
        onCollapse()
    }

    fun sendReply(text: String) {
        if (text.isBlank() || notification == null) return
        IslandStateManager.onSendReply?.invoke(notification.key, text.trim())
        IslandStateManager.setKeyboardFocused(false)
        IslandStateManager.dismissCurrentNotification()
        onCollapse()
    }

    val iconSize = (40.dp * scale).coerceIn(32.dp, 48.dp)
    val iconGap = (12.dp * scale).coerceIn(8.dp, 16.dp)
    val titleSize = (17 * scale).coerceIn(14f, 19f).sp
    val appNameSize = (13 * scale).coerceIn(11.5f, 15f).sp
    val bodySize = (15 * scale).coerceIn(13f, 17f).sp
    val bodyLineHeight = (20 * scale).coerceIn(17f, 22f).sp
    val bodyMaxLines = if (scale < 0.88f) 2 else 3
    val buttonHeight = (38.dp * scale).coerceIn(32.dp, 44.dp)
    val buttonFontSize = (13.5f * scale).coerceIn(12f, 15f).sp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Header + Body text
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App icon (tap to open app)
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2C2C2E))
                        .clickable {
                            launchAppOrIntent(context, notification?.packageName, notification?.contentIntent)
                            onCollapse()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (notification?.iconBitmap != null) {
                        Image(
                            bitmap = notification.iconBitmap.asImageBitmap(),
                            contentDescription = "App Icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFFE5E5EA),
                            modifier = Modifier.size((24.dp * scale).coerceIn(18.dp, 28.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(iconGap))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification?.title ?: "Notification",
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                    )
                    Text(
                        text = notification?.appName ?: "",
                        color = Color(0xFFA1A1AA),
                        fontSize = appNameSize,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = {
                        IslandStateManager.setKeyboardFocused(false)
                        IslandStateManager.dismissCurrentNotification()
                        onCollapse()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (!notification?.text.isNullOrBlank()) {
                Spacer(modifier = Modifier.height((6.dp * scale).coerceIn(3.dp, 8.dp)))

                Text(
                    text = notification?.text ?: "",
                    color = Color(0xFFF4F4F5),
                    fontSize = bodySize,
                    lineHeight = bodyLineHeight,
                    fontWeight = FontWeight.Normal,
                    maxLines = bodyMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openNotification() }
                )
            }
        }

        // Action Section: Either Inline Reply Row or Action Buttons
        if (isReplying) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Smart reply choices chips
                val smartChoices = remember(replyAction) {
                    val choices = replyAction?.remoteInputChoices ?: emptyList()
                    if (choices.isNotEmpty()) choices else listOf("OK", "Sure", "Thanks!")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    smartChoices.take(3).forEach { choice ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2C2C2E))
                                .clickable { sendReply(choice) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = choice,
                                color = Color(0xFFE5E5EA),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Inline Reply Text Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Color(0xFF212121))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    IslandStateManager.setKeyboardFocused(true)
                                }
                            },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Color(0xFFE5E5EA)),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { sendReply(replyText) }),
                        decorationBox = { innerTextField ->
                            if (replyText.isEmpty()) {
                                Text(
                                    text = replyAction?.remoteInputLabel ?: "Type a reply...",
                                    color = Color(0xFF71717A),
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    IconButton(
                        onClick = { sendReply(replyText) },
                        enabled = replyText.isNotBlank(),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Reply",
                            tint = if (replyText.isNotBlank()) Color.White else Color(0xFF52525B),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            isReplying = false
                            IslandStateManager.setKeyboardFocused(false)
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color(0xFF71717A),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        } else {
            // Action Buttons: Taller, dynamically dividing available width equally, spaced by 4dp,
            // with inner corners facing adjacent buttons being squarish
            val actionItems = remember(notification, replyAction) {
                val list = mutableListOf<NotificationActionItem>()
                val notifActions = notification?.actions ?: emptyList()
                notifActions.take(2).forEach { action ->
                    val isThisReply = action == replyAction || action.isReply || action.title.contains("Reply", ignoreCase = true)
                    list.add(
                        NotificationActionItem(
                            title = action.title,
                            isPrimary = false,
                            onClick = {
                                if (isThisReply) {
                                    isReplying = true
                                    IslandStateManager.setKeyboardFocused(true)
                                } else {
                                    runCatching { action.actionIntent?.send() }
                                    IslandStateManager.dismissCurrentNotification()
                                    onCollapse()
                                }
                            }
                        )
                    )
                }
                list.add(
                    NotificationActionItem(
                        title = "Open",
                        isPrimary = true,
                        onClick = { openNotification() }
                    )
                )
                list
            }

            val totalButtons = actionItems.size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actionItems.forEachIndexed { index, item ->
                    val shape = when {
                        totalButtons <= 1 -> RoundedCornerShape(14.dp)
                        index == 0 -> RoundedCornerShape(
                            topStart = 14.dp,
                            bottomStart = 14.dp,
                            topEnd = 4.dp,
                            bottomEnd = 4.dp
                        )
                        index == totalButtons - 1 -> RoundedCornerShape(
                            topStart = 4.dp,
                            bottomStart = 4.dp,
                            topEnd = 14.dp,
                            bottomEnd = 14.dp
                        )
                        else -> RoundedCornerShape(4.dp)
                    }

                    FilledTonalButton(
                        onClick = item.onClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = shape,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (item.isPrimary) Color(0xFFE5E5EA) else Color(0xFF2C2C2E),
                            contentColor = if (item.isPrimary) Color.Black else Color(0xFFF4F4F5)
                        )
                    ) {
                        Text(
                            text = item.title,
                            fontSize = buttonFontSize,
                            fontWeight = if (item.isPrimary) FontWeight.Bold else FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private data class NotificationActionItem(
    val title: String,
    val isPrimary: Boolean,
    val onClick: () -> Unit
)

@Composable
fun ExpandedProgressView(notification: NotificationInfo?, onCollapse: () -> Unit, scale: Float = 1.0f) {
    val context = LocalContext.current
    val progressFraction = notification?.progressFraction ?: 0f
    val isIndet = notification?.isIndeterminate == true
    val percentageText = if (isIndet) "" else "${(progressFraction * 100).roundToInt()}%"

    fun openProgressApp() {
        launchAppOrIntent(context, notification?.packageName, notification?.contentIntent)
        onCollapse()
    }

    val iconSize = (40.dp * scale).coerceIn(32.dp, 48.dp)
    val iconGap = (12.dp * scale).coerceIn(8.dp, 16.dp)
    val titleSize = (17 * scale).coerceIn(14f, 19f).sp
    val appNameSize = (13 * scale).coerceIn(11.5f, 15f).sp
    val vertGap = (10.dp * scale).coerceIn(6.dp, 12.dp)
    val bodySize = (15 * scale).coerceIn(13f, 17f).sp
    val bodyLineHeight = (20 * scale).coerceIn(17f, 22f).sp
    val buttonHeight = (42.dp * scale).coerceIn(34.dp, 48.dp)
    val buttonFontSize = (13.5f * scale).coerceIn(12f, 15f).sp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Header + Detail text + Progress bar
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App icon (tap to open app)
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF212121))
                        .clickable {
                            openProgressApp()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (notification?.iconBitmap != null) {
                        Image(
                            bitmap = notification.iconBitmap.asImageBitmap(),
                            contentDescription = "App Icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = Color(0xFFE5E5EA),
                            modifier = Modifier.size((24.dp * scale).coerceIn(18.dp, 28.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(iconGap))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification?.title?.ifBlank { "Downloading" } ?: "Downloading",
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                    )
                    Text(
                        text = notification?.appName ?: "",
                        color = Color(0xFFA1A1AA),
                        fontSize = appNameSize,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!isIndet) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF2C2C2E)
                    ) {
                        Text(
                            text = percentageText,
                            color = Color(0xFFE5E5EA),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.5.dp,
                        color = Color(0xFFE5E5EA),
                        trackColor = Color(0xFF2C2C2E)
                    )
                }


            }

            Spacer(modifier = Modifier.height(vertGap))

            val rawProgressText = notification?.text ?: "In progress..."
            val displayProgressText = remember(rawProgressText) {
                var s = rawProgressText.trim()
                while (s.startsWith("•") || s.startsWith("·") || s.startsWith("-")) {
                    s = s.substring(1).trim()
                }
                while (s.endsWith("•") || s.endsWith("·") || s.endsWith("-")) {
                    s = s.substring(0, s.length - 1).trim()
                }
                s.ifBlank { "In progress..." }
            }

            Text(
                text = displayProgressText,
                color = Color(0xFFF4F4F5),
                fontSize = bodySize,
                lineHeight = bodyLineHeight,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openProgressApp() }
            )

            Spacer(modifier = Modifier.height(vertGap))

            // Linear Progress Bar
            if (isIndet) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFE5E5EA),
                    trackColor = Color(0xFF2C2C2E)
                )
            } else {
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFE5E5EA),
                    trackColor = Color(0xFF2C2C2E)
                )
            }
        }

        Spacer(modifier = Modifier.height((16.dp * scale).coerceIn(12.dp, 24.dp)))

        // Action Buttons: Taller, dynamically dividing available width equally, spaced by 4dp,
        // with inner corners facing adjacent buttons being squarish
        val actionItems = remember(notification) {
            val list = mutableListOf<NotificationActionItem>()
            val notifActions = notification?.actions ?: emptyList()
            notifActions.take(2).forEach { action ->
                list.add(
                    NotificationActionItem(
                        title = action.title,
                        isPrimary = false,
                        onClick = {
                            runCatching { action.actionIntent?.send() }
                        }
                    )
                )
            }
            list.add(
                NotificationActionItem(
                    title = "Open",
                    isPrimary = true,
                    onClick = { openProgressApp() }
                )
            )
            list
        }

        val totalButtons = actionItems.size
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            actionItems.forEachIndexed { index, item ->
                val shape = when {
                    totalButtons <= 1 -> RoundedCornerShape(14.dp)
                    index == 0 -> RoundedCornerShape(
                        topStart = 14.dp,
                        bottomStart = 14.dp,
                        topEnd = 4.dp,
                        bottomEnd = 4.dp
                    )
                    index == totalButtons - 1 -> RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp,
                        topEnd = 14.dp,
                        bottomEnd = 14.dp
                    )
                    else -> RoundedCornerShape(4.dp)
                }

                FilledTonalButton(
                    onClick = item.onClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
                    shape = shape,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (item.isPrimary) Color(0xFFE5E5EA) else Color(0xFF2C2C2E),
                        contentColor = if (item.isPrimary) Color.Black else Color(0xFFF4F4F5)
                    )
                ) {
                    Text(
                        text = item.title,
                        fontSize = buttonFontSize,
                        fontWeight = if (item.isPrimary) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedRecordingView(recording: ScreenRecordingState, onCollapse: () -> Unit, scale: Float = 1.0f) {
    val minutes = recording.durationSeconds / 60
    val seconds = recording.durationSeconds % 60
    val timerText = String.format(Locale.US, "%02d:%02d", minutes, seconds)
    val timerSize = (22 * scale).coerceIn(18f, 26f).sp
    val buttonHeight = (42.dp * scale).coerceIn(34.dp, 48.dp)
    val buttonFontSize = (13.5f * scale).coerceIn(12f, 15f).sp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            PulsingRecordingDot(modifier = Modifier.size(11.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = timerText,
                color = Color.White,
                fontSize = timerSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val leftShape = RoundedCornerShape(
                topStart = 14.dp,
                bottomStart = 14.dp,
                topEnd = 4.dp,
                bottomEnd = 4.dp
            )
            FilledTonalButton(
                onClick = {
                    IslandStateManager.onTogglePauseRecording?.invoke()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = leftShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF2C2C2E),
                    contentColor = Color(0xFFF4F4F5)
                )
            ) {
                Icon(
                    imageVector = if (recording.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (recording.isPaused) "Resume" else "Pause",
                    fontSize = buttonFontSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            val rightShape = RoundedCornerShape(
                topStart = 4.dp,
                bottomStart = 4.dp,
                topEnd = 14.dp,
                bottomEnd = 14.dp
            )
            FilledTonalButton(
                onClick = {
                    IslandStateManager.onStopRecording?.invoke()
                    onCollapse()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = rightShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF2C2C2E),
                    contentColor = Color(0xFFFF453A)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    tint = Color(0xFFFF453A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Stop",
                    fontSize = buttonFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF453A)
                )
            }
        }
    }
}

@Composable
fun ExpandedIdleView(onCollapse: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Droidbubble Island",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Ready for Media, Notifications & Recording",
            color = Color(0xFFA1A1AA),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        FilledTonalButton(
            onClick = onCollapse,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF2C2C2E),
                contentColor = Color.White
            )
        ) {
            Text("Collapse", fontSize = 12.sp)
        }
    }
}

@Composable
fun PulsingRecordingDot(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
    val alphaState = infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .size(11.dp)
            .graphicsLayer {
                val a = alphaState.value
                scaleX = 1f + (a * 0.12f)
                scaleY = 1f + (a * 0.12f)
                this.alpha = a
            }
            .clip(CircleShape)
            .background(Color(0xFFFF3B30))
    )
}

@Composable
fun EqualizerVisualizer(isPlaying: Boolean, barColor: Color = Color(0xFFE5E5EA)) {
    if (!isPlaying) {
        // Zero CPU usage when paused: render static equalizer bars without triggering infinite frame recompositions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.height(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((16 * 0.3f).dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((16 * 0.5f).dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(barColor)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((16 * 0.2f).dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(barColor)
            )
        }
        return
    }

    val transition = rememberInfiniteTransition(label = "eq_bars")

    val h1State = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h1"
    )
    val h2State = transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h2"
    )
    val h3State = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h3"
    )

    // Perf: the bars are full-height (16dp) boxes scaled vertically about their centre inside a
    // graphicsLayer. The animated fractions are only read in the draw phase, so this no longer
    // recomposes + re-measures on every frame while music is playing.
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .graphicsLayer { scaleY = h1State.value }
                .clip(RoundedCornerShape(1.5.dp))
                .background(barColor)
        )
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .graphicsLayer { scaleY = h2State.value }
                .clip(RoundedCornerShape(1.5.dp))
                .background(barColor)
        )
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .graphicsLayer { scaleY = h3State.value }
                .clip(RoundedCornerShape(1.5.dp))
                .background(barColor)
        )
    }
}

private fun formatDuration(millis: Long): String {
    if (millis <= 0L) return "0:00"
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

@Composable
fun CellularSignalBars(
    level: Int,
    modifier: Modifier = Modifier
) {
    val activeLevel = level.coerceIn(0, 4)
    Row(
        modifier = modifier.height(11.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
    ) {
        val barHeights = listOf(3.5.dp, 6.dp, 8.5.dp, 11.dp)
        for (i in 0 until 4) {
            val isLit = activeLevel > i || (activeLevel == 0 && i == 0)
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(barHeights[i])
                    .clip(RoundedCornerShape(0.8.dp))
                    .background(if (isLit) Color.White else Color.White.copy(alpha = 0.25f))
            )
        }
    }
}

@Composable
fun ExpandedFpsView(
    fpsInfo: FpsInfo,
    onCollapse: () -> Unit
) {
    val targetHz = fpsInfo.refreshRateHz.coerceAtLeast(60)
    val fpsFraction = (fpsInfo.fps.toFloat() / targetHz).coerceIn(0f, 1f)
    val isSmooth = fpsInfo.isSmooth
    val statusColor = if (isSmooth) Color(0xFF30D158) else Color(0xFFFFD60A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Feature icon + Title / Subtitle + Top-Right Status Chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF212121)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "FPS",
                    tint = statusColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Display Frame Rate",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${targetHz} Hz Refresh Target",
                    color = Color(0xFFA1A1AA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF2C2C2E)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${fpsInfo.fps} FPS",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Center section: Minimal horizontal frame timing progress bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Frame Pacing",
                    color = Color(0xFFA1A1AA),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isSmooth) "Smooth" else "Drop Detected",
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C2E)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fpsFraction)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }
        }

        // Bottom section: Clean inline metadata
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(Locale.US, "Latency: %.1f ms", fpsInfo.frameTimeMs),
                color = Color(0xFFA1A1AA),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${targetHz} Hz Display",
                color = Color(0xFFA1A1AA),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ExpandedWeatherView(
    weatherInfo: WeatherInfo,
    onCollapse: () -> Unit
) {
    val weatherIcon = when {
        weatherInfo.condition.contains("Sun", ignoreCase = true) || weatherInfo.condition.contains("Clear", ignoreCase = true) -> Icons.Default.WbSunny
        weatherInfo.condition.contains("Rain", ignoreCase = true) || weatherInfo.condition.contains("Drizzle", ignoreCase = true) || weatherInfo.condition.contains("Shower", ignoreCase = true) -> Icons.Default.WaterDrop
        weatherInfo.condition.contains("Snow", ignoreCase = true) -> Icons.Default.AcUnit
        weatherInfo.condition.contains("Thunder", ignoreCase = true) -> Icons.Default.Bolt
        else -> Icons.Default.Cloud
    }
    val iconTint = if (weatherIcon == Icons.Default.WbSunny) Color(0xFFFFD60A) else Color(0xFF64D2FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Feature icon + City / Condition + Temp & H/L Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF212121)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = weatherIcon,
                    contentDescription = "Weather",
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = weatherInfo.cityName.ifBlank { "Local Weather" },
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = weatherInfo.condition.ifBlank { "Partly Cloudy" },
                    color = Color(0xFFA1A1AA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF2C2C2E)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${weatherInfo.temperatureC}°",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "H:${weatherInfo.highC}° L:${weatherInfo.lowC}°",
                        color = Color(0xFFA1A1AA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Center section: Minimal 5-hour micro forecast strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF1C1C1E))
                .padding(vertical = 7.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val forecast = if (weatherInfo.hourlyForecast.isNotEmpty()) {
                weatherInfo.hourlyForecast.take(5)
            } else {
                listOf(
                    HourlyWeather("Now", weatherInfo.temperatureC, weatherInfo.condition),
                    HourlyWeather("+1h", weatherInfo.temperatureC, weatherInfo.condition),
                    HourlyWeather("+2h", weatherInfo.temperatureC, weatherInfo.condition),
                    HourlyWeather("+3h", weatherInfo.temperatureC, weatherInfo.condition),
                    HourlyWeather("+4h", weatherInfo.temperatureC, weatherInfo.condition)
                )
            }
            forecast.forEach { hour ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = hour.timeLabel,
                        color = Color(0xFFA1A1AA),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${hour.tempC}°",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Bottom section: Clean inline stats: Humidity, Wind, UV
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Humidity: ${weatherInfo.humidityPct}%",
                color = Color(0xFFA1A1AA),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "•",
                color = Color(0xFF3A3A3C),
                fontSize = 11.sp
            )
            Text(
                text = "Wind: ${weatherInfo.windKmh} km/h",
                color = Color(0xFFA1A1AA),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "•",
                color = Color(0xFF3A3A3C),
                fontSize = 11.sp
            )
            Text(
                text = "UV Index: ${weatherInfo.uvIndex}",
                color = Color(0xFFA1A1AA),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ExpandedNetworkSpeedView(
    networkSpeedInfo: NetworkSpeedInfo,
    onCollapse: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top section: Feature icon + Network label / Latency + Top-right speed chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF212121)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (networkSpeedInfo.isWifi) Icons.Default.Wifi else Icons.Default.SignalCellularAlt,
                    contentDescription = "Network",
                    tint = Color(0xFF64D2FF),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = networkSpeedInfo.networkLabel.ifBlank { "Network Traffic" },
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${networkSpeedInfo.pingMs} ms Latency",
                    color = Color(0xFFA1A1AA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF2C2C2E)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF30D158))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Online",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Center section: Dual minimal cards for Download & Upload (Apple style)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Download Card
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1C1C1E)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Download",
                            tint = Color(0xFF64D2FF),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Download",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFFA1A1AA))
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = networkSpeedInfo.downloadSpeedFormatted,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }

            // Upload Card
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1C1C1E)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Upload",
                            tint = Color(0xFFBF5AF2),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Upload",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFFA1A1AA))
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = networkSpeedInfo.uploadSpeedFormatted,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }
        }

        // Bottom section: Clean session transfer summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Session Transfer",
                color = Color(0xFFA1A1AA),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${networkSpeedInfo.sessionTotalMb} MB Total",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

