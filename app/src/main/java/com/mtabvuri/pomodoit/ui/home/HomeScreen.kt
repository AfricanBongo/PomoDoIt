package com.mtabvuri.pomodoit.ui.home

import android.text.format.DateFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.nav.PortraitLayout
import com.mtabvuri.pomodoit.ui.components.PrimaryButton
import com.mtabvuri.pomodoit.ui.components.SecondaryButton
import com.mtabvuri.pomodoit.ui.settingsIcon
import com.mtabvuri.pomodoit.ui.theme.OnPrimaryMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.OnSecondaryMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme
import com.mtabvuri.pomodoit.ui.theme.Progress

private const val TIME_TEXT_CONSTRAINT = "time_text"
private const val SETTINGS_CONSTRAINT = "settings"
private const val BUTTON_BOX_CONSTRAINT = "button_box"

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(context = LocalContext.current)
    ),
    onSettingsButtonClicked: () -> Unit,
) {

    val tweenSpec = TweenSpec<Color> (
        durationMillis = 1000
    )

    val backgroundColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                MaterialTheme.colors.secondary
            }
            else -> MaterialTheme.colors.background
        },
        animationSpec = tweenSpec
    )

    val primaryContentColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                MaterialTheme.colors.onSecondary
            }
            else -> MaterialTheme.colors.onBackground
        },
        animationSpec = tweenSpec
    )

    val secondaryContentColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                OnSecondaryMediumEmphasis
            }
            else -> OnPrimaryMediumEmphasis
        },
        animationSpec = tweenSpec
    )

    PortraitLayout {
        ConstraintLayout(
            constraintSet = homeScreenConstraints(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(all = 16.dp)
        ) {
            IconButton(
                onClick = onSettingsButtonClicked,
                modifier = Modifier.layoutId(SETTINGS_CONSTRAINT)
            ) {
                Icon(
                    imageVector = settingsIcon.imageVector,
                    contentDescription = stringResource(settingsIcon.contentDescription),
                    tint = secondaryContentColor,
                )
            }

            SessionTimeView(
                state = viewModel.pomodoroState,
                colors = primaryContentColor to secondaryContentColor,
                timeInMillis = viewModel.timeInMillis,
                modifier = Modifier.layoutId(TIME_TEXT_CONSTRAINT)
            )

            ButtonBox(
                pomodoroState = viewModel.pomodoroState,
                colors = primaryContentColor to backgroundColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    .layoutId(BUTTON_BOX_CONSTRAINT)
            )
        }
    }
}

private fun homeScreenConstraints(): ConstraintSet {
    return ConstraintSet {
        val timeText = createRefFor(TIME_TEXT_CONSTRAINT)
        val settingsButton = createRefFor(SETTINGS_CONSTRAINT)
        val buttonBox = createRefFor(BUTTON_BOX_CONSTRAINT)
        
        constrain(settingsButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
        
        constrain(timeText) {
            top.linkTo(settingsButton.bottom, 120.dp)
            centerHorizontallyTo(parent)
        }
        
        constrain(buttonBox) {
            top.linkTo(timeText.bottom, 60.dp)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
        }
    }
}

@Composable
private fun SessionTimeView(
    state: PomodoroState,
    timeInMillis: Long,
    modifier: Modifier = Modifier,
    colors: Pair<Color, Color> = OnPrimaryMediumEmphasis to OnSecondaryMediumEmphasis, // First color is the primary color and second is secondary.
) {
    // Convert from long into minutes and seconds notation, i.e. 00:00
    val timeAsString: String = DateFormat.format("mm:ss", timeInMillis).toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = state.session,
            style = MaterialTheme.typography.subtitle1.copy(
                color = colors.second
            )
        )

        Text(
            text = timeAsString,
            style = MaterialTheme.typography.h1.copy(
                fontWeight = FontWeight.Bold,
                color = colors.first
            )
        )
    }
}
/*
A box that has different buttons for each stage of the pomodoro
i.e. pause and play pomodoro stage.
 */
@Composable
private fun ButtonBox(
    pomodoroState: PomodoroState,
    colors: Pair<Color, Color>, // First color is the primary color and second is background.
    modifier: Modifier = Modifier,
) {
    when (pomodoroState) {
        is PomodoroState.Waiting -> {
            StartPomodoroButton(
                onClick = pomodoroState.onStart,
                modifier = modifier,
            )
        }

        is PomodoroState.Pomodoro -> {
            PausePomodoroButton(
                modifier = modifier,
                onClick = pomodoroState.onPause,
                colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.second,
                contentColor = colors.first
            ))
        }
        is PomodoroState.Pause -> {
            ResumePomodoroButton(
                onClick = pomodoroState.onResume,
                modifier = modifier,
            )
        }
        else -> { /* Nothing */ }
    }
}

@Composable
private fun ProgressGroup(
    progress: Float,
    modifier: Modifier = Modifier
) {

//    TODO
//    val progressPillModifier = Modifier
//        .clip(MaterialTheme.shapes.small)
//        .background(PrimaryMediumEmphasis)
//        .requiredWidth(40.dp)
//        .requiredHeight(24.dp)
//
//    val filledPills: Int = progress.coerceIn(0f, 1f).div(0.25f).toInt()
//    var individualProgress: Float
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//       for (i in 1..filledPills) {
//           individualProgress = if (filledPills > i) 1f
//           else (progress % 0.25f) * 4
//
//           ProgressPill(progress = individualProgress, modifier = progressPillModifier)
//       }
//    }
}

@Composable
private fun ProgressPill(
    progress: Float,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        var filledProgress = progress.coerceIn(0f, 1f)
        filledProgress = (filledProgress / 1f) * maxWidth.value

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .requiredWidth(filledProgress.dp)
                .background(Progress)
        )

    }
}

@Composable
fun ResumePomodoroButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PrimaryButton(
        modifier = modifier,
        text = stringResource(R.string.resume_btn),
        onClick = onClick,
    )
}

@Composable
fun PausePomodoroButton(
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderStrokeColor = colors.contentColor(enabled = true).value
    SecondaryButton(
        modifier = modifier,
        text = stringResource(R.string.pause_btn),
        onClick = onClick,
        colors = colors,
        border = BorderStroke(2.dp,borderStrokeColor)
    )
}

@Composable
fun StartPomodoroButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PrimaryButton(
        modifier = modifier,
        text = stringResource(R.string.start_btn),
        onClick = onClick,
    )
}

@Preview
@Composable
fun SessionTimeViewPreview() {
    PomoDoItTheme {
        SessionTimeView(PomodoroState.Waiting { /* Nothing */ }, (10000 * 78.6).toLong())
    }
}

@Preview(heightDp = 800, widthDp = 320)
@Composable
fun HomeScreenPreview() {
    PomoDoItTheme {
        HomeScreen {}
    }
}