package com.mtabvuri.pomodoit.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.ui.fullLogo

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SplashScreen() {
    var startLogoAnimation by remember { mutableStateOf(false) }
    var startTextAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val textAnim by animateFloatAsState(
        targetValue = if (startTextAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 1000
        )
    )

    LaunchedEffect(key1 = true) {
        startLogoAnimation = true
        startTextAnimation = true
    }

    SplashBox(alphaAnim, textAnim)

}

@Composable
private fun SplashBox(alphaAnim: Float, textAnim: Float) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val constraints = if (maxWidth < maxHeight) {
            splashConstraints(margin = 16.dp) // Portrait constraints
        } else {
            splashConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(
            constraintSet = constraints,
            modifier = Modifier.fillMaxSize()
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