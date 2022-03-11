package com.mtabvuri.pomodoit.ui.splash

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.nav.LockScreenOrientation
import com.mtabvuri.pomodoit.ui.fullLogo
import kotlinx.coroutines.delay

/**
 * The splash screen of the app.
 * @param onSplashAnimationEnd The callback to be run when the splash screen animations are all finished.
 */
@Composable
fun SplashScreen(onSplashAnimationEnd: () -> Unit) {

    // Show the screen in only portrait mode.
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    // Animations for the logo and text statement.
    var startLogoAnimation by remember { mutableStateOf(false) }
    var startTextAnimation by remember { mutableStateOf(false) }

    val logoAlphaAnim by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val textAlphaAnim by animateFloatAsState(
        targetValue = if (startTextAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 1000
        )
    )

    LaunchedEffect(key1 = true) {
        startLogoAnimation = true
        startTextAnimation = true
        delay(2400)
        onSplashAnimationEnd()
    }

    SplashBox(logoAlphaAnim, textAlphaAnim)

}

@Composable
private fun SplashBox(alphaAnim: Float, textAnim: Float) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)) {
        val constraints = if (maxWidth < maxHeight) {
            splashConstraints(margin = 16.dp) // Portrait constraints
        } else {
            splashConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(
            constraintSet = constraints
        ) {

            Image(
                painter = painterResource(fullLogo.drawableId),
                contentDescription = stringResource(fullLogo.stringId),
                modifier = Modifier
                    .layoutId("logo")
                    .alpha(alphaAnim)
            )

            Text(
                text = stringResource(R.string.splash_statement),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .layoutId("text")
                    .alpha(textAnim)
            )

        }
    }
}

private fun splashConstraints(margin: Dp) = ConstraintSet {
    val logo = createRefFor("logo")
    val text = createRefFor("text")

    constrain(logo) {
        centerTo(parent)
    }

    constrain(text) {
        top.linkTo(logo.bottom)
        centerHorizontallyTo(parent)
    }
}